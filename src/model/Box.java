package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The box is the actual puzzle, it contains cells which represent the puzzle.
 *
 * @author Robert Leenders
 * @see Cell
 * @see Placement
 */
public class Box implements Iterable<List<Cell>> {
    /** The number of rows and the number of columns form the dimension of the box */
    /**
     * The number of rows
     */
    private int rowCount;
    /**
     * The number of column
     */
    private int columnCount;
    /**
     * The cells which represent the puzzle with the dimensions of rowCount and columnCount
     */
    private List<List<Cell>> cells;
    /**
     * A list with positions where the blocked cells are.
     */
    private ArrayList<Position> blockedPositions;


    /**
     * Constructs a new box with the given size
     *
     * @param rowCount    the number of rows
     * @param columnCount the number of column
     * @throws IllegalArgumentException if {@code rowCount <= 0 || columnCount <= 0}
     * @modifies rowCount, columnCount and cells
     * @post Creates a new box with the given dimension if {@code rowCount > 0 || columnCount > 0}
     * @see Box#createBox(int, int, java.util.ArrayList)
     */
    public Box(int rowCount, int columnCount) throws IllegalArgumentException {
        if (rowCount <= 0)
            throw new IllegalArgumentException("rowCount is below or equal to 0; rowCount:" + rowCount);
        if (columnCount <= 0)
            throw new IllegalArgumentException("columnCount is below or equal to 0; columnCount:" + columnCount);

        createBox(rowCount, columnCount, new ArrayList<Position>());
    }

    /**
     * Creates the new box with the given size
     *
     * @param rowCount         the number of rows
     * @param columnCount      the number of column
     * @param blockedPositions collection with positions which are blocked
     * @pre {@code rowCount > 0 && columnCount > 0 && blockedPosition != null}
     * @modifies rowCount, columnCount and cells
     * @post Creates a new box with the given dimension if {@code rowCount > 0 || columnCount > 0}
     */
    private void createBox(int rowCount, int columnCount, ArrayList<Position> blockedPositions) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.blockedPositions = blockedPositions;

