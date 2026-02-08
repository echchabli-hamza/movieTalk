package com.MovieTalk.MT.service;

import com.MovieTalk.MT.entity.ListMovie;
import java.util.List;

public interface ListMovieService {
    ListMovie add(Long listId, Long movieId);
    void delete(Long listId, Long movieId);
    List<ListMovie> findByListId(Long listId);
}
