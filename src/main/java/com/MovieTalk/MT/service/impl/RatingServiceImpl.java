package com.MovieTalk.MT.service.impl;

import com.MovieTalk.MT.entity.Rating;
import com.MovieTalk.MT.repository.RatingRepository;
import com.MovieTalk.MT.service.RatingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    /**
     * Upsert: update existing rating if same user+movie exists, otherwise insert.
     */
    @Override
    public Rating upsert(Rating rating) {
        Optional<Rating> existing = ratingRepository.findByUserAndMovie(rating.getUser(), rating.getMovie());
        if (existing.isPresent()) {
            Rating existingRating = existing.get();
            existingRating.setRating(rating.getRating());
            return ratingRepository.save(existingRating);
        }
        return ratingRepository.save(rating);
    }

    @Override
    public void delete(Long id) {
        if (!ratingRepository.existsById(id)) {
            throw new RuntimeException("Rating not found");
        }
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
