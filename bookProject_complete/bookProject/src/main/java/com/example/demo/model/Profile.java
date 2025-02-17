package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;

@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob  // Indicates that this field will store a large object (text)
    @Column(columnDefinition = "LONGTEXT")
    private String bio;

    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;


    @OneToOne
    @JoinColumn(name = "author_id", nullable = false)  // Create foreign key for author
    private Author author;
    
    @Transient  // This tells JPA not to persist this field in the database
    private String base64Image;

    public Profile() {
    }

    public Profile(Long id, String bio, byte[] image, Author author) {
        this.id = id;
        this.bio = bio;
        this.image = image;
        this.author = author;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
    
    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}
