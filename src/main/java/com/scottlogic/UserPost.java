package com.scottlogic;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class UserPost {
    private String author;
    private OffsetDateTime dateTime;
    private String contents;
    private int likeCount;

    public UserPost(String author, OffsetDateTime dateTime, String contents, int likeCount) {
        this.author = author;
        this.dateTime = dateTime;
        this.contents = contents;
        this.likeCount = likeCount;
    }

    public String getAuthor() {
        return author;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public String getContents() {
        return contents;
    }

    public int getLikeCount() {
        return likeCount;
    }

    @Override
    public String toString() {
        return "Author: " + author + "\n" +
               "Time Posted: " + DateTimeFormatter.RFC_1123_DATE_TIME.format(dateTime) + "\n" +
               "Likes: " + likeCount + "\n" +
               contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPost userPost = (UserPost) o;
        return likeCount == userPost.likeCount &&
                Objects.equals(author, userPost.author) &&
                Objects.equals(dateTime, userPost.dateTime) &&
                Objects.equals(contents, userPost.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, dateTime, contents, likeCount);
    }
}
