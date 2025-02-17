package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Author;
import com.example.demo.model.Book;
import com.example.demo.repository.AuthorRepository;

@Service
public class AuthorService {
	
	@Autowired
	AuthorRepository authorRepository;
	
	public List<Author> getAllAuthors() {
		return (List<Author>) authorRepository.findAll();
	}
	
	public Author getAuthorById(Long id) {
		return authorRepository.findById(id).orElse(null);
	}
	
	public Author saveAuthor(Author author) {
		return authorRepository.save(author);
	}
	
	public void deleteAuthorById(Long id) {
		authorRepository.deleteById(id);
	}
	
	public Author updateAuthor(Author updatedAuthor) {
	    Author existingAuthor = authorRepository.findById(updatedAuthor.getId())
	        .orElseThrow(() -> new RuntimeException("Author not found"));

	    // Update author details
	    existingAuthor.setName(updatedAuthor.getName());
	    existingAuthor.setEmail(updatedAuthor.getEmail());

	    // Handle the books
	    List<Book> existingBooks = existingAuthor.getBooks();

	    // Remove books that are no longer in the updated list
	    existingBooks.removeIf(book -> !updatedAuthor.getBooks().contains(book));

	    // Update or add new books
	    for (Book updatedBook : updatedAuthor.getBooks()) {
	        if (existingBooks.contains(updatedBook)) {
	            // Update existing book
	            Book existingBook = existingBooks.get(existingBooks.indexOf(updatedBook));
	            existingBook.setTitle(updatedBook.getTitle());
	            existingBook.setIsbn(updatedBook.getIsbn());
	            existingBook.setGenre(updatedBook.getGenre());
	            existingBook.setPublishedDate(updatedBook.getPublishedDate());
	        } else {
	            // Add new book
	            updatedBook.setAuthor(existingAuthor);
	            existingBooks.add(updatedBook);
	        }
	    }

	    // Save the updated author
	    return authorRepository.save(existingAuthor);
	}
	
	
	//For getAllBooks() in BookController
	public Author getDefaultAuthor() {
	    List<Author> authors = getAllAuthors();
	    return authors.isEmpty() ? null : authors.get(0); // Return the first author or null if none exist
	}

	public List<Author> searchAuthorsByName(String name) {
	    return authorRepository.findByNameContainingIgnoreCase(name);
	}
	
	// Search author by ID
    public List<Author> searchAuthorsByID(Long id) {
        Author author = authorRepository.findById(id).orElse(null);
        return author != null ? List.of(author) : List.of(); // Return author as a list or empty
    }



}
