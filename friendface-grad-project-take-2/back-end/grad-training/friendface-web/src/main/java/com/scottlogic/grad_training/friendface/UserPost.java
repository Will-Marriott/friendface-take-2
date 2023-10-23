package com.scottlogic.grad_training.friendface;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
    @Table(name="posts")
    public class UserPost {
        @Id
        @Column int id;
        @Column
        private String author;
        @Column
        private int likes;
        @Column
        private String colour;
        @Column
        private String content;
        @Column
        private String date;


    public UserPost(String author, String dateTime, String content, int likeCount) {
        this.author = author;
        this.date = dateTime;
        this.content = content;
        this.likes = likeCount;
    }

    public UserPost() {

    }

//Setters

    public void setId(int id) {
        this.id = id;
    }
    public void setAuthor(String author) {
            this.author = author;
        }
    public void setLikes(int likes) {
        this.likes = likes;
    }
    public void setColour(String colour) { this.colour = colour;}
    public void setContent(String content) {this.content = content;}
    public void setDate(String date) {this.date = date;}


    
    //Getters
    public int getId() {
        return id;
    }
    public String getAuthor() {
        return author;
    }
    public int getLikes() {
        return likes;
    }

    public String getColour() {
        return colour;
    }
    public String getContent() {
        return content;
    }
    public String getDate() {
        return date;
    }

//public Object getUserPosts() {
//        return
//}
    }
