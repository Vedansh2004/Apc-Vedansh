package com.example.blogengine.controller;

import com.example.blogengine.model.Blog;
import com.example.blogengine.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/blogs")
public class BlogController {

    private final BlogService service;

    public BlogController(BlogService service) {
        this.service = service;
    }

    @GetMapping
    public String listBlogs(@RequestParam(value = "q", required = false) String q, Model model) {
        if (q != null && !q.isBlank()) {
            List<Blog> results = service.search(q);
            if (results.size() == 1) {
                return "redirect:/blogs/view/" + results.get(0).getId();
            }
            model.addAttribute("blogs", results);
            model.addAttribute("searchQuery", q);
            model.addAttribute("searchCount", results.size());
            return "blogs";
        }
        model.addAttribute("blogs", service.findAll());
        return "blogs";
    }

    @GetMapping("/new")
    public String newBlog(Model model) {
        model.addAttribute("blog", new Blog());
        model.addAttribute("isEdit", false);
        return "blog-form";
    }

    @GetMapping("/edit/{id}")
    public String editBlog(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Blog blog = service.findById(id);
        if (blog == null) {
            redirectAttributes.addFlashAttribute("error", "Blog not found!");
            return "redirect:/blogs";
        }
        model.addAttribute("blog", blog);
        model.addAttribute("isEdit", true);
        return "blog-form";
    }

    @PostMapping
    public String saveBlog(@ModelAttribute Blog blog, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            if (blog.getAuthor() == null || blog.getAuthor().isEmpty()) {
                blog.setAuthor(principal != null ? principal.getName() : "Anonymous");
            }
            service.save(blog);
            redirectAttributes.addFlashAttribute("success", "Blog saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving blog: " + e.getMessage());
        }
        return "redirect:/blogs";
    }

    @GetMapping("/view/{id}")
    public String viewBlog(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes, Principal principal) {
        Blog blog = service.findById(id);
        if (blog == null) {
            redirectAttributes.addFlashAttribute("error", "Blog not found!");
            return "redirect:/blogs";
        }
        model.addAttribute("blog", blog);
        if (blog.getCreatedAt() != null) {
            model.addAttribute("publishedAt", blog.getCreatedAt().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' HH:mm")));
        }
        if (blog.getUpdatedAt() != null) {
            model.addAttribute("updatedAtFormatted", blog.getUpdatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
        }
        boolean showUpdated = blog.getUpdatedAt() != null && blog.getCreatedAt() != null
                && !blog.getUpdatedAt().toLocalDate().isEqual(blog.getCreatedAt().toLocalDate());
        model.addAttribute("showUpdated", showUpdated);
        int contentLength = blog.getContent() != null ? blog.getContent().length() : 0;
        model.addAttribute("contentLength", contentLength);
        String readingTime = contentLength > 400 ? "3 min" : (contentLength > 200 ? "2 min" : "1 min");
        model.addAttribute("readingTime", readingTime);
        return "blog-view";
    }

    @GetMapping("/delete/{id}")
    public String deleteBlog(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("success", "Blog deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting blog: " + e.getMessage());
        }
        return "redirect:/blogs";
    }
}
