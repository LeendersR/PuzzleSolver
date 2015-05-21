package model;

import java.awt.*;
import java.util.*;

/**
 * Class for a piece
 *
 * @author Robert Leenders
 * @see Orientation
 * @see Placement
 * @see BagOfPieces
 */
public class Piece implements Comparable<Piece> {
    /**
     * Name of this piece
     */
    private String name;
    /**
     * Color of this piece
     */
    private Color color;
    /**
     * Number of times this piece is in the bag
     */
    private int multiplicity;
    /**
     * All the possible orientations of this piece
     */
    private Set<Orientation> orientations;

    /**
     * Constructs a new piece object.
     *
     * @param name         the name
     * @param color        the color
     * @param multiplicity number of pieces of this type
     * @throws IllegalArgumentException if {@code multiplicity <= 0}
     * @modifies name, color, multiplicity and orientations
     * @post sets name, color, multiplicity and orientations; if {@code multiplicity <= 0}
     */
    public Piece(String name, Color color, int multiplicity) throws IllegalArgumentException {
        setMultiplicity(multiplicity);
        this.name = name;
        this.color = color;
        orientations = new HashSet<Orientation>();
    }

    /**
     * Compares this {@code Piece} to the specified object.
     *
     * The result is {@code true} if and only if the argument is not {@code null} and is a {@code Piece} object that
     * represents the same color, name and orientations as this {@code Piece}.
     *
     * @param o the object to compare this {@code Piece} against
     * @return {@code true} if the {@code Piece} are equal; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Piece piece = (Piece) o;

        if (color != null ? !color.equals(piece.color) : piece.color != null) return false;
        if (name != null ? !name.equals(piece.name) : piece.name != null) return false;
        if (orientations != null ? !orientations.equals(piece.orientations) : piece.orientations != null) return false;

        return true;
    }

    /**
     * Returns a hash code for this {@code Piece}.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (orientations != null ? orientations.hashCode() : 0);
        return result;
    }

    /**
     * Adds an orientation to this piece
     *
     * @param o the orientation
     * @return {@code true} if the new orientation is added; false otherwise
     * @throws IllegalArgumentException if orientation is empty
     * @post orientation is added to the orientations of this piece; if the orientation is not empty
     */
    public boolean addOrientation(Orientation o) throws IllegalArgumentException {
        if (o.isEmpty())
            throw new IllegalArgumentException("orientation should have at least one occupied position.");
        return orientations.add(o);
    }

    /**
     * Compares this object with the specified object for order.  Returns a negative integer, zero, or a positive
     * integer as this object is less than, equal to, or greater than the specified object.
     *
     * This function just compares on name. Since you cannot compare two colors or orientations. So {@code
     * a.compareTo(b) == 0} does not imply {@code a.equals(b) == true}.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
     *         the specified object.
     */
    public int compareTo(Piece o) {
        return name.compareTo(o.getName());
    }

    /**
     * Returns whether the orientations of this piece contain the given orientation
     *
     * @param orientation the to be searched orientation
     * @return {@code true} if the orientation is found; false otherwise
     * @see HashSet#contains(Object)
     */
    public boolean containsOrientation(Orientation orientation) {
        return orientations.contains(orientation);
    }

    /**
     * Returns the color of this piece
     *
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of this piece
     *
     * @param color the color
     * @post color is set
     */
    void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns the multiplicity of this piece
     *
     * @return the multiplicity
     */
    public int getMultiplicity() {
        return multiplicity;
    }

    /**
     * Sets the multiplicity
     *
     * @param multiplicity the multiplicity
     * @throws IllegalArgumentException if {@code multiplicity <= 0}
     * @modifies multiplicity
     * @post sets multiplicity to the new value; if {@code multiplicity > 0}
     */
    void setMultiplicity(int multiplicity) throws IllegalArgumentException {
        if (multiplicity <= 0)
            throw new IllegalArgumentException("multiplicity is below or equal to 0; multiplicity:" + multiplicity);
        this.multiplicity = multiplicity;
    }

    /**
     * Returns the name of this piece
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this piece
     *
     * @param name the name
     * @post name is set
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a iterator for the orientations of this piece
     *
     * @return iterator for orientations of this piece
     */
    public Iterator<Orientation> orientationIterator() {
        return orientations.iterator();
    }

    /**
     * Returns a list-iterator for the orientations of this piece
     *
     * @return list-iterator for orientations of this piece
     */
    public ListIterator<Orientation> orientationListIterator() {
        return new ArrayList<Orientation>(orientations).listIterator();
    }
}
