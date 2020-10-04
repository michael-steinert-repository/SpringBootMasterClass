package com.example.SpringBootMasterClass.service;

import com.example.SpringBootMasterClass.dao.FakeDataDao;
import com.example.SpringBootMasterClass.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class UserServiceTest {

    @Mock
    private FakeDataDao fakeDataDao;
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(fakeDataDao);
    }

    @Test
    void shouldGetAllUsers() {
        UUID userUid = UUID.randomUUID();
        User user = new User(userUid, "Michael", "Steinert", User.Gender.MALE, 26, "steinert-michael@gmx.de");
        ImmutableList<User> users = new ImmutableList.Builder<User>()
                .add(user)
                .build();
        given(fakeDataDao.selectAllUser()).willReturn(users);

        List<User> allUsers = userService.getAllUsers(Optional.empty());
        assertThat(allUsers).hasSize(1);
        User newUser = allUsers.get(0);
        assertUserFields(newUser);
    }

    @Test
    void shouldGetUser() {
        UUID userUid = UUID.randomUUID();
        User user = new User(userUid, "Michael", "Steinert", User.Gender.MALE, 26, "steinert-michael@gmx.de");
        given(fakeDataDao.selectUserByUserUid(userUid)).willReturn(Optional.of(user));

        Optional<User> userOptional = userService.getUser(userUid);
        assertThat(userOptional.isPresent()).isTrue();
        User newUser = userOptional.get();
        assertUserFields(newUser);
    }

    @Test
    public void shouldGetAllUserByGender() throws Exception {
        UUID user1Uid = UUID.randomUUID();
        User user1 = new User(user1Uid, "Michael", "Steinert", User.Gender.MALE, 26, "steinert-michael@gmx.de");
        UUID user2Uid = UUID.randomUUID();
        User user2 = new User(user2Uid, "Marie", "Schmidt", User.Gender.FEMALE, 24, "schmidt-marie@gmx.de");

        ImmutableList<User> users = new ImmutableList.Builder<User>()
                .add(user1)
                .add(user2)
                .build();
        given(fakeDataDao.selectAllUser()).willReturn(users);

        List<User> allMaleUsers = userService.getAllUsers(Optional.of("MALE"));
        assertThat(allMaleUsers).hasSize(1);
        assertUserFields(allMaleUsers.get(0));
    }

    @Test
    public void shouldThrowExceptionWhenGenderIsInvalid() throws Exception {
        assertThatThrownBy(() -> userService.getAllUsers(Optional.of("Transgender")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Invalid Gender.");
    }

    @Test
    void shouldUpdateUser() {
        UUID userUid = UUID.randomUUID();
        User user = new User(userUid, "Michael", "Steinert", User.Gender.MALE, 26, "steinert-michael@gmx.de");
        given(fakeDataDao.selectUserByUserUid(userUid)).willReturn(Optional.of(user));
        given(fakeDataDao.updateUser(user)).willReturn(1);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        int updateResult = userService.updateUser(user);

        verify(fakeDataDao).selectUserByUserUid(userUid);
        verify(fakeDataDao).updateUser(captor.capture());

        User newUser = captor.getValue();
        assertUserFields(newUser);
        assertThat(updateResult).isEqualTo(1);
    }

    @Test
    void shouldRemoveUser() {
        UUID userUid = UUID.randomUUID();
        User user = new User(userUid, "Michael", "Steinert", User.Gender.MALE, 26, "steinert-michael@gmx.de");
        given(fakeDataDao.selectUserByUserUid(userUid)).willReturn(Optional.of(user));
        given(fakeDataDao.deleteUserByUserUid(userUid)).willReturn(1);

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        int deleteResult = userService.removeUser(userUid);

        verify(fakeDataDao).selectUserByUserUid(userUid);
        verify(fakeDataDao).deleteUserByUserUid(captor.capture());

        UUID newUserUid = captor.getValue();
        assertThat(newUserUid).isEqualTo(userUid);
        assertThat(deleteResult).isEqualTo(1);
    }

    @Test
    void shouldInsertUser() {
        User user = new User(null, "Michael", "Steinert", User.Gender.MALE, 26, "steinert-michael@gmx.de");
        given(fakeDataDao.insertUser(any(UUID.class), eq(user))).willReturn(1);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        int insertResult = userService.insertUser(user);

        verify(fakeDataDao).insertUser(any(UUID.class),captor.capture());

        User newUser = captor.getValue();

        assertUserFields(newUser);
        assertThat(insertResult).isEqualTo(1);
    }

    private void assertUserFields(User newUser) {
        assertThat(newUser.getFirstName()).isEqualTo("Michael");
        assertThat(newUser.getLastName()).isEqualTo("Steinert");
        assertThat(newUser.getGender()).isEqualTo(User.Gender.MALE);
        assertThat(newUser.getAge()).isEqualTo(26);
        assertThat(newUser.getEmail()).isEqualTo("steinert-michael@gmx.de");
        assertThat(newUser.getUserUid()).isNotNull();
        assertThat(newUser.getUserUid()).isInstanceOf(UUID.class);
    }
}