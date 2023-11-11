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
@Builder
@AllArgsConstructor
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

    public void like(Like like){
        likes.add(like);
    }

}
