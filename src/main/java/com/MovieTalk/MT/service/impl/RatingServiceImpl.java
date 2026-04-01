package com.MovieTalk.MT.service.impl;

import com.MovieTalk.MT.entity.Movie;
import com.MovieTalk.MT.entity.Rating;
import com.MovieTalk.MT.repository.MovieRepository;
import com.MovieTalk.MT.repository.RatingRepository;
import com.MovieTalk.MT.service.RatingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;

    public RatingServiceImpl(RatingRepository ratingRepository, MovieRepository movieRepository) {
        this.ratingRepository = ratingRepository;
        this.movieRepository = movieRepository;
    }

    /**
     * Upsert: if the user already rated this movie, update the existing rating and
     * recalculate the movie average without changing ratersNumber.
     * If it's a new rating, add it and increment ratersNumber.
     *
     * Formula (new rating): newAvg = (A * N + R) / (N + 1)
     * Formula (update): newAvg = (A * N - oldR + newR) / N
     */
    @Override
    public Rating upsert(Rating rating) {
        Optional<Rating> existing = ratingRepository.findByUserAndMovie(rating.getUser(), rating.getMovie());

        Movie movie = rating.getMovie();
        double currentAvg = movie.getRating() != null ? movie.getRating() : 0.0;
        int n = movie.getRatersNumber() != null ? movie.getRatersNumber() : 0;

        if (existing.isPresent()) {
            // Update existing — N stays the same, subtract old rating, add new one
            Rating existingRating = existing.get();
            int oldR = existingRating.getRating() != null ? existingRating.getRating() : 0;
            int newR = rating.getRating();

            double newAvg = n > 0 ? (currentAvg * n - oldR + newR) / n : newR;
            movie.setRating(newAvg);
            movieRepository.save(movie);

            existingRating.setRating(newR);
            return ratingRepository.save(existingRating);
        }

        // New rating — apply: newAvg = (A * N + R) / (N + 1)
        double newAvg = (currentAvg * n + rating.getRating()) / (n + 1);
        movie.setRating(newAvg);
        movie.setRatersNumber(n + 1);
        movieRepository.save(movie);

        return ratingRepository.save(rating);
    }

    /**
     * Delete a rating and recalculate the movie average.
     * Formula: newAvg = (A * N - R) / (N - 1)
     * If N was 1, reset average to 0 and ratersNumber to 0.
     */
    @Override
    public void delete(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found"));

        Movie movie = rating.getMovie();
        double currentAvg = movie.getRating() != null ? movie.getRating() : 0.0;
        int n = movie.getRatersNumber() != null ? movie.getRatersNumber() : 0;
        int r = rating.getRating() != null ? rating.getRating() : 0;

        if (n <= 1) {
            movie.setRating(0.0);
            movie.setRatersNumber(0);
        } else {
            double newAvg = (currentAvg * n - r) / (n - 1);
            movie.setRating(newAvg);
            movie.setRatersNumber(n - 1);
        }

        movieRepository.save(movie);
        ratingRepository.deleteById(id);
    }

    @Override
    public List<Rating> findByMovieId(Long movieId) {
        return ratingRepository.findByMovieId(movieId);
    }

    @Override
    public List<Rating> findByUserId(Long userId) {
        return ratingRepository.findByUserId(userId);
    }
}
