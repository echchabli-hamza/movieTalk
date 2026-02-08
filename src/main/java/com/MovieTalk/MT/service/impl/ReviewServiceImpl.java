package com.MovieTalk.MT.service.impl;

import com.MovieTalk.MT.entity.Review;
import com.MovieTalk.MT.repository.ReviewRepository;
import com.MovieTalk.MT.service.ReviewService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review add(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public Review update(Long id, Review review) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (review.getRating() != null) {
            existingReview.setRating(review.getRating());
        }
        if (review.getComment() != null) {
            existingReview.setComment(review.getComment());
        }

        return reviewRepository.save(existingReview);
    }

    @Override
    public void delete(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("Review not found");
        }
        reviewRepository.deleteById(id);
    }

    @Override
    public List<Review> findByMovieId(Long movieId) {
        return reviewRepository.findAll().stream()
                .filter(review -> review.getMovie().getId().equals(movieId))
                .toList();
    }
}
