package com.example.springbootangular.Service.Impl;

import com.example.springbootangular.Exception.UserAlreadyExistedException;
import com.example.springbootangular.Exception.UserNotFoundException;
import com.example.springbootangular.Model.User;
import com.example.springbootangular.Model.utils.PagingHeaders;
import com.example.springbootangular.Model.utils.PagingResponse;
import com.example.springbootangular.Repository.UserPageRepository;
import com.example.springbootangular.Repository.UserRepository;
import com.example.springbootangular.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserPageRepository userPageRepository;

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserPageRepository userPageRepository, UserRepository userRepository) {
        this.userPageRepository = userPageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

//    @Override
//    public List<User> findAll() {
//        return u;
//    }

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

    //filter and sort
    public PagingResponse get(Specification<User> spec, HttpHeaders headers, Sort sort) {
        if (isRequestPaged(headers)) {
            return get(spec, buildPageRequest(headers, sort));
        } else {
            final List<User> entities = get(spec, sort);
            return new PagingResponse((long) entities.size(), 0L, 0L, 0L, 0L, entities);
        }
    }

    public PagingResponse get(Specification<User> spec, Pageable pageable) {
        Page<User> page = userPageRepository.findAll(spec, pageable);
        List<User> content = page.getContent();
        return new PagingResponse(page.getTotalElements(), (long) page.getNumber(), (long) page.getNumberOfElements(), pageable.getOffset(), (long) page.getTotalPages(), content);
    }

    private boolean isRequestPaged(HttpHeaders headers) {
        return headers.containsKey(PagingHeaders.PAGE_NUMBER.getName()) && headers.containsKey(PagingHeaders.PAGE_SIZE.getName());
    }

    private Pageable buildPageRequest(HttpHeaders headers, Sort sort) {
        int page = Integer.parseInt(Objects.requireNonNull(headers.get(PagingHeaders.PAGE_NUMBER.getName())).get(0));
        int size = Integer.parseInt(Objects.requireNonNull(headers.get(PagingHeaders.PAGE_SIZE.getName())).get(0));
        return PageRequest.of(page, size, sort);
    }

    public List<User> get(Specification<User> spec, Sort sort) {
        return userPageRepository.findAll(spec, sort);
    }
}
