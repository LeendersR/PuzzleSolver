package model;

import junit.framework.TestCase;
import model.Position;

/**
 * Unit test class for Position.
 */
public class PositionTest extends TestCase {

    /**
     * Tests if the constructor sets row/column correctly.
     */
    public void testConstructor() {
        Position p = new Position(0, 0);
        assertEquals("getRow() should be 0 after Position(0, 0)", 0, p.getRow());
        assertEquals("getHead() should be 0 after Position(0, 0)", 0, p.getColumn());
        p = new Position(50000, 50000);
        assertEquals("getRow() should be 50000 after Position(50000, 50000)", 50000, p.getRow());
        assertEquals("getHead() should be 50000 after Position(50000, 50000)", 50000, p.getColumn());
    }

    /**
     * Tests if setRow sets the row correctly.
     */
    public void testSetRow() {
        Position p = new Position(5, 5);
        p.setRow(10);
        assertEquals(10, p.getRow());
    }

    /**
     * Tests if setRow sets the row correctly.
     */
    public void testSetColumn() {
        Position p = new Position(5, 5);
        p.setColumn(10);
        assertEquals(10, p.getColumn());
    }

    /**
     * Tests if the constructor correctly throws an {@code IllegalArgumentException} when row and/or column are
     * negative.
     */
    public void testConstructorException() {
        try {
            new Position(-1, 0);
            fail("Position(-1, 0) should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            new Position(0, -1);
            fail("Position(0, -1) should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            new Position(-1, -1);
            fail("Position(-1, -1) should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        assertTrue(true);
    }

    /**
     * Tests if setRow correctly throws an {@code IllegalArgumentException} when row is negative
     */
    public void testSetRowException() {
        Position p = new Position(5, 5);
        try {
            p.setRow(-1);
            fail("setRow(-1) should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

    }

    /**
     * Tests if setHead correctly throws an {@code IllegalArgumentException} when column is negative
     */
    public void testSetColumnException() {
        Position p = new Position(5, 5);
        try {
            p.setColumn(-1);
            fail("setHead(-1) should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    static final class Fixture {
        static Position x = new Position(1, 3);
        static Position y = new Position(1, 3);
        static Position z = new Position(1, 3);
        static Position notx = new Position(3, 1);

        static Position small = new Position(1, 1);
        static Position big = new Position(4, 7);
        static Position middle = new Position(2, 3);
        static Position complementMiddle = new Position(3, 2);
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
     * Tests compareTo for bigger vs smaller objects
     */
    public void testCompareToSmaller() {
        assertEquals("Bigger row must return 1", 1, Fixture.big.compareTo(Fixture.small));
        assertEquals("Bigger row must return 1", 1, Fixture.big.compareTo(Fixture.middle));
        assertEquals("Bigger row must return 1", 1, Fixture.big.compareTo(Fixture.complementMiddle));
        assertEquals("Bigger row must return 1", 1, Fixture.complementMiddle.compareTo(Fixture.middle));
    }

    /**
     * Tests compareTo for smaller vs bigger objects
     */
    public void testCompareToBigger() {
        assertEquals("Smaller row must return -1", -1, Fixture.small.compareTo(Fixture.big));
        assertEquals("Smaller row must return -1", -1, Fixture.middle.compareTo(Fixture.big));
        assertEquals("Smaller row must return -1", -1, Fixture.complementMiddle.compareTo(Fixture.big));
        assertEquals("Smaller row must return -1", -1, Fixture.middle.compareTo(Fixture.complementMiddle));
    }

    /**
     * Tests compareTo should return 0 for equal objects
     */
    public void testCompareToEqual() {
        assertEquals("Equal objects must return 0", 0, Fixture.x.compareTo(Fixture.x));
        assertEquals("Equal objects must return 0", 0, Fixture.x.compareTo(Fixture.y));
        assertEquals("Equal objects must return 0", 0, Fixture.x.compareTo(Fixture.z));
    }
}
