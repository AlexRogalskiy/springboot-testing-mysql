package com.mycompany.userservice.service;

import com.mycompany.userservice.exception.UserNotFoundException;
import com.mycompany.userservice.model.User;
import com.mycompany.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@Import(UserServiceImpl.class)
class UserServiceImplTests {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testSaveUser() {
        User user = getDefaultUser();
        given(userRepository.save(any(User.class))).willReturn(user);

        User userSaved = userService.saveUser(user);
        assertThat(userSaved).isEqualTo(user);
    }

    @Test
    void testGetUsersWhenThereIsNone() {
        given(userRepository.findAll()).willReturn(Collections.emptyList());

        List<User> usersFound = userService.getUsers();
        assertThat(usersFound).isEmpty();
    }

    @Test
    void testGetUsersWhenThereIsOne() {
        User user = getDefaultUser();
        List<User> users = Collections.singletonList(user);

        given(userRepository.findAll()).willReturn(users);

        List<User> usersFound = userService.getUsers();
        assertThat(usersFound).hasSize(1);
        assertThat(usersFound.get(0)).isEqualTo(user);
    }

    @Test
    void testValidateAndGetUserByIdWhenExisting() {
        User user = getDefaultUser();
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        User userFound = userService.validateAndGetUserById(user.getId());
        assertThat(userFound).isEqualTo(user);
    }

    @Test
    void testValidateAndGetUserByIdWhenNonExisting() {
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        Throwable exception = assertThrows(UserNotFoundException.class,
                () -> userService.validateAndGetUserById(1L));
        assertThat(exception.getMessage()).isEqualTo("User with id '1' doesn't exist.");
    }

    @Test
    void testValidateAndGetUserByUsernameWhenExisting() {
        User user = getDefaultUser();
        given(userRepository.findUserByUsername(anyString())).willReturn(Optional.of(user));

        User userFound = userService.validateAndGetUserByUsername(user.getUsername());
        assertThat(userFound).isEqualTo(user);
    }

    @Test
    void testValidateAndGetUserByUsernameWhenNonExisting() {
        given(userRepository.findUserByUsername(anyString())).willReturn(Optional.empty());

        Throwable exception = assertThrows(UserNotFoundException.class,
                () -> userService.validateAndGetUserByUsername("ivan"));
        assertThat(exception.getMessage()).isEqualTo("User with username 'ivan' doesn't exist.");
    }

    private User getDefaultUser() {
        User user = new User("ivan", "ivan@test", LocalDate.parse("2018-01-01"));
        user.setId(1L);
        return user;
    }
}
