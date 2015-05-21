package model;

import junit.framework.TestCase;

/**
 * Unit test class for Orientation
 */
public class OrientationTest extends TestCase {

    /**
     * Tests if after an add call the Position added is isPositionOccupied.
     */
    public void testAddPositionAndIsPositionOccupied() {
        Orientation orientation = new Orientation();
        orientation.addPosition(1, 1);
        assertEquals("after adding (1, 1); (1, 1) should exist", true, orientation.isPositionOccupied(1, 1));

        orientation.addPosition(3, 1);
        assertEquals("after adding (3, 1); (3, 1) should exist", true, orientation.isPositionOccupied(3, 1));

        assertEquals("(2, 1) is never added so shouldn't exist", false, orientation.isPositionOccupied(2, 1));
    }

    /**
     * Tests if addPosition generates an exception on negative input
     */
    public void testAddPositionException() {
        Orientation orientation = new Orientation();

        try {
            orientation.addPosition(-1, 0);
            fail("addPosition(-1, 0) should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            orientation.addPosition(0, -1);
            fail("addPosition(0, -1) should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            orientation.addPosition(-1, -1);
            fail("addPosition(-1, -1) should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        assertTrue(true);
    }

    /**
     * Tests if addPosition updates the bounding box correctly
     */
    public void testBoundingBoxUpdatedCorrectly() {
        Orientation orientation = new Orientation();
        assertEquals(0, orientation.getHeight());
        assertEquals(0, orientation.getWidth());

        orientation.addPosition(0, 0);
        assertEquals(1, orientation.getHeight());
        assertEquals(1, orientation.getWidth());

        orientation.addPosition(2, 3);
        assertEquals(3, orientation.getHeight());
        assertEquals(4, orientation.getWidth());

        orientation.addPosition(7, 5);
        assertEquals(8, orientation.getHeight());
        assertEquals(6, orientation.getWidth());
    }

    /**
     * Tests if isPositionOccupied generates false on negative input
     */
    public void testIsPositionOccupiedException() {
        Orientation orientation = new Orientation();
        assertEquals(false, orientation.isPositionOccupied(0, -1));
        assertEquals(false, orientation.isPositionOccupied(-1, 0));
        assertEquals(false, orientation.isPositionOccupied(-1, -1));
    }

    static final class Fixture {
        static Orientation x = new Orientation();
        static Orientation y = new Orientation();
        static Orientation z = new Orientation();
        static Orientation notx = new Orientation();

        static {
            x.addPosition(1, 3);
            x.addPosition(9, 3);
            x.addPosition(3, 7);

            y.addPosition(9, 3);
            y.addPosition(3, 7);
            y.addPosition(1, 3);

            z.addPosition(1, 3);
            z.addPosition(3, 7);
            z.addPosition(9, 3);

            notx.addPosition(3, 1);
            notx.addPosition(9, 3);
            notx.addPosition(3, 7);
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
     * Tests if the string representation is correct
     */
    public void testToString() {
        Orientation o = new Orientation();
        o.addPosition(0, 0);
        String expected = "Orientation{" +
                          "positions=[Position{row=0, column=0}]" +
                          ", width=1" +
                          ", height=1" +
                          '}';
        assertEquals(expected, o.toString());
    }
}