        cells = new ArrayList<List<Cell>>();
        //Cell[rowCount][columnCount];
        for (int i = 0; i < rowCount; ++i) {
            List<Cell> column = new ArrayList<Cell>();
            for (int j = 0; j < columnCount; ++j) {
                boolean cellBlocked = blockedPositions.contains(new Position(i, j));
                column.add(new Cell(cellBlocked));
            }
            cells.add(column);
        }
    }

    /**
     * Constructs a new box with the given size
     *
     * @param rowCount         the number of rows
     * @param columnCount      the number of column
     * @param blockedPositions collection with positions which are blocked
     * @throws IllegalArgumentException if {@code rowCount <= 0 || columnCount <= 0} or when {@code blockedPosition !=
     *                                  null}
     * @modifies rowCount, columnCount and cells
     * @post Creates a new box with the given dimension if {@code rowCount > 0 || columnCount > 0}
     * @see Box#createBox(int, int, java.util.ArrayList)
     */
    public Box(int rowCount, int columnCount, ArrayList<Position> blockedPositions) throws IllegalArgumentException {
        if (rowCount <= 0)
            throw new IllegalArgumentException("rowCount is below or equal to 0; rowCount:" + rowCount);
        if (columnCount <= 0)
            throw new IllegalArgumentException("columnCount is below or equal to 0; columnCount:" + columnCount);
        if (blockedPositions == null)
            throw new IllegalArgumentException("blockedPosition cannot be null (it can be empty)");

        createBox(rowCount, columnCount, blockedPositions);
    }

    /**
     * Compares this {@code box} to the specified object.
     *
     * The result is {@code true} if and only if the argument is not {@code null} and is a {@code box} object that
     * represents the same cells as this {@code box}.
     *
     * @param o the object to compare this {@code box} against
     * @return {@code true} if the boxes are equal; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Box box = (Box) o;

        if (columnCount != box.columnCount) return false;
        if (rowCount != box.rowCount) return false;
        if (cells != null ? !cells.equals(box.cells) : box.cells != null) return false;

        return true;
    }

    /**
     * Returns a hash code for this box.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        int result = rowCount;
        result = 31 * result + columnCount;
        result = 31 * result + (cells != null ? cells.hashCode() : 0);
        return result;
    }

    /**
     * String representation of the cells in the box.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < rowCount; ++i) {
            for (int j = 0; j < columnCount; ++j) {
                builder.append(cells.get(i).get(j).getState() == CellState.FREE ? "." : cells.get(i).get(j).getPlacement().getPiece().getName());
            }
            if (i + 1 < rowCount)
                builder.append(System.getProperty("line.separator"));
        }
        return builder.toString();
    }

    /**
     * Sets the cells according the new placement
     *
     * The placement must fit inside the box and also not overlap with any occupied cells. If no exceptions are thrown
     * the function will set specified the cells to contain the given placement. Which cells will be modified is
     * calculated by the anchor position and by the orientation of the placement.
     *
     * @param placement the placement
     * @throws IllegalOperationException if the placement overlaps with the border of the box or if the cells overlap
     * @modifies cells
     * @post Updates the cells with the new placement
     * @see Placement#anchorPosition
     * @see Placement#orientation
     * @see Cell
     */
    public void addPlacement(Placement placement) throws IllegalOperationException {
        int row = placement.getAnchorPosition().getRow();
        int column = placement.getAnchorPosition().getColumn();
        int maxRow = row + placement.getOrientation().getHeight() - 1;
        int maxColumn = column + placement.getOrientation().getWidth() - 1;

        throwIfPositionNotInBox(row, column);
        throwIfPositionNotInBox(maxRow, maxColumn);

        for (int i = row; i <= maxRow; ++i) {
            for (int j = column; j <= maxColumn; ++j) {
                if (placement.getOrientation().isPositionOccupied(i - row, j - column)) {
                    if (!cells.get(i).get(j).isPlacementPossible())
                        throw new IllegalArgumentException("placement on row: " + i + " and column:" + j + " is not possible; position is not free");
                    cells.get(i).get(j).setPlacement(placement);
                }
            }
        }
    }

    /**
     * Helpers method which checks whether the position is valid (in the box.)
     *
     * @param row    the row
     * @param column the column
     * @throws IllegalArgumentException when the position is invalid
     */
    private void throwIfPositionNotInBox(int row, int column) throws IllegalArgumentException {
        if (row < 0 || row >= rowCount)
            throw new IllegalArgumentException("row cannot be less than 0 or greater than rowCount; row: " + row + "; rowCount: " + rowCount);
        if (column < 0 || column >= columnCount)
            throw new IllegalArgumentException("row cannot be less than 0; row: " + column + "; columnCount: " + column);
    }

    /**
     * Returns the cell on position (row, column)
     *
     * @param row    the row
     * @param column the column
     * @return the cell on that position
     * @throws IllegalArgumentException when the position is invalid
     */
    public Cell get(int row, int column) throws IllegalArgumentException {
        throwIfPositionNotInBox(row, column);
        return cells.get(row).get(column);
    }

    /**
     * Returns a list with the positions which are blocked
     *
     * @return list with blocked positions
     */
    public ArrayList<Position> getBlockedPositions() {
        return blockedPositions;
    }

    /**
     * Returns the amount of columns for this box
     *
     * @return column
     */
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * Returns the amount of rows for this box
     *
     * @return row
     */
    public int getRowCount() {
        return rowCount;
    }

    /**
     * Returns whether the placement is possible
     *
     * @param placement the placement to consider
     * @return whether the placement is possible
     */
    public boolean isPlacementPossible(Placement placement) {
        int row = placement.getAnchorPosition().getRow();
        int column = placement.getAnchorPosition().getColumn();
        int maxRow = row + placement.getOrientation().getHeight() - 1;
        int maxColumn = column + placement.getOrientation().getWidth() - 1;
        if (maxRow >= rowCount || maxColumn >= columnCount)
            return false;

        for (int i = row; i <= maxRow; ++i) {
            for (int j = column; j <= maxColumn; ++j) {
                if (placement.getOrientation().isPositionOccupied(i - row, j - column) && !cells.get(i).get(j).isPlacementPossible()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns an iterator over a set of elements of type {@code List<Cell>}.
     *
     * @return an Iterator.
     */
    public Iterator<List<Cell>> iterator() {
        return cells.iterator();
    }

    /**
     * Resets the cells which have the same placement as the placement of the cell at the given coordinates
     *
     * The placement is retrieved from the cell which lies at the given coordinates. Then we retrieve the anchor
     * position and the orientation so that we can calculate the relevant cells of whom we remove the placement.
     *
     * @param p the position of the placement to be removed
     * @return returns the placement which was removed
     * @throws IllegalArgumentException if row or column is negative or if there is no placement on p
     * @modifies cells
     * @post every cell with the same placement as cells.get(i, j) are removed (\forall cell; cell.placement ==
     * cells.get(i, j).placement; cell.placement = null)
     */
    public Placement removePlacement(Position p) throws IllegalArgumentException {
        int row = p.getRow();
        int column = p.getColumn();
        throwIfPositionNotInBox(row, column);

        Placement placement = cells.get(row).get(column).getPlacement();
        if (placement == null)
            throw new IllegalArgumentException("There is no placement on cell: " + cells.get(row).get(column));

        row = placement.getAnchorPosition().getRow();
        column = placement.getAnchorPosition().getColumn();
        int maxRow = row + placement.getOrientation().getHeight();
        int maxColumn = column + placement.getOrientation().getWidth();
        for (int i = row; i < maxRow; ++i) {
            for (int j = column; j < maxColumn; ++j) {
                if (placement.getOrientation().isPositionOccupied(i - row, j - column)) {
                    cells.get(i).get(j).removePlacement();
                }
            }
        }
        return placement;
    }

    /**
     * Resets the cells which have the same placement as the placement of the cell at the given coordinates
     *
     * The placement is retrieved from the cell which lies at the given coordinates. Then we retrieve the anchor
     * position and the orientation so that we can calculate the relevant cells of whom we remove the placement.
     *
     * @param placement the placement to be removed
     * @return returns the placement which was removed
     * @throws IllegalArgumentException if row or column is negative or if there is no placement on p
     * @modifies cells
     * @post every cell with the same placement as cells.get(i, j) are removed (\forall cell; cell.placement ==
     * cells.get(i, j).placement; cell.placement = null)
     */
    public Placement removePlacement(Placement placement) {
        return removePlacement(new Position(placement.getAnchorPosition().getRow(),
                                           placement.getAnchorPosition().getColumn() + placement.getOrientation().getFirstOccupiedColumn()));
    }
}
