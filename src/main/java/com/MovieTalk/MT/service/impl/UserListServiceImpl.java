package com.MovieTalk.MT.service.impl;

import com.MovieTalk.MT.entity.User;
import com.MovieTalk.MT.entity.UserList;
import com.MovieTalk.MT.repository.UserListRepository;
import com.MovieTalk.MT.repository.UserRepository;
import com.MovieTalk.MT.service.UserListService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserListServiceImpl implements UserListService {

    private final UserListRepository userListRepository;
    private final UserRepository userRepository;

    public UserListServiceImpl(UserListRepository userListRepository, UserRepository userRepository) {
        this.userListRepository = userListRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserList add(UserList userList) {
        return userListRepository.save(userList);
    }

    @Override
    public List<UserList> listAll() {
        return userListRepository.findAll();
    }

    @Override
    public UserList getOneById(Long id) {
        return userListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserList not found"));
    }

    @Override
    public void delete(Long id) {
        if (!userListRepository.existsById(id)) {
            throw new RuntimeException("UserList not found");
        }
        userListRepository.deleteById(id);
    }

    @Override
    public List<UserList> listByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userListRepository.findByUser(user);
    }
}
