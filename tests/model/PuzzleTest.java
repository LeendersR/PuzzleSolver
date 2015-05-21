package model;

import junit.framework.TestCase;

import java.awt.*;

/**
 * INFO
 *
 * @author Robert Leenders
 */
public class PuzzleTest extends TestCase {

    public void setUp() {
        Fixture.reset();
    }

    public void testGetSetName() {
        assertEquals("Get name fails", "Simple_Puzzle", Fixture.x.getName());
        Fixture.x.setName("New_Puzzle");
        assertEquals("Set name fails", "New_Puzzle", Fixture.x.getName());
    }

    static final class Fixture {
        static Puzzle x;
        static Puzzle y;
        static Puzzle z;
        static Puzzle notx;

        static Piece piece1 = new Piece("A", Color.RED, 1);
        static Piece piece2 = new Piece("B", Color.CYAN, 1);
        static Piece piece3 = new Piece("C", Color.YELLOW, 1);

        static {
            reset();
        }

        static void reset() {
            Box box = new Box(3, 3);
            BagOfPieces bagOfPieces = new BagOfPieces();
            Orientation o = new Orientation();
            o.addPosition(0, 0);
            piece1.addOrientation(o);
            bagOfPieces.add(piece1);


            o = new Orientation();
            o.addPosition(0, 0);
            o.addPosition(0, 1);
            piece2.addOrientation(o);
            o = new Orientation();
            o.addPosition(0, 0);
            o.addPosition(1, 0);
            piece2.addOrientation(o);
            bagOfPieces.add(piece2);


            o = new Orientation();
            o.addPosition(0, 1);
            o.addPosition(1, 0);
            o.addPosition(1, 1);
            piece3.addOrientation(o);
            o = new Orientation();
            o.addPosition(0, 0);
            o.addPosition(1, 0);
            o.addPosition(1, 1);
            piece3.addOrientation(o);
            o = new Orientation();
            o.addPosition(0, 1);
            o.addPosition(1, 0);
            o.addPosition(0, 0);
            piece3.addOrientation(o);
            o = new Orientation();
            o.addPosition(0, 1);
            o.addPosition(0, 0);
            o.addPosition(1, 1);
            piece3.addOrientation(o);
            bagOfPieces.add(piece3);


            x = new Puzzle("Simple_Puzzle", box, bagOfPieces);
            y = new Puzzle("Simple_Puzzle", box, bagOfPieces);
            z = new Puzzle("Simple_Puzzle", box, bagOfPieces);
            notx = new Puzzle("Simple_Puzzle2", box, bagOfPieces);
        }
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

    /**
     * model.BoxTest already has tests for addPlacement, so we will only focus on the new problems which might arise.
     */

    /**
     * Tests if addPlacement an exception when the piece is already placed it's maximum times.
     */
    public void testAddPlacementException() {
        Fixture.x.addPlacement(new Placement(new Position(0, 0), Fixture.piece1.orientationIterator().next(), Fixture.piece1));
        try {
            Fixture.x.addPlacement(new Placement(new Position(1, 1), Fixture.piece1.orientationIterator().next(), Fixture.piece1));
            fail("A MaximumPlacementsReached exception should have been thrown.");
        } catch (MaximumPlacementsReached e) {
            assertTrue(true);
        }
    }

    /**
     * Tests if getRemainingPlacements successfully updates the remaining placements. Indirectly also test if add/remove
     * operations are successful. They add/remove options are tested extensively in the test class for the box.
     *
     * @see BoxTest#testAddPlacement()
     * @see BoxTest#testRemovePlacement()
     * @see BoxTest
     */
    public void testGetRemainingPlacements() {
        assertEquals("Test remaining placements fails", 1, Fixture.x.getRemainingPlacementsOfPiece(Fixture.piece1));
        Placement placement = new Placement(new Position(0, 0), Fixture.piece1.orientationIterator().next(), Fixture.piece1);
        Fixture.x.addPlacement(placement);
        assertEquals("Test remaining placements fails", 0, Fixture.x.getRemainingPlacementsOfPiece(Fixture.piece1));
        Fixture.x.removePlacement(placement);
        assertEquals("Test remaining placements fails", 1, Fixture.x.getRemainingPlacementsOfPiece(Fixture.piece1));
    }
}
