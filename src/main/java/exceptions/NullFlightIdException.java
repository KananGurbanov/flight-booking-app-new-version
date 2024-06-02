package exceptions;

public class NullFlightIdException extends RuntimeException{
    public NullFlightIdException(String message) {
        super(message);
    }
}
