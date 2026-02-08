package com.MovieTalk.MT.service;

import com.MovieTalk.MT.entity.Movie;
import java.util.List;

public interface MovieService {
    Movie add(Movie movie);
    Movie update(Long id, Movie movie);
    void delete(Long id);
    List<Movie> listAll();
    Movie listOne(Long id);
}
