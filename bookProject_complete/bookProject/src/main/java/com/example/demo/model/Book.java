package com.example.demo.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String isbn;
    //private String genre;
    private LocalDate publishedDate;
    
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;
    
    @Transient  // This field is not stored in the database
    private String base64Image;
    
    @Lob  // Indicates that this field will store a large object (text)
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
    
    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)  // Establishing relationship with Genre
    private Genre genre;
    
    public Book() {
        super();
    }
    
    public Book(Long id, String title, String isbn, LocalDate publishedDate, Author author, Genre genre, String description) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.publishedDate = publishedDate;
        this.author = author;
        this.genre = genre;
        this.description = description;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    // Getter and setter for 'base64Image' field
    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    // Override equals and hashCode methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
