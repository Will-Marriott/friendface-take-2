package com.scottlogic.grad_training.friendface.filter_api;

import com.scottlogic.grad_training.friendface.user_post_api.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFilterRepository extends JpaRepository<UserPost, Object> {
}
