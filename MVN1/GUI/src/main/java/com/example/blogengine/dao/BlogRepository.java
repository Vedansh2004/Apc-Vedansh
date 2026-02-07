package com.example.blogengine.dao;

import com.example.blogengine.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findTop5ByOrderByCreatedAtDesc();

    List<Blog> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);

    List<Blog> findByAuthorIgnoreCase(String author);
}
