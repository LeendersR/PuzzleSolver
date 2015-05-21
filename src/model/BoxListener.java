package model;

import java.util.EventListener;

/**
 * BoxListener, which listens for an update on the box.
 *
 * @author Robert Leenders
 */
public interface BoxListener extends EventListener {
    void placementRemoved(Placement placement);

    void placementAdded(Placement placement);
}
