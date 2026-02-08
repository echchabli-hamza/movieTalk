package com.MovieTalk.MT.controller;

import com.MovieTalk.MT.entity.Favorite;
import com.MovieTalk.MT.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/{userId}/{movieId}")
    public ResponseEntity<Favorite> add(@PathVariable Long userId, @PathVariable Long movieId) {
        Favorite favorite = favoriteService.add(userId, movieId);
        return ResponseEntity.ok(favorite);
    }

    @DeleteMapping("/{userId}/{movieId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @PathVariable Long movieId) {
        favoriteService.delete(userId, movieId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Favorite>> getAllByUserId(@PathVariable Long userId) {
        List<Favorite> favorites = favoriteService.getAllByUserId(userId);
        return ResponseEntity.ok(favorites);
    }
}
