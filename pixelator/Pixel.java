package pixelator;

import java.awt.*;

public class Pixel {

    private double x, y, red, green, blue;

    public Pixel(double x, double y, double red, double green, double blue) {
        this.x = x;
        this.y = y;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Color getColor() {
        return new Color((int) red, (int) green, (int) blue);
    }
}