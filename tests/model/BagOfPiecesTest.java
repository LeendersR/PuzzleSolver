package model;

import junit.framework.TestCase;
import model.BagOfPieces;
import model.Piece;

import java.awt.*;
import java.util.ArrayList;

/**
 * Unit tests the implementation of BagOfPieces
 *
 * @author Robert Leenders
 * @see BagOfPieces
 */
public class BagOfPiecesTest extends TestCase {

    public void setUp() {
        Fixture.reset();
    }


    static final class Fixture {
        static BagOfPieces x;
        static BagOfPieces y;
        static BagOfPieces z;
        static BagOfPieces notx;
        static Piece piece = new Piece("A", Color.BLACK, 5);
        static Piece piece2 = new Piece("B", Color.BLUE, 2);

        static {
            reset();
        }

        static void reset() {

            x = new BagOfPieces();
            y = new BagOfPieces();
            z = new BagOfPieces();
            notx = new BagOfPieces();

            x.add(piece);
            x.add(piece2);

            y.add(piece);
            y.add(piece2);

            z.add(piece);
            z.add(piece2);

            notx.add(new Piece("B", Color.BLACK, 5));
            notx.add(new Piece("A", Color.BLUE, 2));
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
     * produced. We do not implement this test case because the hashCode function of Set adds up all hashCodes.
     *
     * For x and notx this gives the same hashCode, while they are not equal, this is less optimal but it is the inner
     * working of Set.
     */


    /**
     * Tests whether containsPoint works correctly.
     */
    public void testContains() {
        assertTrue("containsPoint test fails", Fixture.x.contains(Fixture.piece));
        assertFalse("containsPoint test fails", Fixture.notx.contains(Fixture.piece));
    }

    /**
     * Tests whether remove works correctly.
     */
    public void testRemove() {
        assertTrue("remove test fails, piece isn't present", Fixture.x.contains(Fixture.piece));
        Fixture.x.remove(Fixture.piece);
        assertFalse("remove test fails, piece is present", Fixture.x.contains(Fixture.piece));
    }

    /**
     * Tests whether add works correctly.
     */
    public void testAdd() {
        assertFalse("Piece already in collection should return false", Fixture.x.add(Fixture.piece));
        assertTrue("containsPoint test fails", Fixture.x.contains(Fixture.piece));
    }

    /**
     * Tests whether you can iterate over the bag.
     * The items are not returned in a particular order.
     */
    public void testIterator() {
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        for (Piece p : Fixture.x) {
            pieces.add(p);
        }

        assertTrue("iterator test fails", pieces.contains(Fixture.piece));
        assertTrue("iterator test fails", pieces.contains(Fixture.piece2));
    }
}
