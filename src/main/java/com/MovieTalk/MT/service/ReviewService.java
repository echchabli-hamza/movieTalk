package com.MovieTalk.MT.service;

import com.MovieTalk.MT.entity.Review;
import java.util.List;

public interface ReviewService {
    Review add(Review review);
    Review update(Long id, Review review);
    void delete(Long id);
    List<Review> findByMovieId(Long movieId);
}
