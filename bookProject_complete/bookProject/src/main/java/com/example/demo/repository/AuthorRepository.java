package com.example.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Author;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long>{
	
	List<Author> findByNameContainingIgnoreCase(String name);
	
}
