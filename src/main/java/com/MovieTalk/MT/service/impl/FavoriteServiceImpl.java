package com.MovieTalk.MT.service.impl;

import com.MovieTalk.MT.entity.Favorite;
import com.MovieTalk.MT.entity.User;
import com.MovieTalk.MT.entity.Movie;
import com.MovieTalk.MT.repository.FavoriteRepository;
import com.MovieTalk.MT.repository.UserRepository;
import com.MovieTalk.MT.repository.MovieRepository;
import com.MovieTalk.MT.service.FavoriteService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository,
                             UserRepository userRepository,
                             MovieRepository movieRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public Favorite add(Long userId, Long movieId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // Check if already favorited
        if (favoriteRepository.findByUserAndMovie(user, movie).isPresent()) {
            throw new RuntimeException("Movie is already in favorites");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setMovie(movie);
        return favoriteRepository.save(favorite);
    }

    @Override
    public void delete(Long userId, Long movieId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Favorite favorite = favoriteRepository.findByUserAndMovie(user, movie)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));

        favoriteRepository.delete(favorite);
    }

    @Override
    public List<Favorite> getAllByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return favoriteRepository.findByUser(user);
    }
}
