package com.MovieTalk.MT.controller;

import com.MovieTalk.MT.entity.Movie;
import com.MovieTalk.MT.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<Movie> add(@RequestBody Movie movie) {
        Movie createdMovie = movieService.add(movie);
        return ResponseEntity.ok(createdMovie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> update(@PathVariable Long id, @RequestBody Movie movie) {
        Movie updatedMovie = movieService.update(id, movie);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
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
