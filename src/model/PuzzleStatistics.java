package model;

/**
 * Keeps track of various statistics.
 *
 * @author Robert Leenders
 */
public class PuzzleStatistics {
    /**
     * Number of placements done
     */
    private int numPlacements = 0;
    /**
     * Number of placement done since the last solution
     */
    private int numPlacementsSinceLastSolution = 0;
    /**
     * Number of solutions found so far
     */
    private int numSolutionsFound = 0;
    /**
     * The minimum number of placements done for a solution
     */
    private int minNumPlacementsForSolution = Integer.MAX_VALUE;
    /**
     * The maximum number of placements done for a solution
     */
    private int maxNumPlacementsForSolution = Integer.MIN_VALUE;
    /**
     * Mean of number of placement done for a solution, if {@code count > 0}
     */
    private double mean = 0.0;
    /**
     * Variance oof number of placement done for a solution, if {@code count > 1}
     */
    private double variance = 0.0;
    /**
     * The puzzle for which we hold the statistics
     */
    private Puzzle puzzle;
    /**
     * Updates the statistics
     */
    private updateStatistics boxListener;

    /**
     * Constructs a new statistics helper class for this puzzle
     *
     * @param puzzle the puzzle for which we want to hold the statistics
     */
    public PuzzleStatistics(Puzzle puzzle) {
        setPuzzle(puzzle);
    }

    /**
     * Sets the puzzle, adds the necessary listeners and resets all the statistics.
     *
     * @param puzzle the puzzle for which we want to hold the statistics
     */
    public void setPuzzle(Puzzle puzzle) {
        if (this.puzzle != null) {
            this.puzzle.removeBoxListener(boxListener);
        }
        this.puzzle = puzzle;
        if (puzzle != null) {
            boxListener = new updateStatistics();
            puzzle.addBoxListener(boxListener);
        }

        resetStatistics();
    }

    /**
     * Resets all the statistics
     */
    public void resetStatistics() {
        numPlacements = 0;
        numPlacementsSinceLastSolution = 0;
        numSolutionsFound = 0;
        minNumPlacementsForSolution = Integer.MAX_VALUE;
        maxNumPlacementsForSolution = Integer.MIN_VALUE;
        mean = 0.0;
        variance = 0.0;
    }

    /**
     * Returns the maximum number of placements for a solution
     *
     * @return maximum number of placements for a solution
     * @throws IllegalRequestException if number of solutions found is zero
     */
    public int getMaxNumPlacementsForSolution() throws IllegalRequestException {
        if (numSolutionsFound == 0)
            throw new IllegalRequestException("No solutions found yet");
        return maxNumPlacementsForSolution;
    }

    /**
     * Returns the minimum number of placements for a solution
     *
     * @return minimum number of placements for a solution
     * @throws IllegalRequestException if number of solutions found is zero
     */
    public int getMinNumPlacementsForSolution() throws IllegalRequestException {
        if (numSolutionsFound == 0)
            throw new IllegalRequestException("No solutions found yet");
        return minNumPlacementsForSolution;
    }

    public int getNumPlacements() {
        return numPlacements;
    }

    public int getNumSolutionsFound() {
        return numSolutionsFound;
    }

    /**
     * Returns the standard deviation for the number of placements for a solution
     *
     * @return standard deviation for the number of placements for a solution
     * @throws IllegalRequestException if number of solutions found lower than two
     */
    public double getStdDevPlacementsForSolution() throws IllegalRequestException {
        if (numSolutionsFound <= 1)
            throw new IllegalRequestException("The number of values added to the statistics is lower than 2");
        return Math.sqrt(variance);
    }

    /**
     * Class which implements box listener and updates all statistics from within
     *
     * @pre puzzle != null
     */
    private class updateStatistics implements BoxListener {
        /**
         * Method which is called when a placement is added
         *
         * @param placement the newly placed placement
         */
        public void placementAdded(Placement placement) {
            ++numPlacements;
            ++numPlacementsSinceLastSolution;
            if (puzzle.isUniquelySolved()) {
                ++numSolutionsFound;
                minNumPlacementsForSolution = Math.min(numPlacementsSinceLastSolution, minNumPlacementsForSolution);
                maxNumPlacementsForSolution = Math.max(numPlacementsSinceLastSolution, maxNumPlacementsForSolution);

                double delta = numPlacementsSinceLastSolution - mean;
                updateMean(delta); // You must update the mean before updating the variance
                updateVariance(delta);

                numPlacementsSinceLastSolution = 0;
            }
        }

        /**
         * Updates the mean
         *
         * @param delta the differential between the number of placements since the last solution and the mean
         */
        private void updateMean(double delta) {
            mean += delta / numSolutionsFound;
        }

        /**
         * Updates the variance
         *
         * @param delta the differential between the number of placements since the last solution and the mean
         */
        private void updateVariance(double delta) {
            if (numSolutionsFound > 1) {
                variance += (delta * (numPlacementsSinceLastSolution - mean) - variance) / (numSolutionsFound - 1);
            }
        }

        /**
         * Empty method since removing a placement doesn't alter the statistics.
         *
         * @param placement
         */
        public void placementRemoved(Placement placement) {
        }
    }
}
