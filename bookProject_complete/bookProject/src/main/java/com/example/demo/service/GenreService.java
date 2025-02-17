package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Genre;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.GenreRepository;

@Service
public class GenreService {

    @Autowired
    GenreRepository genreRepository;
    @Autowired
    BookRepository bookRepository;

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre getGenreById(Long id) {
        return genreRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Genre not found"));
    }

    public Genre saveGenre(Genre genre) {
        return genreRepository.save(genre);
    }
    
    public void deleteGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Genre not found"));

        // Remove all books associated with this genre
        genre.getBooks().forEach(book -> {
            bookRepository.delete(book);  // Assuming you have a BookRepository
        });

        // Now delete the genre itself
        genreRepository.deleteById(id);
    }

    
    public void deleteGenreWithBooks(Long id) {
        Genre genre = genreRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Genre not found"));
        
        genre.getBooks().forEach(book -> book.setGenre(null));  // Unlink books before deleting genre
        
        genreRepository.deleteById(id);
    }
    
 // Method to search genres by name
    public List<Genre> searchGenresByName(String name) {
        return genreRepository.findByNameContainingIgnoreCase(name);
    }


}
