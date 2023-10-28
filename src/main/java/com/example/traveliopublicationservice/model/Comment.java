package com.example.traveliopublicationservice.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Author author;
    private String body;
    private LocalDateTime createdAt;
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Like> likes;
    private ERefType refType;
    private long refEntityId;

}
