package com.MovieTalk.MT.service;

import com.MovieTalk.MT.entity.UserList;
import java.util.List;

public interface UserListService {
    UserList add(UserList userList);
    List<UserList> listAll();
    UserList getOneById(Long id);
    void delete(Long id);
}
