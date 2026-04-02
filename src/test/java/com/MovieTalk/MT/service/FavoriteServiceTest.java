package com.MovieTalk.MT.service;

import com.MovieTalk.MT.entity.Favorite;
import com.MovieTalk.MT.entity.Movie;
import com.MovieTalk.MT.entity.User;
import com.MovieTalk.MT.repository.FavoriteRepository;
import com.MovieTalk.MT.repository.MovieRepository;
import com.MovieTalk.MT.repository.UserRepository;
import com.MovieTalk.MT.service.impl.FavoriteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    private User testUser;
    private Movie testMovie;
    private Favorite testFavorite;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testMovie = new Movie();
        testMovie.setId(1L);
        testMovie.setTitle("Test Movie");

        testFavorite = new Favorite();
        testFavorite.setId(1L);
        testFavorite.setUser(testUser);
        testFavorite.setMovie(testMovie);
    }

    @Test
    void testAddFavorite_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));
        when(favoriteRepository.findByUserAndMovie(testUser, testMovie)).thenReturn(Optional.empty());
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(testFavorite);

        // Act
        Favorite result = favoriteService.add(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(testFavorite.getId(), result.getId());
        assertEquals(testUser.getId(), result.getUser().getId());
        assertEquals(testMovie.getId(), result.getMovie().getId());
        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }

    @Test
    void testAddFavorite_UserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> favoriteService.add(1L, 1L));
        assertEquals("User not found", exception.getMessage());
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    void testAddFavorite_MovieNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> favoriteService.add(1L, 1L));
        assertEquals("Movie not found", exception.getMessage());
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    void testAddFavorite_AlreadyFavorited() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));
        when(favoriteRepository.findByUserAndMovie(testUser, testMovie)).thenReturn(Optional.of(testFavorite));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> favoriteService.add(1L, 1L));
        assertEquals("Movie is already in favorites", exception.getMessage());
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    void testDeleteFavorite_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));
        when(favoriteRepository.findByUserAndMovie(testUser, testMovie)).thenReturn(Optional.of(testFavorite));

        // Act
        favoriteService.delete(1L, 1L);

        // Assert
        verify(favoriteRepository, times(1)).delete(testFavorite);
    }

    @Test
    void testDeleteFavorite_UserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> favoriteService.delete(1L, 1L));
        assertEquals("User not found", exception.getMessage());
        verify(favoriteRepository, never()).delete(any());
    }

    @Test
    void testDeleteFavorite_MovieNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> favoriteService.delete(1L, 1L));
        assertEquals("Movie not found", exception.getMessage());
        verify(favoriteRepository, never()).delete(any());
    }

    @Test
    void testDeleteFavorite_FavoriteNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));
        when(favoriteRepository.findByUserAndMovie(testUser, testMovie)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> favoriteService.delete(1L, 1L));
        assertEquals("Favorite not found", exception.getMessage());
        verify(favoriteRepository, never()).delete(any());
    }

    @Test
    void testGetAllByUserId_Success() {
        // Arrange
        List<Favorite> favorites = new ArrayList<>();
        favorites.add(testFavorite);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(favoriteRepository.findByUser(testUser)).thenReturn(favorites);

        // Act
        List<Favorite> result = favoriteService.getAllByUserId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testFavorite.getId(), result.get(0).getId());
        verify(favoriteRepository, times(1)).findByUser(testUser);
    }

    @Test
    void testGetAllByUserId_UserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> favoriteService.getAllByUserId(1L));
        assertEquals("User not found", exception.getMessage());
        verify(favoriteRepository, never()).findByUser(any());
    }

    @Test
    void testGetAllByUserId_EmptyList() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(favoriteRepository.findByUser(testUser)).thenReturn(new ArrayList<>());

        // Act
        List<Favorite> result = favoriteService.getAllByUserId(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(favoriteRepository, times(1)).findByUser(testUser);
    }
}
