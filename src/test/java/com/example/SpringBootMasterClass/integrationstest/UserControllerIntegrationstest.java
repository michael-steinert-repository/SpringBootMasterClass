package com.example.SpringBootMasterClass.integrationstest;

import com.example.SpringBootMasterClass.controller.UserController;
import com.example.SpringBootMasterClass.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllerIntegrationstest {

	@Autowired
	private UserController userController;

	@Test
	public void itShouldFetchAllUsers() throws Exception {
		//Given
		User user = new User(null, "Michael", "Steinert", User.Gender.MALE, 26, "steinert-michael@gmx.de");

		//When
		userController.insertNewUser(user);
		List<User> users = userController.fetchUsers(null);

		//Then
		assertThat(users).hasSizeGreaterThanOrEqualTo(1);

	}

	@Test
	public void itShouldFetchUsersByGender() throws Exception {
		//Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "Michael", "Steinert", User.Gender.MALE, 26, "steinert-michael@gmx.de");

		//when
		userController.insertNewUser(user);

		//Then
		List<User> femaleUsers = userController.fetchUsers(User.Gender.FEMALE.name());
		assertThat(femaleUsers).extracting("userUid").doesNotContain(userUid);
		assertThat(femaleUsers).extracting("firstName").doesNotContain(user.getFirstName());
		assertThat(femaleUsers).extracting("lastName").doesNotContain(user.getLastName());
		assertThat(femaleUsers).extracting("gender").doesNotContain(user.getGender());
		assertThat(femaleUsers).extracting("age").doesNotContain(user.getAge());
		assertThat(femaleUsers).extracting("email").doesNotContain(user.getEmail());

		List<User> maleUsers = userController.fetchUsers(User.Gender.MALE.name());
		assertThat(maleUsers).extracting("userUid").contains(userUid);
		assertThat(maleUsers).extracting("firstName").contains(user.getFirstName());
		assertThat(maleUsers).extracting("lastName").contains(user.getLastName());
		assertThat(maleUsers).extracting("gender").contains(user.getGender());
		assertThat(maleUsers).extracting("age").contains(user.getAge());
		assertThat(maleUsers).extracting("email").contains(user.getEmail());
	}

	@Test
	public void itShouldInsertUser() throws Exception {
		//Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "Michael", "Steinert", User.Gender.MALE, 26, "steinert-michael@gmx.de");

		//When
		userController.insertNewUser(user);

		//Then
		User newUser = userController.fetchUser(userUid);
		assertThat(newUser).isEqualToComparingFieldByField(user);
	}

	@Test
	public void itShouldUpdateUser() throws Exception {
		//Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "Michael", "Steinert", User.Gender.MALE, 26, "steinert-michael@gmx.de");

		//When
		userController.insertNewUser(user);
		User newUser = new User(userUid, "Marie", "Schmidt", User.Gender.FEMALE, 24, "schmidt-marie@gmx.de");
		userController.updateUpdate(newUser);

		//Then
		user = userController.fetchUser(userUid);
		assertThat(user).isEqualToComparingFieldByField(newUser);
	}

	@Test
	public void itShouldDeleteUser() throws Exception {
		//Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "Michael", "Steinert", User.Gender.MALE, 26, "steinert-michael@gmx.de");

		//When
		userController.insertNewUser(user);
		userController.deleteUser(userUid);

		//Then
		//assertThat(userController.fetchUser(userUid)).isEqualTo(NotFoundException.class);
	}
}
