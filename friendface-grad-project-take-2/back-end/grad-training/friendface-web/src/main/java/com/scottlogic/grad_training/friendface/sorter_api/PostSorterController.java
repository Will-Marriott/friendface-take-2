package com.scottlogic.grad_training.friendface.sorter_api;

import com.scottlogic.grad_training.friendface.user_post_api.UserPost;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/sorter-api")

public class PostSorterController {

    private final PostSorterService postSorterService;

    public PostSorterController(PostSorterService postSorterService) {
        this.postSorterService = postSorterService;
    }


// Author sorting
    @GetMapping("/sort-author-asc")
    public List<UserPost> getUserPostsAuthorAsc() {
    return postSorterService.sortAuthorAsc();
}

    @GetMapping("/sort-author-desc")
    public List<UserPost> getUserPostsAuthorDesc() {
        return postSorterService.sortAuthorDesc();
    }

    @GetMapping("/sort-date-newest-first")
    public List<UserPost> getUserPostsDateNewestFirst() {
        return postSorterService.sortDateNewestFirst();
    }

    @GetMapping("/sort-date-oldest-first")
    public List<UserPost> getUserPostsDateOldestFirst() {
        return postSorterService.sortDateOldestFirst();
    }
}
