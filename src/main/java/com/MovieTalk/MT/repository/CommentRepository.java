package com.MovieTalk.MT.repository;

import com.MovieTalk.MT.entity.Comment;
import com.MovieTalk.MT.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMovie(Movie movie);

    List<Comment> findByMovieId(Long movieId);

    List<Comment> findByUserId(Long userId);
}
