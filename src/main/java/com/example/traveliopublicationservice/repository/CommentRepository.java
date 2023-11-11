package com.example.traveliopublicationservice.repository;

import com.example.traveliopublicationservice.model.Comment;
import com.example.traveliopublicationservice.model.ERefType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    long countAllByRefEntityId(long id);
    void deleteAllByRefEntityIdAndRefType(long id, ERefType type);
    List<Comment> findAllByRefEntityIdAndRefType(long id, ERefType type);
}
