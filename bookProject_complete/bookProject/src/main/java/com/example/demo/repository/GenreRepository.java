package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {  // Change to JpaRepository
	List<Genre> findByNameContainingIgnoreCase(String name);
}
