package com.example.springbootangular.Service.Impl;

import com.example.springbootangular.Exception.UserAlreadyExistedException;
import com.example.springbootangular.Exception.UserNotFoundException;
import com.example.springbootangular.Model.User;
import com.example.springbootangular.Repository.UserRepository;
import com.example.springbootangular.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        var findUser = userRepository.findById(userName);
        if (findUser.isPresent()) {
            return findUser;
        } else {
            throw new UserNotFoundException("User " + userName + " was not found");
        }
    }

    @Override
    public User addUser(User user) {
        var userList = (List<User>) userRepository.findAll();
        if (checkTrung(userList, user.getUserName())) {
            throw new UserAlreadyExistedException("User " + user.getUserName() + " already existed");
        } else {
            return userRepository.save(user);
        }
    }

    public boolean checkTrung(List<User> users, String userName) {
        for (User s : users) {
            if (s.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void removeUser(String userName) {
        userRepository.deleteById(userName);
    }

//    @Override
//    public Optional<User> updateByUserName(String userName) {
//        var findUser= userRepository.findById(userName);
//
//    }
}
