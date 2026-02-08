package com.MovieTalk.MT.service.impl;

import com.MovieTalk.MT.entity.ListMovie;
import com.MovieTalk.MT.entity.UserList;
import com.MovieTalk.MT.entity.Movie;
import com.MovieTalk.MT.repository.ListMovieRepository;
import com.MovieTalk.MT.repository.UserListRepository;
import com.MovieTalk.MT.repository.MovieRepository;
import com.MovieTalk.MT.service.ListMovieService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ListMovieServiceImpl implements ListMovieService {

    private final ListMovieRepository listMovieRepository;
    private final UserListRepository userListRepository;
    private final MovieRepository movieRepository;

    public ListMovieServiceImpl(ListMovieRepository listMovieRepository,
                              UserListRepository userListRepository,
                              MovieRepository movieRepository) {
        this.listMovieRepository = listMovieRepository;
        this.userListRepository = userListRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public ListMovie add(Long listId, Long movieId) {
        UserList list = userListRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("UserList not found"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        ListMovie listMovie = new ListMovie();
        listMovie.setList(list);
        listMovie.setMovie(movie);
        return listMovieRepository.save(listMovie);
    }

    @Override
    public void delete(Long listId, Long movieId) {
        UserList list = userListRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("UserList not found"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        ListMovie listMovie = listMovieRepository.findByList(list).stream()
                .filter(lm -> lm.getMovie().getId().equals(movieId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ListMovie not found"));

        listMovieRepository.delete(listMovie);
    }

    @Override
    public List<ListMovie> findByListId(Long listId) {
        UserList list = userListRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("UserList not found"));
        return listMovieRepository.findByList(list);
    }
}
