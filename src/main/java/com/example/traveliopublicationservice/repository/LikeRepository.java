package com.example.traveliopublicationservice.repository;

import com.example.traveliopublicationservice.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
