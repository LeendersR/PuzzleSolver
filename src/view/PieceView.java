package view;

import model.Orientation;
import model.Piece;
import model.Position;

import java.awt.*;

/**
 * A graphical view of a piece
 *
 * @author Robert Leenders
 */
public class PieceView {
    /**
     * Width (in pixels) of one block, a piece is specified by one or multiple blocks.
     */
    final static int WIDTH_OF_ONE_BLOCK = 30;
    /**
     * Height (in pixels) of one block, a piece is specified by one or multiple blocks.
     */
    final static int HEGIHT_OF_ONE_BLOCK = 30;

    /**
     * Coordinates of its top left view corner
     */
    private Point topLeft;

    /**
     * The orientation in which this piece lies
     */
    private Orientation orientation;

    /**
     * The piece it self
     */
    private Piece piece;


    /**
     * Constructs a view for the given piece in the given orientation at the given location.
     *
     * @param topLeft     the coordinates of the top left view corner
     * @param orientation the orientation in which the piece lies
     * @param piece       the piece
     */
    public PieceView(Point topLeft, Orientation orientation, Piece piece) {
        this.topLeft = topLeft;
        setOrientation(orientation);
        setPiece(piece);
    }

    /**
     * Returns the orientation
     *
     * @return the orientation of the piece
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Sets the orientation for this piece
     *
     * @param orientation the orientation
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Returns the piece
     *
     * @return the piece
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Sets the piece
     *
     * @param piece the piece
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * Returns the top left coordinates.
     *
     * @return the topleft point
     */
    public Point getTopleft() {
        return topLeft;
    }

    /**
     * Moves this pieceview to a new location by setting it's topleft attribute.
     *
     * @param newTopLeft the new topleft point of this pieceview
     */
    public void move(Point newTopLeft) {
        topLeft = newTopLeft;
    }

    /**
     * Paints the view on the given graphics
     *
     * @param g the graphics context
     */
    public void paint(final Graphics g) {
        for (Position position : orientation) {
            int x = topLeft.x + position.getColumn() * WIDTH_OF_ONE_BLOCK;
            int y = topLeft.y + position.getRow() * HEGIHT_OF_ONE_BLOCK;
            g.setColor(piece.getColor());
            g.fillRect(x, y, WIDTH_OF_ONE_BLOCK, HEGIHT_OF_ONE_BLOCK);

            drawPieceBorder(g, position, x, y);
        }
    }

    private void drawPieceBorder(Graphics g, Position position, int x, int y) {
        g.setColor(Color.BLACK);
        drawPieceTopBorder(g, position, x, y);
        drawPieceRightBorder(g, position, x, y);
        drawPieceBottomBorder(g, position, x, y);
        drawPieceLeftBorder(g, position, x, y);
    }

    private void drawPieceTopBorder(Graphics g, Position position, int x, int y) {
        if (!orientation.isPositionOccupied(position.getRow() - 1, position.getColumn()))
            g.drawLine(x, y, x + PieceView.WIDTH_OF_ONE_BLOCK, y);
    }

    private void drawPieceRightBorder(Graphics g, Position position, int x, int y) {
        if (!orientation.isPositionOccupied(position.getRow(), position.getColumn() + 1))
            g.drawLine(x + PieceView.WIDTH_OF_ONE_BLOCK, y, x + PieceView.WIDTH_OF_ONE_BLOCK, y + PieceView.HEGIHT_OF_ONE_BLOCK);
    }

    private void drawPieceBottomBorder(Graphics g, Position position, int x, int y) {
        if (!orientation.isPositionOccupied(position.getRow() + 1, position.getColumn()))
            g.drawLine(x, y + PieceView.HEGIHT_OF_ONE_BLOCK, x + PieceView.WIDTH_OF_ONE_BLOCK, y + PieceView.HEGIHT_OF_ONE_BLOCK);
    }

    private void drawPieceLeftBorder(Graphics g, Position position, int x, int y) {
        if (!orientation.isPositionOccupied(position.getRow(), position.getColumn() - 1))
            g.drawLine(x, y, x, y + PieceView.HEGIHT_OF_ONE_BLOCK);
    }

    public Position pointToPosition(final Point point) {
        if (point.y < topLeft.y || point.x < topLeft.x)
            throw new IllegalArgumentException("Point is not inside piece");
        int row = (point.y - topLeft.y - 1) / PieceView.HEGIHT_OF_ONE_BLOCK;
        int column = (point.x - topLeft.x - 1) / PieceView.WIDTH_OF_ONE_BLOCK;
        return new Position(row, column);
    }
}
