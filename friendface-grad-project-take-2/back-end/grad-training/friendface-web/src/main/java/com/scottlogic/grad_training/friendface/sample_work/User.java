package com.scottlogic.grad_training.friendface.sample_work;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
    @Table(name="users")
    public class User {
        @Id
        @Column
        private String username;

    public User(String username) {
        this.username = username;
    }

    public User() {

    }

    public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }


    }
