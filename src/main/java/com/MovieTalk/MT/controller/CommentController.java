package com.MovieTalk.MT.controller;

import com.MovieTalk.MT.entity.Comment;
import com.MovieTalk.MT.entity.Movie;
import com.MovieTalk.MT.entity.User;
import com.MovieTalk.MT.repository.MovieRepository;
import com.MovieTalk.MT.repository.UserRepository;
import com.MovieTalk.MT.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public CommentController(CommentService commentService, UserRepository userRepository,
            MovieRepository movieRepository) {
        this.commentService = commentService;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    /** GET /comments/movie/{movieId} — public */
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Comment>> getByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(commentService.findByMovieId(movieId));
    }

    /**
     * POST /comments — authenticated, adds a new comment (multiple allowed per
     * user/movie)
     */
    @PostMapping
    public ResponseEntity<Comment> create(@RequestBody Map<String, Object> body, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long movieId = Long.valueOf(body.get("movieId").toString());
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setMovie(movie);
        comment.setText(body.get("text") != null ? body.get("text").toString() : null);

        return ResponseEntity.ok(commentService.add(comment));
    }

    /** PUT /comments/{id} — authenticated, updates own comment text */
    @PutMapping("/{id}")
    public ResponseEntity<Comment> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Comment update = new Comment();
        if (body.containsKey("text")) {
            update.setText(body.get("text").toString());
        }
        return ResponseEntity.ok(commentService.update(id, update));
    }

    /** DELETE /comments/{id} — authenticated */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
