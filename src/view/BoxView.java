package view;

import model.*;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A graphical view of a box.
 *
 * @author Robert Leenders
 */
public class BoxView {
    /**
     * Set containing the views for the placements
     */
    Set<PlacementView> placementViews;

    /**
     * Its dimension
     */
    private final Dimension dimension;
    /**
     * The box being viewed
     */
    private final Box box;
    /**
     * The topleft coordinate for this box
     */
    private final Point topLeft;

    /**
     * Constructs a view for the given box at the given location.
     *
     * @param box     the box the be viewed
     * @param topLeft coordinates of the view area's top left corner
     */
    public BoxView(Box box, Point topLeft) {
        this.box = box;
        dimension = new Dimension(box.getColumnCount() * PieceView.WIDTH_OF_ONE_BLOCK,
                                 box.getRowCount() * PieceView.HEGIHT_OF_ONE_BLOCK);
        this.topLeft = topLeft;
        placementViews = new HashSet<PlacementView>();
        Set<Placement> alreadyInView = new HashSet<Placement>();

        int y = topLeft.y;
        for (List<Cell> row : box) {
            int x = topLeft.x;
            for (Cell column : row) {
                if (column.getState() == CellState.OCCUPIED) {
                    if (!alreadyInView.contains(column.getPlacement())) {
                        placementViews.add(new PlacementView(column.getPlacement(), new Point(x, y)));
                        alreadyInView.add(column.getPlacement());
                    }
                }
                x += PieceView.WIDTH_OF_ONE_BLOCK;
            }
            y += PieceView.HEGIHT_OF_ONE_BLOCK;
        }
    }

    /**
     * Constructs a view for this placement and adds it to the box.
     *
     * @param placement the placement to be added
     */
    public void addPlacement(Placement placement) {
        final int x = topLeft.x + PieceView.WIDTH_OF_ONE_BLOCK * placement.getAnchorPosition().getColumn();
        final int y = topLeft.y + PieceView.HEGIHT_OF_ONE_BLOCK * placement.getAnchorPosition().getRow();
        placementViews.add(new PlacementView(placement, new Point(x, y)));
    }

    /**
     * Returns the preferred dimension.
     *
     * @return preferred dimension
     */
    public Dimension getPreferredSize() {
        return dimension;
    }

    /**
     * Returns whether the point has a placement view on it.
     *
     * @param point the coordinate of the point
     * @return {@code true} if the point overlaps with a placement view; false otherwise
     * @throws IllegalArgumentException if the point does not lie in the box
     */
    public boolean hasPlacementViewOn(final Point point) throws IllegalArgumentException {
        return getPlacementViewOn(point) != null;
    }

    /**
     * Gets the placement view on the given point.
     *
     * @param point the point to be considered
     * @return the placementview on the given point, if there is none then {@code null} is returned
     * @throws IllegalArgumentException if the position does not lie in the box
     */
    PlacementView getPlacementViewOn(final Point point) throws IllegalArgumentException {
        if (!containsPoint(point))
            return null; // Point is not even in this box
        Position p = pointToPosition(point);
        Placement placement = box.get(p.getRow(), p.getColumn()).getPlacement();

        for (PlacementView pv : placementViews) {
            if (pv.getPlacement() == placement) {
                return pv;
            }
        }
        return null;
    }

    /**
     * Returns whether the given point lies in this box.
     *
     * @param point the point to be considered
     * @return {@code true} if the point lies in the box; false otherwise
     */
    public boolean containsPoint(final Point point) {
        final int rel_x = point.x - topLeft.x;
        final int rel_y = point.y - topLeft.y;
        return 0 <= rel_x && rel_x <= dimension.getWidth() &&
               0 <= rel_y && rel_y <= dimension.getHeight();
    }

    /**
     * Converts the given point to a position in the box.
     *
     * @param point the point to be considered
     * @return the position where the point is at
     * @throws IllegalArgumentException if the position does not lie in the box
     */
    Position pointToPosition(final Point point) throws IllegalArgumentException {
        if (!containsPoint(point))
            throw new IllegalArgumentException("Point is not inside box");
        int row = (point.y - topLeft.y - 1) / PieceView.HEGIHT_OF_ONE_BLOCK;
        int column = (point.x - topLeft.x - 1) / PieceView.WIDTH_OF_ONE_BLOCK;
        return new Position(row, column);
    }

    /**
     * Paints the view on the given graphics
     *
     * @param g               the graphics context
     * @param paintPlacements boolean which indicates whether the placement should be drawn
     */
    public void paint(Graphics g, boolean paintPlacements) {
        paintEmptyBox(g);
        if (paintPlacements)
            paintPlacements(g);
    }

    /**
     * Paints the empty box on the given graphics.
     *
     * @param g the graphics context
     */
    private void paintEmptyBox(Graphics g) {
        int y = topLeft.y;
        for (List<Cell> row : box) {
            int x = topLeft.x;
            for (Cell column : row) {
                if (column.getState() == CellState.BLOCKED)
                    g.setColor(Color.DARK_GRAY);
                else
                    g.setColor(Color.WHITE);
                g.fillRect(x, y, PieceView.WIDTH_OF_ONE_BLOCK, PieceView.HEGIHT_OF_ONE_BLOCK);
                g.setColor(new Color(0xD1, 0xD1, 0xD1));
                g.drawRect(x, y, PieceView.WIDTH_OF_ONE_BLOCK, PieceView.HEGIHT_OF_ONE_BLOCK);
                x += PieceView.WIDTH_OF_ONE_BLOCK;
            }
            y += PieceView.HEGIHT_OF_ONE_BLOCK;
        }
    }

    /**
     * Paints the placements on the given graphics.
     *
     * @param g the graphics context
     */
    private void paintPlacements(Graphics g) {
        for (PlacementView pv : placementViews) {
            pv.paint(g);
        }
    }

    /**
     * Removes a placement view from the box.
     *
     * @param placement which indicates which placementview should be removed
     */
    public void removePlacement(Placement placement) {
        for (PlacementView pv : placementViews) {
            if (pv.getPlacement() == placement) {
                placementViews.remove(pv);
                return;
            }
        }
    }
}
