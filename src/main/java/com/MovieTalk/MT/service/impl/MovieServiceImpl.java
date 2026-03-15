package com.MovieTalk.MT.service.impl;

import com.MovieTalk.MT.entity.Movie;
import com.MovieTalk.MT.entity.Rating;
import com.MovieTalk.MT.repository.MovieRepository;
import com.MovieTalk.MT.repository.UserRepository;
import com.MovieTalk.MT.repository.FavoriteRepository;
import com.MovieTalk.MT.repository.RatingRepository;
import com.MovieTalk.MT.entity.User;
import com.MovieTalk.MT.entity.Favorite;
import com.MovieTalk.MT.service.MovieService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final RatingRepository ratingRepository;
    private final FileStorageService fileStorageService;

    public MovieServiceImpl(MovieRepository movieRepository, FileStorageService fileStorageService,
            UserRepository userRepository, FavoriteRepository favoriteRepository,
            RatingRepository ratingRepository) {
        this.fileStorageService = fileStorageService;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Movie add(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public Movie add(Movie movie, MultipartFile image) {

        String imagePath = fileStorageService.save(image);
        movie.setImagePath(imagePath);

        return movieRepository.save(movie);
    }

    @Override
    public Movie update(Long id, Movie movie) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        if (movie.getTitle() != null)
            existingMovie.setTitle(movie.getTitle());
        if (movie.getSynopsis() != null)
            existingMovie.setSynopsis(movie.getSynopsis());
        if (movie.getReleaseYear() != null)
            existingMovie.setReleaseYear(movie.getReleaseYear());
        if (movie.getDirector() != null)
            existingMovie.setDirector(movie.getDirector());
        if (movie.getActors() != null)
            existingMovie.setActors(movie.getActors());
        if (movie.getPopularityScore() != null)
            existingMovie.setPopularityScore(movie.getPopularityScore());
        if (movie.getRating() != null)
            existingMovie.setRating(movie.getRating());
        if (movie.getImagePath() != null)
            existingMovie.setImagePath(movie.getImagePath());
        if (movie.getCategory() != null)
            existingMovie.setCategory(movie.getCategory());

        return movieRepository.save(existingMovie);
    }

    @Override
    public Movie update(Long id, Movie movie, MultipartFile image) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        if (movie.getTitle() != null)
            existingMovie.setTitle(movie.getTitle());
        if (movie.getSynopsis() != null)
            existingMovie.setSynopsis(movie.getSynopsis());
        if (movie.getReleaseYear() != null)
            existingMovie.setReleaseYear(movie.getReleaseYear());
        if (movie.getDirector() != null)
            existingMovie.setDirector(movie.getDirector());
        if (movie.getActors() != null)
            existingMovie.setActors(movie.getActors());
        if (movie.getPopularityScore() != null)
            existingMovie.setPopularityScore(movie.getPopularityScore());
        if (movie.getRating() != null)
            existingMovie.setRating(movie.getRating());
        if (movie.getCategory() != null)
            existingMovie.setCategory(movie.getCategory());

        // Handle new image upload
        if (image != null && !image.isEmpty()) {
            String imagePath = fileStorageService.save(image);
            existingMovie.setImagePath(imagePath);
        }

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

    @Override
    public List<Movie> getFeaturedMovies() {
        List<Movie> featured = movieRepository.findFeaturedMovies();
        return featured.size() > 10 ? featured.subList(0, 10) : featured;
    }

    @Override
    public List<Movie> getTrendingMovies() {
        return movieRepository.findTop10ByOrderByPopularityScoreDesc();
    }

    @Override
    public List<Movie> getNewReleases() {
        return movieRepository.findTop10ByOrderByReleaseYearDesc();
    }

    @Override
    public List<Movie> getTopRatedMovies() {
        return movieRepository.findTop10ByOrderByRatingDesc();
    }

    @Override
    public List<Movie> getRecommendationsForUser(String username) {
        try {
            User user = userRepository.findByEmail(username).orElse(null);
            if (user == null)
                return new ArrayList<>();

            Long userId = user.getId();
            List<Favorite> favorites = favoriteRepository.findByUserId(userId);
            List<Rating> ratings = ratingRepository.findByUserId(userId);

            if (favorites.isEmpty() && ratings.isEmpty()) {
                return getTrendingMovies();
            }

            // Score categories based on user interactions
            Map<Long, Integer> categoryScore = new HashMap<>();

            // Favorites give a weight of 3 per category
            for (Favorite fav : favorites) {
                Long catId = fav.getMovie().getCategory().getId();
                categoryScore.merge(catId, 3, Integer::sum);
            }

            // Ratings add their value as weight per category
            for (Rating rating : ratings) {
                if (rating.getRating() != null) {
                    Long catId = rating.getMovie().getCategory().getId();
                    categoryScore.merge(catId, rating.getRating(), Integer::sum);
                }
            }

            // Rank categories by score descending
            List<Long> rankedCategories = categoryScore.entrySet().stream()
                    .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            // Exclude movies the user already interacted with
            Set<Long> seenMovieIds = new HashSet<>();
            favorites.forEach(fav -> seenMovieIds.add(fav.getMovie().getId()));
            ratings.forEach(r -> seenMovieIds.add(r.getMovie().getId()));

            // Pick up to 6 recommendations from top-ranked categories
            List<Movie> recommendations = new ArrayList<>();
            for (Long categoryId : rankedCategories) {
                if (recommendations.size() >= 6)
                    break;

                List<Movie> categoryMovies = movieRepository.findByCategoryId(categoryId);
                categoryMovies.stream()
                        .filter(movie -> !seenMovieIds.contains(movie.getId()))
                        .limit(3)
                        .forEach(recommendations::add);
            }

            return recommendations.size() > 6 ? recommendations.subList(0, 6) : recommendations;

        } catch (Exception e) {
            return getTrendingMovies();
        }
    }

    @Override
    public List<Movie> getMoviesByCategory(Long categoryId) {
        return movieRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Movie> searchMovies(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return movieRepository.searchByTitleDirectorOrActors(query.trim());
    }
}
