package solvers;

import model.*;

import java.util.Iterator;
import java.util.List;

/**
 * BacktrackSolver to solve the puzzles.
 * I don't really use it, I just left it here for 'viewing purposes'.
 *
 * The algorithm this puzzle solver uses can be found at DLX.java
 *
 * @author Robert Leenders
 * @see DLX
 */
public class BacktrackSolver extends Solver {

    /**
     * Constructs a new backtrack solver for given puzzle.
     *
     * @param puzzle the puzzle to be solved
     */
    public BacktrackSolver(Puzzle puzzle) {
        super(puzzle);
    }

    /**
     * Finds all solutions of the puzzle, and reports them one by one to the listener.
     */
    @Override
    public void findAll() {
        if (puzzle.isSolved()) {
            ++nSolutionsFound;
            puzzleSolved();
        }

        int rowCount = 0;
        int columnCount;

        for (List<Cell> row : puzzle) {
            columnCount = 0;
            for (Cell cell : row) {
                if (cell.getState() != CellState.FREE) {
                    ++columnCount;
                    continue;
                }

                for (Iterator<Piece> bagOfPiecesIterator = puzzle.bagOfPiecesIterator(); bagOfPiecesIterator.hasNext(); ) {
                    final Piece piece = bagOfPiecesIterator.next();
                    if (puzzle.getRemainingPlacementsOfPiece(piece) <= 0)
                        continue;
                    for (Iterator<Orientation> orientationIterator = piece.orientationIterator(); orientationIterator.hasNext(); ) {
                        final Orientation orientation = orientationIterator.next();
                        if (columnCount - orientation.getFirstOccupiedColumn() < 0)
                            continue;
                        final Position anchorPosition = new Position(rowCount, columnCount - orientation.getFirstOccupiedColumn());
                        final Placement placement = new Placement(anchorPosition, orientation, piece);
                        if (puzzle.isPlacementPossible(placement)) {
                            puzzle.addPlacement(placement);
                            findAll();
                            puzzle.removePlacement(placement);
                        }
                    }
                }
                return;
            }
            ++rowCount;
        }
    }
}
