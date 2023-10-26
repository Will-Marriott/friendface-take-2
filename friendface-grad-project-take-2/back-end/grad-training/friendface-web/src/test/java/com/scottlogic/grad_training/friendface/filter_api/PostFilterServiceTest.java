package com.scottlogic.grad_training.friendface.filter_api;

import com.scottlogic.grad_training.friendface.user_post_api.UserPost;
import com.scottlogic.grad_training.friendface.user_post_api.UserPostsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostFilterServiceTest {

    private UserPostsRepository userPostsRepository;
    private PostFilterService postFilterService;

    @BeforeEach
    void setUp() {
        userPostsRepository = mock(UserPostsRepository.class);
        postFilterService = new PostFilterService(userPostsRepository);
    }

    @Test
    public void testFilterByKeyword() {
        // Arrange
        List<UserPost> mockPosts = List.of(
                new UserPost("Author1", "2023-10-10", "Content1", 10),
                new UserPost("Author2", "2023-10-11", "Content2", 5)
        );

        when(userPostsRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<UserPost> filteredPosts = postFilterService.filterByKeyword("Author1");

        // Assert
        assertEquals(1, filteredPosts.size());
        assertEquals("Author1", filteredPosts.get(0).getAuthor());
    }

    @Test
    public void testFilterByKeyword_NoMatch() {
        // Arrange
        List<UserPost> mockPosts = List.of(
                new UserPost("Author1", "2023-10-10", "Content1", 10),
                new UserPost("Author2", "2023-10-11", "Content2", 5)
        );

        when(userPostsRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<UserPost> filteredPosts = postFilterService.filterByKeyword("Author3");

        // Assert
        assertEquals(0, filteredPosts.size());
    }

    @Test
    public void testFilterByDateRange() {
        // Arrange
        List<UserPost> mockPosts = List.of(
                new UserPost("Author1", "2023-10-10", "Content1", 10),
                new UserPost("Author2", "2023-10-11", "Content2", 5)
        );

        when(userPostsRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<UserPost> filteredPosts = postFilterService.filterByDateRange("2023-10-10", "2023-10-10");

        // Assert
        assertEquals(1, filteredPosts.size());
        assertEquals("Author1", filteredPosts.get(0).getAuthor());
    }

    @Test
    public void testFilterByDateRange_NoMatch() {
        // Arrange
        List<UserPost> mockPosts = List.of(
                new UserPost("Author1", "2023-10-10", "Content1", 10),
                new UserPost("Author2", "2023-10-11", "Content2", 5)
        );

        when(userPostsRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<UserPost> filteredPosts = postFilterService.filterByDateRange("2023-10-12", "2023-10-13");

        // Assert
        assertEquals(0, filteredPosts.size());
    }

    @Test
    public void testFilterByDateRange_InvalidDate() {
        // Arrange
        List<UserPost> mockPosts = List.of(
                new UserPost("Author1", "2023-10-10", "Content1", 10),
                new UserPost("Author2", "2023-10-11", "Content2", 5)
        );

        when(userPostsRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<UserPost> filteredPosts = postFilterService.filterByDateRange("2023-10-10", "InvalidDate");

        // Assert
        assertEquals(0, filteredPosts.size());
    }
}
