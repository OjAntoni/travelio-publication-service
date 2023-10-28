package com.example.traveliopublicationservice.util;

import com.example.traveliopublicationservice.dto.PostResponseDto;
import com.example.traveliopublicationservice.model.Post;
import com.example.traveliopublicationservice.repository.CommentRepository;
import com.example.traveliopublicationservice.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private LikeRepository likeRepository;

    public PostResponseDto map(Post post){
        return PostResponseDto.builder()
                .id(post.getId())
                .author(post.getAuthor())
                .body(post.getBody())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .image("some-url")
                .commentsCount(commentRepository.countAllByRefEntityId(post.getId()))
                .likesCount(post.getLikes().size())
                .build();
    }
}
