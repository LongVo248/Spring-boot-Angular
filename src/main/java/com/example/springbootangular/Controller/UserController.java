package com.example.springbootangular.Controller;

import com.example.springbootangular.Model.User;
import com.example.springbootangular.Model.utils.PagingHeaders;
import com.example.springbootangular.Model.utils.PagingResponse;
import com.example.springbootangular.Repository.UserPageRepository;
import com.example.springbootangular.Service.Impl.UserServiceImpl;
import com.example.springbootangular.Service.UserService;
import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserPageRepository userPageRepository;
    private final UserServiceImpl userServiceImpl;
    private final UserService userService;

    @Autowired
    public UserController(UserPageRepository userPageRepository, UserServiceImpl userServiceImpl, UserService userService) {
        this.userPageRepository = userPageRepository;
        this.userServiceImpl = userServiceImpl;
        this.userService = userService;
    }

//    @GetMapping("/users/registeruser")
//    public User registerUser(@RequestBody User user){
//        String username= user.getUserName();
//        if (username!=null && !"".equals(username)){
//            User userobj= userService.findByUserName(username);
//        }
//    }
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser() {
        return new ResponseEntity<>(userService.findAllUser(), HttpStatus.OK);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUserByUsername(@Valid @PathVariable("username") String username) {
        Optional<User> userOptional = userService.findByUserName(username);
        return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
//        String pwd= user.getPwd();
//        String encryptPwd= passwordEncoder.encode(pwd);
//        user.setPwd(encryptPwd);
        User newUser = userService.addUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/users/{username}")
    public ResponseEntity<User> updateUser(@Valid @PathVariable("username") String username, @Valid @RequestBody User user) {
        Optional<User> userOptional = userService.findByUserName(username);
        return userOptional.map(user1 -> {
            user.setUserName(user1.getUserName());
            return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<User> deleteUser(@Valid @PathVariable("username") String username) {
        try {
            userService.removeUser(username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //page
    @GetMapping("/users/page")
    public Page<User> getUsers(@RequestParam Optional<Integer> page, @RequestParam Optional<String> sortBy, @RequestParam Optional<Integer> size) {
        return userPageRepository.findAll(
                PageRequest.of(
                        page.orElse(0),
                        size.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("userName")
                )
        );
    }


    //filter- searching
    @Transactional
    @GetMapping(value = "/users/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<User>> get(
            @And({
                    @Spec(path = "username", params = "username", spec = Like.class),
                    @Spec(path = "firstname", params = "firstname", spec = Like.class),
                    @Spec(path = "lastname", params = "lastname", spec = In.class),
                    @Spec(path = "email", params = "email", spec = Like.class),
            }) Specification<User> spec,
            Sort sort,
            @RequestHeader HttpHeaders headers) {
        final PagingResponse response = userServiceImpl.get(spec, headers, sort);
        return new ResponseEntity<>(response.getElements(), returnHttpHeaders(response), HttpStatus.OK);
    }

    public HttpHeaders returnHttpHeaders(PagingResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(PagingHeaders.COUNT.getName(), String.valueOf(response.getCount()));
        headers.set(PagingHeaders.PAGE_SIZE.getName(), String.valueOf(response.getPageSize()));
        headers.set(PagingHeaders.PAGE_OFFSET.getName(), String.valueOf(response.getPageOffset()));
        headers.set(PagingHeaders.PAGE_NUMBER.getName(), String.valueOf(response.getPageNumber()));
        headers.set(PagingHeaders.PAGE_TOTAL.getName(), String.valueOf(response.getPageTotal()));
        return headers;
    }
}
