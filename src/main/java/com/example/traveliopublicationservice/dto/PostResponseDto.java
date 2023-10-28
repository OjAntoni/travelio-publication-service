package com.example.traveliopublicationservice.dto;

import com.example.traveliopublicationservice.model.Author;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class PostResponseDto {
    public long id;
    public LocalDateTime createdAt;
    public Author author;
    public String title;
    public String body;
    public String image;
    public long likesCount;
    public long commentsCount;
}
