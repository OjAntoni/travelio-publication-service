package com.example.traveliopublicationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime createdAt;
    @ManyToOne
    private Author author;
    private String title;
    private String image;
    private String body;
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Like> likes;

}
