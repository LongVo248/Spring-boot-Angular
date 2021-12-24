package com.example.springbootangular.Service;

import com.example.springbootangular.Model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUser();

    Optional<User> findByUserName(String userName);

    User addUser(User user);

    User saveUser(User user);

//    Optional<User> updateByUserName(String userName);

    void removeUser(String userName);
}
