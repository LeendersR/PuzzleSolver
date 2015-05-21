package model;

/**
 * Class for holding a position in a box.
 *
 * @author Robert Leenders
 */
public class Position implements Comparable<Position> {
    /**
     * The row, which is always positive
     */
    private int row;
    /**
     * The column, which is always positive
     */
    private int column;

    /**
     * Constructs a new Position object from a row and a column.
     *
     * @param row    the row
     * @param column the column
     * @throws IllegalArgumentException if {@code row < 0 || column < 0}
     * @modifies row, column; if {@code row >= 0 && column >= 0}
     * @post sets row and column
     */
    public Position(int row, int column) throws IllegalArgumentException {
        setRow(row);
        setColumn(column);
    }

    /**
     * Compares this {@code Position} to the specified object.
     *
     * The result is {@code true} if and only if the argument is not {@code null} and is a {@code Position} object that
     * represents the same row and column as this {@code Position}.
     *
     * @param o the object to compare this {@code Position} against
     * @return {@code true} if the {@code Positions} are equal; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (column != position.column) return false;
        if (row != position.row) return false;

        return true;
    }

    /**
     * Returns a hash code for this {@code Position}.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + row;
        result = 37 * result + column;
        return result;
    }

    /**
     * Returns the string representation of this {@code Position}.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "Position{" +
               "row=" + row +
               ", column=" + column +
               '}';
    }

    /**
     * Compares this object with the specified object for order.  Returns a negative integer, zero, or a positive
     * integer as this object is less than, equal to, or greater than the specified object.
     *
     * The row is compared first, so it weights more than the column.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
     *         the specified object.
     */
    public int compareTo(Position o) {
        if (this == o) return 0;
        if (row > o.row) return 1;
        else if (row < o.row) return -1;
        if (column > o.column) return 1;
        else if (column < o.column) return -1;

        return 0;
    }

    /**
     * Returns the {@code column}
     *
     * @return column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Sets the column instance variable
     *
     * @param column the column
     * @throws IllegalArgumentException if {@code column < 0}
     * @modifies column if {@code column >= 0}
     * @post sets column to the new value; if {@code column >= 0}
     */
    public void setColumn(int column) throws IllegalArgumentException {
        if (column < 0)
            throw new IllegalArgumentException("Column is below 0; column:" + column);
        this.column = column;
    }

    /**
     * Returns the {@code row}
     *
     * @return row
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the row instance variable
     *
     * @param row the row
     * @throws IllegalArgumentException if {@code row < 0}
     * @modifies row if {@code row >= 0}
     * @post sets row to the new value; if {@code row >= 0}
     */
    public void setRow(int row) throws IllegalArgumentException {
        if (row < 0)
            throw new IllegalArgumentException("Row is below 0; row: " + row);
        this.row = row;
    }
}
