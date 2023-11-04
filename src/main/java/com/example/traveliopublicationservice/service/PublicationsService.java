package com.example.traveliopublicationservice.service;

import com.example.traveliopublicationservice.dto.NewPostDto;
import com.example.traveliopublicationservice.model.Author;
import com.example.traveliopublicationservice.model.Post;
import com.example.traveliopublicationservice.repository.AuthorRepository;
import com.example.traveliopublicationservice.repository.CommentRepository;
import com.example.traveliopublicationservice.repository.LikeRepository;
import com.example.traveliopublicationservice.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PublicationsService {
    private AuthorRepository authorRepository;
    private CommentRepository commentRepository;
    private LikeRepository likeRepository;
    private PostRepository postRepository;

    @Transactional
    public Post createPost(NewPostDto dto, String username){
        Author author;
        if (!authorRepository.existsByUsername(username)) {
            author = authorRepository.save(Author.builder().
                    username(username)
                    .rating(0).build());
        } else {
            author = authorRepository.getAuthorByUsername(username);
        }
        author.setRating(author.getRating()+10);

        Post post = Post.builder().
                createdAt(LocalDateTime.now())
                .likes(List.of())
                .title(dto.title)
                .body(dto.body)
                .author(author)
                .build();
        return postRepository.save(post);
    }

    @Transactional
    public Post getPost(long id) {
        Optional<Post> byId = postRepository.findById(id);
        if (byId.isPresent()) {
            Post post = byId.get();
            Hibernate.initialize(post.getAuthor());
            Hibernate.initialize(post.getLikes());
            return post;
        }
        return null;
    }

    public void update(Post post) {
        postRepository.save(post);
    }

    public void removeImg(long id) {
        Optional<Post> byId = postRepository.findById(id);
        if (byId.isPresent()) {
            byId.get().setImage(null);
            postRepository.save(byId.get());
        }
    }

    @Transactional
    public Post updatePost(long id, NewPostDto dto) {
        postRepository.findById(id).ifPresent(p -> {
            p.setTitle(dto.title);
            p.setBody(dto.body);
        });
        Optional<Post> byId = postRepository.findById(id);
        if (byId.isPresent()) {
            Post post = byId.get();
            Hibernate.initialize(post.getAuthor());
            Hibernate.initialize(post.getLikes());
            return post;
        } else {
            return null;
        }
    }

    public void removePost(long id){
        postRepository.findById(id);
    }
}
