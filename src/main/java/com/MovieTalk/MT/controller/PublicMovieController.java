package com.MovieTalk.MT.controller;

import com.MovieTalk.MT.dto.MovieDTO;
import com.MovieTalk.MT.entity.Movie;
import com.MovieTalk.MT.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
public class PublicMovieController {

    private final MovieService movieService;

    public PublicMovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<MovieDTO>> listAll() {
        List<MovieDTO> movies = movieService.listAll().stream()
                .map(MovieDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> listOne(@PathVariable Long id) {
        Movie movie = movieService.listOne(id);
        return ResponseEntity.ok(MovieDTO.fromEntity(movie));
    }

    @GetMapping("/featured")
    public ResponseEntity<List<MovieDTO>> getFeaturedMovies() {
        List<MovieDTO> featuredMovies = movieService.getFeaturedMovies().stream()
                .map(MovieDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(featuredMovies);
    }


    @GetMapping("/trending")
    public ResponseEntity<List<MovieDTO>> getTrendingMovies() {
        List<MovieDTO> trendingMovies = movieService.getTrendingMovies().stream()
                .map(MovieDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(trendingMovies);
    }


    @GetMapping("/new-releases")
    public ResponseEntity<List<MovieDTO>> getNewReleases() {
        List<MovieDTO> newReleases = movieService.getNewReleases().stream()
                .map(MovieDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(newReleases);
    }


    @GetMapping("/top-rated")
    public ResponseEntity<List<MovieDTO>> getTopRatedMovies() {
        List<MovieDTO> topRatedMovies = movieService.getTopRatedMovies().stream()
                .map(MovieDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(topRatedMovies);
    }


    @GetMapping("/recommendations")
    public ResponseEntity<List<MovieDTO>> getRecommendations(Principal principal) {
        if (principal == null) {
            return ResponseEntity.ok(List.of()); // Return empty list for unauthenticated users
        }
        
        String username = principal.getName();
        List<MovieDTO> recommendations = movieService.getRecommendationsForUser(username).stream()
                .map(MovieDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recommendations);
    }


    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<List<MovieDTO>> getMoviesByCategory(@PathVariable Long categoryId) {
        List<MovieDTO> moviesByCategory = movieService.getMoviesByCategory(categoryId).stream()
                .map(MovieDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(moviesByCategory);
    }


    @GetMapping("/search")
    public ResponseEntity<List<MovieDTO>> searchMovies(@RequestParam String query) {
        List<MovieDTO> searchResults = movieService.searchMovies(query).stream()
                .map(MovieDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(searchResults);
    }
}
