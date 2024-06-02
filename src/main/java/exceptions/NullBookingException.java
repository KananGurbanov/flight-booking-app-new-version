package exceptions;

public class NullBookingException extends RuntimeException{
    public NullBookingException(String message) {
        super(message);
    }
}
