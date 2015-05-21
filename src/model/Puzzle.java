package model;

import java.util.*;

/**
 * Puzzle class, the upper class of the model.
 *
 * @author Robert Leenders
 */
public class Puzzle implements Iterable<List<Cell>> {
    /**
     * The undo stack
     */
    Stack<Action> undoStack = new Stack<Action>();
    /**
     * The redo stack
     */
    Stack<Action> redoStack = new Stack<Action>();
    /**
     * The name of this puzzle
     */
    private String name;
    /**
     * The box where placement can be made for this puzzle
     */
    private Box box;
    /**
     * The bag with pieces, pieces from this bag can be placed inside the box
     */
    private BagOfPieces bagOfPieces;
    /**
     * This map counts the amount of times a piece is placed inside the box (\forall piece; n =
     * placedPiecesCount.get(piece); n >= 0 && n <= piece.multiplicity)
     */
    private Map<Piece, Integer> placedPiecesCount;
    /**
     * List of BoxListeners
     */
    private List<BoxListener> listeners;
    /**
     * Lists of solutions so far, so we can keep track of unique solutions
     */
    private List<String> solutionsSoFar;


    /**
     * Constructs a new puzzle from a box, a bag with pieces and name.
     *
     * @param name        the name of this puzzle
     * @param box         the box of this puzzle
     * @param bagOfPieces the bag with pieces of this puzzle
     */
    public Puzzle(String name, Box box, BagOfPieces bagOfPieces) {
        this.name = name;
        this.box = box;
        this.bagOfPieces = bagOfPieces;
        placedPiecesCount = new HashMap<Piece, Integer>();
        listeners = new ArrayList<BoxListener>();
        solutionsSoFar = new ArrayList<String>();
    }

    /**
     * Compares this {@code Puzzle} to the specified object.
     *
     * The result is {@code true} if and only if the argument is not {@code null} and is a {@code Puzzle} object that
     * represents the same bag, box and name as this {@code Puzzle}.
     *
     * @param o the object to compare this {@code Puzzle} against
     * @return {@code true} if the {@code Puzzle} are equal; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Puzzle puzzle = (Puzzle) o;

        if (bagOfPieces != null ? !bagOfPieces.equals(puzzle.bagOfPieces) : puzzle.bagOfPieces != null) return false;
        if (box != null ? !box.equals(puzzle.box) : puzzle.box != null) return false;
        if (name != null ? !name.equals(puzzle.name) : puzzle.name != null) return false;

        return true;
    }

    /**
     * Returns a hash code for this {@code Puzzle}.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (box != null ? box.hashCode() : 0);
        result = 31 * result + (bagOfPieces != null ? bagOfPieces.hashCode() : 0);
        return result;
    }

    /**
     * Returns a string representation of this puzzle.
     *
     * @return string representation of this puzzle.
     */
    @Override
    public String toString() {
        return "Puzzle{" +
               "name='" + name + '\'' +
               "\n, box=\n" + box +
               "\n, bagOfPieces=" + bagOfPieces +
               ", placedPiecesCount=" + placedPiecesCount +
               '}';
    }

