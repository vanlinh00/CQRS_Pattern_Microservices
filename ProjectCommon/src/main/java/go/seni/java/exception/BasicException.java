package go.seni.java.exception;

import go.seni.java.enums.MessageCode;
import lombok.Data;

import java.util.List;

/**
 * @author PhuocVD
 */
@Data
public class BasicException extends RuntimeException {

    private String message;
    private MessageCode messageCode;
    private List<String> errors;

    public BasicException() {

    }

    public BasicException(String message) {
        this.message = message;
    }

    public BasicException(MessageCode messageCode) {
        this.messageCode = messageCode;
    }

    public BasicException(String message, MessageCode messageCode) {
        this.message = message;
        this.messageCode = messageCode;
    }

    public BasicException(List<String> errors) {
        this.errors = errors;
    }

//    public BasicException(List<String> errors, String message) {
//        super(message);
//        this.message = message;
//        this.messageCode = MessageCode.BAD_REQUEST;
//        this.errors = errors;
//    }
//
//    public BasicException(List<String> errors, String message, MessageCode messageCode) {
//        super(message);
//        this.message = message;
//        this.messageCode = messageCode;
//        this.errors = errors;
//    }
}
