package view;

import model.Orientation;
import model.Piece;
import model.Puzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ListIterator;
import java.util.ResourceBundle;

/**
 * Displays the bag of pieces along with the current selected piece.
 *
 * @author Robert Leenders
 */
public class BagOfPiecesView extends JComponent {
    /**
     * The puzzle from which the bag of pieces is from
     */
    private Puzzle puzzle;
    /**
     * List-iterator for the pieces
     */
    private ListIterator<Piece> bagOfPiecesIterator;
    /**
     * The pieceview which displays the current piece
     */
    private PieceView pieceView;
    /**
     * A box which holds the button to navigate between pieces/orientations
     */
    private Box buttons;
    /**
     * The current piece (which is displayed)
     */
    private Piece currentPiece;
    /**
     * The current orientation of {@code currentPiece}
     */
    private Orientation currentPieceOrientation;
    /**
     * List-iterator for the orientations of {@code currentPiece}
     */
    private ListIterator<Orientation> pieceOrientationsIterator;
    /**
     * The maximum width of a piece/orientation combination
     */
    private int maxPieceWidth;
    /**
     * The maximum height of a piece/orientation combination
     */
    private int maxPieceHeight;


    /**
     * Creates a view for the given bag of pieces.
     *
     * @param puzzle the puzzle where we get the bag of pieces from
     */
    public BagOfPiecesView(Puzzle puzzle) {
        this.puzzle = puzzle;
        bagOfPiecesIterator = puzzle.bagOfPiecesListIterator();
        if (bagOfPiecesIterator.hasNext())
            currentPiece = bagOfPiecesIterator.next();
        pieceOrientationsIterator = currentPiece.orientationListIterator();
        if (pieceOrientationsIterator.hasNext())
            currentPieceOrientation = pieceOrientationsIterator.next();

        ListIterator<Piece> tempPiecesIterator = puzzle.bagOfPiecesListIterator();
        while (tempPiecesIterator.hasNext()) {
            Piece p = tempPiecesIterator.next();
            ListIterator<Orientation> tempOrientationsIterator = p.orientationListIterator();
            while (tempOrientationsIterator.hasNext()) {
                Orientation o = tempOrientationsIterator.next();
                maxPieceWidth = Math.max(maxPieceWidth, o.getWidth() + 2);
                maxPieceHeight = Math.max(maxPieceHeight, o.getHeight() + 1);
            }
        }
        maxPieceWidth *= PieceView.WIDTH_OF_ONE_BLOCK;
        maxPieceHeight *= PieceView.HEGIHT_OF_ONE_BLOCK;
        initComponents();
    }

