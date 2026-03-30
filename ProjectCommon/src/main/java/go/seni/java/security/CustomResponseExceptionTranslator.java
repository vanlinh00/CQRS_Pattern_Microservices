package go.seni.java.security;

import go.seni.java.enums.MessageCode;
import go.seni.java.exception.BasicException;
import go.seni.java.exception.CustomOAuth2Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
@Slf4j
public class CustomResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    private final HttpServletRequest req;

    public CustomResponseExceptionTranslator(HttpServletRequest req) {
        this.req = req;
    }

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        if (e instanceof BasicException) {
            BasicException basicException = (BasicException) e;
            return basicExceptionResponse(basicException);
        }
        if (e.getCause() instanceof UnauthorizedUserException) {
            BasicException basicException = new BasicException(MessageCode.FORBIDDEN);
            return basicExceptionResponse(basicException);
        }
//        if (e instanceof InvalidTokenException && ApiPath.ADMIN.OAUTH_CHECK_TOKEN.equals(req.getRequestURI())) {
//            Map<String, String> mapError = new HashMap<>();
//            mapError.put("aud", null);
//            mapError.put("user_name", null);
//            mapError.put("scope", null);
//            mapError.put("exp", null);
//            mapError.put("active", String.valueOf(false));
//            mapError.put("jti", null);
//            mapError.put("client_id", null);
//            OAuth2Exception auth2Exception = OAuth2Exception.valueOf(mapError);
//            return new ResponseEntity<>(auth2Exception, HttpStatus.OK);
//        }

//        if (e instanceof InvalidGrantException) {
//            String grantType = req.getParameter("grant_type");
//            if (grantType != null && grantType.equals(BaseConst.GRANT_TYPE_PASSWORD)) {
//
//                String code = MessageCode.ACCOUNT_INCORRECT.getCode();
//
//                Map<String, String> mapError = new HashMap<>();
//                mapError.put(StringConst.SUCCESS, StringConst.FALSE);
//                mapError.put(StringConst.CODE, code);
//                mapError.put(StringConst.MESSAGE, BaseUtils.getErrorMessageLanguage(req, code));
//                mapError.put(StringConst.DETAILS, e.getMessage());
//
//                OAuth2Exception auth2Exception = OAuth2Exception.valueOf(mapError);
//                log.error("Login fail with details = {}", mapError);
//                return new ResponseEntity<>(auth2Exception, HttpStatus.BAD_REQUEST);
//            } else if (grantType != null && grantType.equals(BaseConst.GRANT_TYPE_REFRESH)) {
//                Map<String, String> mapError = new HashMap<>();
//                mapError.put(StringConst.SUCCESS, StringConst.FALSE);
//                mapError.put(StringConst.CODE, "invalid_grant");
//                mapError.put(StringConst.MESSAGE, "Invalid grant token");
//                mapError.put(StringConst.DETAILS, e.getMessage());
//
//                OAuth2Exception auth2Exception = OAuth2Exception.valueOf(mapError);
//                return new ResponseEntity<>(auth2Exception, HttpStatus.BAD_REQUEST);
//            }
//        }

        if (Objects.nonNull(e.getCause()) && e.getCause() instanceof BasicException) {
            BasicException basicException = (BasicException) e.getCause();
            return basicExceptionResponse(basicException);
        }

        return super.translate(e);
    }

    public ResponseEntity<OAuth2Exception> basicExceptionResponse(BasicException basicException) {
        Map<String, Object> mapError = new HashMap<>();
        mapError.put("success", false);
        mapError.put("code", basicException.getMessageCode().getCode());
        String message = basicException.getMessage();
        if (StringUtils.isEmpty(message)) {
          //  message = BaseUtils.getErrorMessageLanguage(req, basicException.getMessageCode().getCode());
        }
        mapError.put("message", message);
        CustomOAuth2Exception auth2Exception = CustomOAuth2Exception.fromMap(mapError);
        log.error("Basic exception with details = {}", mapError);
        return new ResponseEntity<>(auth2Exception, HttpStatus.BAD_REQUEST);
    }

}
