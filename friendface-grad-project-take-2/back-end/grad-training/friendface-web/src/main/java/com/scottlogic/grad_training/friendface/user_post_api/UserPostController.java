package com.scottlogic.grad_training.friendface.user_post_api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/posts-api")
public class UserPostController {
    private final UserPostsService userPostsService;


    public UserPostController(UserPostsService userPostsService) {
        this.userPostsService = userPostsService;
    }

    @GetMapping("/posts")
    public List<UserPost> getUserPosts() {
        return userPostsService.findAllPosts();
    };

    @PostMapping("/posts")
    @CrossOrigin("http://localhost:3000")
    public ResponseEntity<UserPost> addPost(@RequestBody UserPost post) {
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Save the UserPost to the database using your service
        UserPost savedPost = new UserPost();

        userPostsService.save(post);

        if (savedPost != null) {
            return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<UserPost> getPostById(@PathVariable Long id) {
        // Use your userPostsService to fetch the post by ID
        UserPost post = userPostsService.findById(id);

        if (post != null) {
            return new ResponseEntity<>(post, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<UserPost> updatePost(
            @PathVariable Long id,
            @RequestBody UserPost updatedPost) {
        UserPost existingPost = userPostsService.findById(id);

        if (existingPost == null) {
            return ResponseEntity.notFound().build();
        }

        // Update the properties of the existing post with the data from the updatedPost
        existingPost.setAuthor(updatedPost.getAuthor());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setDate(updatedPost.getDate());
        existingPost.setColour(updatedPost.getColour());
        existingPost.setLikes(updatedPost.getLikes());

        // Save the updated post
        UserPost updated = userPostsService.updatePost(existingPost);

        return ResponseEntity.ok(updated);
    }



}
