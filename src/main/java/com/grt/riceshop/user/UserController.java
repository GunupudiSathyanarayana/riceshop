package com.grt.riceshop.user;

import com.grt.riceshop.user.dto.CreateUserRequest;
import com.grt.riceshop.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        UserResponse response = userService.createUser(createUserRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        boolean isAdmin = jwt.getClaimAsStringList("roles").contains("ADMIN");
        boolean isOwner = Long.toString(id).equals(String.valueOf(jwt.getClaim("userId")));
        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("You can only access your own account");
        }

        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok().body(response);
    }
}
