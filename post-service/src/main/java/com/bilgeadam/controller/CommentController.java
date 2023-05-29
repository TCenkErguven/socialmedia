package com.bilgeadam.controller;

import com.bilgeadam.repository.entity.Comment;
import com.bilgeadam.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/find-all")
    public ResponseEntity<List<Comment>> findAll(){
        return ResponseEntity.ok(commentService.findAll());
    }




}
