package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "author")
public class Author {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();  // Initialize here

    public Author() {
        super();
    }
    
    @OneToOne(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;  // One-to-one relationship with Profile

    public Author(Long id, String name, String email) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.books = new ArrayList<>();  // Initialize here too
    }
	
	@Override
	public String toString() {
	    return "Author [id=" + id + ", name=" + name + ", email=" + email + "]";
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
	public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
	
}
