package com.travelplanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.travelplanner.entity.User;
import com.travelplanner.repository.UserRepository;
import com.travelplanner.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Anbu Selvan");
        user.setEmail("anbu@gmail.com");
        user.setPassword("password123");
    }

    @Test
    void registerUser_shouldRegisterNewUser() {

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(user))
                .thenReturn(user);

        User result = userService.registerUser(user);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        assertEquals("USER", result.getRole());

        verify(userRepository).findByEmail(user.getEmail());
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(user);
    }

    @Test
    void registerUser_shouldReturnNullWhenEmailAlreadyExists() {

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        User result = userService.registerUser(user);

        assertNull(result);

        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void findByEmail_shouldReturnUser() {

        when(userRepository.findByEmail("anbu@gmail.com"))
                .thenReturn(Optional.of(user));

        Optional<User> result =
                userService.findByEmail("anbu@gmail.com");

        assertTrue(result.isPresent());
        assertEquals("anbu@gmail.com", result.get().getEmail());

        verify(userRepository).findByEmail("anbu@gmail.com");
    }

    @Test
    void findById_shouldReturnUser() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(userRepository).findById(1L);
    }

    @Test
    void findById_shouldReturnNullWhenUserNotFound() {

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        User result = userService.findById(99L);

        assertNull(result);

        verify(userRepository).findById(99L);
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {

        User secondUser = new User();
        secondUser.setId(2L);
        secondUser.setName("Test User");
        secondUser.setEmail("test@gmail.com");

        when(userRepository.findAll())
                .thenReturn(List.of(user, secondUser));

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("anbu@gmail.com", result.get(0).getEmail());
        assertEquals("test@gmail.com", result.get(1).getEmail());

        verify(userRepository).findAll();
    }
}