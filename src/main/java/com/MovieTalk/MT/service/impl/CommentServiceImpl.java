package com.MovieTalk.MT.service.impl;

import com.MovieTalk.MT.entity.Comment;
import com.MovieTalk.MT.repository.CommentRepository;
import com.MovieTalk.MT.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment add(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(Long id, Comment comment) {
        Comment existing = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (comment.getText() != null) {
            existing.setText(comment.getText());
        }
        return commentRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Comment not found");
        }
        commentRepository.deleteById(id);
    }

    @Override
    public List<Comment> findByMovieId(Long movieId) {
        return commentRepository.findByMovieId(movieId);
    }

    @Override
    public List<Comment> findByUserId(Long userId) {
        return commentRepository.findByUserId(userId);
    }
}
