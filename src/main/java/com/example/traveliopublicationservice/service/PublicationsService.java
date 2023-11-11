package com.example.traveliopublicationservice.service;

import com.example.traveliopublicationservice.dto.NewPostDto;
import com.example.traveliopublicationservice.model.*;
import com.example.traveliopublicationservice.repository.AuthorRepository;
import com.example.traveliopublicationservice.repository.CommentRepository;
import com.example.traveliopublicationservice.repository.LikeRepository;
import com.example.traveliopublicationservice.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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
        Optional<Post> byId = postRepository.findById(id);
        byId.ifPresent(p -> {
            removeCommentRecursive(p.getId());
            commentRepository.deleteAllByRefEntityIdAndRefType(p.getId(), ERefType.POST);
            postRepository.delete(p);
        });
    }

    private void removeCommentRecursive(long postId){
        List<Comment> commToPost = commentRepository.findAllByRefEntityIdAndRefType(postId, ERefType.POST);
        commToPost.forEach(this::removeComm);
    }

    private void removeComm(Comment c) {
        List<Comment> cc = commentRepository.findAllByRefEntityIdAndRefType(c.getId(), ERefType.Comment);
        cc.forEach(this::removeComm);
        commentRepository.delete(c);
    }

    @Transactional
    public void likePost(long id, String username) {
        Author author = null;
        if (!authorRepository.existsByUsername(username)) {
             author = authorRepository.save(Author.builder().username(username).rating(1).build());
        }
        author = authorRepository.getAuthorByUsername(username);

        Optional<Post> byId = postRepository.findById(id);
        if(byId.isPresent()){
            Post post = byId.get();
            Author finalAuthor = author;
            boolean b = post.getLikes().stream().anyMatch(l -> l.getAuthor().getUsername().equals(finalAuthor.getUsername()));
            if(!b){
                Like saved = likeRepository.save(Like.builder().createdAt(LocalDateTime.now()).author(author).refType(ERefType.POST).build());
                post.like(saved);
            }
        }
    }

    @Transactional
    public void dislikePost(long id, String username) {
        Author author = null;
        if (!authorRepository.existsByUsername(username)) {
            return;
        }
        author = authorRepository.getAuthorByUsername(username);

        Optional<Post> byId = postRepository.findById(id);
        if(byId.isPresent()){
            Post post = byId.get();
            Author finalAuthor = author;
            post.getLikes().removeIf(l -> l.getAuthor().getUsername().equals(finalAuthor.getUsername()));
        }
    }

    public List<Like> getLikes(long id) {
        AtomicReference<List<Like>> likes = new AtomicReference<>(List.of());
        Optional<Post> byId = postRepository.findById(id);
        byId.ifPresent(p -> likes.set(p.getLikes()));
        return likes.get();
    }

    @Transactional
    public List<Comment> getComments(long id) {
        List<Comment> finalList = new ArrayList<>(32);
        List<Comment> commToPost = commentRepository.findAllByRefEntityIdAndRefType(id, ERefType.POST);
        finalList.addAll(commToPost);
        commToPost.forEach(c-> finalList.addAll(getCommentsToComment(c)));
        return finalList;
    }

    private List<Comment> getCommentsToComment(Comment comment){
        List<Comment> cc = commentRepository.findAllByRefEntityIdAndRefType(comment.getId(), ERefType.Comment);
        for (Comment c : cc) {
            cc.addAll(getCommentsToComment(c));
        }
        return cc;
    }

    public Author getAuthor(String username){
        if (authorRepository.existsByUsername(username)) {
            return authorRepository.getAuthorByUsername(username);
        } else {
            return authorRepository.save(Author.builder().username(username).rating(0).build());
        }
    }

    public Comment createComment(Comment c) {
        return commentRepository.save(c);
    }

    public void deleteComment(long id) {
        commentRepository.findById(id).ifPresent(c -> c.setBody("DELETED"));
    }

    @Transactional
    public Comment updateComment(long id, String newText) {
        Optional<Comment> byId = commentRepository.findById(id);
        byId.ifPresent(c -> c.setBody(newText));
        return byId.orElse(null);
    }

    public void likeComment(long id, String username) {
        Author author = null;
        if (!authorRepository.existsByUsername(username)) {
            author = authorRepository.save(Author.builder().username(username).rating(1).build());
        }
        author = authorRepository.getAuthorByUsername(username);

        Optional<Comment> byId = commentRepository.findById(id);
        if(byId.isPresent()){
            Comment comment = byId.get();
            Author finalAuthor = author;
            boolean b = comment.getLikes().stream().anyMatch(l -> l.getAuthor().getUsername().equals(finalAuthor.getUsername()));
            if(!b){
                Like saved = likeRepository.save(Like.builder().createdAt(LocalDateTime.now()).author(author).refType(ERefType.Comment).build());
                comment.like(saved);
            }
        }
    }

    public void dislikeComment(long id, String username) {
        Author author = null;
        if (!authorRepository.existsByUsername(username)) {
            return;
        }
        author = authorRepository.getAuthorByUsername(username);

        Optional<Comment> byId = commentRepository.findById(id);
        if(byId.isPresent()){
            Comment comment = byId.get();
            Author finalAuthor = author;
            comment.getLikes().removeIf(l -> l.getAuthor().getUsername().equals(finalAuthor.getUsername()));
        }
    }
}
