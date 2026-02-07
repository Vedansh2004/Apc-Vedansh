package com.example.blogengine.service;

import com.example.blogengine.model.Blog;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class ChatService {

    private final BlogService blogService;

    public ChatService(BlogService blogService) {
        this.blogService = blogService;
    }

    public String reply(String message, String username) {
        if (message == null || message.trim().isEmpty()) {
            return "Hi" + (username != null ? " " + username : "") + "! Ask me about blogs, help, or say 'list blogs'.";
        }
        String msg = message.trim().toLowerCase(Locale.ROOT);

        // Simple intents
        if (containsAny(msg, new String[]{"hi", "hello", "hey"})) {
            return "Hello" + (username != null ? " " + username : "") + "! How can I help you today?";
        }
        if (msg.contains("help")) {
            return "I can help you: \n" +
                   "- create a blog (say 'create blog' or go to /blogs/new)\n" +
                   "- list blogs (say 'list blogs' or 'recent')\n" +
                   "- search blogs (say 'search <keyword>')\n" +
                   "- blogs by author (say 'author <name>')\n" +
                   "- view a blog (say 'open <id>')\n" +
                   "- current time (say 'time')\n" +
                   "- general info (say 'about').";
        }
        if (msg.contains("about")) {
            return "I'm a simple, self-hosted assistant for your BlogEngine. I can answer basic questions and help navigate your blogs—no external APIs required.";
        }
        if (msg.contains("time") || msg.contains("date")) {
            return "Server time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        if (msg.contains("list blogs") || msg.contains("show blogs") || msg.contains("all blogs")) {
            List<Blog> blogs = blogService.findAll();
            if (blogs.isEmpty()) {
                return "No blogs yet. You can create one at /blogs/new";
            }
            StringBuilder sb = new StringBuilder("Here are some recent blogs (id - title by author):\n");
            blogs.stream().limit(5).forEach(b -> sb.append(b.getId())
                    .append(" - ").append(safe(b.getTitle()))
                    .append(" by ").append(safe(b.getAuthor()))
                    .append('\n'));
            sb.append("You can say 'open <id>' to view a blog.");
            return sb.toString();
        }
        if (msg.contains("recent")) {
            List<Blog> blogs = blogService.recent();
            if (blogs.isEmpty()) return "No recent blogs yet. Create one at /blogs/new";
            StringBuilder sb = new StringBuilder("Recent blogs: id - title (author)\n");
            blogs.forEach(b -> sb.append(b.getId()).append(" - ")
                    .append(safe(b.getTitle())).append(" (")
                    .append(safe(b.getAuthor())).append(")\n"));
            return sb.toString();
        }
        if (msg.startsWith("search ")) {
            String q = message.substring(message.toLowerCase(Locale.ROOT).indexOf("search ") + 7).trim();
            if (q.isBlank()) return "Please provide a keyword, e.g., 'search spring'";
            List<Blog> results = blogService.search(q);
            if (results.isEmpty()) return "No results for '" + q + "'.";
            StringBuilder sb = new StringBuilder("Found ").append(results.size()).append(" result(s):\n");
            results.stream().limit(5).forEach(b -> sb.append(b.getId()).append(": ")
                    .append(safe(b.getTitle())).append(" by ")
                    .append(safe(b.getAuthor())).append('\n'));
            sb.append("You can say 'open <id>' to view a blog.");
            return sb.toString();
        }
        if (msg.startsWith("author ")) {
            String name = message.substring(message.toLowerCase(Locale.ROOT).indexOf("author ") + 7).trim();
            if (name.isBlank()) return "Please provide an author name, e.g., 'author Alice'";
            List<Blog> results = blogService.byAuthor(name);
            if (results.isEmpty()) return "No blogs found for author '" + name + "'.";
            StringBuilder sb = new StringBuilder("Blogs by ").append(name).append(":\n");
            results.stream().limit(5).forEach(b -> sb.append(b.getId()).append(": ")
                    .append(safe(b.getTitle())).append('\n'));
            return sb.toString();
        }
        if (msg.contains("create blog") || msg.contains("new blog") || msg.contains("write blog")) {
            return "To create a blog, go to /blogs/new. You'll need a title, author, and content. Say 'help' for more tips.";
        }
        if (msg.startsWith("open ")) {
            try {
                String num = msg.replace("open", "").trim();
                Long id = Long.parseLong(num);
                Blog b = blogService.findById(id);
                if (b == null) return "I couldn't find a blog with id " + id + ".";
                return "Opening blog '" + safe(b.getTitle()) + "' by " + safe(b.getAuthor()) + ". Go to /blogs/view/" + b.getId();
            } catch (Exception e) {
                return "Please provide a numeric id, e.g., 'open 1'.";
            }
        }

        // Default fallback
        return "I'm not sure about that. You can say 'help', 'recent', 'search <keyword>', 'author <name>', or 'list blogs'.";
    }

    private boolean containsAny(String text, String[] tokens) {
        for (String t : tokens) {
            if (text.contains(t)) return true;
        }
        return false;
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
