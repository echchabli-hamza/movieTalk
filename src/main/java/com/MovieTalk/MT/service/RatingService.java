package com.MovieTalk.MT.service;

import com.MovieTalk.MT.entity.Rating;

import java.util.List;

public interface RatingService {
    Rating upsert(Rating rating);

    void delete(Long id);

    List<Rating> findByMovieId(Long movieId);

    List<Rating> findByUserId(Long userId);
}
