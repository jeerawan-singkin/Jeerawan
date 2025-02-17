package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    GenreService genreService;  // Inject GenreService for handling Genre entity

    public List<Book> getAllBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }
    
    public Book updateBook(Long id, Book book) {
        Book existingBook = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Book not found"));
        existingBook.setTitle(book.getTitle());
        existingBook.setIsbn(book.getIsbn());
        existingBook.setGenre(book.getGenre());
        existingBook.setPublishedDate(book.getPublishedDate());
        existingBook.setDescription(book.getDescription());  // Update description
        return bookRepository.save(existingBook);
    }

    
    public List<Book> getBooksByGenre(Long genreId) {
        return bookRepository.findByGenreId(genreId);  // Assuming you have a query for this
    }


    public List<Book> getBooksByPublishedDate(LocalDate publishedDate) {
        return bookRepository.findByPublishedDate(publishedDate);
    }
    
    public Page<Book> getPaginatedBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Page<Book> getPaginatedBooksByGenre(Long genreId, Pageable pageable) {
        return bookRepository.findByGenreId(genreId, pageable); // Assuming you have a query for this
    }
    
    // Method to get books by genre and author
    public List<Book> getBooksByGenreAndAuthor(Long genreId, Long authorId) {
        return bookRepository.findByGenreIdAndAuthorId(genreId, authorId);
    }

    // Method to get books by author only
    public List<Book> getBooksByAuthor(Long authorId) {
        return bookRepository.findByAuthorId(authorId);
    }
    
    public Page<Book> getPaginatedBooksById(Long id, Pageable pageable) {
        return bookRepository.findById(id, pageable);
    }
    
    public Page<Book> getPaginatedBooksByTitle(String title, Pageable pageable) {
        return bookRepository.findByTitleContaining(title, pageable);
    }

}
