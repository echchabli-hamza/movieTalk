package com.MovieTalk.MT.service.impl;

import com.MovieTalk.MT.entity.Movie;
import com.MovieTalk.MT.repository.MovieRepository;
import com.MovieTalk.MT.service.MovieService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie add(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public Movie update(Long id, Movie movie) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        
        if (movie.getTitle() != null) existingMovie.setTitle(movie.getTitle());
        if (movie.getSynopsis() != null) existingMovie.setSynopsis(movie.getSynopsis());
        if (movie.getReleaseYear() != null) existingMovie.setReleaseYear(movie.getReleaseYear());
        if (movie.getDirector() != null) existingMovie.setDirector(movie.getDirector());
        if (movie.getActors() != null) existingMovie.setActors(movie.getActors());
        if (movie.getPopularityScore() != null) existingMovie.setPopularityScore(movie.getPopularityScore());
        if (movie.getRating() != null) existingMovie.setRating(movie.getRating());
        if (movie.getImagePath() != null) existingMovie.setImagePath(movie.getImagePath());
        if (movie.getCategory() != null) existingMovie.setCategory(movie.getCategory());

        return movieRepository.save(existingMovie);
    }

    @Override
    public void delete(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Movie not found");
        }
        movieRepository.deleteById(id);
    }

    @Override
    public List<Movie> listAll() {
        return movieRepository.findAll();
    }

    @Override
    public Movie listOne(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
    }
}
