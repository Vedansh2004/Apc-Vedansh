package com.example.blogengine.service;

import com.example.blogengine.dao.BlogRepository;
import com.example.blogengine.model.Blog;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {
    private final BlogRepository repo;

    public BlogService(BlogRepository repo) {
        this.repo = repo;
    }

    public List<Blog> findAll() {
        return repo.findAll();
    }

    public Blog findById(Long id) {
        Optional<Blog> blog = repo.findById(id);
        return blog.orElse(null);
    }

    public Blog save(Blog blog) {
        return repo.save(blog);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    // New helpers for chatbot
    public List<Blog> recent() {
        return repo.findTop5ByOrderByCreatedAtDesc();
    }

    public List<Blog> search(String query) {
        if (query == null || query.isBlank()) return List.of();
        return repo.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query);
    }

    public List<Blog> byAuthor(String author) {
        if (author == null || author.isBlank()) return List.of();
        return repo.findByAuthorIgnoreCase(author);
    }
}
