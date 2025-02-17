package com.example.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Author;
import com.example.demo.model.Profile;
import com.example.demo.service.AuthorService;
import com.example.demo.service.ProfileService;

import java.util.Base64;


import java.awt.Graphics2D;


@Controller
@RequestMapping("/authors/{authorId}/profile")
public class AuthorProfileController {

    @Autowired
    ProfileService profileService;

    @Autowired
    AuthorService authorService;
    
 // Existing view profile method
    @GetMapping
    public String viewProfile(@PathVariable("authorId") Long authorId, Model model) {
        Author author = authorService.getAuthorById(authorId);
        Profile profile = profileService.getProfileByAuthorId(authorId);

        if (profile == null) {
            profile = new Profile();
            profile.setAuthor(author);
        }

        model.addAttribute("author", author);
        model.addAttribute("profile", profile);

        if (profile.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(profile.getImage());
            model.addAttribute("base64Image", base64Image);
        }

        return "author_profile";
    }
    
    
    @GetMapping("/edit")
    public String editProfile(@PathVariable("authorId") Long authorId, Model model) {
        Author author = authorService.getAuthorById(authorId);
        Profile profile = profileService.getProfileByAuthorId(authorId);

        if (profile == null) {
            profile = new Profile();
            profile.setAuthor(author);
        }

        model.addAttribute("author", author);
        model.addAttribute("profile", profile);

        if (profile.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(profile.getImage());
            model.addAttribute("base64Image", base64Image);
        }

        return "author_profile_edit";
    }
    
    @PostMapping
    public String saveOrUpdateProfile(@PathVariable("authorId") Long authorId, 
                                      @RequestParam(value = "bio", required = false) String bio,
                                      @RequestParam("imageFile") MultipartFile imageFile) {
        Author author = authorService.getAuthorById(authorId);
        Profile existingProfile = profileService.getProfileByAuthorId(authorId);

        if (existingProfile == null) {
            existingProfile = new Profile();
            existingProfile.setAuthor(author);
        }

        // Update bio only if provided
        if (bio != null && !bio.isEmpty()) {
            existingProfile.setBio(bio);
        }

        // Handle image file
        try {
            if (!imageFile.isEmpty()) {
                BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageFile.getBytes()));

                int targetWidth = 400;
                int targetHeight = 400;
                BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

                Graphics2D g2d = resizedImage.createGraphics();
                g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
                g2d.dispose();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(resizedImage, "jpg", baos);
                baos.flush();
                byte[] resizedImageBytes = baos.toByteArray();
                baos.close();

                existingProfile.setImage(resizedImageBytes);
            }
        } catch (IOException e) {
            System.err.println("Error processing the image file: " + e.getMessage());
            e.printStackTrace();
        }

        existingProfile.setAuthor(author);
        profileService.saveProfile(existingProfile);
        return "redirect:/authors/" + authorId + "/profile";
    }



}
