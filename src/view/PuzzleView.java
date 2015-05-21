package view;

import gui.MainFrame;
import model.BoxListener;
import model.Placement;
import model.Position;
import model.Puzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

/**
 * Displays various information about the puzzle. Currently displayed are: <ul> <li>The box</li> <li>The bag with
 * pieces</li> <li>The statistics</li> </ul>
 *
 * @author Robert Leenders
 */
public class PuzzleView extends JComponent implements BoxListener {
    /**
     * The view which displays the box
     */
    private BoxView boxView;
    /**
     * The view which displays the bag of pieces
     */
    private BagOfPiecesView bagOfPiecesView;
    /**
     * The drag state which handles Drag'n'Drop
     */
    private DragState dragState;
    /**
     * The view which displays the statistics
     */
    private PuzzleStatisticsView statisticsView;
    /**
     * The puzzle which is viewed
     */
    private Puzzle puzzle;
    /**
     * A boolean which indicates whether we need to paint the placement
     */
    private boolean paintPlacements = true;
    /**
     * The mainframe which calls us
     */
    private final MainFrame mainFrame;
    /**
     * A boolean which indicates whether the last action is performed by the user
     */
    private boolean lastActionByUser = false;

    /**
     * The preferred dimension of this view
     */
    private Dimension preferredDimension = new Dimension(0, 0);


    /**
     * Constructs a new view for a puzzle.
     *
     * @param mainFrame the mainframe which created us
     */
    public PuzzleView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                puzzleMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                puzzleMouseReleased(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                puzzleMouseMoved(evt);
            }

