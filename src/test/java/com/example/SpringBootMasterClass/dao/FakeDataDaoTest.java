package com.example.SpringBootMasterClass.dao;

import com.example.SpringBootMasterClass.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.junit.jupiter.api.Assertions.*;

class FakeDataDaoTest {

    private FakeDataDao fakeDataDao;

    @BeforeEach
    void setUp() {
        fakeDataDao = new FakeDataDao();
    }

    @Test
    void shouldSelectAllUser() {
        List<User> users = fakeDataDao.selectAllUser();
        assertThat(users).hasSize(1);
        User user = users.get(0);
        assertThat(user.getFirstName()).isEqualTo("Michael");
        assertThat(user.getLastName()).isEqualTo("Steinert");
        assertThat(user.getGender()).isEqualTo(User.Gender.MALE);
        assertThat(user.getEmail()).isEqualTo("steinert-michael@gmx.de");
        assertThat(user.getUserUid()).isNotNull();
    }

    @Test
    void shouldSelectUserByUserUid() {
        UUID userUid = UUID.randomUUID();
        User user = new User(userUid, "Louisa", "Schmidt", User.Gender.FEMALE, 25, "louseia-schmdit@gmx.de");
        fakeDataDao.insertUser(userUid, user);
        assertThat(fakeDataDao.selectAllUser()).hasSize(2);
        Optional<User> userOptional = fakeDataDao.selectUserByUserUid(userUid);
        assertThat(userOptional.isPresent()).isTrue();
        assertThat(userOptional.get()).isEqualToComparingFieldByField(user);
    }

    @Test
    void shouldNotSelectUserByUserUid() {
        Optional<User> userOptional = fakeDataDao.selectUserByUserUid(UUID.randomUUID());
        assertThat(userOptional.isPresent()).isFalse();
    }

    @Test
    void shouldUpdateUser() {
        UUID userUid = fakeDataDao.selectAllUser().get(0).getUserUid();
        User user = new User(userUid, "Louisa", "Schmidt", User.Gender.FEMALE, 25, "louseia-schmdit@gmx.de");
        fakeDataDao.updateUser(user);
        Optional<User> newUser = fakeDataDao.selectUserByUserUid(userUid);
        assertThat(newUser.isPresent()).isTrue();
        assertThat(fakeDataDao.selectAllUser()).hasSize(1);
        assertThat(newUser.get()).isEqualToComparingFieldByField(user);
    }

    @Test
    void shouldDeleteUserByUserUid() {
        UUID userUid = fakeDataDao.selectAllUser().get(0).getUserUid();
        fakeDataDao.deleteUserByUserUid(userUid);
        assertThat(fakeDataDao.selectUserByUserUid(userUid).isPresent()).isFalse();
        assertThat(fakeDataDao.selectAllUser()).isEmpty();
    }

    @Test
    void shouldInsertUser() {
        UUID userUid = UUID.randomUUID();
        User user = new User(userUid, "Louisa", "Schmidt", User.Gender.FEMALE, 25, "louseia-schmdit@gmx.de");
        fakeDataDao.insertUser(userUid, user);
        List<User> users = fakeDataDao.selectAllUser();
        assertThat(users).hasSize(2);
        assertThat(fakeDataDao.selectUserByUserUid(userUid).get()).isEqualToComparingFieldByField(user);
    }
}