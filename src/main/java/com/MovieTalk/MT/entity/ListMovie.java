package com.MovieTalk.MT.entity;

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
    private UserList list;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;


}
