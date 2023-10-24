package com.scottlogic.grad_training.friendface.sorter_api;

import com.scottlogic.grad_training.friendface.user_post_api.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostSorterRepository extends JpaRepository<UserPost, Object> {

}
