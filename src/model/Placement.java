package model;

/**
 * Class for placement info
 *
 * @author Robert Leenders
 * @see Box
 * @see Piece
 * @see Position
 * @see Orientation
 */
public class Placement {
    /**
     * Anchor position of the placement
     */
    private Position anchorPosition;
    /**
     * The particular orientation of this placement; this placement must be present in the pieces orientation
     */
    private Orientation orientation;
    /**
     * The piece which is placed
     */
    private Piece piece;

    /**
     * Constructs a new placement object
     *
     * @param anchorPosition anchor position of this placement
     * @param orientation    orientation of this placement
     * @param piece          piece of this placement
     * @throws IllegalArgumentException if given orientation is not present in the pieces orientations
     * @post sets all instance variables if no exception is thrown
     * @modifies anchorPosition, orientation and piece
     */
    public Placement(Position anchorPosition, Orientation orientation, Piece piece) throws IllegalArgumentException {
        if (!piece.containsOrientation(orientation))
            throw new IllegalArgumentException("The given orientation is not present in the pieces orientation(s)");
        this.anchorPosition = anchorPosition;
        this.orientation = orientation;
        this.piece = piece;
    }

    /**
     * Compares this {@code Placement} to the specified object.
     *
     * The result is {@code true} if and only if the argument is not {@code null} and is a {@code Placement} object that
     * represents the same anchor position, orientation and the same piece as this {@code Placement}.
     *
     * @param o the object to compare this {@code Placement} against
     * @return {@code true} if the {@code Placements} are equal; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Placement placement = (Placement) o;

        if (anchorPosition != null ? !anchorPosition.equals(placement.anchorPosition) : placement.anchorPosition != null)
            return false;
        if (orientation != null ? !orientation.equals(placement.orientation) : placement.orientation != null)
            return false;
        if (piece != null ? !piece.equals(placement.piece) : placement.piece != null) return false;

        return true;
    }

    /**
     * Returns a hash code for this {@code Placement}.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        int result = anchorPosition != null ? anchorPosition.hashCode() : 0;
        result = 31 * result + (orientation != null ? orientation.hashCode() : 0);
        result = 31 * result + (piece != null ? piece.hashCode() : 0);
        return result;
    }

    /**
     * Returns the anchor position of this placement
     *
     * @return the anchor position
     */
    public Position getAnchorPosition() {
        return anchorPosition;
    }

    /**
     * Returns the orientation of this placement
     *
     * @return the orientation
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Returns the piece of this placement
     *
     * @return the piece
     */
    public Piece getPiece() {
        return piece;
    }
}