    /**
     * Adds listener for generator events.
     *
     * @param listener the listener to be added
     */
    public void addBoxListener(BoxListener listener) {
        listeners.add(listener);
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
     * @throws MaximumPlacementsReached  if the maximum number of placement have been reached for the piece in the
     *                                   placement
     * @post A placement has been made inside the box and the number of placements of the piece on position p is
     * increased with 1.
     * @see Box#addPlacement(Placement)
     */
    public void addPlacement(Placement placement) throws IllegalOperationException, MaximumPlacementsReached {
        addPlacement(placement, true);
        if (isUniquelySolved()) {
            solutionsSoFar.add(box.toString());
        }
    }

    /**
     * Determines whether the current box is a solution and if so if it unique.
     *
     * @return {@code true} when this solution is unique; false otherwise
     */
    public boolean isUniquelySolved() {
        return isSolved() && !solutionsSoFar.contains(box.toString());
    }

    /**
     * Returns whether a puzzle is solved, a puzzle is solved if no cell is free. A cell is free if it has no placement
     * and it is not blocked.
     *
     * @return {@code true} if (\forall cell; cell in box; cell.state != free); false otherwise
     */
    public boolean isSolved() {
        for (List<Cell> row : box)
            for (Cell c : row)
                if (c.getState() == CellState.FREE)
                    return false;
        return true;
    }

    /**
     * Sets the cells according the new placement
     *
     * The placement must fit inside the box and also not overlap with any occupied cells. If no exceptions are thrown
     * the function will set specified the cells to contain the given placement. Which cells will be modified is
     * calculated by the anchor position and by the orientation of the placement.
     *
     * @param placement   the placement
     * @param isNewAction is the action made by the user? (Hence is it an new action)
     * @throws IllegalOperationException if the placement overlaps with the border of the box or if the cells overlap
     * @throws MaximumPlacementsReached  if the maximum number of placement have been reached for the piece in the
     *                                   placement
     * @post A placement has been made inside the box and the number of placements of the piece on position p is
     * increased with 1.
     * @see Box#addPlacement(Placement)
     */
    private void addPlacement(Placement placement, boolean isNewAction) throws IllegalOperationException, MaximumPlacementsReached {
        // Amount of pieces already placed in the box of type piece from placement.
        Integer numPlaced = placedPiecesCount.get(placement.getPiece());
        if (numPlaced == null)
            numPlaced = 0;

        if (placement.getPiece().getMultiplicity() == numPlaced)
            throw new MaximumPlacementsReached("The maximum amount of pieces placed of this type is reached.", placement.getPiece());

        box.addPlacement(placement);
        placedPiecesCount.put(placement.getPiece(), numPlaced + 1);
        updateUndoRedo(new Action(placement, PlacementAction.ADD), isNewAction);
        notifyListenersPlacementAdded(placement);
    }

    /**
     * Updates the undo/redo stack according to the new action.
     *
     * @param action      the new action
     * @param isNewAction {@code true} if the action is new; false otherwise
     */
    private void updateUndoRedo(Action action, boolean isNewAction) {
        if (isNewAction) {
            redoStack.clear();
            undoStack.add(action);
        } else {
            redoStack.add(action);
        }
    }

    /**
     * Notifies all registered listeners.
     *
     * @param placement the placement to be given to the listeners
     */
    void notifyListenersPlacementAdded(Placement placement) {
        for (BoxListener listener : listeners) {
            listener.placementAdded(placement);
        }
    }

    /**
     * Returns the iterator for the bag of pieces.
     *
     * @return iterator to iterate over the bag
     */
    public Iterator<Piece> bagOfPiecesIterator() {
        return bagOfPieces.iterator();
    }

    /**
     * Returns the iterator for the bag of pieces.
     *
     * @return iterator to iterate over the bag
     */
    public ListIterator<Piece> bagOfPiecesListIterator() {
        return bagOfPieces.listIterator();
    }

    /**
     * Returns the string representation of the box.
     *
     * @return string representation of the box
     */
    public String boxToString() {
        return box.toString();
    }

    /**
     * Returns the bag with pieces.
     *
     * @return bag with pieces
     */
    public BagOfPieces getBagOfPieces() {
        return bagOfPieces;
    }

    /**
     * Returns the box.
     *
     * @return the box
     */
    public Box getBox() {
        return box;
    }

    /**
     * Gets the name of this puzzle
     *
     * @return the name of this puzzle
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this puzzle
     *
     * @param name the new name of this puzzle
     * @post name is updated with it's new name
     * @modifies name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Checks if the place is free for this placement. (Not if this placement is possible since it's doesn't take in
     * account the multiplicity of the pieces.
     *
     * @param placement the placement to consider
     * @return whether there is place for this placement
     * @see Puzzle#isPlacementPossible(Placement)
     */
    public boolean isPlaceFree(Placement placement) {
        return box.isPlacementPossible(placement);
    }

    /**
     * Checks if the placement is possible.
     *
     * @param placement the placement to consider
     * @return whether you can still place this piece and if there is room for it
     */
    public boolean isPlacementPossible(Placement placement) {
        return (getRemainingPlacementsOfPiece(placement.getPiece()) > 0) && box.isPlacementPossible(placement);
    }

    /**
     * Calculates how many placement can still be done for a certain piece.
     *
     * @param p the piece
     * @return remaining number placements for piece p
     */
    public int getRemainingPlacementsOfPiece(Piece p) {
        int max = p.getMultiplicity();
        Integer numPlaced = placedPiecesCount.get(p);
        if (numPlaced == null)
            numPlaced = 0;
        return max - numPlaced;
    }

    /**
     * Returns the iterator for the box
     *
     * @return iterator to iterate over the box
     */
    public Iterator<List<Cell>> iterator() {
        return box.iterator();
    }

    /**
     * Redoes the last action.
     */
    public void redo() {
        Action action = redoStack.pop();
        action.doAction();
        undoStack.add(action);
    }

    /**
     * Returns whether redo is possible. Redo is possible iff the redo stack is not empty.
     *
     * @return {@code true} if redo is possible; false otherwise
     */
    public boolean redoPossible() {
        return !redoStack.isEmpty();
    }

    /**
     * Removes listener for generator events.
     *
     * @param listener the listener to be removed
     */
    public void removeBoxListener(BoxListener listener) {
        listeners.remove(listener);
    }

    /**
     * Removes the given placement.
     *
     * @param placement the placement
     * @throws IllegalOperationException if row or column is negative or if there is no placement on p
     * @post every cell with the same placement as cells.get(i, j) are removed. (\forall cell; cell.placement ==
     * cells.get(i, j).placement; cell.placement = null) and the number of placements of the piece is decreased with 1.
     * @modifies box, placedPiecesCount
     */
    public void removePlacement(Placement placement) {
        removePlacement(placement, true);
    }

    /**
     * Removes the given placement. If the action is a new one, the undo/redo history is updated.
     *
     * @param placement   the placement
     * @param isNewAction is the action made by the user? (Hence is it an new action)
     * @throws IllegalOperationException if row or column is negative or if there is no placement on p
     * @post every cell with the same placement as cells.get(i, j) are removed. (\forall cell; cell.placement ==
     * cells.get(i, j).placement; cell.placement = null) and the number of placements of the piece is decreased with 1.
     * @modifies box, placedPiecesCount
     */
    private void removePlacement(Placement placement, boolean isNewAction) {
        Placement removedPlacement = box.removePlacement(placement);
        Piece removedPiece = removedPlacement.getPiece();
        placedPiecesCount.put(removedPiece, placedPiecesCount.get(removedPiece) - 1);
        updateUndoRedo(new Action(placement, PlacementAction.REMOVE), isNewAction);
        notifyListenersPlacementRemoved(removedPlacement);
    }

    /**
     * Notifies all registered listeners.
     *
     * @param placement the placement which have to be given to the listeners
     */
    void notifyListenersPlacementRemoved(Placement placement) {
        for (BoxListener listener : listeners) {
            listener.placementRemoved(placement);
        }
    }

    /**
     * Undoes the last action.
     */
    public void undo() {
        Action action = undoStack.pop();
        action.undoAction();
        redoStack.add(action);
    }

    /**
     * Returns whether undo is possible. Undo is possible iff the undo stack is not empty.
     *
     * @return {@code true} if undo is possible; false otherwise
     */
    public boolean undoPossible() {
        return !undoStack.isEmpty();
    }

    /**
     * Enum defining which action has been taking, either removing a piece, or adding one.
     */
    private enum PlacementAction {
        ADD, REMOVE
    }

    /**
     * An inner class which saves the state of action. It simulates the CommandPattern.
     */
    private class Action {
        /**
         * Which action has been done
         */
        private PlacementAction action;
        /**
         * The placements on which it operated
         */
        private Placement placement;

        /**
         * Constructs a new Action.
         *
         * @param placement the placement
         * @param action    the action which have been done
         */
        Action(Placement placement, PlacementAction action) {
            this.placement = placement;
            this.action = action;
        }

        /**
         * Does the action.
         */
        void doAction() {
            if (action == PlacementAction.ADD)
                addPlacement(placement, false);
            else if (action == PlacementAction.REMOVE)
                removePlacement(placement, false);
        }

        /**
         * Undoes the action.
         */
        void undoAction() {
            if (action == PlacementAction.ADD)
                removePlacement(placement, false);
            else if (action == PlacementAction.REMOVE)
                addPlacement(placement, false);
        }
    }
}
