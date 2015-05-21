package model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Class which represent a piece in a particular way, a so called orientation.
 *
 * @author Robert Leenders
 * @see Piece
 * @see Position
 */
public class Orientation implements Iterable<Position> {
    /**
     * Set containing all the positions of this orientation
     */
    private Set<Position> positions;
    /** The width and height form the bounding box for this orientation */
    /**
     * Width of the bounding box this orientation is in
     */
    private int width;
    /**
     * Height of the bounding box this orientation is in
     */
    private int height;

    /**
     * Holds the column of the first occupied cell (positions)
     */
    private int firstOccupiedColumn = Integer.MAX_VALUE;

    /**
     * Creates a new orientation object
     *
     * @modifies positions
     * @post Initializes {@code positions}
     */
    public Orientation() {
        positions = new HashSet<Position>();
    }

    /**
     * Compares this {@code Orientation} to the specified object.
     *
     * The result is {@code true} if and only if the argument is not {@code null} and is a {@code Orientation} object
     * that represents the same width, height and positions as this {@code Orientation}.
     *
     * @param o the object to compare this {@code Orientation} against
     * @return {@code true} if the {@code Orientation} are equal; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Orientation that = (Orientation) o;

        if (height != that.height) return false;
        if (width != that.width) return false;
        if (positions != null ? !positions.equals(that.positions) : that.positions != null) return false;

        return true;
    }

    /**
     * Returns a hash code for this {@code Orientation}.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        int result = positions != null ? positions.hashCode() : 0;
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }

    /**
     * Returns the string representation of this {@code Orientation}.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "Orientation{" +
               "positions=" + positions +
               ", width=" + width +
               ", height=" + height +
               '}';
    }

    /**
     * Adds a new position to this orientation
     *
     * @param row    the row of the new position
     * @param column the column of the new position
     * @throws IllegalArgumentException if row or column is negative
     * @modifies positions, width and height
     * @post adds a new {@code Position(row, column)} to {@code positions} if the new position isn't already occupied
     * and updates the bounding box if needed
     */
    public void addPosition(int row, int column) throws IllegalArgumentException {
        boolean positionAdded = positions.add(new Position(row, column));
        if (positionAdded) {
            updateBoundingBox(row, column);
            if (row == 0) // A first cell can only occur in the first row
                updateFirstOccupiedColumn(column);
        }
    }

    /**
     * Makes sure the bounding box has the right size.
     *
     * If the row or column of the added position is greater than the current box, it must be updated. This method
     * should not be called independently, it should be only called if a new position is added.
     *
     * @param row    the row of the new position
     * @param column the column of the new position
     * @pre row and column must be positive
     * @modifies width and height
     * @post {@code width = \max(width, column); height = \max(height, row)}
     * @see Orientation#addPosition(int, int)
     */
    private void updateBoundingBox(int row, int column) {
        assert (row >= 0);
        assert (column >= 0);
        width = Math.max(width, column + 1);
        height = Math.max(height, row + 1);
    }

    /**
     * Updates the column if needed.
     *
     * @param firstOccupiedColumn the first occupied column of the added piece
     */
    private void updateFirstOccupiedColumn(int firstOccupiedColumn) {
        this.firstOccupiedColumn = Math.min(this.firstOccupiedColumn, firstOccupiedColumn);
    }

    /**
     * Returns the first occupied column
     *
     * @return
     */
    public int getFirstOccupiedColumn() {
        return firstOccupiedColumn;
    }

    /**
     * Returns the height of the bounding box
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the width of the bounding box
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns whether there are any positions
     *
     * @return {@code true} if there is at least one position; false otherwise
     * @see java.util.HashSet#isEmpty()
     */
    public boolean isEmpty() {
        return positions.isEmpty();
    }

    /**
     * Returns a boolean whether the given position is occupied
     *
     * @param row    the row of the position
     * @param column the column of the positions
     * @return {@code true} if this orientation containsPoint {@code new Position(row, column)}; false otherwise
     */
    public boolean isPositionOccupied(int row, int column) {
        return !(row < 0 || column < 0) && positions.contains(new Position(row, column));
    }

    /**
     * Returns an iterator to iterate over the positions.
     *
     * @return iterator to iterate over the positions
     */
    public Iterator<Position> iterator() {
        return positions.iterator();
    }
}
