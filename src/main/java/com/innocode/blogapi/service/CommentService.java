package com.innocode.blogapi.service;

import com.innocode.blogapi.exception.CommentNotFoundException;
import com.innocode.blogapi.exception.PostNotFoundException;
import com.innocode.blogapi.exception.UserNotFoundException;
import com.innocode.blogapi.model.entity.Comment;
import com.innocode.blogapi.model.entity.Post;
import com.innocode.blogapi.model.entity.User;
import com.innocode.blogapi.model.request.CommentRequest;
import com.innocode.blogapi.model.response.ApiResponse;
import com.innocode.blogapi.model.response.CommentResponse;
import com.innocode.blogapi.repository.CommentRepository;
import com.innocode.blogapi.repository.PostRepository;
import com.innocode.blogapi.repository.UserRepository;
import com.innocode.blogapi.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ApiResponse<Object> createComment(Long postId, CommentRequest commentReq) {
        String email = SecurityUtil.getAuthenticatedUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException(String.format("User with email '%s' not found", email)));

        Post post = postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException(String.format("Post with id '%s' not found", postId)));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(user);
        comment.setContent(commentReq.getContent());
        comment = commentRepository.save(comment);

        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setAuthor(user.getUsername());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());

        return ApiResponse.builder()
                .status("SUCCESS")
                .message("Comment created successfully")
                .data(response)
                .build();
    }

    public ApiResponse<Object> updateComment(Long id, CommentRequest commentReq) {
        String email = SecurityUtil.getAuthenticatedUserEmail();
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new CommentNotFoundException(String.format("Comment with id '%s' not found", id)));

        if (!comment.getAuthor().getEmail().equals(email)) {
            return new ApiResponse<>("FORBIDDEN", "You are not allowed to update this comment", null);
        }

        comment.setContent(commentReq.getContent());
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setAuthor(comment.getAuthor().getUsername());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());

        return new ApiResponse<>("SUCCESS", "Comment updated successfully", response);
    }

    public ApiResponse<Object> deleteComment(Long id) {
        String email = SecurityUtil.getAuthenticatedUserEmail();
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new CommentNotFoundException(String.format("Comment with id '%s' not found", id)));

        if (!comment.getAuthor().getEmail().equals(email)) {
            return new ApiResponse<>("FORBIDDEN", "You are not allowed to delete this comment", null);
        }

        commentRepository.delete(comment);
        return new ApiResponse<>("SUCCESS", "Comment deleted successfully", null);
    }

    public ApiResponse<Page<CommentResponse>> getAllCommentsForPost(Long postId, int pageNumber, int pageSize) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException(String.format("Post with id '%s' not found", postId)));

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> comments = commentRepository.findByPost(post, pageable);

        Page<CommentResponse> commentResponses = comments.map(comment -> {
            CommentResponse response = new CommentResponse();
            response.setId(comment.getId());
            response.setContent(comment.getContent());
            response.setAuthor(comment.getAuthor().getUsername());
            response.setCreatedAt(comment.getCreatedAt());
            response.setUpdatedAt(comment.getUpdatedAt());
            return response;
        });

        return ApiResponse.<Page<CommentResponse>>builder()
                .status("SUCCESS")
                .message("Comments retrieved successfully")
                .data(commentResponses)
                .build();
    }
}
