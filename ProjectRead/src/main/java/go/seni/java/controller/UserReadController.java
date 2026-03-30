package go.seni.java.controller;

import go.seni.java.Factory.ResponseFactory;
import go.seni.java.dto.UserResponse;
import go.seni.java.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class UserReadController {

    private final UserService userService;

    public UserReadController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseFactory.success(UserResponse.from(userService.getById(id)));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe() {
        return ResponseFactory.success(userService.getCurrentUser());
    }
}
