package com.scottlogic.grad_training.friendface.filter_api;

import com.scottlogic.grad_training.friendface.user_post_api.UserPost;
import com.scottlogic.grad_training.friendface.user_post_api.UserPostsRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PostFilterService {

    private final UserPostsRepository userPostsRepository;
    private final Logger logger = Logger.getLogger(PostFilterService.class.getName()); // Fixed logger instantiation

    public PostFilterService(UserPostsRepository userPostsRepository) {
        this.userPostsRepository = userPostsRepository;
    }

    public List<UserPost> filterByKeyword(String keyword) {
        List<UserPost> allPosts = userPostsRepository.findAll();
        if (allPosts == null) {
            return Collections.emptyList();
        }

        return allPosts.stream()
                .filter(post -> post.getAuthor().toLowerCase().contains(keyword.toLowerCase())
                        || post.getContent().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<UserPost> filterByDateRange(String fromDate, String toDate) {
        List<UserPost> allPosts = userPostsRepository.findAll();
        if (allPosts == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date from, to;

        try {
            from = dateFormat.parse(fromDate);
            to = dateFormat.parse(toDate);
        } catch (ParseException e) {
            // Log the error and return an empty list
            logger.log(Level.SEVERE, "Error parsing date: " + e.getMessage(), e);
            return Collections.emptyList();
        }

        return allPosts.stream()
                .filter(post -> {
                    try {
                        Date postDate = dateFormat.parse(post.getDate());
                        return postDate.compareTo(from) >= 0 && postDate.compareTo(to) <= 0;
                    } catch (ParseException e) {
                        // Log the error and skip the post
                        logger.log(Level.SEVERE, "Error parsing date: " + e.getMessage(), e);
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
}
