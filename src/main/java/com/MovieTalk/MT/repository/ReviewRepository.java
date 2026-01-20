package com.MovieTalk.MT.repository;


import com.MovieTalk.MT.entity.*;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMovie(Movie movie);
    Optional<Review> findByUserAndMovie(User user, Movie movie);
}
