package com.grt.riceshop.user;

import com.grt.riceshop.user.dto.CreateUserRequest;
import com.grt.riceshop.user.dto.UserResponse;
import com.grt.riceshop.user.entity.Role;
import com.grt.riceshop.user.entity.User;
import com.grt.riceshop.user.enums.UserStatus;
import com.grt.riceshop.user.mapper.UserMapper;
import com.grt.riceshop.user.repository.RoleRepository;
import com.grt.riceshop.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
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
        return userMapper.mapToDTO(savedUser);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.mapToDTO(user);
    }
}
