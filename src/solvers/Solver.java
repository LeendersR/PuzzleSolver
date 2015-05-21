package solvers;

import model.Puzzle;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for (automatic) puzzle solvers.
 *
 * A single listener can be registered via {@code setSolverListener}.
 *
 * @author Robert Leenders
 */
public class Solver {
    protected Puzzle puzzle; // the puzzle being solved
    protected int nSolutionsFound = 0; // number of solutions found
    protected List<SolverListener> listeners;

    /**
     * Constructs a new solver for given puzzle.
     *
     * @param puzzle the puzzle to be solved
     */
    public Solver(Puzzle puzzle) {
        listeners = new ArrayList<SolverListener>();
        this.puzzle = puzzle;
    }

    /**
     * Sets listener for solver events.
     *
     * @param listener the listener to be notified
     */
    public void addListener(SolverListener listener) {
        listeners.add(listener);
    }

    /**
     * Finds all solutions of the puzzle, and reports them one by one to the listener.
     */
    public void findAll() {
    }

    /**
     * Gets the value of nSolutionsFound
     *
     * @return the value of nSolutionsFound
     */
    public int getNSolutionsFound() {
        return nSolutionsFound;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    /**
     * Sets puzzle to solve.
     *
     * @param puzzle the puzzle to solve
     */
    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    protected void puzzleSolved() {
        for (SolverListener listener : listeners) {
            listener.solutionFound(nSolutionsFound, puzzle);
        }
    }

    public void removeListener(SolverListener listener) {
        listeners.remove(listener);
    }
}
