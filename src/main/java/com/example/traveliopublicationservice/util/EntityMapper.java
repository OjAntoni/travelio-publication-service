package com.example.traveliopublicationservice.util;

import com.example.traveliopublicationservice.dto.PostResponseDto;
import com.example.traveliopublicationservice.model.Post;
import com.example.traveliopublicationservice.repository.CommentRepository;
import com.example.traveliopublicationservice.repository.LikeRepository;
import com.example.traveliopublicationservice.service.FirebaseService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private FirebaseService firebaseService;

    @SneakyThrows
    public PostResponseDto map(Post post){
        if(post==null) return null;
        return PostResponseDto.builder()
                .id(post.getId())
                .author(post.getAuthor())
                .body(post.getBody())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .image(post.getImage()!=null ? firebaseService.generateURL(post.getImage()).toString() : null)
                .commentsCount(commentRepository.countAllByRefEntityId(post.getId()))
                .likesCount(post.getLikes().size())
                .build();
    }
}
