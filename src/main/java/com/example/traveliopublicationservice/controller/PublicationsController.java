package com.example.traveliopublicationservice.controller;

import com.example.traveliopublicationservice.dto.CommentRequestDto;
import com.example.traveliopublicationservice.dto.NewPostDto;
import com.example.traveliopublicationservice.model.Comment;
import com.example.traveliopublicationservice.model.Post;
import com.example.traveliopublicationservice.service.FirebaseService;
import com.example.traveliopublicationservice.service.PublicationsService;
import com.example.traveliopublicationservice.util.EntityMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/publications")
public class PublicationsController {
    @Autowired
    private PublicationsService publicationsService;
    @Autowired
    private EntityMapper mapper;
    @Autowired
    private FirebaseService firebaseService;


    @GetMapping("/publication")
    public ResponseEntity<?> getPublication(@RequestParam long id) {
        return new ResponseEntity<>(mapper.map(publicationsService.getPost(id)), HttpStatus.OK);
    }

    @PostMapping("/publication")
    public ResponseEntity<?> createPublication(@RequestBody NewPostDto dto, @SessionAttribute String username) {
        Post post = publicationsService.createPost(dto, username);
        return new ResponseEntity<>(mapper.map(post), HttpStatus.OK);
    }

    @PatchMapping("/publication/{id}")
    public ResponseEntity<?> updatePublication(@RequestBody NewPostDto dto, @PathVariable long id) {
        Post post = publicationsService.updatePost(id, dto);
        return new ResponseEntity<>(mapper.map(post), HttpStatus.OK);
    }

    @DeleteMapping("/publication/{id}")
    public ResponseEntity<?> deletePublication(@PathVariable long id) {
        publicationsService.removePost(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping("/publication/{id}/image")
    public ResponseEntity<?> uploadImage(@PathVariable long id, @RequestParam MultipartFile image) {
        System.out.println(image);
        if (publicationsService.getPost(id) == null) {
            return ResponseEntity.badRequest().build();
        }
        Post post = publicationsService.getPost(id);
        String img = firebaseService.uploadFile(image);
        post.setImage(img);
        publicationsService.update(post);
        return new ResponseEntity<>(mapper.map(post), HttpStatus.OK);
    }

    @SneakyThrows
    @DeleteMapping("/publication/{id}/image")
    public ResponseEntity<?> deleteImage(@PathVariable long id) {
        if (publicationsService.getPost(id) == null) {
            return ResponseEntity.badRequest().build();
        }
        publicationsService.removeImg(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/publication/{id}/like")
    public ResponseEntity<HttpStatus> likePublication(@PathVariable long id, @SessionAttribute String username) {
        publicationsService.likePost(id, username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/publication/{id}/dislike")
    public ResponseEntity<HttpStatus> dislikePublication(@PathVariable long id, @SessionAttribute String username) {
        publicationsService.dislikePost(id, username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //get likes for publication
    @GetMapping("/publication/{id}/likes")
    public ResponseEntity<?> getLikesForPublication(@PathVariable long id) {
        return new ResponseEntity<>(publicationsService.getLikes(id), HttpStatus.OK);
    }

    //get comments for publication
    @GetMapping("/publication/{id}/comments")
    public ResponseEntity<?> getCommentsForPost(@PathVariable long id) {
        return new ResponseEntity<>(publicationsService.getComments(id), HttpStatus.OK);
    }

    //create comment
    @PostMapping("/publication/comment")
    public ResponseEntity<Comment> createComment(@RequestBody CommentRequestDto dto, @SessionAttribute String username) {
        Comment comment = publicationsService.createComment(
                Comment.builder()
                        .refType(dto.type)
                        .refEntityId(dto.refEntityId)
                        .createdAt(LocalDateTime.now())
                        .body(dto.text)
                        .author(publicationsService.getAuthor(username))
                        .build()
        );
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    //delete comment
    @DeleteMapping("/publication/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable long id){
        publicationsService.deleteComment(id);
        return ResponseEntity.ok().build();
    }

    //modify comment
    @PatchMapping("/publication/comment/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable long id, @RequestParam(name = "text") String newText){
        return new ResponseEntity<>(publicationsService.updateComment(id, newText), HttpStatus.OK);
    }

    //like comment
    @PostMapping("/publication/comment/{id}/like")
    public ResponseEntity<?> likeComment(@PathVariable long id, @SessionAttribute String username){
        publicationsService.likeComment(id, username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //dislike comment
    @DeleteMapping("/publication/comment/{id}/dislike")
    public ResponseEntity<?> dislikeComment(@PathVariable long id, @SessionAttribute String username){
        publicationsService.dislikeComment(id, username);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
