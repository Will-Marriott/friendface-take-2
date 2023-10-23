package com.scottlogic.grad_training.friendface.sample_work;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
