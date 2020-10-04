package com.example.SpringBootMasterClass.controller;

import com.example.SpringBootMasterClass.model.User;
import com.example.SpringBootMasterClass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/users")
    public List<User> fetchUsers(@QueryParam("gender") String gender) {
        return userService.getAllUsers(Optional.ofNullable(gender));
    }

    @GetMapping(path = "{userUid}")
    public ResponseEntity<?> fetchUser(@PathVariable("userUid") UUID userUid) {
        //Optional<User> userOptional = userService.getUser(userUid);
        //if(userOptional.isPresent()) {
        //    return ResponseEntity.ok(userOptional.get());
        //}
        //return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("User: " + userUid + " was not found."));
        //Functional Programming
        return userService.getUser(userUid).<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage("User: " + userUid + " was not found.")));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertNewUser(@RequestBody User user) {
        int result = userService.insertUser(user);
        return getIntegerResponseEntity(result);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> updateUpdate(@RequestBody User user) {
        int result = userService.updateUser(user);
        return getIntegerResponseEntity(result);
    }

    @DeleteMapping(path = "{userUid}")
    public ResponseEntity<Integer> deleteUser(@PathVariable("userUid") UUID userUid) {
        int result = userService.removeUser(userUid);
        return getIntegerResponseEntity(result);
    }

    private ResponseEntity<Integer> getIntegerResponseEntity(int result) {
        if (result == 1) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
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
