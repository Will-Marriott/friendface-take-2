package com.scottlogic.grad_training.friendface.filter_api;

import com.scottlogic.grad_training.friendface.user_post_api.UserPost;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/filter-api")
public class PostFilterController {

    private final PostFilterService postFilterService;

    public PostFilterController(PostFilterService postFilterService) {
        this.postFilterService = postFilterService;
    }

    @GetMapping("/keyword-filter")
    public List<UserPost> filterByKeyword(@RequestParam String keyword) {
        return postFilterService.filterByKeyword(keyword);
    }



    @GetMapping("/date-filter")
    public List<UserPost> filterByDateRange(@RequestParam String fromDate, @RequestParam String toDate) {
        return postFilterService.filterByDateRange(fromDate, toDate);
    }

}
