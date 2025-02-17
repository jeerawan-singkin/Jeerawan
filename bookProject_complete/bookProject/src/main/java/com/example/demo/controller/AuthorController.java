package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Author;
import com.example.demo.service.AuthorService;
import com.example.demo.service.ProfileService;



@Controller
@RequestMapping("/authors")
public class AuthorController {

	
    @Autowired
    private AuthorService authorService;
    
    @Autowired
    private ProfileService profileService;

    @GetMapping
    public String getAllAuthors(Model model) {
        List<Author> listAuthors = authorService.getAllAuthors();
        
        // Convert each author's profile image to Base64 if it exists
        listAuthors.forEach(author -> {
            if (author.getProfile() != null && author.getProfile().getImage() != null) {
                String base64Image = profileService.convertToBase64(author.getProfile().getImage());
                author.getProfile().setBase64Image(base64Image);  // Assuming you have a 'base64Image' field in Profile
            }
        });
        
        model.addAttribute("authors", listAuthors);
        return "authors";
    }
    
    
    @GetMapping("/{id}")
    public String getAuthorById(@PathVariable("id") Long id, Model model) {
        Author author = authorService.getAuthorById(id);
        model.addAttribute("author", author);
        return "author_form";
    }

    @GetMapping("/new")
    public String createAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "author_form";
    }
    
    @PostMapping
    public String createOrUpdateAuthor(Author author) {
        if (author.getId() != null) {
            // Existing author: fetch and retain books
            Author existingAuthor = authorService.getAuthorById(author.getId());
            if (existingAuthor != null) {
                author.setBooks(existingAuthor.getBooks());
                author.setProfile(existingAuthor.getProfile());// Retain existing profile.
            }
        } else {
            // New author: initialize books if needed
            author.setBooks(List.of()); // Or handle as per your requirement
        }
        authorService.saveAuthor(author);
        return "redirect:/authors";
    }


    @GetMapping("/edit/{id}")
    public String editAuthor(@PathVariable("id") Long id, Model model) {
        Author author = authorService.getAuthorById(id);
        model.addAttribute("author", author);
        return "author_form";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable("id") Long id) {
        authorService.deleteAuthorById(id);
        return "redirect:/authors";
    }
    
    @GetMapping("/search")
    public String searchAuthors(@RequestParam String query, Model model) {
        List<Author> authors;

        try {
            // ลองแปลง query เป็น Long สำหรับ ID
            Long id = Long.parseLong(query);
            authors = authorService.searchAuthorsByID(id);
        } catch (NumberFormatException e) {
            // ถ้าไม่สามารถแปลงได้ ค้นหาจากชื่อ
            authors = authorService.searchAuthorsByName(query);
        }

        // แปลงภาพโปรไฟล์เป็น Base64 ถ้ามี
        authors.forEach(author -> {
            if (author.getProfile() != null && author.getProfile().getImage() != null) {
                String base64Image = profileService.convertToBase64(author.getProfile().getImage());
                author.getProfile().setBase64Image(base64Image);
            }
        });

        // แสดงผลลัพธ์การค้นหา
        model.addAttribute("authors", authors);
        return "authors";
    }


}
