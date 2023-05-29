package com.bilgeadam.controller;

import com.bilgeadam.dto.request.CreateNewPostRequestDto;
import com.bilgeadam.dto.request.DeleteCommentRequest;
import com.bilgeadam.dto.request.PostCommentRequestDto;
import com.bilgeadam.dto.request.UpdatePostRequest;
import com.bilgeadam.repository.entity.Post;
import com.bilgeadam.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constant.ApiUrls.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @PostMapping("/create-post-feign")
    public ResponseEntity<Post> createPostFeign(String token, CreateNewPostRequestDto dto){
        return ResponseEntity.ok(postService.createPostFeign(token, dto));
    }
    @PostMapping("/create-post")
    public ResponseEntity<Post> createPost(String token, CreateNewPostRequestDto dto){
        return ResponseEntity.ok(postService.createPost(token, dto));
    }
    @GetMapping(FIND_ALL)
    public ResponseEntity<List<Post>> findAll(){
        return ResponseEntity.ok(postService.findAll());
    }

    @PostMapping("/like-post")
    public ResponseEntity<Boolean> likePost(String token,String postId){
        return ResponseEntity.ok(postService.likePost(token,postId));
    }
    @PostMapping("/dislike-post")
    public ResponseEntity<Boolean> dislikePost(String token,String postId){
        return ResponseEntity.ok(postService.dislikePost(token,postId));
    }
    @DeleteMapping ("/delete-post-by-id" + "{id}")
    public ResponseEntity<Boolean> deletePostById(String token,@PathVariable String id){
        postService.deletePostById(token,id);
        return ResponseEntity.ok().build();
    }
    @PutMapping ("/update-post/{token}")
    public ResponseEntity<Post> updatePost(@PathVariable String token,@RequestBody String postId, UpdatePostRequest dto){
        return ResponseEntity.ok(postService.updatePost(token, postId, dto));
    }
    @PostMapping ("/post-comment/{token}")
    public ResponseEntity<Post> postComment(@PathVariable String token,@RequestBody PostCommentRequestDto dto){
        return ResponseEntity.ok(postService.postComment(token, dto));
    }
    @PutMapping("/update-comment/{token}")
    public ResponseEntity<Boolean> updateComment(@PathVariable String token,@RequestBody PostCommentRequestDto dto){
        return ResponseEntity.ok(postService.updateComment(token,dto));
    }
    @DeleteMapping ("/delete-comment/{token}")
    public ResponseEntity<Boolean> deleteCommentById(@PathVariable String token,@RequestBody DeleteCommentRequest dto){
        return ResponseEntity.ok(postService.deleteCommentById(token,dto));
    }


}
