package solvers;

import junit.framework.TestCase;
import model.Puzzle;
import textio.PuzzleReader;

import java.io.FileNotFoundException;

/**
 * Tests the implementation of BacktrackSolver
 *
 * @author Robert Leenders
 */
public class BacktrackSolverTest extends TestCase {

    public void testSimplePuzzle() {
        Puzzle result = null;
        try {
            result = PuzzleReader.read("tests\\files\\SimplePuzzle.txt");
        } catch (FileNotFoundException e) {
            fail("File not found, path: " + e.getMessage());
        }
        BacktrackSolver bts = new BacktrackSolver(result);
        bts.addListener(new SolverListener() {
            public void solutionFound(int solutionNumber, Puzzle puzzle) {
                solutionFoundSimplePuzzle(solutionNumber, puzzle);
            }
        });
        bts.findAll();
    }

    private String[] simplePuzzleTestData = {"BAC\r\nBCC", "BCC\r\nBCA", "BCC\r\nBAC", "BCA\r\nBCC", "BBC\r\nACC", "ACB\r\nCCB", "ACC\r\nBBC", "CCB\r\nCAB", "CCA\r\nCBB", "CCB\r\nACB", "CBB\r\nCCA", "CAB\r\nCCB"};
    private void solutionFoundSimplePuzzle(int solutionNumber, Puzzle puzzle) {
        assertEquals("Puzzle didn't report the right one (or in the right order)", simplePuzzleTestData[solutionNumber - 1], puzzle.boxToString());
    }
}
