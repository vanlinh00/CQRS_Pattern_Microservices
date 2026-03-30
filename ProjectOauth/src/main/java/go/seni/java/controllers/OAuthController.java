package go.seni.java.controllers;

import go.seni.java.Factory.GenericResponse;
import go.seni.java.Factory.ResponseFactory;
import go.seni.java.constants.ApiPath;
import go.seni.java.entity.OAuthLoginRequest;
import go.seni.java.security.AdminOauthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class OAuthController {
    private final AdminOauthService adminOauthService;

    public OAuthController(AdminOauthService adminOauthService) {
        this.adminOauthService = adminOauthService;
    }

    @PostMapping(value = ApiPath.OAUTH_TOKEN)
    public ResponseEntity<?> getToken(@RequestBody @Valid OAuthLoginRequest request) throws HttpRequestMethodNotSupportedException {
        return ResponseFactory.success(adminOauthService.getToken(request));
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = ApiPath.OAUTH_LOGOUT)
    public ResponseEntity<GenericResponse<Void>>  logout(HttpServletRequest request)
    {
        adminOauthService.logout(request);
       // return ResponseFactory.success(HttpStatus.NO_CONTENT);
        return ResponseFactory.success();

    }
}
