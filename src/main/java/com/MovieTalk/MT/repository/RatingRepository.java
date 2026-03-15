package com.MovieTalk.MT.repository;

import com.MovieTalk.MT.entity.Movie;
import com.MovieTalk.MT.entity.Rating;
import com.MovieTalk.MT.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByMovie(Movie movie);

    Optional<Rating> findByUserAndMovie(User user, Movie movie);

    List<Rating> findByUserId(Long userId);

    List<Rating> findByMovieId(Long movieId);
}
