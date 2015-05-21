package textio;

import junit.framework.TestCase;
import model.*;

import java.awt.*;
import java.io.FileNotFoundException;

/**
 * Test class for the Puzzle Reader
 *
 * @author Robert Leenders
 */
public class PuzzleReaderTest extends TestCase {
    /**
     * Construct a dummy puzzle which is the same as the puzzle inside "SimplePuzzle.txt".
     * Then it tests if the puzzle reader reads the puzzle correctly.
     */
    public void testPuzzleReader() {
        final String pathTextFilePuzzle = "tests\\files\\SimplePuzzle.txt";

        Box box = new Box(2, 3);
        BagOfPieces bagOfPieces = new BagOfPieces();
        Piece piece = new Piece("A", Color.RED, 1);
        Orientation o = new Orientation();
        o.addPosition(0, 0);
        piece.addOrientation(o);
        bagOfPieces.add(piece);

        piece = new Piece("B", Color.CYAN, 1);
        o = new Orientation();
        o.addPosition(0, 0);
        o.addPosition(0, 1);
        piece.addOrientation(o);
        o = new Orientation();
        o.addPosition(0, 0);
        o.addPosition(1, 0);
        piece.addOrientation(o);
        bagOfPieces.add(piece);

        piece = new Piece("C", Color.YELLOW, 1);
        o = new Orientation();
        o.addPosition(0, 1);
        o.addPosition(1, 0);
        o.addPosition(1, 1);
        piece.addOrientation(o);
        o = new Orientation();
        o.addPosition(0, 0);
        o.addPosition(1, 0);
        o.addPosition(1, 1);
        piece.addOrientation(o);
        o = new Orientation();
        o.addPosition(0, 1);
        o.addPosition(1, 0);
        o.addPosition(0, 0);
        piece.addOrientation(o);
        o = new Orientation();
        o.addPosition(0, 1);
        o.addPosition(0, 0);
        o.addPosition(1, 1);
        piece.addOrientation(o);
        bagOfPieces.add(piece);


        Puzzle expected = new Puzzle("Simple_Puzzle", box, bagOfPieces);

        Puzzle result = null;
        try {
            result = PuzzleReader.read(pathTextFilePuzzle);
        } catch (FileNotFoundException e) {
            fail("File not found, path: " + e.getMessage());
        }

        assertEquals("Puzzle not read correctly", expected, result);

    }

}
