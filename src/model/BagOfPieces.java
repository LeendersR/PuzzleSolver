package model;

import java.util.*;

/**
 * Bag which holds the pieces which are allowed in the puzzle
 *
 * @author Robert Leenders
 * @see Piece
 */
public class BagOfPieces implements Iterable<Piece> {
    /**
     * The pieces the bag has
     */
    private Set<Piece> pieces;

    /**
     * Creates a new BagOfPieces object
     *
     * @modifies pieces
     * @post pieces is created
     */
    public BagOfPieces() {
        pieces = new HashSet<Piece>();
    }

    /**
     * Compares this {@code bag} to the specified object.
     *
     * The result is {@code true} if and only if the argument is not {@code null} and is a {@code bag} object that
     * represents the same pieces as this {@code bag}.
     *
     * @param o the object to compare this {@code bag} against
     * @return {@code true} if the bags are equal; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BagOfPieces that = (BagOfPieces) o;

        if (pieces != null ? !pieces.equals(that.pieces) : that.pieces != null) return false;

        return true;
    }

    /**
     * Returns a hash code for this bag.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return pieces != null ? pieces.hashCode() : 0;
    }

    /**
     * Adds a piece to the bag
     *
     * The bag cannot contain duplicate elements. If the element is already in the bag this operation returns false
     *
     * @param piece the piece to be added
     * @return {@code true} if the operation was successful; false otherwise.
     * @modifies pieces
     * @post the piece is added to bag if isn't already in the bag
     * @see HashSet#add(Object)
     */
    public boolean add(Piece piece) {
        return pieces.add(piece);
    }

    /**
     * Returns a boolean whether the given piece is in the bag
     *
     * @param piece the piece to be searched for
     * @return {@code true} is the piece is present; false otherwise.
     * @see HashSet#contains(Object)
     */
    public boolean contains(Piece piece) {
        return pieces.contains(piece);
    }

    public Set<Piece> getPieces() {
        return pieces;
    }

    /**
     * Returns a iterator to iterate over the bag.
     *
     * The items aren't returned in a particular order.
     *
     * @return iterator for the bag
     * @see java.util.HashSet#iterator()
     */
    public Iterator<Piece> iterator() {
        return pieces.iterator();
    }

    /**
     * Returns a list-iterator to iterate over the bag.
     *
     * The items aren't returned in a particular order.
     *
     * @return list-iterator for the bag
     * @see java.util.List#listIterator()
     */
    public ListIterator<Piece> listIterator() {
        return new ArrayList<Piece>(pieces).listIterator();
    }

    /**
     * Removes a piece from the bag
     *
     * @param piece the piece to be removed
     * @return {@code true} if the operation was successful; false otherwise.
     * @modifies pieces
     * @post the bag doesn't contain the given piece anymore
     * @see HashSet#remove(Object)
     */
    public boolean remove(Piece piece) {
        return pieces.remove(piece);
    }
}
