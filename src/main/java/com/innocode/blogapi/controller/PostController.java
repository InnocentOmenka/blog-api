package com.innocode.blogapi.controller;

import com.innocode.blogapi.model.request.PostRequest;
import com.innocode.blogapi.model.response.ApiResponse;
import com.innocode.blogapi.model.response.PostResponse;
import com.innocode.blogapi.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(
            @RequestBody @Valid PostRequest postReq) {
        return ResponseEntity.ok(postService.createPost(postReq));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getPostById(@PathVariable Long id) {
        ApiResponse<Object> response = postService.getPostById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ApiResponse<Page<PostResponse>> response = postService.getAllPosts(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updatePost(@PathVariable Long id, @RequestBody @Valid PostRequest postReq) {
        ApiResponse<Object> response = postService.updatePost(id, postReq);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deletePost(@PathVariable Long id) {
        ApiResponse<Object> response = postService.deletePost(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
