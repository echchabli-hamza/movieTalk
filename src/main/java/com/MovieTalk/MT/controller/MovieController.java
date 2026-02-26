package com.MovieTalk.MT.controller;

import com.MovieTalk.MT.entity.Movie;
import com.MovieTalk.MT.service.MovieService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/admin/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // @PostMapping
    // public ResponseEntity<Movie> add(@RequestBody Movie movie) {
    //     Movie createdMovie = movieService.add(movie);
    //     return ResponseEntity.ok(createdMovie);
    // }

     @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
      public ResponseEntity<Movie> add(
               @RequestPart("movie") Movie movie,
               @RequestPart("image") MultipartFile image) {
        
        try {
            Movie createdMovie = movieService.add(movie, image);
            return ResponseEntity.ok(createdMovie);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
           }
        

    @PutMapping("/{id}")
    public ResponseEntity<Movie> update(@PathVariable Long id, @RequestBody Movie movie) {
        Movie updatedMovie = movieService.update(id, movie);
        return ResponseEntity.ok(updatedMovie);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Movie> updateWithImage(
            @PathVariable Long id,
            @RequestPart("movie") Movie movie,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        
        try {
            Movie updatedMovie = movieService.update(id, movie, image);
            return ResponseEntity.ok(updatedMovie);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
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
