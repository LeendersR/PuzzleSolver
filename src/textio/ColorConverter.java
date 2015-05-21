package textio;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Scanner;

/**
 * Library with static methods to read colors from text streams.
 *
 * @author Robert Leenders
 */
public class ColorConverter {

    /**
     * Converts the give name to a {@Color} object, if it is defined as constant in {@code java.awt.Color}.
     *
     * @param name the name to convert
     * @return color with given name, if known, else returns {@code null}
     */
    public static Color fromName(String name) {
        try {
            Class clazz = Class.forName("java.awt.Color");
            Field f = clazz.getField(name);
            return (Color) f.get(null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Reads a token from the {@code scanner} and returns the corresponding {@code Color} object if it is defined as
     * constant in {@code java.awt.Color}.
     *
     * @param scanner the scanner to read from
     * @return next color from {@code scanner}, if known, else returns {@code null}
     */
    public static Color readColor(Scanner scanner) {
        return fromName(scanner.next());
    }

}
