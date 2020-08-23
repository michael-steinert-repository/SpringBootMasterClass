package com.example.SpringBootMasterClass.dao;

import com.example.SpringBootMasterClass.model.User;

import java.util.*;

public class FakeDataDao implements UserDao{

    private static Map<UUID, User> database;

    static {
        database = new HashMap<>();
        UUID userUid = UUID.randomUUID();
        database.put(userUid, new User(userUid, "Michael", "Steinert", User.Gender.MALE, 26, "steinert-michael@gmx.de"));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(database.values());
    }

    @Override
    public User getUser(UUID userUid) {
        return database.get(userUid);
    }

    @Override
    public int updateUser(User user) {
        database.put(user.getUserUid(), user);
        return 1;
    }

    @Override
    public int removeUser(UUID userUid) {
        database.remove(userUid);
        return 1;
    }

    @Override
    public int insertUser(UUID userUIid, User user) {
        database.put(userUIid, user);
        return 1;
    }
}
