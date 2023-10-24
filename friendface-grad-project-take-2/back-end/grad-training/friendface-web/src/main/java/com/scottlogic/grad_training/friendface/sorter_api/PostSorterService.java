package com.scottlogic.grad_training.friendface.sorter_api;

import com.scottlogic.grad_training.friendface.user_post_api.UserPost;
import com.scottlogic.grad_training.friendface.user_post_api.UserPostsRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
public class PostSorterService {

    private final PostSorterRepository postSorterRepository;

    private final UserPostsRepository userPostsRepository;



    public PostSorterService(PostSorterRepository postSorterRepository, UserPostsRepository userPostsRepository) {
        this.postSorterRepository = postSorterRepository;
        this.userPostsRepository = userPostsRepository;
    }

    public List<UserPost> sortAuthorAsc() {
        List<UserPost> databasePosts = userPostsRepository.findAll();

        // Check if databasePosts is null, return an empty list in that case
        if (databasePosts == null) {
            return Collections.emptyList();
        }

        // Sort the list in ascending order by author (case-insensitive)
        databasePosts.sort(Comparator.comparing(post -> post.getAuthor().toLowerCase()));

        return databasePosts;
    }

    public List<UserPost> sortAuthorDesc() {
        List<UserPost> databasePosts = userPostsRepository.findAll();

        if (databasePosts == null || databasePosts.isEmpty()) {
            // Handle the case when the list is null or empty
            return Collections.emptyList(); // You can return an empty list or throw an exception as needed
        }

        // Sort the list in descending order by author (case-insensitive)
        databasePosts.sort(Comparator.comparing(post -> post.getAuthor().toLowerCase(), Comparator.reverseOrder()));

        return databasePosts;
    }

    public List<UserPost> sortDateOldestFirst() {
        List<UserPost> databasePosts = userPostsRepository.findAll();

        // Sort the list in ascending order by date (oldest first)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return databasePosts.stream()
                .sorted(Comparator.comparing(post -> {
                    try {
                        return dateFormat.parse(post.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return null; // Handle date parsing error
                    }
                }))
                .collect(Collectors.toList());
    }

    public List<UserPost> sortDateNewestFirst() {
        List<UserPost> databasePosts = userPostsRepository.findAll();

        // Sort the list in descending order by date (newest first)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return databasePosts.stream()
                .sorted(Comparator.comparing(post -> {
                    try {
                        return dateFormat.parse(post.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return null; // Handle date parsing error
                    }
                }, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

}
