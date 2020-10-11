package com.example.SpringBootMasterClass.controller;

import com.example.SpringBootMasterClass.model.User;
import com.example.SpringBootMasterClass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/user")
@Validated
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> fetchUsers(@QueryParam("gender") String gender) {
        return userService.getAllUsers(Optional.ofNullable(gender));
    }

    @GetMapping(path = "{userUid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User fetchUser(@PathVariable("userUid") UUID userUid) {
        return userService
                .getUser(userUid)
                .orElseThrow(() -> new NotFoundException("User " + userUid + " not found."));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer insertNewUser(@RequestBody @Valid User user) {
        return userService.insertUser(user);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer updateUpdate(@RequestBody @Valid User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping(path = "{userUid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer deleteUser(@PathVariable("userUid") UUID userUid) {
        return userService.removeUser(userUid);
    }

    class ErrorMessage {
        String errorMessage;

        public ErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
