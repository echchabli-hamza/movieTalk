package com.MovieTalk.MT.controller;

import com.MovieTalk.MT.entity.Movie;
import com.MovieTalk.MT.entity.Rating;
import com.MovieTalk.MT.entity.User;
import com.MovieTalk.MT.repository.MovieRepository;
import com.MovieTalk.MT.repository.UserRepository;
import com.MovieTalk.MT.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService ratingService;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public RatingController(RatingService ratingService, UserRepository userRepository,
            MovieRepository movieRepository) {
        this.ratingService = ratingService;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    /** GET /ratings/movie/{movieId} — public */
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Rating>> getByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(ratingService.findByMovieId(movieId));
    }

    /**
     * POST /ratings — authenticated, upserts the current user's rating for a movie
     */
    @PostMapping
    public ResponseEntity<Rating> upsert(@RequestBody Map<String, Object> body, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long movieId = Long.valueOf(body.get("movieId").toString());
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setMovie(movie);
        rating.setRating(Integer.valueOf(body.get("rating").toString()));

        return ResponseEntity.ok(ratingService.upsert(rating));
    }

    /** DELETE /ratings/{id} — authenticated */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ratingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
