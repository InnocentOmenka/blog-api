package com.innocode.blogapi.controller;


import com.innocode.blogapi.model.request.CommentRequest;
import com.innocode.blogapi.model.response.ApiResponse;
import com.innocode.blogapi.model.response.CommentResponse;
import com.innocode.blogapi.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<Object> createComment(
            @PathVariable Long postId,
            @RequestBody @Valid CommentRequest commentReq) {
        ApiResponse<Object> response = commentService.createComment(postId, commentReq);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<Page<CommentResponse>>> getAllCommentsForPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ApiResponse<Page<CommentResponse>> response = commentService.getAllCommentsForPost(postId, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<Object>> updateComment(
            @PathVariable Long id,
            @RequestBody @Valid CommentRequest commentReq) {
        ApiResponse<Object> response = commentService.updateComment(id, commentReq);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteComment(@PathVariable Long id) {
        ApiResponse<Object> response = commentService.deleteComment(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
