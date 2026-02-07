package com.example.blogmanagement;

import com.example.blogmanagement.config.AppConfig;
import com.example.blogmanagement.dao.BlogDAO;
import com.example.blogmanagement.model.Blog;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import java.util.Scanner;

public class BlogManagementApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static BlogDAO blogDAO;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        blogDAO = context.getBean(BlogDAO.class);

        int choice = 0;
        while (choice != 5) {
            printMenu();
            try {
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> addBlog();
                    case 2 -> searchBlog();
                    case 3 -> deleteBlog();
                    case 4 -> updateBlog();
                    case 5 -> System.out.println("Exiting... Goodbye!");
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }

        context.close();
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n=== Blog Management System ===");
        System.out.println("1. Add Blog");
        System.out.println("2. Search Blog by ID");
        System.out.println("3. Delete Blog by ID");
        System.out.println("4. Update Blog by ID");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addBlog() {
        System.out.println("\n-- Add Blog --");
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Content: ");
        String content = scanner.nextLine();
        System.out.print("Author: ");
        String author = scanner.nextLine();

        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setContent(content);
        blog.setAuthor(author);

        int rows = blogDAO.save(blog);
        if (rows > 0) {
            System.out.println("Blog added successfully!");
        } else {
            System.out.println("Failed to add blog.");
        }
    }

    private static void searchBlog() {
        System.out.print("\nEnter Blog ID to search: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Blog blog = blogDAO.getById(id);
            // System.out.println(blog);
            if (blog != null) {
                System.out.println("Blog found: " + blog);
            } else {
                System.out.println("Blog with ID " + id + " not found.");
            }
        } catch (Exception e) {
            System.out.println("Error finding blog. Please ensure the ID is correct.");
        }
    }

    private static void deleteBlog() {
        System.out.print("\nEnter Blog ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            int rows = blogDAO.delete(id);
            if (rows > 0) {
                System.out.println("Blog deleted successfully.");
            } else {
                System.out.println("Blog with ID " + id + " not found.");
            }
        } catch (Exception e) {
            System.out.println("Error deleting blog. Please ensure the ID is correct.");
        }
    }

    private static void updateBlog() {
        System.out.print("\nEnter Blog ID to update: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Blog blog = blogDAO.getById(id);
            if (blog == null) {
                System.out.println("Blog with ID " + id + " not found.");
                return;
            }

            System.out.println("Current Blog Details: " + blog);
            System.out.print("New Title (leave blank to keep current): ");
            String title = scanner.nextLine();
            if (!title.isBlank()) blog.setTitle(title);

            System.out.print("New Content (leave blank to keep current): ");
            String content = scanner.nextLine();
            if (!content.isBlank()) blog.setContent(content);

            System.out.print("New Author (leave blank to keep current): ");
            String author = scanner.nextLine();
            if (!author.isBlank()) blog.setAuthor(author);

            int rows = blogDAO.update(blog);
            if (rows > 0) {
                System.out.println("Blog updated successfully!");
            } else {
                System.out.println("Failed to update blog.");
            }
        } catch (Exception e) {
            System.out.println("Error updating blog. Please ensure the ID is correct.");
        }
    }
}
