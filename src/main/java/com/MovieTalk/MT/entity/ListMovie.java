package com.MovieTalk.MT.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "list_movies")
@Data
public class ListMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime addedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "list_id")
    @JsonIgnoreProperties("movies")
    private UserList list;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    @JsonIgnoreProperties({"favorites", "reviews", "listMovies"})
    private Movie movie;


}
