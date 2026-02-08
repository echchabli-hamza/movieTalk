package com.MovieTalk.MT.controller;

import com.MovieTalk.MT.entity.ListMovie;
import com.MovieTalk.MT.service.ListMovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user/list-movies")
public class ListMovieController {

    private final ListMovieService listMovieService;

    public ListMovieController(ListMovieService listMovieService) {
        this.listMovieService = listMovieService;
    }

    @PostMapping("/{listId}/{movieId}")
    public ResponseEntity<ListMovie> add(@PathVariable Long listId, @PathVariable Long movieId) {
        ListMovie listMovie = listMovieService.add(listId, movieId);
        return ResponseEntity.ok(listMovie);
    }

    @DeleteMapping("/{listId}/{movieId}")
    public ResponseEntity<Void> delete(@PathVariable Long listId, @PathVariable Long movieId) {
        listMovieService.delete(listId, movieId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{listId}")
    public ResponseEntity<List<ListMovie>> findByListId(@PathVariable Long listId) {
        List<ListMovie> listMovies = listMovieService.findByListId(listId);
        return ResponseEntity.ok(listMovies);
    }
}

