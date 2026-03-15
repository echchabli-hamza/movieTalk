package com.MovieTalk.MT.service;

import com.MovieTalk.MT.entity.Movie;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface MovieService {
    Movie add(Movie movie);
    Movie add(Movie movie, MultipartFile image);
    Movie update(Long id, Movie movie);
    Movie update(Long id, Movie movie, MultipartFile image);
    void delete(Long id);
    List<Movie> listAll();
    Movie listOne(Long id);
    
    // New methods for public endpoints
    List<Movie> getFeaturedMovies();
    List<Movie> getTrendingMovies();
    List<Movie> getNewReleases();
    List<Movie> getTopRatedMovies();
    List<Movie> getRecommendationsForUser(String username);
    List<Movie> getMoviesByCategory(Long categoryId);
    List<Movie> searchMovies(String query);
}
