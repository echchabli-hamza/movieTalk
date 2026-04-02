package com.MovieTalk.MT.controller;

import com.MovieTalk.MT.entity.UserList;
import com.MovieTalk.MT.entity.Movie;
import com.MovieTalk.MT.service.UserListService;
import com.MovieTalk.MT.service.ListMovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/lists")
public class UserListController {

    private final UserListService userListService;
    private final ListMovieService listMovieService;

    public UserListController(UserListService userListService, ListMovieService listMovieService) {
        this.userListService = userListService;
        this.listMovieService = listMovieService;
    }

    @PostMapping
    public ResponseEntity<UserList> add(@RequestBody UserList userList) {
        UserList createdUserList = userListService.add(userList);
        return ResponseEntity.ok(createdUserList);
    }

    @GetMapping
    public ResponseEntity<List<UserList>> listAll() {
        List<UserList> userLists = userListService.listAll();
        return ResponseEntity.ok(userLists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserList> getOneById(@PathVariable Long id) {
        UserList userList = userListService.getOneById(id);
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/{id}/movies")
    public ResponseEntity<List<Movie>> getMoviesInList(@PathVariable Long id) {
        List<Movie> movies = listMovieService.findByListId(id).stream()
                .map(listMovie -> listMovie.getMovie())
                .collect(Collectors.toList());
        return ResponseEntity.ok(movies);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userListService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserList>> listByUser(@PathVariable Long userId) {
        List<UserList> userLists = userListService.listByUser(userId);
        return ResponseEntity.ok(userLists);
    }
}
