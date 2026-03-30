package go.seni.java.Factory;


import go.seni.java.enums.MessageCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResponseFactory {

    private static ResponseFactory factory;

    private HttpServletRequest httpServletRequest;

    @PostConstruct
    public void init() {
        factory = this;
        factory.httpServletRequest = this.httpServletRequest;
    }

    public static ResponseEntity success() {
        GenericResponse<Object> responseObject = getSuccessResponse();
        return ResponseEntity.ok(responseObject);
    }
    public static ResponseEntity success(Object data) {
        GenericResponse<Object> responseObject = getSuccessResponse();
        responseObject.setData(data);
        return ResponseEntity.ok(responseObject);
    }

    private static GenericResponse<Object> getSuccessResponse() {
        GenericResponse<Object> responseObject = new GenericResponse<>();
        responseObject.setSuccess(true);
        responseObject.setCode(MessageCode.SUCCESS.getCode());
        responseObject.setMessage("OK");
        return responseObject;
    }





    public static ResponseEntity error(String code, String message) {
        return error(HttpStatus.BAD_REQUEST, code, message, null);
    }

    public static ResponseEntity error(String code, String message, String... details) {
        return error(HttpStatus.BAD_REQUEST, code, message, details != null ? Arrays.asList(details) : null);
    }

    public static ResponseEntity error(String code, String message, List<String> details) {
        return error(HttpStatus.BAD_REQUEST, code, message, details);
    }

    public static ResponseEntity error(HttpStatus httpStatus, String code, String message, List<String> details) {
        GenericResponse<Object> responseObject = new GenericResponse<>();
        responseObject
                .setSuccess(false)
                .setCode(code)
                .setMessage(message)
                .setDetails(details == null ? new ArrayList<>() : details);
        return new ResponseEntity<>(responseObject, httpStatus);
    }
}
