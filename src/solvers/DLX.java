package solvers;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Solver which uses the DLX algorithm
 *
 * <a href="http://arxiv.org/abs/cs/0011047">See knuths paper on 'Algorithm X' for more information</a>
 *
 * @author Robert Leenders
 */
public class DLX extends Solver {
    private Node root;
    private boolean stopSolver = false;


    /**
     * Constructs a new solver for given puzzle.
     *
     * @param puzzle the puzzle to be solved
     */
    public DLX(Puzzle puzzle) {
        super(puzzle);
        buildLinks(buildMatrix()); // Load everything up
    }

    void buildLinks(Matrix matrix) {
        List<Position> columnPositions = matrix.columnPositions;
        List<Boolean[]> rows = matrix.rows;
        List<Placement> placementOfRow = matrix.placementOfRow;
        List<Piece> pieces = matrix.pieces;
        root = new Node();
        root.left = root.right = root;
        Node previous = root;

        List<Node> columns = new ArrayList<Node>();
        for (Position p : columnPositions) {
            Node column = new Node();
            column.left = previous;
            column.right = root;
            previous.right = column;
            root.left = column;
            column.above = column.below = column;
            columns.add(column);
            previous = column;
        }

        for (Piece p : pieces) {
            for (int i = 0; i < p.getMultiplicity(); ++i) {
                Node column = new Node();
                column.left = previous;
                column.right = root;
                previous.right = column;
                root.left = column;
                column.above = column.below = column;
                columns.add(column);
                previous = column;
                column.piece = p;
            }
        }

        int rowIndex = 0;
        for (Boolean[] row : rows) {
            Node first = null;
            Node last = null;
            for (int i = 0; i < row.length; ++i) {
                boolean value = row[i];
                if (value) {
                    Node column = columns.get(i);
                    Node node = new Node();
                    node.column = column;
                    node.above = column.above;
                    node.below = column;
                    if (first == null) {
                        first = node;
                        last = node;
                    }

                    column.above.below = node;
                    column.above = node;
                    node.left = last;
                    node.right = first;
                    last.right = node;
                    first.left = node;
                    column.size += 1;
                    last = node;
                    node.placement = placementOfRow.get(rowIndex);
                }
            }
            ++rowIndex;
        }
    }

    Matrix buildMatrix() {
        List<Boolean[]> rows = new ArrayList<Boolean[]>();
        List<Position> columnPositions = new ArrayList<Position>();
        List<Placement> placementOfRow = new ArrayList<Placement>();
        List<Piece> pieces = new ArrayList<Piece>(puzzle.getBagOfPieces().getPieces());
        HashMap<Position, Integer> columnsIndexes = new HashMap<Position, Integer>();
        HashMap<Piece, Integer> piecesIndexes = new HashMap<Piece, Integer>();

        int index = 0;
        int rowIndex = 0;
        for (List<Cell> row : puzzle) {
            int colIndex = 0;
            for (Cell column : row) {
                if (column.getState() == CellState.FREE) {
                    Position p = new Position(rowIndex, colIndex);
                    columnPositions.add(p);
                    columnsIndexes.put(p, index);
                    ++index;
                }
                ++colIndex;
            }
            ++rowIndex;
        }

        for (Piece p : pieces) {
            for (int i = 0; i < p.getMultiplicity(); ++i) {
                piecesIndexes.put(p, index);
                ++index;
            }
        }

        int maxColumn = index;

        rowIndex = 0;
        for (List<Cell> row : puzzle) {
            int colIndex = 0;
            for (Cell column : row) {
                for (Piece piece : pieces) {
                    for (int multiplicity = 0; multiplicity < piece.getMultiplicity(); ++multiplicity) {
                        for (Iterator<Orientation> orientationIterator = piece.orientationIterator(); orientationIterator.hasNext(); ) {
                            final Orientation orientation = orientationIterator.next();
                            if (colIndex - orientation.getFirstOccupiedColumn() < 0)
                                continue;
                            final Position anchorPosition = new Position(rowIndex, colIndex - orientation.getFirstOccupiedColumn());
                            final Placement placement = new Placement(anchorPosition, orientation, piece);
                            if (puzzle.isPlaceFree(placement)) {
                                Boolean[] newRow = new Boolean[maxColumn];
                                for (int i = 0; i < newRow.length; ++i)
                                    newRow[i] = false;
                                for (Position pos : orientation) {
                                    Position absolutePos = new Position(anchorPosition.getRow() + pos.getRow(), anchorPosition.getColumn() + pos.getColumn());
                                    newRow[columnsIndexes.get(absolutePos)] = true;
                                }
                                newRow[piecesIndexes.get(piece) - multiplicity] = true;
                                rows.add(newRow);
                                placementOfRow.add(placement);
                            }
                        }
                    }
                }
                ++colIndex;
            }
            ++rowIndex;
        }
        return new Matrix(columnPositions, rows, placementOfRow, pieces);
    }

    public void findAll() {
        if (stopSolver)
            return;

        if (puzzle.isSolved()) {
            ++nSolutionsFound;
            puzzleSolved();
            return;
        }

        Node column = chooseColumn();
        cover(column);

        for (Node row = column.below; row != column; row = row.below) {
            puzzle.addPlacement(row.placement);


            for (Node rightOfRow = row.right; rightOfRow != row; rightOfRow = rightOfRow.right) {
                cover(rightOfRow.column);
            }

            findAll();
            if (stopSolver)
                return;

            for (Node leftOfRow = row.left; leftOfRow != row; leftOfRow = leftOfRow.left) {
                uncover(leftOfRow.column);
            }

            puzzle.removePlacement(row.placement);
        }

        uncover(column);
    }

    Node chooseColumn() {
        Node column = new Node(Integer.MAX_VALUE);
        for (Node i = root.right; i != root; i = i.right) {
            if (i.size < column.size)
                column = i;
        }
        return column;
    }

    void cover(Node column) {
        column.right.left = column.left;
        column.left.right = column.right;

        for (Node i = column.below; i != column; i = i.below) {
            for (Node j = i.right; i != j; j = j.right) {
                j.below.above = j.above;
                j.above.below = j.below;
                j.column.size -= 1;
            }
        }
    }

    void uncover(Node column) {
        for (Node i = column.above; i != column; i = i.above) {
            for (Node j = i.left; i != j; j = j.left) {
                j.below.above = j;
                j.above.below = j;
                j.column.size += 1;
            }
        }
        column.right.left = column;
        column.left.right = column;
    }

    public void stop() {
        stopSolver = true;
    }

    class Matrix {
        List<Boolean[]> rows;
        List<Position> columnPositions;
        List<Placement> placementOfRow;
        List<Piece> pieces;


        public Matrix(List<Position> columnPositions, List<Boolean[]> rows, List<Placement> placementOfRow, List<Piece> pieces) {
            this.columnPositions = columnPositions;
            this.rows = rows;
            this.placementOfRow = placementOfRow;
            this.pieces = pieces;
        }
    }

    protected class Node {
        Node left;
        Node right;
        Node above;
        Node below;
        Node column;
        int size;
        Piece piece;
        Placement placement;


        public Node() {
        }

        public Node(int size) {
            this.size = size;
        }
    }
}
