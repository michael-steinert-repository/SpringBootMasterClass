package com.example.SpringBootMasterClass.service;

import com.example.SpringBootMasterClass.dao.UserDao;
import com.example.SpringBootMasterClass.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers(Optional<String> gender) {
        List<User> users = userDao.selectAllUser();
        if(gender.isPresent()){
            try {
                User.Gender getGender = User.Gender.valueOf(gender.get().toUpperCase());
                return users.stream()
                    .filter(user -> user.getGender().equals(getGender))
                    .collect(Collectors.toList());
            } catch (Exception e) {
                throw new IllegalStateException("Invalid Gender.", e);
            }
        }
        return users;
    }

    public Optional<User> getUser(UUID userUid) {
        return userDao.selectUserByUserUid(userUid);
    }

    public int updateUser(User user) {
        Optional<User> optionalUser = getUser(user.getUserUid());
        if (optionalUser.isPresent()) {
            return userDao.updateUser(user);
        }
        throw new NotFoundException("User " + user.getUserUid() + " not found.");
    }

    public int removeUser(UUID uid) {
        UUID userUid = getUser(uid)
                .map(User::getUserUid)
                .orElseThrow(() -> new NotFoundException("User " + uid + " not found."));
        return userDao.deleteUserByUserUid(userUid);
    }

    public int insertUser(User user) {
        UUID userUid = (user.getUserUid() == null) ? UUID.randomUUID() : user.getUserUid();
        return userDao.insertUser(userUid, User.newUser(userUid, user));
    }
}
