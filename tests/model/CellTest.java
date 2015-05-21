package model;

import junit.framework.TestCase;
import model.*;

/**
 * Unit test class for Cell
 */
public class CellTest extends TestCase {

    /**
     * Tests if the constructor sets the state correctly
     */
    public void testConstructor() {
        Cell c = new Cell(false);
        assertEquals("Cell should be free", CellState.FREE, c.getState());

        c = new Cell(true);
        assertEquals("Cell should be blocked", CellState.BLOCKED, c.getState());
    }

    final static class Fixture {
        static Placement placement = null;

        static {
            Orientation o = new Orientation();
            o.addPosition(0, 0);
            Piece p = new Piece("1", null, 1);
            p.addOrientation(o);
            placement = new Placement(new Position(0, 0), o, p);
        }
    }

    /**
     * Tests if setPlacement will throw an exception when the cell is blocked
     */
    public void testSetPlacementExceptionBlockedCell() {
        Cell c = new Cell(true);
        try {
            c.setPlacement(Fixture.placement);
            fail("Should throw IllegalOperationException when placing something on a blocked cell.");
        } catch (IllegalOperationException e) {
            assertTrue(true);
        }


    }

    /**
     * Tests if setPlacement will throw an exception when the cell is occupied
     */
    public void testSetPlacementExceptionOccupiedCell() {
        Cell c = new Cell(false);
        c.setPlacement(Fixture.placement);
        try {
            c.setPlacement(Fixture.placement);
            fail("Should throw IllegalOperationException when placing something on an occupied cell.");
        } catch (IllegalOperationException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests if setPlacement correctly sets all variables
     */
    public void testSetPlacement() {
        Cell c = new Cell(false);
        c.setPlacement(Fixture.placement);
        assertEquals("Given placement should be equal to retrieved placement", Fixture.placement, c.getPlacement());
        assertEquals("Cell state should be occupied", CellState.OCCUPIED, c.getState());
    }

    /**
     * Tests if removePlacement correctly resets the cell
     */
    public void testRemovePlacement() {
        Cell c = new Cell(false);
        c.setPlacement(Fixture.placement);
        assertTrue("Remove operation should be successful since there is a placement", c.removePlacement());
        assertEquals("Placement of a cell which has just been reset should be null", null, c.getPlacement());
        assertEquals("Cell state of a cell which has just been reset should be free", CellState.FREE, c.getState());
    }

    /**
     * Tests if removePlacement returns false (and if it doesn't have any side effects) when the cell is blocked
     */
    public void testRemovePlacementCellBlocked() {
        Cell c = new Cell(true);
        assertFalse("Remove operation should be successful since there is a placement", c.removePlacement());
        assertEquals("Placement of a blocked cell which has just been reset should be null", null, c.getPlacement());
        assertEquals("Cell state of a blocked cell which has just been reset should be blocked", CellState.BLOCKED, c.getState());
    }

    /**
     * Tests if isPlacementPossible works correctly on all cell states
     */
    public void testIsPlacementPossible() {
        Cell c = new Cell(true);
        assertFalse("Placement on blocked cell shouldn't be possible", c.isPlacementPossible());
        c = new Cell(false);
        assertTrue("Placement on free cell should be possible", c.isPlacementPossible());

        c.setPlacement(Fixture.placement);
        assertFalse("Placement on occupied cell shouldn't be possible", c.isPlacementPossible());

    }
}
