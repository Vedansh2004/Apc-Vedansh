package com.example.blogengine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Principal principal) {
        // If user is logged in, redirect to blogs, otherwise show landing page
        if (principal != null) {
            return "redirect:/blogs";
        }
        return "index";
    }
    
    @GetMapping("/home")
    public String homePage() {
        return "index";
    }
    
    @GetMapping("/landing")
    public String landingPage() {
        return "index";
    }
}
