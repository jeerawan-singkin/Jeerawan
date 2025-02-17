package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Book;
import com.example.demo.model.Genre;
import com.example.demo.service.BookService;
import com.example.demo.service.GenreService;

import java.util.Base64;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;


@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private GenreService genreService;

    // List books with pagination
    @GetMapping
    public String getAllBooks(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10);  // 10 books per page
        Page<Book> booksPage = bookService.getPaginatedBooks(pageable);
        List<Genre> genres = genreService.getAllGenres();

        booksPage.forEach(book -> {
            if (book.getImage() != null) {
                book.setBase64Image(convertToBase64(book.getImage()));
            }
        });

        model.addAttribute("books", booksPage.getContent());
        model.addAttribute("genres", genres);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", booksPage.getTotalPages());

        return "books";
    }
    
    @GetMapping("/{id}")
    public String getBookById(@PathVariable("id") Long id, Model model) {
        Book book = bookService.getBookById(id);
        System.out.println("Fetched Book: " + book);

        if (book == null) {
            return "error/404";
        }

        if (book.getImage() != null) {
            model.addAttribute("base64Image", convertToBase64(book.getImage()));
        } else {
            model.addAttribute("base64Image", "");
        }

        model.addAttribute("book", book);
        return "book_details";
    }

    
    @GetMapping("/filter")
    public String getBooksByGenre(@RequestParam(value = "genreId", required = false) Long genreId,
                                  @RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10); // 10 books per page
        Page<Book> booksPage;

        if (genreId == null || genreId == 0) {
            booksPage = bookService.getPaginatedBooks(pageable); // No genre filter
        } else {
            booksPage = bookService.getPaginatedBooksByGenre(genreId, pageable);
        }

        List<Genre> genres = genreService.getAllGenres();

        booksPage.forEach(book -> {
            if (book.getImage() != null) {
                book.setBase64Image(convertToBase64(book.getImage()));
            }
        });

        model.addAttribute("books", booksPage.getContent());
        model.addAttribute("genres", genres);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", booksPage.getTotalPages());
        model.addAttribute("selectedGenreId", genreId);

        return "books";
    }

    // Utility method for image conversion
    private String convertToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }
    
    @GetMapping("/search")
    public String searchBooks(@RequestParam(required = false) String searchTerm, 
                              @RequestParam(defaultValue = "0") int page, 
                              Model model) {
        Pageable pageable = PageRequest.of(page, 10);  // 10 books per page
        Page<Book> booksPage;
    
        try {
            // Try to parse searchTerm as Long for id search
            Long id = Long.parseLong(searchTerm);
            booksPage = bookService.getPaginatedBooksById(id, pageable);
        } catch (NumberFormatException e) {
            // If parsing fails, search by title
            booksPage = bookService.getPaginatedBooksByTitle(searchTerm, pageable);
        }
    
        List<Genre> genres = genreService.getAllGenres();
    
        booksPage.forEach(book -> {
            if (book.getImage() != null) {
                book.setBase64Image(convertToBase64(book.getImage()));
            }
        });
    
        model.addAttribute("books", booksPage.getContent());
        model.addAttribute("genres", genres);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", booksPage.getTotalPages());
    
        return "books";
    }

}