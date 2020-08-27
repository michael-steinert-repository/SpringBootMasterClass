package com.example.SpringBootMasterClass.dao;

import com.example.SpringBootMasterClass.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FakeDataDao implements UserDao{

    private Map<UUID, User> database;

    public FakeDataDao() {
        database = new HashMap<>();
        UUID userUid = UUID.randomUUID();
        database.put(userUid, new User(userUid, "Michael", "Steinert", User.Gender.MALE, 26, "steinert-michael@gmx.de"));
    }

    @Override
    public List<User> selectAllUser() {
        return new ArrayList<>(database.values());
    }

    @Override
    public Optional<User> selectUserByUserUid(UUID userUid) {
        return Optional.ofNullable(database.get(userUid));
    }

    @Override
    public int updateUser(User user) {
        database.put(user.getUserUid(), user);
        return 1;
    }

    @Override
    public int deleteUserByUserUid(UUID userUid) {
        database.remove(userUid);
        return 1;
    }

    @Override
    public int insertUser(UUID userUIid, User user) {
        database.put(userUIid, user);
        return 1;
    }
}
