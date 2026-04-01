package com.MovieTalk.MT.service;

import com.MovieTalk.MT.entity.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User updateUserActiveStatus(Long id, boolean active);
}
