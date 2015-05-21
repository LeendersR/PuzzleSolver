package model;

import junit.framework.TestCase;

import java.awt.*;
import java.util.ArrayList;

/**
 * INFO
 *
 * @author Robert Leenders
 */
public class BoxTest extends TestCase {

    /**
     * Tests if the constructor correctly throws an {@code IllegalArgumentException} when rowCount/columnCount is
     * negative
     */
    public void testConstructorException() {
        try {
            new Box(1, 0);
            fail("Constructor should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            new Box(0, 1);
            fail("Constructor should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            new Box(0, 0);
            fail("Constructor should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

         try {
            new Box(1, 1, null);
            fail("Constructor should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            new Box(1, 0, new ArrayList<Position>());
            fail("Constructor should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            new Box(0, 1, new ArrayList<Position>());
            fail("Constructor should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            new Box(0, 0, new ArrayList<Position>());
            fail("Constructor should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        assertTrue(true);

    }

    static final class Fixture {
        static Piece p = new Piece("1", Color.BLACK, 5);
        static Orientation o1 = new Orientation();
        static Orientation o2 = new Orientation();

        static Box x = new Box(3, 3);
        static Box y = new Box(3, 3);
        static Box z = new Box(3, 3);
        static Box notx = new Box(3, 3);



        static {
            o1.addPosition(0, 0);
            o1.addPosition(1, 0);
            p.addOrientation(o1);

            o2.addPosition(0, 0);
            o2.addPosition(0, 1);
            p.addOrientation(o2);

            x.addPlacement(new Placement(new Position(0, 0), Fixture.o2, Fixture.p));
            x.addPlacement(new Placement(new Position(1, 0), Fixture.o1, Fixture.p));

            y.addPlacement(new Placement(new Position(0, 0), Fixture.o2, Fixture.p));
            y.addPlacement(new Placement(new Position(1, 0), Fixture.o1, Fixture.p));

            z.addPlacement(new Placement(new Position(1, 0), Fixture.o1, Fixture.p));
            z.addPlacement(new Placement(new Position(0, 0), Fixture.o2, Fixture.p));

            notx.addPlacement(new Placement(new Position(0, 1), Fixture.o2, Fixture.p));
            notx.addPlacement(new Placement(new Position(0, 0), Fixture.o1, Fixture.p));
        }
    }

    /**
     * Tests if getColumnCount returns the right amount of columns
     */
    public void testGetColumnCount() {
        assertEquals("getColumnCount test fails", 3, Fixture.x.getColumnCount());
    }

    /**
     * Tests if getRowCount returns the right amount of rows
     */
    public void testGetRowCount() {
        assertEquals("getRowCount test fails", 3, Fixture.x.getRowCount());
    }

    /**
     * Tests if the placement doesn't overlap with the borders. (row)
     */
    public void testAddPlacementMaxRowGreaterThanRowCount() {
        Box box = new Box(3, 3);
        try {
            box.addPlacement(new Placement(new Position(2, 0), Fixture.o1, Fixture.p));
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests if the placement doesn't overlap with the borders. (column)
     */
    public void testAddPlacementMaxColumnGreaterThanColumnCount() {
        Box box = new Box(3, 3);
        try {
            box.addPlacement(new Placement(new Position(0, 2), Fixture.o2, Fixture.p));
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests if the placement doesn't overlap with the borders. (column)
     */
    public void testAddPlacementOnOccupiedCell() {
        Box box = new Box(3, 3);
        try {
            box.addPlacement(new Placement(new Position(0, 0), Fixture.o2, Fixture.p));
            box.addPlacement(new Placement(new Position(0, 0), Fixture.o1, Fixture.p));
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests if addPlacement works correctly.
     */
    public void testAddPlacement() {
        Box box = new Box(3, 3, new ArrayList<Position>());
        String eol = System.getProperty("line.separator");
        String expected = "11." + eol + "1.." + eol + "1..";
        box.addPlacement(new Placement(new Position(0, 0), Fixture.o2, Fixture.p));
        box.addPlacement(new Placement(new Position(1, 0), Fixture.o1, Fixture.p));
        assertEquals("String representation of the boxes must be equal", expected, box.toString());
    }

    /**
     * Tests if removePlacement works correctly.
     */
    public void testRemovePlacement() {
        Box box = new Box(3, 3, new ArrayList<Position>());
        String eol = System.getProperty("line.separator");
        String expected = "..." + eol + "1.." + eol + "1..";
        box.addPlacement(new Placement(new Position(0, 0), Fixture.o2, Fixture.p));
        box.addPlacement(new Placement(new Position(1, 0), Fixture.o1, Fixture.p));
        box.removePlacement(new Position(0, 0)); // Remove on the anchor position
        assertEquals("String representation of the boxes must be equal", expected, box.toString());
        box.addPlacement(new Placement(new Position(0, 0), Fixture.o2, Fixture.p));
        box.removePlacement(new Position(0, 1)); // Remove on a non-anchor position
        assertEquals("String representation of the boxes must be equal", expected, box.toString());
    }

    /**
     * Tests if removePlacement throws an exception on negative input
     */
    public void testRemovePlacementException() {
        Box box = new Box(1, 1);
        try {
            box.removePlacement(new Position(-1, 0));
            fail("removePlacement should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            box.removePlacement(new Position(0, -1));
            fail("removePlacement should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            box.removePlacement(new Position(-1, -1));
            fail("removePlacement should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        assertTrue(true);
    }

    /**
     * Tests if removePlacement throws an exception on negative input
     */
    public void testRemovePlacementOnEmptyPlacement() {
        Box box = new Box(1, 1);
        try {
            box.removePlacement(new Position(0, 0));
            fail("removePlacement should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        assertTrue(true);
    }

    /**
     * Class is equal to itself
     */
    public void testEqualsReflexive() {
        assertTrue("Reflexive test fails x", Fixture.x.equals(Fixture.x));
    }

    /**
     * x.equals(y) must be the same as y.equals(x)
     */
    public void testEqualsSymmetric() {
        assertEquals("Symmetric test fails x, y", Fixture.x.equals(Fixture.y), Fixture.y.equals(Fixture.x));
    }

    /**
     * x.equals(y) returns true y.equals(z) returns true x.equals(z) must also return true
     */
    public void testEqualsTransitive() {
        assertTrue("Transitive test fails x, y, z", Fixture.x.equals(Fixture.y));
        assertTrue("Transitive test fails x, y, z", Fixture.y.equals(Fixture.z));
        assertTrue("Transitive test fails x, y, z", Fixture.x.equals(Fixture.z));
    }

    /**
     * Repeated calls to equals must consistently return true or false
     */
    public void testEqualsConsistent() {
        assertTrue("Consistent test fails x, y", Fixture.x.equals(Fixture.y));
        assertTrue("Consistent test fails x, y", Fixture.x.equals(Fixture.y));
        assertTrue("Consistent test fails x, y", Fixture.x.equals(Fixture.y));
        assertFalse(Fixture.notx.equals(Fixture.x));
        assertFalse(Fixture.notx.equals(Fixture.x));
        assertFalse(Fixture.notx.equals(Fixture.x));
    }

    /**
     * x.equals(null) must return false
     */
    public void testEqualsNullReference() {
        assertFalse("For any non-null reference value x, x.equals(null) should return false", Fixture.x.equals(null));
    }

    /**
     * x.equals(WrongType) must return false
     */
    public void testEqualsIncompatibleType() {
        assertFalse("Incompatible objects should return false", Fixture.x.equals("test"));
    }


    /**
     * Repeated calls to hashcode must consistently return the same integer.
     */
    public void testHashCodeConsistent() {

        int initialHashcode = Fixture.x.hashCode();

        assertEquals("Consistent hashcode test fails", initialHashcode, Fixture.x.hashCode());
        assertEquals("Consistent hashcode test fails", initialHashcode, Fixture.x.hashCode());
    }

    /**
     * Objects that are equal using the equals method should return the same integer.
     */
    public void testHashCodeEqualObjects() {

        int xHashcode = Fixture.x.hashCode();
        int yHashcode = Fixture.y.hashCode();

        assertEquals("Equal objects must return equal hashcodes test fails", xHashcode, yHashcode);
    }

    /**
     * A more optimal implementation of hashcode ensures that if the objects are unequal different integers are
     * produced.
     */
    public void testHashCodeUnequalObjects() {

        int xHashcode = Fixture.x.hashCode();
        int yHashcode = Fixture.notx.hashCode();

        assertFalse("Unequal objects must return unequal hashcodes test fails", xHashcode == yHashcode);
    }

}
