package com.grt.riceshop.user;

import com.grt.riceshop.user.dto.LoginRequest;
import com.grt.riceshop.user.dto.UserResponse;
import com.grt.riceshop.user.entity.Role;
import com.grt.riceshop.user.entity.User;
import com.grt.riceshop.user.enums.UserStatus;
import com.grt.riceshop.user.mapper.UserMapper;
import com.grt.riceshop.user.repository.UserRepository;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;
    private JwtEncoder jwtEncoder;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userMapper = mock(UserMapper.class);
        jwtEncoder = mock(JwtEncoder.class);
        authService = new AuthService(userService, userRepository, passwordEncoder, userMapper, jwtEncoder);
    }

    @Test
    void loginReturnsBearerTokenForActiveUserWithMatchingPassword() {
        User user = activeUser();
        when(userRepository.findByEmailIgnoreCase(user.email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("correct-password", user.passwordHash)).thenReturn(true);
        when(userMapper.mapToDTO(user)).thenReturn(new UserResponse(user.id, user.fullName, user.email));
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(
                Jwt.withTokenValue("signed-token")
                        .header("alg", "HS256")
                        .issuedAt(Instant.now())
                        .expiresAt(Instant.now().plusSeconds(900))
                        .build()
        );

        var response = authService.login(new LoginRequest(user.email, "correct-password"));

        assertEquals("signed-token", response.accessToken());
        assertEquals("Bearer", response.tokenType());
        assertEquals(900, response.expiresIn());
        assertEquals(user.id, response.user().id());
    }

    @Test
    void loginRejectsUnknownEmail() {
        when(userRepository.findByEmailIgnoreCase("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () ->
                authService.login(new LoginRequest("unknown@example.com", "password")));

        verify(jwtEncoder, never()).encode(any(JwtEncoderParameters.class));
    }

    private User activeUser() {
        User user = new User();
        user.id = 42L;
        user.email = "customer@example.com";
        user.fullName = "Test Customer";
        user.passwordHash = "encoded-password";
        user.status = UserStatus.ACTIVE;
        Role role = new Role();
        role.name = "CUSTOMER";
        user.roles.add(role);
        return user;
    }
}
