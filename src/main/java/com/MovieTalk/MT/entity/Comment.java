package com.MovieTalk.MT.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({ "favorites", "ratings", "comments", "lists", "password" })
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    @JsonIgnoreProperties({ "favorites", "ratings", "comments", "listMovies" })
    private Movie movie;
}
