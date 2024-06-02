package exceptions;

public class NullBookingIdException extends RuntimeException{
    public NullBookingIdException(String message) {
        super(message);
    }
}
