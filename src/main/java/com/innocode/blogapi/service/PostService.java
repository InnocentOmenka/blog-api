package com.innocode.blogapi.service;

import com.innocode.blogapi.exception.PostNotFoundException;
import com.innocode.blogapi.model.entity.Post;
import com.innocode.blogapi.model.entity.User;
import com.innocode.blogapi.model.request.PostRequest;
import com.innocode.blogapi.model.response.ApiResponse;
import com.innocode.blogapi.model.response.PostResponse;
import com.innocode.blogapi.repository.PostRepository;
import com.innocode.blogapi.repository.UserRepository;
import com.innocode.blogapi.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public ApiResponse<Object> createPost(PostRequest postReq) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException(String.format("User with email '%s' not found", email)));

        Post post = new Post();
        post.setTitle(postReq.getTitle());
        post.setContent(postReq.getContent());
        post.setAuthor(user);
        post = postRepository.save(post);
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setAuthor(user.getUsername());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());

        return ApiResponse.builder()
                .status("SUCCESS")
                .message("Post created successfully")
                .data(response)
                .build();
    }

    public ApiResponse<Object> getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new PostNotFoundException(String.format("Post with id '%s' not found", id)));

        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setAuthor(post.getAuthor().getUsername());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        return new ApiResponse<>("SUCCESS", "Post retrieved successfully", response);
    }

    public ApiResponse<Page<PostResponse>> getAllPosts(int pageNumber, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> posts = postRepository.findAll(pageRequest);

        Page<PostResponse> postResponses = posts.map(post -> {
            PostResponse response = new PostResponse();
            response.setId(post.getId());
            response.setTitle(post.getTitle());
            response.setContent(post.getContent());
            response.setAuthor(post.getAuthor().getUsername());
            response.setCreatedAt(post.getCreatedAt());
            response.setUpdatedAt(post.getUpdatedAt());
            return response;
        });

        return ApiResponse.<Page<PostResponse>>builder()
                .status("SUCCESS")
                .message("Posts retrieved successfully")
                .data(postResponses)
                .build();
    }

    public ApiResponse<Object> updatePost(Long id, PostRequest postReq) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Post post = postRepository.findById(id).orElseThrow(() ->
                new PostNotFoundException(String.format("Post with id '%s' not found", id)));

        if (!post.getAuthor().getEmail().equals(email)) {
            return new ApiResponse<>("FORBIDDEN", "You are not allowed to update this post", null);
        }
        post.setTitle(postReq.getTitle());
        post.setContent(postReq.getContent());
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);

        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setAuthor(post.getAuthor().getUsername());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());

        return new ApiResponse<>("SUCCESS", "Post updated successfully", response);
    }

    public ApiResponse<Object> deletePost(Long id) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Post post = postRepository.findById(id).orElseThrow(() ->
                new PostNotFoundException(String.format("Post with id '%s' not found", id)));

        if (!post.getAuthor().getEmail().equals(email)) {
            return new ApiResponse<>("FORBIDDEN", "You are not allowed to delete this post", null);
        }
        postRepository.delete(post);
        return new ApiResponse<>("SUCCESS", "Post deleted successfully", null);
    }
}
