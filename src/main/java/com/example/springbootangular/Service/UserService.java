package com.example.springbootangular.Service;

import com.example.springbootangular.Model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUser();

//    List<User> findAll();

    Optional<User> findByUserName(String userName);

    User addUser(User user);

    User saveUser(User user);


//    Optional<User> updateByUserName(String userName);

    void removeUser(String userName);

    UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException;
}
