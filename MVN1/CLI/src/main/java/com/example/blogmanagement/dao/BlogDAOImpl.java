package com.example.blogmanagement.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.example.blogmanagement.model.Blog;

public class BlogDAOImpl implements BlogDAO {

    private final JdbcTemplate jdbcTemplate;

    public BlogDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class BlogRowMapper implements RowMapper<Blog> {
        @Override
        public Blog mapRow(ResultSet rs, int rowNum) throws SQLException {
            Blog blog = new Blog();
            blog.setId(rs.getInt("id"));
            blog.setTitle(rs.getString("title"));
            blog.setContent(rs.getString("content"));
            blog.setAuthor(rs.getString("author"));
            return blog;
        }
    }

    @Override
    public int save(Blog blog) {
        String sql = "INSERT INTO blog(title, content, author) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, blog.getTitle(), blog.getContent(), blog.getAuthor());
    }

    @Override
    public int update(Blog blog) {
        String sql = "UPDATE blog SET title=?, content=?, author=? WHERE id=?";
        return jdbcTemplate.update(sql, blog.getTitle(), blog.getContent(), blog.getAuthor(), blog.getId());
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM blog WHERE id=?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public Blog getById(int id) {
        String sql = "SELECT * FROM blog WHERE id=?";
        
        return jdbcTemplate.queryForObject(sql, new BlogRowMapper(), id);
    }

    @Override
    public List<Blog> getAll() {
        String sql = "SELECT * FROM blog";
        return jdbcTemplate.query(sql, new BlogRowMapper());
    }
}
