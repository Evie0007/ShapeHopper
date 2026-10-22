package org.example;

import javafx.scene.shape.Shape;
import javafx.scene.text.Font;

/** Shared look used by every screen. */
public class Theme {

    /** Picks a handwritten font: Segoe Print on Windows, Chalkboard SE on Mac. */
    public static String handwrittenFont() {
        for (String font : new String[] {"Segoe Print", "Chalkboard SE", "Comic Sans MS"}) {
            if (Font.getFamilies().contains(font)) {
                return font;
            }
        }
        return "System";
    }

    /** Gives a shape the black-outline look from style.css. */
    public static Shape sketch(Shape shape) {
        shape.getStyleClass().add("sketch");
        return shape;
    }
}