            public void mouseDragged(java.awt.event.MouseEvent evt) {
                puzzleMouseDragged(evt);
            }
        });
    }

    /**
     * Method which is called when the mouse is pressed. It handles the Drag'n'Drop.
     *
     * @param evt the mouse event
     */
    public void puzzleMousePressed(java.awt.event.MouseEvent evt) {
        if (!isEnabled())
            return;
        final java.awt.Point point = evt.getPoint();
        if (boxView.containsPoint(point)) {
            final PlacementView pv = boxView.getPlacementViewOn(point);
            if (pv != null) {
                dragState = new DragState(pv, point, false);
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }
        } else {
            if (puzzle.getRemainingPlacementsOfPiece(bagOfPiecesView.getCurrentPiece()) <= 0)
                return;
            PieceView pv = bagOfPiecesView.getPieceView();
            point.x -= bagOfPiecesView.getLocation().x;
            Position p;
            try {
                p = pv.pointToPosition(point);
            } catch (IllegalArgumentException e) {
                return; // Point not inside piece
            }
            if (pv.getOrientation().isPositionOccupied(p.getRow(), p.getColumn())) {
                PieceView newPieceView = new PieceView(pv.getTopleft(), pv.getOrientation(), pv.getPiece());
                dragState = new DragState(newPieceView, point, true);
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }
        }
    }

    /**
     * Method which is called when the mouse is released. It handles the Drag'n'Drop.
     *
     * @param evt the mouse event
     */
    public void puzzleMouseReleased(java.awt.event.MouseEvent evt) {
        final java.awt.Point point = evt.getPoint();
        if (dragState == null) { // no drag in progress; ignore
            return;
        }
        // Drag in progress, so end it.
        lastActionByUser = true;
        dragState.endDrag(point);
        dragState = null;
        setCursor(Cursor.getDefaultCursor());
        repaint();
    }

    /**
     * Method which is called when the mouse is moved It handles the Drag'n'Drop
     *
     * @param evt the mouse event
     */
    public void puzzleMouseMoved(java.awt.event.MouseEvent evt) {
        if (!isEnabled())
            return;
        final java.awt.Point point = evt.getPoint();
        if (boxView.containsPoint(point)) {
            if (boxView.hasPlacementViewOn(point)) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                return;
            }
        } else {
            if (puzzle.getRemainingPlacementsOfPiece(bagOfPiecesView.getCurrentPiece()) <= 0) {
                setCursor(Cursor.getDefaultCursor());
                return;
            }

            PieceView pv = bagOfPiecesView.getPieceView();
            // The point is relative to the PuzzleView, translate this to relative of BagOfPiecesView
            point.x -= bagOfPiecesView.getLocation().x;
            point.y -= bagOfPiecesView.getLocation().y;

            Position p;
            try {
                p = pv.pointToPosition(point);
            } catch (IllegalArgumentException e) {
                setCursor(Cursor.getDefaultCursor());
                return; // Point not inside piece
            }
            if (pv.getOrientation().isPositionOccupied(p.getRow(), p.getColumn())) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                return;
            }
        }
        setCursor(Cursor.getDefaultCursor());
    }

    /**
     * Method which is called when the mouse is dragged.
     *
     * @param evt the mouse event
     */
    public void puzzleMouseDragged(java.awt.event.MouseEvent evt) {
        if (dragState != null) {
            dragState.drag(evt);
            if (dragState.canDrop(evt.getPoint())) {
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            } else {
                setCursor(Cursor.getDefaultCursor());
            }
            repaint();
        }
    }

    /**
     * Returns the preferred size of this view.
     *
     * @return preferred dimension
     */
    public Dimension getPreferredSize() {
        return preferredDimension;
    }

    /**
     * Returns the puzzle being viewed.
     *
     * @return the puzzle
     */
    public Puzzle getPuzzle() {
        return puzzle;
    }

    /**
     * Paints the view on the given graphics.
     *
     * @param g the graphics context
     */
    public void paint(final Graphics g) {
        super.paint(g);
        boxView.paint(g, paintPlacements);
        if (dragState != null)
            dragState.paint(g);
        statisticsView.paint(g);
    }

    /**
     * Called when a placement is added to the puzzle.
     *
     * @param placement the placement
     */
    public void placementAdded(Placement placement) {
        boxView.addPlacement(placement);
        repaint();
        if (lastActionByUser)
            congratulateIfPuzzleIsUniquelySolved();
        lastActionByUser = false;
        mainFrame.updateMenu();
    }

    /**
     * Helper function which displays a new dialog when the user found an unique solution.
     */
    private void congratulateIfPuzzleIsUniquelySolved() {
        if (puzzle.isUniquelySolved()) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ResourceBundle bundle = ResourceBundle.getBundle("gui.properties_en");
                    JOptionPane.showMessageDialog(null, bundle.getString("PuzzleView.puzzleSolved.text"), bundle.getString("PuzzleView.puzzleSolved.header"), JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }
    }

    /**
     * Called when a placement is removed from the puzzle.
     *
     * @param placement the placement
     */
    public void placementRemoved(Placement placement) {
        boxView.removePlacement(placement);
        repaint();
    }

    /**
     * Sets a boolean indicating whether we need to paint the placements
     *
     * @param paintPlacements boolean indicating whether we need to paint the placements
     */
    public void setPaintPlacements(boolean paintPlacements) {
        this.paintPlacements = paintPlacements;
    }

    /**
     * Sets the puzzle to be viewed by this view. Everything is re-constructed when this method is called.
     *
     * @param puzzle the puzzle.
     */
    public void setPuzzle(Puzzle puzzle) {
        if (bagOfPiecesView != null)
            remove(bagOfPiecesView);
        if (statisticsView != null)
            statisticsView.setPuzzle(puzzle);
        this.puzzle = puzzle;
        initComponents();
        mainFrame.updateMenu();
    }

    /**
     * Initializes all the components.
     */
    private void initComponents() {
        boxView = new BoxView(puzzle.getBox(), new Point(5, 5));
        statisticsView = new PuzzleStatisticsView(puzzle, new Point(5, boxView.getPreferredSize().height + 30));
        bagOfPiecesView = new BagOfPiecesView(puzzle);
        bagOfPiecesView.setBounds(Math.max(statisticsView.getWidth(), boxView.getPreferredSize().width) + 10, 5, bagOfPiecesView.getPreferredSize().width, bagOfPiecesView.getPreferredSize().height);
        preferredDimension = new Dimension(Math.max(statisticsView.getWidth(), boxView.getPreferredSize().width) + bagOfPiecesView.getPreferredSize().width + 20, Math.max(boxView.getPreferredSize().height + statisticsView.getHeight(), bagOfPiecesView.getPreferredSize().height) + 10);
        puzzle.addBoxListener(this);

        add(bagOfPiecesView);
        revalidate();
        repaint();
    }

    /**
     * Inner class which helps with Drag'n'Drop. Largely stolen from the given Drag'n'Drop example.
     */
    private class DragState {
        /**
         * Item location where drag started (for cancel)
         */
        final Point start;
        /**
         * Vector to drawing origin of item being dragged w.r.t. mouse location
         */
        final Point offset;
        /**
         * Item being dragged
         */
        final PieceView pv;

        /**
         * Is the item dragged from the bag of pieces?
         */
        final boolean dragFromBagOfPieces;


        /**
         * Constructs a new drag state from the given arguments.
         *
         * @param pv                  the piece which is dragged
         * @param point               the point from where it is dragged
         * @param dragFromBagOfPieces boolean which indicates whether this is a new piece
         */
        public DragState(PieceView pv, Point point, boolean dragFromBagOfPieces) {
            this.pv = pv;
            this.start = point;
            this.dragFromBagOfPieces = dragFromBagOfPieces;
            this.offset = new Point(pv.getTopleft().x - point.x,
                                   pv.getTopleft().y - point.y);
            if (!dragFromBagOfPieces) {
                PlacementView placementView = (PlacementView) pv;
                boxView.removePlacement(placementView.getPlacement());
                puzzle.getBox().removePlacement(new Position(placementView.getPlacement().getAnchorPosition().getRow(),
                                                            placementView.getPlacement().getAnchorPosition().getColumn() + placementView.getOrientation().getFirstOccupiedColumn()));
            }
        }

        /**
         * Method which handles the drag part of Drag'n'Drop
         *
         * @param e the mouse event
         */
        public void drag(final MouseEvent e) {
            final java.awt.Point point = e.getPoint();
            Point newPosition = new Point(point);
            newPosition.translate(offset.x, offset.y);
            pv.move(newPosition);
        }

        /**
         * Ends the drag, either by dropping item at detected target, or putting it back at the oldPlacement where it
         * came from.
         */
        void endDrag(Point point) {
            if (canDrop(point)) {
                if (!dragFromBagOfPieces) {
                    PlacementView placementView = (PlacementView) pv;
                    puzzle.getBox().addPlacement(placementView.getPlacement());
                    if (!boxView.containsPoint(point)) {
                        puzzle.removePlacement(placementView.getPlacement());
                    } else if (boxView.pointToPosition(point) == placementView.getPlacement().getAnchorPosition()) {
                        cancel(); // Same position as before
                    } else if (boxView.containsPoint(point)) {
                        puzzle.removePlacement(placementView.getPlacement());
                        Position startPosition = boxView.pointToPosition(start);
                        Position oldAnchorPosition = placementView.getPlacement().getAnchorPosition();
                        Position anchorPosition = boxView.pointToPosition(point);
                        anchorPosition.setRow(anchorPosition.getRow() + (oldAnchorPosition.getRow() - startPosition.getRow()));
                        anchorPosition.setColumn(anchorPosition.getColumn() + (oldAnchorPosition.getColumn() - startPosition.getColumn()));
                        puzzle.addPlacement(new Placement(anchorPosition, placementView.getOrientation(), placementView.getPiece()));
                    }
                } else {
                    Position anchorPosition = calculatePositionInBoxNewPiece(point);
                    if (anchorPosition == null)
                        return;
                    puzzle.addPlacement(new Placement(anchorPosition, pv.getOrientation(), pv.getPiece()));
                }
            } else {
                cancel();
            }
        }

        /**
         * Determines whether item can be dropped at current location.
         */
        boolean canDrop(Point point) {
            if (!boxView.containsPoint(point))
                return true;
            Position anchorPosition;
            if (!dragFromBagOfPieces) {
                PlacementView placementView = (PlacementView) pv;
                anchorPosition = boxView.pointToPosition(point);
                try {
                    Position startPosition = boxView.pointToPosition(start);
                    Position oldAnchorPosition = placementView.getPlacement().getAnchorPosition();

                    anchorPosition.setRow(anchorPosition.getRow() + (oldAnchorPosition.getRow() - startPosition.getRow()));
                    anchorPosition.setColumn(anchorPosition.getColumn() + (oldAnchorPosition.getColumn() - startPosition.getColumn()));
                } catch (IllegalArgumentException e) {
                    return false;
                }
            } else {
                anchorPosition = calculatePositionInBoxNewPiece(point);
                if (anchorPosition == null)
                    return false;
            }
            return puzzle.isPlaceFree(new Placement(anchorPosition, pv.getOrientation(), pv.getPiece()));
        }

        /**
         * Helper function which calculates the position in the box if a new piece is dropped.
         *
         * @param point the point where we are now
         * @return The position for this piece in the box based on the given point
         * @throws IllegalArgumentException when the point is invalid
         */
        private Position calculatePositionInBoxNewPiece(Point point) throws IllegalArgumentException {
            Position anchorPosition;
            try {
                Position startPosition = bagOfPiecesView.getPieceView().pointToPosition(start);
                anchorPosition = boxView.pointToPosition(point);
                anchorPosition.setRow(anchorPosition.getRow() - startPosition.getRow());
                anchorPosition.setColumn(anchorPosition.getColumn() - startPosition.getColumn());
            } catch (IllegalArgumentException ex) {
                return null;
            }
            return anchorPosition;
        }

        /**
         * Cancels the drag, by putting item back where it came from.
         */
        void cancel() {
            if (!dragFromBagOfPieces) {
                pv.move(pv.getTopleft());
                PlacementView placementView = (PlacementView) pv;
                boxView.addPlacement(placementView.getPlacement());
                puzzle.getBox().addPlacement(placementView.getPlacement());
            }
        }

        /**
         * Paints the view on the given graphics.
         *
         * @param g the graphics context
         */
        public void paint(final Graphics g) {
            if (pv != null)
                pv.paint(g);
        }
    }
}
