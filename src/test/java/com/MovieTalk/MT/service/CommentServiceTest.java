package com.MovieTalk.MT.service;

import com.MovieTalk.MT.entity.Comment;
import com.MovieTalk.MT.entity.Movie;
import com.MovieTalk.MT.entity.User;
import com.MovieTalk.MT.repository.CommentRepository;
import com.MovieTalk.MT.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment testComment;
    private User testUser;
    private Movie testMovie;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testMovie = new Movie();
        testMovie.setId(1L);
        testMovie.setTitle("Test Movie");

        testComment = new Comment();
        testComment.setId(1L);
        testComment.setText("Great movie!");
        testComment.setUser(testUser);
        testComment.setMovie(testMovie);
        testComment.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testAddComment_Success() {
        // Arrange
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        // Act
        Comment result = commentService.add(testComment);

        // Assert
        assertNotNull(result);
        assertEquals(testComment.getId(), result.getId());
        assertEquals("Great movie!", result.getText());
        assertEquals(testUser.getId(), result.getUser().getId());
        verify(commentRepository, times(1)).save(testComment);
    }

    @Test
    void testAddComment_WithNullText() {
        // Arrange
        Comment commentWithNullText = new Comment();
        commentWithNullText.setText(null);
        commentWithNullText.setUser(testUser);
        commentWithNullText.setMovie(testMovie);

        when(commentRepository.save(any(Comment.class))).thenReturn(commentWithNullText);

        // Act
        Comment result = commentService.add(commentWithNullText);

        // Assert
        assertNotNull(result);
        assertNull(result.getText());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testUpdateComment_Success() {
        // Arrange
        Comment updatedComment = new Comment();
        updatedComment.setText("Updated comment text");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        // Act
        Comment result = commentService.update(1L, updatedComment);

        // Assert
        assertNotNull(result);
        assertEquals("Updated comment text", result.getText());
        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testUpdateComment_NotFound() {
        // Arrange
        Comment updatedComment = new Comment();
        updatedComment.setText("Updated text");

        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> commentService.update(1L, updatedComment));
        assertEquals("Comment not found", exception.getMessage());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void testUpdateComment_WithNullText() {
        // Arrange
        Comment updateData = new Comment();
        updateData.setText(null);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        // Act
        Comment result = commentService.update(1L, updateData);

        // Assert
        assertNotNull(result);
        // Text should remain unchanged if null is passed
        assertEquals("Great movie!", result.getText());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testDeleteComment_Success() {
        // Arrange
        when(commentRepository.existsById(1L)).thenReturn(true);

        // Act
        commentService.delete(1L);

        // Assert
        verify(commentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteComment_NotFound() {
        // Arrange
        when(commentRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> commentService.delete(1L));
        assertEquals("Comment not found", exception.getMessage());
        verify(commentRepository, never()).deleteById(any());
    }

    @Test
    void testFindByMovieId_Success() {
        // Arrange
        List<Comment> comments = new ArrayList<>();
        comments.add(testComment);

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setText("Another comment");
        comment2.setUser(testUser);
        comment2.setMovie(testMovie);
        comments.add(comment2);

        when(commentRepository.findByMovieId(1L)).thenReturn(comments);

        // Act
        List<Comment> result = commentService.findByMovieId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Great movie!", result.get(0).getText());
        assertEquals("Another comment", result.get(1).getText());
        verify(commentRepository, times(1)).findByMovieId(1L);
    }

    @Test
    void testFindByMovieId_EmptyList() {
        // Arrange
        when(commentRepository.findByMovieId(999L)).thenReturn(new ArrayList<>());

        // Act
        List<Comment> result = commentService.findByMovieId(999L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(commentRepository, times(1)).findByMovieId(999L);
    }

    @Test
    void testFindByUserId_Success() {
        // Arrange
        List<Comment> comments = new ArrayList<>();
        comments.add(testComment);

        when(commentRepository.findByUserId(1L)).thenReturn(comments);

        // Act
        List<Comment> result = commentService.findByUserId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUser.getId(), result.get(0).getUser().getId());
        verify(commentRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testFindByUserId_EmptyList() {
        // Arrange
        when(commentRepository.findByUserId(999L)).thenReturn(new ArrayList<>());

        // Act
        List<Comment> result = commentService.findByUserId(999L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(commentRepository, times(1)).findByUserId(999L);
    }

    @Test
    void testFindByUserId_MultipleComments() {
        // Arrange
        List<Comment> comments = new ArrayList<>();
        comments.add(testComment);

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setText("Another movie review");
        comment2.setUser(testUser);

        Movie anotherMovie = new Movie();
        anotherMovie.setId(2L);
        anotherMovie.setTitle("Another Movie");
        comment2.setMovie(anotherMovie);

        comments.add(comment2);

        when(commentRepository.findByUserId(1L)).thenReturn(comments);

        // Act
        List<Comment> result = commentService.findByUserId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(c -> c.getUser().getId().equals(1L)));
        verify(commentRepository, times(1)).findByUserId(1L);
    }
}
