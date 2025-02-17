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

import com.example.demo.model.Genre;
import com.example.demo.service.GenreService;

@Controller
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    GenreService genreService;
    
 // Get all genres or search by name
    @GetMapping
    public String getAllGenres(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Genre> genres;
        if (search != null && !search.isEmpty()) {
            genres = genreService.searchGenresByName(search);
        } else {
            genres = genreService.getAllGenres();
        }
        model.addAttribute("genres", genres);
        return "genres";
    }

    // Display form to create a new genre
    @GetMapping("/new")
    public String createGenreForm(Model model) {
        model.addAttribute("genre", new Genre());
        return "genre_form";  // Create a 'genre_form.html' view for the genre form
    }
    
    @PostMapping
    public String createOrUpdateGenre(Genre genre) {
        if (genre.getId() != null) {
            Genre existingGenre = genreService.getGenreById(genre.getId());
            if (existingGenre != null) {
                // Update existing genre
                genre.setBooks(existingGenre.getBooks());
            }
        }
        genreService.saveGenre(genre);
        return "redirect:/genres";
    }

    // Edit genre
    @GetMapping("/edit/{id}")
    public String editGenre(@PathVariable("id") Long id, Model model) {
        Genre genre = genreService.getGenreById(id);
        model.addAttribute("genre", genre);
        return "genre_form";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteGenre(@PathVariable("id") Long id, Model model) {
        try {
            genreService.deleteGenreById(id);  // This will now delete associated books too
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/api/genres?error=" + e.getMessage();
        }
        return "redirect:/genres";
    }
    

}
