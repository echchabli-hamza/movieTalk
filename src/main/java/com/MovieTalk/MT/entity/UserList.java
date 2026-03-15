package com.MovieTalk.MT.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Entity
@Table(name = "lists")
@Data
public class UserList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Boolean isPublic = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"favorites", "reviews", "lists", "password"})
    private User user;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("list")
    private List<ListMovie> movies;


}
