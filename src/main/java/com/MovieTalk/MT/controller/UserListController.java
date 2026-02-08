package com.MovieTalk.MT.controller;

import com.MovieTalk.MT.entity.UserList;
import com.MovieTalk.MT.service.UserListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user/lists")
public class UserListController {

    private final UserListService userListService;

    public UserListController(UserListService userListService) {
        this.userListService = userListService;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userListService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
