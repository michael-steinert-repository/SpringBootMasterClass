package com.example.SpringBootMasterClass.dao;

import com.example.SpringBootMasterClass.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDao {
    List<User> selectAllUser();

    Optional<User> selectUserByUserUid(UUID userUid);

    int updateUser(User user);

    int deleteUserByUserUid(UUID userUid);

    int insertUser(UUID userUid, User user);
}
