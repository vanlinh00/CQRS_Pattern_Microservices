package go.seni.java.dto;

import go.seni.java.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserResponse {
    private Long id;
    private Long groupId;
    private String loginId;
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private Float heightCm;
    private Float weightKg;
    private String email;
    private Integer point;
    private String role;

    public static UserResponse from(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .groupId(u.getGroupId())
                .loginId(u.getLoginId())
                .fullName(u.getFullName())
                .gender(u.getGender())
                .dateOfBirth(u.getDateOfBirth())
                .heightCm(u.getHeightCm())
                .weightKg(u.getWeightKg())
                .email(u.getEmail())
                .point(u.getPoint())
                .role(u.getRole())
                .build();
    }
}