package com.grt.riceshop.user;

import com.grt.riceshop.user.dto.AuthResponse;
import com.grt.riceshop.user.dto.CreateUserRequest;
import com.grt.riceshop.user.dto.LoginRequest;
import com.grt.riceshop.user.dto.UserResponse;
import com.grt.riceshop.user.entity.User;
import com.grt.riceshop.user.enums.UserStatus;
import com.grt.riceshop.user.mapper.UserMapper;
import com.grt.riceshop.user.repository.UserRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Duration TOKEN_LIFETIME = Duration.ofMinutes(15);

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtEncoder jwtEncoder;

    public AuthService(
            UserService userService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            JwtEncoder jwtEncoder
    ) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtEncoder = jwtEncoder;
    }

    public UserResponse register(CreateUserRequest request) {
        return userService.createUser(request);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email())
                .filter(account -> account.status == UserStatus.ACTIVE)
                .filter(account -> passwordEncoder.matches(request.password(), account.passwordHash))
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(TOKEN_LIFETIME);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("riceshop")
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .subject(user.email)
                .claim("userId", user.id)
                .claim("roles", user.roles.stream()
                        .map(role -> role.name)
                        .collect(Collectors.toList()))
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        return new AuthResponse(
                token,
                "Bearer",
                TOKEN_LIFETIME.toSeconds(),
                userMapper.mapToDTO(user)
        );
    }
}
