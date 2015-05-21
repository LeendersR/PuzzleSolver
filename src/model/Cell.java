package model;

/**
 * The cell class which represent a cell on the puzzle. This class is used by Box to make a grid of cells which
 * represent the puzzle.
 *
 * It should be more cleaner if there were two separate cells from one abstract cell, namely a normal cell and a blocked
 * cell. But a enum which a blocked option is less work.
 *
 * @author Robert Leenders
 * @see Box
 * @see CellState
 */
public class Cell {

    /**
     * The state of this cell it is free, occupied or blocked
     */
    private CellState state;
    /**
     * The placement which occupies this cell, cannot be set if the state is free or blocked
     */
    private Placement placement;

    /**
     * Constructs a new cell object
     *
     * @param cellBlocked if the cell is blocked
     * @modifies state
     * @post sets the state accordingly
     */
    public Cell(boolean cellBlocked) {
        state = cellBlocked ? CellState.BLOCKED : CellState.FREE;
    }

    /**
     * Retrieves the state of the cell
     *
     * @return cells state
     */
    public CellState getState() {
        return state;
    }

    /**
     * Retrieves the placement of the cell
     *
     * @return cells placement
     */
    public Placement getPlacement() {
        return placement;
    }

    /**
     * Sets a new placement for this cell.
     *
     * A placement can only be set if the cell is {@code free}.
     *
     * @param placement the new placement for the cell
     * @throws IllegalOperationException if the cell is blocked
     * @modifies placement and state
     * @post if the cell is not {@code occupied} or {@code blocked}, the state will be set to {@code occupied} and the
     * placement will be set
     */
    public void setPlacement(Placement placement) throws IllegalOperationException {
        if (!isPlacementPossible())
            throw new IllegalOperationException("Cannot place something on a non-free cell.");
        this.placement = placement;
        state = CellState.OCCUPIED;
    }

    /**
     * Removes the placement on this cell
     *
     * @return {@code false} if the cell is blocked; true otherwise
     * @post if the cell is not {@code blocked} the state will be set to {@code free} and the placement will be reset to
     * {@code null}
     */
    public boolean removePlacement() {
        if (state == CellState.BLOCKED)
            return false;
        placement = null;
        state = CellState.FREE;
        return true;
    }

    /**
     * Checks if placement is possible on this cell.
     *
     * Placement is not possible if the cell is already {@code occupied} or if the cell is {@code blocked}
     *
     * @return {@code true} if state is free; false otherwise
     */
    public boolean isPlacementPossible() {
        return state == CellState.FREE;
    }


    /**
     * String representation of the instance variables in this cell
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "Cell{" +
               "state=" + state +
               ", placement=" + placement +
               '}';
    }

    /**
     * Compares this {@code Cell} to the specified object.
     *
     * The result is {@code true} if and only if the argument is not {@code null} and is a {@code Cell} object that
     * represents the same placement and state as this {@code Cell}.
     *
     * @param o the object to compare this {@code Cell} against
     * @return {@code true} if the {@code Cells} are equal; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (placement != null ? !placement.equals(cell.placement) : cell.placement != null) return false;
        if (state != cell.state) return false;

        return true;
    }

    /**
     * Returns a hash code for this {@code Cell}.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        int result = state != null ? state.hashCode() : 0;
        result = 31 * result + (placement != null ? placement.hashCode() : 0);
        return result;
    }
}
