package org.example;

import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;


public class ShapeLibrary {

    /** All shape names, in gallery order. */
    public static final List<String> NAMES = List.of(
            "triangle", "circle", "square", "star", "trapezium", "oval", "rhombus",
            "hexagon", "rectangle", "pentagon", "cross", "ring", "semicircle", "parallelogram");

    /** Returns the named shape, centered at (0, 0), about 80px across. */
    public static Node draw(String name) {
        return switch (name) {
            case "triangle"      -> Theme.sketch(new Polygon(0, -40, 42, 32, -42, 32));
            case "circle"        -> Theme.sketch(new Circle(38));
            case "square"        -> Theme.sketch(new Rectangle(-35, -35, 70, 70));
            case "star"          -> Theme.sketch(star(42, 18));
            case "trapezium"     -> Theme.sketch(new Polygon(-24, -28, 24, -28, 44, 28, -44, 28));
            case "oval"          -> Theme.sketch(new Ellipse(30, 40));
            case "rhombus"       -> Theme.sketch(new Polygon(0, -44, 32, 0, 0, 44, -32, 0));
            case "hexagon"       -> Theme.sketch(regularPolygon(6, 40));
            case "rectangle"     -> Theme.sketch(new Rectangle(-48, -28, 96, 56));
            case "pentagon"      -> Theme.sketch(regularPolygon(5, 42));
            case "cross"         -> Theme.sketch(new Polygon(
                                        -12, -40, 12, -40, 12, -12, 40, -12, 40, 12, 12, 12,
                                        12, 40, -12, 40, -12, 12, -40, 12, -40, -12, -12, -12));
            case "ring"          -> new Group(Theme.sketch(new Circle(38)), Theme.sketch(new Circle(18)));
            case "semicircle"    -> semicircle();
            case "parallelogram" -> Theme.sketch(new Polygon(-22, -28, 50, -28, 22, 28, -50, 28));
            default -> throw new IllegalArgumentException("Unknown shape: " + name);
        };
    }

    /** Equal sides, one corner pointing up (pentagon, hexagon). */
    private static Polygon regularPolygon(int sides, double radius) {
        Polygon polygon = new Polygon();
        for (int i = 0; i < sides; i++) {
            double angle = Math.toRadians(i * 360.0 / sides - 90);
            polygon.getPoints().addAll(radius * Math.cos(angle), radius * Math.sin(angle));
        }
        return polygon;
    }

    /** 10 corners alternating between far from and close to the center. */
    private static Polygon star(double outer, double inner) {
        Polygon star = new Polygon();
        for (int i = 0; i < 10; i++) {
            double radius = (i % 2 == 0) ? outer : inner;
            double angle = Math.toRadians(i * 36 - 90);
            star.getPoints().addAll(radius * Math.cos(angle), radius * Math.sin(angle));
        }
        return star;
    }

    /** Top half of a circle, closed with a straight bottom edge. */
    private static Node semicircle() {
        Arc arc = new Arc(0, 18, 44, 44, 0, 180);
        arc.setType(ArcType.CHORD);
        return Theme.sketch(arc);
    }
}