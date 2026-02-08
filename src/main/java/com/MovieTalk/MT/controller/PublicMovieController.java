package com.MovieTalk.MT.controller;

import com.MovieTalk.MT.entity.Movie;
import com.MovieTalk.MT.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/public/movies")
public class PublicMovieController {

    private final MovieService movieService;

    public PublicMovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<Movie>> listAll() {
        List<Movie> movies = movieService.listAll();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> listOne(@PathVariable Long id) {
        Movie movie = movieService.listOne(id);
        return ResponseEntity.ok(movie);
    }
}
