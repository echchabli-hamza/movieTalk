package com.MovieTalk.MT.service;

import com.MovieTalk.MT.entity.Favorite;
import java.util.List;

public interface FavoriteService {
    Favorite add(Long userId, Long movieId);
    void delete(Long userId, Long movieId);
    List<Favorite> getAllByUserId(Long userId);
}
