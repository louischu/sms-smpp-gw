package smpp.exceptions;

/**
 * Created by luyenchu on 6/28/16.
 */
public class SendSmsException extends Exception {
    public SendSmsException() {
    }

    public SendSmsException(Throwable cause) {
        super(cause);
    }

    public SendSmsException(String message) {
        super(message);
    }

    public SendSmsException(String message, Throwable cause) {
        super(message, cause);
    }
}
