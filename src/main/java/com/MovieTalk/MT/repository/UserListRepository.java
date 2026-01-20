package com.MovieTalk.MT.repository;


import com.MovieTalk.MT.entity.*;
import com.MovieTalk.MT.entity.UserList;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserListRepository extends JpaRepository<UserList, Long> {
    List<UserList> findByUser(User user);
}
