package go.seni.java.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import java.util.Map;

@JsonSerialize(using = CustomOAuth2ExceptionSerializer.class)
public class CustomOAuth2Exception extends OAuth2Exception {

    private final boolean success;

    public CustomOAuth2Exception(boolean success) {
        super("");
        this.success = success;
    }

    @JsonProperty("success")
    public boolean isSuccess() {
        return success;
    }

    public static CustomOAuth2Exception fromMap(Map<String, Object> params) {
        boolean successFlag = Boolean.FALSE;
        Object successObj = params.get("success");
        if (successObj instanceof Boolean) {
            successFlag = (Boolean) successObj;
        }
        CustomOAuth2Exception ex = new CustomOAuth2Exception( successFlag);
        params.forEach((key, value) -> {
            if (!"error".equals(key) && !"error_description".equals(key) && !"success".equals(key)) {
                ex.addAdditionalInformation(key, String.valueOf(value));
            }
        });
        return ex;
    }
}