    /**
     * Initializes all components of this component
     */
    private void initComponents() {
        ResourceBundle bundle = ResourceBundle.getBundle("gui.properties_en");

        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttons = Box.createVerticalBox();

        JButton prevPiece = new JButton();
        JButton nextPiece = new JButton();
        JButton prevOrientation = new JButton();
        JButton nextOrientation = new JButton();

        prevPiece.setFocusable(false);
        nextPiece.setFocusable(false);
        prevOrientation.setFocusable(false);
        nextOrientation.setFocusable(false);

        prevPiece.setText(bundle.getString("BagOfPiecesView.prevPiece.text"));
        prevPiece.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                prevPiece();
            }
        });
        nextPiece.setText(bundle.getString("BagOfPiecesView.nextPiece.text"));
        nextPiece.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextPiece();
            }
        });

        prevOrientation.setText(bundle.getString("BagOfPiecesView.prevOrientation.text"));
        prevOrientation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                prevOrientation();
            }
        });
        nextOrientation.setText(bundle.getString("BagOfPiecesView.nextOrientation.text"));
        nextOrientation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextOrientation();
            }
        });

        buttons.add(nextPiece);
        buttons.add(prevPiece);

        buttons.add(nextOrientation);
        buttons.add(prevOrientation);

        add(buttons);

        pieceView = new PieceView(new Point(getLocation().x + buttons.getPreferredSize().width + 30, getLocation().y), currentPieceOrientation, currentPiece);
    }

    /**
     * Goes to the previous piece
     */
    private void prevPiece() {
        if (bagOfPiecesIterator.hasPrevious()) {
            Piece newPiece = bagOfPiecesIterator.previous();
            // Did we just reverse direction? If so by the working of the ListIterator we get the same element
            if (newPiece.equals(currentPiece)) {
                prevPiece();
                return;
            }
            currentPiece = newPiece;
        } else {
            while (bagOfPiecesIterator.hasNext())
                currentPiece = bagOfPiecesIterator.next();
        }

        pieceView.setPiece(currentPiece);
        pieceOrientationsIterator = currentPiece.orientationListIterator();
        if (pieceOrientationsIterator.hasNext())
            currentPieceOrientation = pieceOrientationsIterator.next();
        pieceView.setOrientation(currentPieceOrientation);
        repaint();
    }

    /**
     * Goes the the next piece
     */
    private void nextPiece() {
        if (bagOfPiecesIterator.hasNext()) {
            Piece newPiece = bagOfPiecesIterator.next();
            // Did we just reverse direction? If so by the working of the ListIterator we get the same element
            if (newPiece.equals(currentPiece)) {
                nextPiece();
                return;
            }
            currentPiece = newPiece;
        } else {
            while (bagOfPiecesIterator.hasPrevious())
                currentPiece = bagOfPiecesIterator.previous();
        }

        pieceView.setPiece(currentPiece);
        pieceOrientationsIterator = currentPiece.orientationListIterator();
        if (pieceOrientationsIterator.hasNext())
            currentPieceOrientation = pieceOrientationsIterator.next();
        pieceView.setOrientation(currentPieceOrientation);
        repaint();
    }

    /**
     * Goes to the previous orientation
     */
    private void prevOrientation() {
        if (pieceOrientationsIterator.hasPrevious()) {
            Orientation newOrientation = pieceOrientationsIterator.previous();
            // Did we just reverse direction? If so by the working of the ListIterator we get the same element
            if (newOrientation.equals(currentPieceOrientation)) {
                prevOrientation();
                return;
            }
            currentPieceOrientation = newOrientation;
        } else {
            while (pieceOrientationsIterator.hasNext())
                currentPieceOrientation = pieceOrientationsIterator.next();
        }
        pieceView.setOrientation(currentPieceOrientation);
        repaint();
    }

    /**
     * Goes to the next orientation
     */
    private void nextOrientation() {
        if (pieceOrientationsIterator.hasNext()) {
            Orientation newOrientation = pieceOrientationsIterator.next();
            // Did we just reverse direction? If so by the working of the ListIterator we get the same element
            if (newOrientation.equals(currentPieceOrientation)) {
                nextOrientation();
                return;
            }
            currentPieceOrientation = newOrientation;
        } else {
            while (pieceOrientationsIterator.hasPrevious())
                currentPieceOrientation = pieceOrientationsIterator.previous();
        }
        pieceView.setOrientation(currentPieceOrientation);
        repaint();
    }

    /**
     * Returns the piece which is currently displayed.
     *
     * @return the current piece
     */
    public Piece getCurrentPiece() {
        return currentPiece;
    }

    /**
     * Returns the piece view which displays the current piece.
     *
     * @return pieceView
     */
    public PieceView getPieceView() {
        return pieceView;
    }

    /**
     * Returns the preferred size for this component
     *
     * @return dimension with the preferred size
     */
    public Dimension getPreferredSize() {
        return new Dimension(buttons.getPreferredSize().width + maxPieceWidth, Math.max(buttons.getPreferredSize().height, maxPieceHeight) + 50);
    }

    /**
     * Paints the view on the given graphics.
     *
     * @param g the graphics context
     */
    public void paint(final Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        ResourceBundle bundle = ResourceBundle.getBundle("gui.properties_en");
        g.drawString(MessageFormat.format(bundle.getString("BagOfPiecesView.numPiecesLeft.text"), puzzle.getRemainingPlacementsOfPiece(currentPiece)), 5, Math.max(buttons.getPreferredSize().height + 20, maxPieceHeight));
        pieceView.paint(g);
    }
}
