package com.example.traveliopublicationservice.controller;

import com.example.traveliopublicationservice.dto.NewPostDto;
import com.example.traveliopublicationservice.model.Post;
import com.example.traveliopublicationservice.service.PublicationsService;
import com.example.traveliopublicationservice.util.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publications")
public class PublicationsController {
    @Autowired
    private PublicationsService service;
    @Autowired
    private EntityMapper mapper;


    @GetMapping("/publication")
    public ResponseEntity<?> getPublication(@RequestParam long id){
        return null;
    }

    @PostMapping("/publication")
    public ResponseEntity<?> createPublication(@RequestBody NewPostDto dto, @SessionAttribute String username){
        Post post = service.createPost(dto, username);
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
