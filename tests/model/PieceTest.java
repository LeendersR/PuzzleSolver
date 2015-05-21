package model;

import junit.framework.TestCase;
import model.Orientation;
import model.Piece;

import java.awt.*;

/**
 * Unit test class for Piece
 */
public class PieceTest extends TestCase {

    /**
     * Tests if the constructor correctly throws an {@code IllegalArgumentException} when multiplicity is negative
     */
    public void testConstructorException() {

        try {
            Piece p = new Piece("1", Color.BLACK, 0);
            fail("Constructor should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

    }

    /**
     * Tests if addOrientation returns true when all arguments are valid
     */
    public void testAddOrientation() {
        Piece p = new Piece("1", Color.BLACK, 1);
        Orientation o = new Orientation();
        o.addPosition(0, 0);
        assertTrue("Add operation should be successful", p.addOrientation(o));
    }

    /**
     * Tests if addOrientation throws an exception on empty an orientation
     */
    public void testAddOrientationOrientationEmpty() {
        try {
            new Piece("1", Color.BLACK, 1).addOrientation(new Orientation());
            fail("addOrientation on empty orientation test fails");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests if setMultiplicity throws an exception when {@code multiplicity <= 0} and the normal working.
     */
    public void testGetSetMultiplicity() {
        Piece p = new Piece("1", Color.BLACK, 5);
        assertEquals("Multiplicity set correctly test fails", 5, p.getMultiplicity());
        p.setMultiplicity(15);
        assertEquals("Multiplicity changed correctly test fails", 15, p.getMultiplicity());
        
        try {
            p.setMultiplicity(-1);
            fail("setMultiplicity(-1) should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        assertTrue(true);

    }

    /**
     * Tests getter and setter of name
     */
    public void testGetSetName() {
        Piece p = new Piece("1", Color.BLACK, 5);
        assertEquals("Name set correctly test fails", "1", p.getName());
        p.setName("2");
        assertEquals("Name changed correctly test fails", "2", p.getName());
    }

    /**
     * Tests getter and setter of name
     */
    public void testGetSetColor() {
        Piece p = new Piece("1", Color.BLACK, 5);
        assertEquals("Color set correctly test fails", Color.BLACK, p.getColor());
        p.setColor(Color.BLUE);
        assertEquals("Color changed correctly test fails", Color.BLUE, p.getColor());
    }

    static final class Fixture {
        static Piece x = new Piece("1", Color.BLACK, 5);
        static Piece y = new Piece("1", Color.BLACK, 5);
        static Piece z = new Piece("1", Color.BLACK, 5);
        static Piece notx = new Piece("1", Color.BLUE, 5);
        static Piece noty = new Piece("2", Color.BLACK, 5);
        static {
            Orientation o = new Orientation();
            o.addPosition(1, 1);
            x.addOrientation(o);
            y.addOrientation(o);
            z.addOrientation(o);
            Orientation o2 = new Orientation();
            o2.addPosition(0, 0);
            notx.addOrientation(o2);
            noty.addOrientation(o2);
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
        assertFalse(Fixture.noty.equals(Fixture.y));
        assertFalse(Fixture.noty.equals(Fixture.y));
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
     * Tests compareTo for bigger vs smaller objects
     */
    public void testCompareToSmaller() {
        assertEquals("Bigger name must return 1", 1, Fixture.noty.compareTo(Fixture.x));
    }

    /**
     * Tests compareTo for smaller vs bigger objects
     */
    public void testCompareToBigger() {
        assertEquals("Smaller row must return -1", -1, Fixture.x.compareTo(Fixture.noty));
    }

    /**
     * Tests compareTo should return 0 for equal objects
     */
    public void testCompareToEqual() {
        assertEquals("Equal names must return 0", 0, Fixture.x.compareTo(Fixture.x));
    }
}
