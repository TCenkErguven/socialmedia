package com.bilgeadam.controller;

import com.bilgeadam.repository.entity.Like;
import com.bilgeadam.service.LikeService;
import com.bilgeadam.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;


    @GetMapping("/find-all")
    public ResponseEntity<List<Like>> findAll(){
        return ResponseEntity.ok(likeService.findAll());
    }
}
