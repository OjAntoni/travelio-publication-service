package com.example.traveliopublicationservice.repository;

import com.example.traveliopublicationservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    long countAllByRefEntityId(long id);
}
