package com.example.demo.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Author;
import com.example.demo.model.Book;
import com.example.demo.model.Genre;
import com.example.demo.service.AuthorService;
import com.example.demo.service.BookService;
import com.example.demo.service.GenreService;

@Controller
@RequestMapping("/authors/{authorId}/books")
public class AuthorBookController {

    @Autowired
    BookService bookService;

    @Autowired
    AuthorService authorService;

    @Autowired
    GenreService genreService;  // Inject GenreService to handle genres
	
	private String convertToBase64(byte[] imageBytes) {
	    return Base64.getEncoder().encodeToString(imageBytes);
	}

	
	@GetMapping
	public String getAllBooks(@PathVariable("authorId") Long authorId, Model model) {
	    Author author = authorService.getAuthorById(authorId);
	    List<Book> listBooks = author.getBooks();  // Fetch books for this author
	    List<Genre> genres = genreService.getAllGenres();  // Fetch genres for the filter
	    
	    // Convert each book's image to Base64
	    listBooks.forEach(book -> {
	        if (book.getImage() != null) {
	            book.setBase64Image(convertToBase64(book.getImage()));
	        }
	    });

	    model.addAttribute("books", listBooks);
	    model.addAttribute("author", author);
	    model.addAttribute("genres", genres);  // Add genres to the model
	    return "author_books";
	}



    @GetMapping("/{id}")
    public String getBookById(@PathVariable("authorId") Long authorId, @PathVariable("id") Long id, Model model) {
        Book book = bookService.getBookById(id);
        List<Genre> genres = genreService.getAllGenres();  // Fetch all genres for the form
        model.addAttribute("book", book);
        model.addAttribute("author", authorService.getAuthorById(authorId));
        model.addAttribute("genres", genres);  // Pass genres to the form
        
        // Convert book image to base64 and add to model
        if (book.getImage() != null) {
            model.addAttribute("base64Image", convertToBase64(book.getImage()));
        }

        return "author_book_form";
    }
    
    @GetMapping("/new")
    public String createBookForm(@PathVariable("authorId") Long authorId, Model model) {
        Author author = authorService.getAuthorById(authorId);
        Book book = new Book();
        book.setAuthor(author);  // Ensure the new book is associated with the correct author
        List<Genre> genres = genreService.getAllGenres();  // Fetch genres for the new book form
        model.addAttribute("book", book);
        model.addAttribute("author", author);
        model.addAttribute("genres", genres);  // Add genres to model
        return "author_book_form";  // Render the form
    }
    
    @PostMapping
    public String createOrUpdateBook(
        @PathVariable("authorId") Long authorId,
        @ModelAttribute Book book, // Use @ModelAttribute to bind form data to Book object
        @RequestParam("genreId") Long genreId,
        @RequestParam("description") String description,
        @RequestParam("imageFile") MultipartFile imageFile,  // File upload for book cover image
        Model model) {

        Author author = authorService.getAuthorById(authorId);
        Genre genre = genreService.getGenreById(genreId);

        // Handle book ID presence
        if (book.getId() != null) {
            Book existingBook = bookService.getBookById(book.getId());
            if (existingBook == null) {
                return "redirect:/authors/" + authorId + "/books?error=BookNotFound";
            }
            book.setImage(existingBook.getImage()); // Retain existing image if no new image is uploaded
        }

        book.setAuthor(author);
        book.setGenre(genre);
        book.setDescription(description);

        // Handle image processing
        try {
            if (!imageFile.isEmpty()) {
                BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageFile.getBytes()));
                BufferedImage resizedImage = new BufferedImage(400, 600, BufferedImage.TYPE_INT_RGB);

                Graphics2D g2d = resizedImage.createGraphics();
                g2d.drawImage(originalImage, 0, 0, 400, 600, null);
                g2d.dispose();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(resizedImage, "jpg", baos);
                baos.flush();
                byte[] resizedImageBytes = baos.toByteArray();
                baos.close();

                book.setImage(resizedImageBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save the book and redirect
        bookService.saveBook(book);
        return "redirect:/authors/" + authorId + "/books";
    }

    
    
    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable("authorId") Long authorId, @PathVariable("id") Long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return "redirect:/authors/" + authorId + "/books?error=BookNotFound";  // Redirect with error message
        }
        List<Genre> genres = genreService.getAllGenres();
        model.addAttribute("book", book);
        model.addAttribute("author", authorService.getAuthorById(authorId));
        model.addAttribute("genres", genres);
        
        // Convert book image to base64 and add to model
        if (book.getImage() != null) {
            model.addAttribute("base64Image", convertToBase64(book.getImage()));
        }

        return "author_book_form";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("authorId") Long authorId, @PathVariable("id") Long id) {
        bookService.deleteBookById(id);
        return "redirect:/authors/" + authorId + "/books";
    }
    
    @GetMapping("/genre")
    public String getBooksByGenre(@PathVariable("authorId") Long authorId,
                                  @RequestParam(value = "genreId", required = false) Long genreId,
                                  Model model) {
        Author author = authorService.getAuthorById(authorId);
        List<Book> books;

        if (genreId == null || genreId == 0) {
            // If genreId is null or 0, show all books for this author
            books = bookService.getBooksByAuthor(authorId);
        } else {
            // Fetch books by specific genre
            books = bookService.getBooksByGenreAndAuthor(genreId, authorId);  // Assumes you have this method
        }

        List<Genre> genres = genreService.getAllGenres();  // Fetch genres for the dropdown

        // Convert each book's image to Base64
        books.forEach(book -> {
            if (book.getImage() != null) {
                book.setBase64Image(convertToBase64(book.getImage()));
            }
        });

        model.addAttribute("books", books);
        model.addAttribute("author", author);
        model.addAttribute("genres", genres);
        model.addAttribute("selectedGenreId", genreId);  // Pass the selected genreId to the view
        return "author_books";
    }



    @GetMapping("/publishedDate")
    public String getBooksByPublishedDate(@PathVariable("authorId") Long authorId, @RequestParam("publishedDate") String publishedDateStr, Model model) {
        try {
            LocalDate publishedDate = LocalDate.parse(publishedDateStr);
            List<Book> books = bookService.getBooksByPublishedDate(publishedDate);
            
            // Convert each book's image to Base64
            books.forEach(book -> {
                if (book.getImage() != null) {
                    book.setBase64Image(convertToBase64(book.getImage()));
                }
            });

            model.addAttribute("books", books);
            model.addAttribute("author", authorService.getAuthorById(authorId));
            model.addAttribute("genres", genreService.getAllGenres());  // Add genres to the model for filtering

        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Invalid date format");
            return "author_books";  // Show an error on the same page
        }
        return "author_books";
    }


}
