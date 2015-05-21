package view;

import model.IllegalRequestException;
import model.Puzzle;
import model.PuzzleStatistics;

import java.awt.*;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Displays the statistics.
 *
 * @author Robert Leenders
 */
public class PuzzleStatisticsView {
    /**
     * The puzzle statistics
     */
    private PuzzleStatistics puzzleStatistics;
    /**
     * The topleft coordinates for this view
     */
    private Point topLeft;
    /**
     * The height of this view
     */
    private int height = 100;
    /**
     * The width of this view
     */
    private int width = 430;

    /**
     * Constructs a new puzzlestatisticsview for the given puzzle at the given location.
     *
     * @param puzzle  the puzzle for which we display the statistics
     * @param topLeft the topleft coordinate for this view
     */
    public PuzzleStatisticsView(Puzzle puzzle, Point topLeft) {
        setPuzzle(puzzle);
        this.topLeft = topLeft;
    }

    /**
     * Sets the puzzle for which we view the statistics
     *
     * @param puzzle the puzzle
     */
    public void setPuzzle(Puzzle puzzle) {
        puzzleStatistics = new PuzzleStatistics(puzzle);
    }

    /**
     * Returns the height of this view
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the width of this view
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Paints this view on the given graphics.
     *
     * @param g the graphics context
     */
    public void paint(final Graphics g) {
        g.setColor(Color.BLACK);

        ResourceBundle bundle = ResourceBundle.getBundle("gui.properties_en");
        int heightSpacing = 15;
        int numDrawing = 0;
        g.drawString(MessageFormat.format(bundle.getString("PuzzleStatisticsView.numPlacements.text"), puzzleStatistics.getNumPlacements()), topLeft.x, topLeft.y);
        g.drawString(MessageFormat.format(bundle.getString("PuzzleStatisticsView.numSolutionsFound.text"), puzzleStatistics.getNumSolutionsFound()), topLeft.x, topLeft.y + heightSpacing * ++numDrawing);

        try {
            g.drawString(MessageFormat.format(bundle.getString("PuzzleStatisticsView.numPlacementsPerFoundSolutionMin.text"), puzzleStatistics.getMinNumPlacementsForSolution()), topLeft.x, topLeft.y + heightSpacing * ++numDrawing);
        } catch (IllegalRequestException ignored) {
        }

        try {
            g.drawString(MessageFormat.format(bundle.getString("PuzzleStatisticsView.numPlacementsPerFoundSolutionMax.text"), puzzleStatistics.getMaxNumPlacementsForSolution()), topLeft.x, topLeft.y + heightSpacing * ++numDrawing);
        } catch (IllegalRequestException ignored) {
        }

        try {
            g.drawString(MessageFormat.format(bundle.getString("PuzzleStatisticsView.numPlacementsPerFoundSolutionStdDev.text"), puzzleStatistics.getStdDevPlacementsForSolution()), topLeft.x, topLeft.y + heightSpacing * ++numDrawing);
        } catch (IllegalRequestException ignored) {
        }
    }
}
