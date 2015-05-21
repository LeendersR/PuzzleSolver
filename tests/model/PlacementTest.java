package model;

import junit.framework.TestCase;
import model.Orientation;
import model.Piece;
import model.Placement;
import model.Position;

import java.awt.*;

/**
 * Unit test class for Placement
 *
 * @author Robert Leenders
 */
public class PlacementTest extends TestCase {

    static final class Fixture {
        static Piece p = new Piece("1", Color.BLACK, 5);
        static Orientation o1 = new Orientation();
        static Orientation o2 = new Orientation();
        static Position pos = new Position(0, 0);

        static Placement x;
        static Placement y;
        static Placement z;
        static Placement notx;

        static {
            o1.addPosition(0, 0);
            o1.addPosition(1, 0);
            p.addOrientation(o1);

            o2.addPosition(0, 0);
            o2.addPosition(0, 1);

            x = new Placement(pos, o1, p);
            y = new Placement(pos, o1, p);
            z = new Placement(pos, o1, p);
            notx = new Placement(new Position(0, 1), o1, p);
        }
    }

    /**
     * Tests if the constructor correctly sets all variables
     */
    public void testConstructor() {
        Placement placement = new Placement(Fixture.pos, Fixture.o1, Fixture.p);
        assertEquals("anchorPosition set correctly test fails", Fixture.pos, placement.getAnchorPosition());
        assertEquals("orientation set correctly test fails", Fixture.o1, placement.getOrientation());
        assertEquals("piece set correctly test fails", Fixture.p, placement.getPiece());
    }

    /**
     * Tests if the constructor raises an exception when the orientation given is not available to the piece given
     */
    public void testConstructorInvalidOrientation() {
        try {
            Placement placement = new Placement(Fixture.pos, Fixture.o2, Fixture.p);
            fail("given orientation which is not in piece orientations test fails");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
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
}
