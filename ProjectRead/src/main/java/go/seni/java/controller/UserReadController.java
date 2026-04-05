package go.seni.java.controller;

import go.seni.java.Factory.ResponseFactory;
import go.seni.java.dto.UserResponse;
import go.seni.java.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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


    @GetMapping("/users/list")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> fakeUsers = new ArrayList<>();

        // 1. Add Sơn Tùng M-TP
        fakeUsers.add(UserResponse.builder()
                .id(1L)
                .fullName("Sơn Tùng M-TP")
                .loginId("sontungmtp")
                .email("sontung@mtp.vn")
                .role("ROLE_USER")
                .build());

        // 2. Add multiple Đen Vâu entries (as seen in your screenshot)
        for (long i = 2; i <= 9; i++) {
            fakeUsers.add(UserResponse.builder()
                    .id(i)
                    .fullName("Đen Vâu")
                    .loginId("denvau")
                    .email("den@vau.vn")
                    .role("ROLE_USER")
                    .build());
        }

        return ResponseFactory.success(fakeUsers);

    }

//    @GetMapping("api/users/{id}")
//    public ResponseEntity<UserResponse>  getById(@PathVariable Long id) {
//
//
//        return ResponseFactory.success(UserResponse.from(userService.getById(id)));
//    }

}
