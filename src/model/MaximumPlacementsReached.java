package model;

/**
 * INFO
 *
 * @author Robert Leenders
 */
public class MaximumPlacementsReached extends RuntimeException {
    private Piece piece;

    /**
     * Default constructor.
     */
    public MaximumPlacementsReached() {
        super();
    }

    /**
     * A constructor that takes a message.
     *
     * @param s message
     */
    public MaximumPlacementsReached(String s) {
        super(s);
    }

    /**
     * A constructor that takes a message and the piece for which the maximum is reached.
     *
     * @param s message
     * @param p the piece
     */
    public MaximumPlacementsReached(String s, Piece p) {
        super(s);
        piece = p;
    }
}
