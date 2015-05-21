package model;

/**
 * An unchecked exception to signal an illegal request.
 */
public class IllegalRequestException extends RuntimeException {

    /**
     * A constructor that takes a message.
     *
     * @param s message
     */
    public IllegalRequestException(String s) {
        super(s);
    }
}