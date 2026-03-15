package com.MovieTalk.MT.service;

import com.MovieTalk.MT.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment add(Comment comment);

    Comment update(Long id, Comment comment);

    void delete(Long id);

    List<Comment> findByMovieId(Long movieId);

    List<Comment> findByUserId(Long userId);
}
