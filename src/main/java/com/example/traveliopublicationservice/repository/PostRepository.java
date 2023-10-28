package com.example.traveliopublicationservice.repository;

import com.example.traveliopublicationservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
