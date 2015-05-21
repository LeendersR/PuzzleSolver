package textio;

import model.*;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Constructs a puzzle from a file or a scanner.
 *
 * @author Robert Leenders
 */
public class PuzzleReader {

    /**
     * Reads a puzzle from {@code in}
     *
     * @param file scanner from which input is read
     * @return constructed puzzle from the info file scanner {@code in}
     * @throws FileNotFoundException if the file, for the box or the bag, file scanner {@code in} doesn't exists
     */
    public static Puzzle read(File file) throws FileNotFoundException {
        Scanner in = new Scanner(file);

        String nameOfPuzzle = in.next();
        String nameTextFileBox = in.next();
        String nameTextFilePieces = in.next();

        File boxFile = new File(file.getParent(), nameTextFileBox);
        File piecesFile = new File(file.getParent(), nameTextFilePieces);

        Box boxOfPuzzle = readBox(boxFile);
        BagOfPieces bagOfPiecesOfPuzzle = readPieces(piecesFile);

        return new Puzzle(nameOfPuzzle, boxOfPuzzle, bagOfPiecesOfPuzzle);
    }

    /**
     * Reads a puzzle from a file
     *
     * @param pathTextFilePuzzle the path to the file
     * @return constructed puzzel from the info inside the file
     * @throws FileNotFoundException if the file, for the puzzle, the box, or the bag, is not found
     */
    public static Puzzle read(String pathTextFilePuzzle) throws FileNotFoundException {
        File file = new File(pathTextFilePuzzle);
        return read(file);
    }

    /**
     * Reads a bag with pieces from a file
     *
     * @param pathTextFilePieces the path to the file
     * @return constructed bag with pieces from the info inside the file
     * @throws FileNotFoundException if the file for the bag with pieces is not found
     */
    private static BagOfPieces readPieces(File pathTextFilePieces) throws FileNotFoundException {
        Scanner in = new Scanner(pathTextFilePieces);
        return readPieces(in);
    }

    /**
     * Reads a bag with pieces from scanner {@code in}
     *
     * @param in the scanner from which the bag is constructed
     * @return constructed bag with pieces from the info inside the scanner
     */
    private static BagOfPieces readPieces(Scanner in) {
        BagOfPieces bagOfPieces = new BagOfPieces();
        String nameOfBag = in.next();
        in.nextLine(); // Skip to next line
        String line = in.nextLine();
        while (line != null && line.charAt(0) == '=') {

            String nameOfPiece = in.next();
            Color colorOfPiece = ColorConverter.fromName(in.next());
            int multiplicityOfPiece = in.nextInt();
            in.nextLine(); // Skip to next line
            Piece piece = new Piece(nameOfPiece, colorOfPiece, multiplicityOfPiece);
            line = in.nextLine();
            while (line != null && line.charAt(0) == '-') {
                Orientation orientation = new Orientation();
                line = in.nextLine();
                for (int i = 0; line != null && line.charAt(0) != '-' && line.charAt(0) != '='; ++i) {
                    for (int j = 0; j < line.length(); ++j) {
                        if (line.charAt(j) == nameOfPiece.charAt(0)) {
                            orientation.addPosition(i, j);
                        }
                    }
                    if (in.hasNextLine())
                        line = in.nextLine();
                    else
                        line = null;
                }
                piece.addOrientation(orientation);
            }
            bagOfPieces.add(piece);
        }
        return bagOfPieces;
    }

    /**
     * Reads a box from a file
     *
     * @param pathTextFileBox path to the file
     * @return constructed box with the info inside the file
     * @throws FileNotFoundException if the file for the box is not found
     */
    private static Box readBox(File pathTextFileBox) throws FileNotFoundException {
        Scanner in = new Scanner(pathTextFileBox);
        return readBox(in);
    }

    private static Box readBox(Scanner in) {
        String nameOfBox = in.next(); // Box doesn't have a name, only puzzle does

        int rowCount = in.nextInt();
        int columnCount = in.nextInt();
        in.nextLine(); // Skip to the next line
        ArrayList<Position> blockedPositions = new ArrayList<Position>();
        for (int i = 0; i < rowCount; ++i) {
            String line = in.nextLine();
            for (int j = 0; j < columnCount; ++j) {
                boolean blockedPosition = line.charAt(j) == '#';
                if (blockedPosition)
                    blockedPositions.add(new Position(i, j));
            }
        }
        return new Box(rowCount, columnCount, blockedPositions);
    }
}
