package view;

import model.Placement;

import java.awt.*;

/**
 * A graphical view for a placement.
 *
 * @author Robert Leenders
 */
public class PlacementView extends PieceView {
    /**
     * The placement which is viewed
     */
    private final Placement placement;


    /**
     * Constructs a new placementview for the given placement at the given location
     *
     * @param placement the to be viewed placement
     * @param topLeft the topleft coordinate
     */
    public PlacementView(Placement placement, Point topLeft) {
        super(topLeft, placement.getOrientation(), placement.getPiece());
        this.placement = placement;
    }

    /**
     * Returns the placement which is viewed.
     *
     * @return the placement
     */
    public Placement getPlacement() {
        return placement;
    }
}
