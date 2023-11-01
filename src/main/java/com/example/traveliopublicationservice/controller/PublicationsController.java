package com.example.traveliopublicationservice.controller;

import com.example.traveliopublicationservice.dto.NewPostDto;
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
    public ResponseEntity<?> getPublication(@RequestParam long id){
        return new ResponseEntity<>(mapper.map(publicationsService.getPost(id)), HttpStatus.OK);
    }

    @PostMapping("/publication")
    public ResponseEntity<?> createPublication(@RequestBody NewPostDto dto, @SessionAttribute String username){
        Post post = publicationsService.createPost(dto, username);
        return new ResponseEntity<>(mapper.map(post), HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping("/publication/{id}/image")
    public ResponseEntity<?> uploadImage(@PathVariable long id, @RequestParam MultipartFile image){
        System.out.println(image);
        if (publicationsService.getPost(id)==null) {
            return ResponseEntity.badRequest().build();
        }
        Post post = publicationsService.getPost(id);
        String img = firebaseService.uploadFile(image);
        post.setImage(img);
        publicationsService.update(post);
        return new ResponseEntity<>(mapper.map(post), HttpStatus.OK);
    }
//    @PatchMapping("/publication")
//    public ResponseEntity<?> getPublication(@RequestParam long id){
//
//    }
//
//    @DeleteMapping("/publication")
//    public ResponseEntity<?> getPublication(@RequestParam long id){
//
//    }
//
//    @PatchMapping("/publication/like")
//    public ResponseEntity<?> getPublication(@RequestParam long id){
//
//    }
//
//    @PostMapping("/publication/comment")
//    public ResponseEntity<?> getPublication(@RequestParam long id){
//
//    }
//
//    @GetMapping("/publication/comments")
//    public ResponseEntity<?> getPublication(@RequestParam long id){
//
//    }
//
//    @PatchMapping("/publication/comment")
//    public ResponseEntity<?> getPublication(@RequestParam long id){
//
//    }
//
//    @PostMapping("/publication/comment/like")
//    public ResponseEntity<?> getPublication(@RequestParam long id){
//
//    }

}
