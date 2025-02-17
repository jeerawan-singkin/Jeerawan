package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.demo.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
	
	List<Book> findByGenreId(Long genreId);
    
    List<Book> findByPublishedDate(LocalDate publishedDate);
    
    List<Book> findByDescriptionContaining(String keyword);
    
    Page<Book> findByGenreId(Long genreId, Pageable pageable);
    
    List<Book> findByGenreIdAndAuthorId(Long genreId, Long authorId);

    // Method to get books by author only
    List<Book> findByAuthorId(Long authorId);
    
    Page<Book> findById(Long id, Pageable pageable);

    Page<Book> findByTitleContaining(String title, Pageable pageable);


}
