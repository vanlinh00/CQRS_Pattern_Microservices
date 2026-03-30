package go.seni.java.entity;

import lombok.Data;

@Data
public class OAuthLoginRequest {

    private String username;

    private String password;

    private String deviceToken;

    private String grant_type = "password";

    private boolean remember = true;

    private String refresh_token;

}
