package com.scottlogic.grad_training.friendface.sorter_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.scottlogic.grad_training.friendface.user_post_api.UserPost;
import com.scottlogic.grad_training.friendface.user_post_api.UserPostsRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostSorterServiceTest {

    @Mock
    private PostSorterRepository postSorterRepository;

    @Mock
    private UserPostsRepository userPostsRepository;

    private PostSorterService postSorterService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        postSorterService = new PostSorterService(postSorterRepository, userPostsRepository);
    }

    @Test
    public void testSortAuthorAscWithEmptyList() {
        // Arrange
        List<UserPost> mockPosts = new ArrayList<>();

        when(userPostsRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<UserPost> sortedPosts = postSorterService.sortAuthorAsc();

        // Assert
        assertEquals(0, sortedPosts.size());
    }

    @Test
    public void testSortAuthorAscWithSinglePost() {
        // Arrange
        List<UserPost> mockPosts = new ArrayList<>();
        mockPosts.add(new UserPost("Alice", "2022-01-01", "Content 1", 0));

        when(userPostsRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<UserPost> sortedPosts = postSorterService.sortAuthorAsc();

        // Assert
        assertEquals(1, sortedPosts.size());
        assertEquals("Alice", sortedPosts.get(0).getAuthor());
    }

    @Test
    public void testSortAuthorAscWithMultiplePosts() {
        // Arrange
        List<UserPost> mockPosts = new ArrayList<>();
        mockPosts.add(new UserPost("John", "2022-01-01", "Content 1", 0));
        mockPosts.add(new UserPost("Alice", "2022-01-02", "Content 2", 0));
        mockPosts.add(new UserPost("Bob", "2022-01-03", "Content 3", 0));

        when(userPostsRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<UserPost> sortedPosts = postSorterService.sortAuthorAsc();

        // Assert
        assertEquals("Alice", sortedPosts.get(0).getAuthor());
        assertEquals("Bob", sortedPosts.get(1).getAuthor());
        assertEquals("John", sortedPosts.get(2).getAuthor());
    }

    @Test
    public void testSortAuthorAscWithNullList() {
        // Arrange
        List<UserPost> mockPosts = null; // Simulate a null list of posts

        when(userPostsRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<UserPost> sortedPosts = postSorterService.sortAuthorAsc();

        // Assert
        assertEquals(0, sortedPosts.size());
    }

    @Test
    void testSortAuthorDesc() {
        // Assuming your UserPostsRepository returns a non-empty list of UserPosts
        PostSorterService sorterService = new PostSorterService(postSorterRepository, userPostsRepository);
        List<UserPost> sortedPosts = sorterService.sortAuthorDesc();

        // Assuming you have expected sorted data, you can add assertions
        // Here's a simple assertion, you may need to adjust based on your data
        for (int i = 1; i < sortedPosts.size(); i++) {
            assertTrue(sortedPosts.get(i - 1).getAuthor().compareToIgnoreCase(sortedPosts.get(i).getAuthor()) >= 0);
        }
    }

    @Test
    void testSortAuthorDescWithEmptyList() {
        // Assuming your UserPostsRepository returns an empty list
        PostSorterService sorterService = new PostSorterService(postSorterRepository, userPostsRepository);
        List<UserPost> sortedPosts = sorterService.sortAuthorDesc();

        // Assert that the result is an empty list
        assertTrue(sortedPosts.isEmpty());
    }

    @Test
    void testSortAuthorDescWithNullList() {
        // Mocking a scenario where your UserPostsRepository returns null
        UserPostsRepository mockUserPostsRepository = mock(UserPostsRepository.class);
        when(mockUserPostsRepository.findAll()).thenReturn(null);
        PostSorterService sorterService = new PostSorterService(postSorterRepository, mockUserPostsRepository);
        List<UserPost> sortedPosts = sorterService.sortAuthorDesc();

        // Assert that the result is an empty list or handle it accordingly
        assertTrue(sortedPosts.isEmpty());
    }


    @Test
    public void testSortDateOldestFirstWithEmptyList() {
        // Arrange
        List<UserPost> mockPosts = new ArrayList<>();

        Mockito.when(userPostsRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<UserPost> sortedPosts = postSorterService.sortDateOldestFirst();

        // Assert
        assertEquals(0, sortedPosts.size());
    }

    @Test
    public void testSortDateOldestFirstWithOnePost() {
        // Arrange
        List<UserPost> mockPosts = new ArrayList<>();
        UserPost post = new UserPost("User1", "2020-01-01", "Content", 0);
        mockPosts.add(post);

        Mockito.when(userPostsRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<UserPost> sortedPosts = postSorterService.sortDateOldestFirst();

        // Assert
        assertEquals(1, sortedPosts.size());
        assertEquals(post, sortedPosts.get(0));
    }

    @Test
    public void testSortDateOldestFirstWithMultiplePosts() {
        // Arrange
        List<UserPost> mockPosts = new ArrayList<>();
        UserPost post1 = new UserPost("User1", "2020-01-01", "Content", 0);
        UserPost post2 = new UserPost("User2", "2019-12-01", "Content", 0);
        UserPost post3 = new UserPost("User3", "2021-03-15", "Content", 0);
        mockPosts.add(post1);
        mockPosts.add(post2);
        mockPosts.add(post3);

        Mockito.when(userPostsRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<UserPost> sortedPosts = postSorterService.sortDateOldestFirst();

        // Assert
        assertEquals(3, sortedPosts.size());
        assertEquals(post2, sortedPosts.get(0));  // Oldest
        assertEquals(post1, sortedPosts.get(1));
        assertEquals(post3, sortedPosts.get(2));  // Newest
    }

    @Test
    public void testSortDateNewestFirstWithEmptyList() {
        // Arrange
        List<UserPost> mockPosts = new ArrayList<>();

        Mockito.when(userPostsRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<UserPost> sortedPosts = postSorterService.sortDateNewestFirst();

        // Assert
        assertEquals(0, sortedPosts.size());
    }

    @Test
    public void testSortDateNewestFirstWithOnePost() {
        // Arrange
        List<UserPost> mockPosts = new ArrayList<>();
        UserPost post = new UserPost("User1", "2020-01-01", "Content", 0);
        mockPosts.add(post);

        Mockito.when(userPostsRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<UserPost> sortedPosts = postSorterService.sortDateNewestFirst();

        // Assert
        assertEquals(1, sortedPosts.size());
        assertEquals(post, sortedPosts.get(0));
    }

    @Test
    public void testSortDateNewestFirstWithMultiplePosts() {
        // Arrange
        List<UserPost> mockPosts = new ArrayList<>();
        UserPost post1 = new UserPost("User1", "2020-01-01", "Content", 0);
        UserPost post2 = new UserPost("User2", "2019-12-01", "Content", 0);
        UserPost post3 = new UserPost("User3", "2021-03-15", "Content", 0);
        mockPosts.add(post1);
        mockPosts.add (post2);
        mockPosts.add(post3);

        Mockito.when(userPostsRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<UserPost> sortedPosts = postSorterService.sortDateNewestFirst();

        // Assert
        assertEquals(3, sortedPosts.size());
        assertEquals(post3, sortedPosts.get(0));  // Newest
        assertEquals(post1, sortedPosts.get(1));
        assertEquals(post2, sortedPosts.get(2));  // Oldest
    }
}





