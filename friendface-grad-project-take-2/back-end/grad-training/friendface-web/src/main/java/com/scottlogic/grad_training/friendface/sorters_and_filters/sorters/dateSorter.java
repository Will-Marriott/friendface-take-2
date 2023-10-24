package com.scottlogic.grad_training.friendface.sorters_and_filters.sorters;
import com.scottlogic.grad_training.friendface.user_post_api.UserPost;

import java.util.List;
import java.util.stream.Collectors;
public class dateSorter {


    public static class DateSorter {

        public List<UserPost> dateSorter(List<UserPost> posts) {
            return posts.stream()
                    .sorted((post1, post2) -> post1.getDate().compareTo(post2.getDate()))
                    .collect(Collectors.toList());
        }
    }
}
