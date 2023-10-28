package com.example.traveliopublicationservice.repository;

import com.example.traveliopublicationservice.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByUsername(String username);
    Author getAuthorByUsername(String username);
}
