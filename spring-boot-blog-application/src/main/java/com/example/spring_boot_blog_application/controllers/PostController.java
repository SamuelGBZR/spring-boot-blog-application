package com.example.spring_boot_blog_application.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.spring_boot_blog_application.models.Account;
import com.example.spring_boot_blog_application.models.Post;
import com.example.spring_boot_blog_application.services.AccountService;
import com.example.spring_boot_blog_application.services.PostService;

@Controller
public class PostController {
    
    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        //  find post by id
        Optional<Post> optionalPost = postService.getById(id);
        //  if post exists, shove into model
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post";
        } else {
            return "404";
        }
    }

    @GetMapping("/posts/new")
    public String createNewPost(Model model) {
        Optional<Account> optionalAccount = accountService.findByEmail("user.user@domain.com");
        if (optionalAccount.isPresent()) {
            Post post = new Post();
            post.setAccount(optionalAccount.get());
            model.addAttribute("post", post);
            return "post_new";
        } else {
            return "404";
        }
    }

    @PostMapping("/posts/new")
    public String saveNewPost(@ModelAttribute Post post) {
        postService.save(post);
        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/posts/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getPostforEdit(@PathVariable Long id, Model model) {

        //  find post by id
        Optional<Post> optionalPost = postService.getById(id);
        //  if post exists, shove into model
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post_edit";
        } else {
            return "404";
        }
    }
    
    @PostMapping("/posts/{id}")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@PathVariable Long id, Post post, BindingResult result, Model model) {
        
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post existingpost = optionalPost.get();
            
            existingpost.setTitle(post.getTitle());
            existingpost.setBody(post.getBody());

            postService.save(existingpost);
        }

        return "redirect:/posts/" + post.getId();
    }
}
