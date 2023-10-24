package com.scottlogic.grad_training.friendface.user_post_api;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Component
@Service

public class UserPostsService {
    private final UserPostsRepository userPostsRepository;


    public UserPostsService(UserPostsRepository userPostsRepository) {
        this.userPostsRepository = userPostsRepository;
    }

    public List<UserPost> findAllPosts() {
        return userPostsRepository.findAll();
    }

    public void save(UserPost post) {
        userPostsRepository.save(post);
    }

    public UserPost findById(Long id) {
        Optional<UserPost> post = userPostsRepository.findById(id);
        return post.orElse(null);
    }

    public UserPost updatePost(UserPost updatedPost) {
        // Fetch the existing post by ID
        UserPost existingPost = userPostsRepository.findById(updatedPost.getId()).orElse(null);

        if (existingPost != null) {
            // Update the existing post with the new data
            existingPost.setAuthor(updatedPost.getAuthor());
            existingPost.setContent(updatedPost.getContent());
            existingPost.setDate(updatedPost.getDate());
            existingPost.setColour(updatedPost.getColour());

            // Save the updated post to the database
            return userPostsRepository.save(existingPost);
        }

        return null; // Handle the case where the post doesn't exist
    }
}
