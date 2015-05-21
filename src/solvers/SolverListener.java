package solvers;

import model.Puzzle;

import java.util.EventListener;

/**
 * Interface with events that can be triggered by a {@code Solver}.
 *
 * @author Robert Leenders
 */
public interface SolverListener extends EventListener {

    /**
     * Reports a solution, given by current state of {@code puzzle}.
     *
     * @param solutionNumber sequence number of solution
     * @param puzzle         the solved puzzle
     * @pre {@code puzzle} is solved
     */
    void solutionFound(int solutionNumber, Puzzle puzzle);

}
