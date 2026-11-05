package com.grt.riceshop.user;

import com.grt.riceshop.user.dto.CreateUserRequest;
import com.grt.riceshop.user.dto.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(CreateUserRequest createUserRequest) {
        if (userRepository.existsByEmailIgnoreCase(createUserRequest.email())) {
            throw new EmailAlreadyExistsException();
        }

        Instant now = Instant.now();

        User user = new User();
        user.fullName = createUserRequest.name();
        user.email = createUserRequest.email();
        user.passwordHash = passwordEncoder.encode(createUserRequest.password());
        user.status = UserStatus.ACTIVE;
        user.emailVerified = false;
        user.createdAt = now;
        user.updatedAt = now;

        Role customerRole = roleRepository.findByName("CUSTOMER");
        user.roles.add(customerRole);

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser.id, savedUser.fullName, savedUser.email);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(user.id, user.fullName, user.email);
    }
}
