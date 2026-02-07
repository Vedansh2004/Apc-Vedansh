package com.example.blogmanagement.dao;

import java.util.List;

import com.example.blogmanagement.model.Blog;

public interface BlogDAO {
    int save(Blog blog);
    int update(Blog blog);
    int delete(int id);
    Blog getById(int id);
    List<Blog> getAll();
}
