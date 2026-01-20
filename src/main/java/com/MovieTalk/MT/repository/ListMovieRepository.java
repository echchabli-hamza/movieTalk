package com.MovieTalk.MT.repository;


import com.MovieTalk.MT.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ListMovieRepository extends JpaRepository<ListMovie, Long> {
    List<ListMovie> findByList(UserList list);
}
