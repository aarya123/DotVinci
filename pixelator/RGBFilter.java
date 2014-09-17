package pixelator;

public class RGBFilter implements IFilter {
    public Pixel filterPixel(Pixel pixel) {
        return new Pixel(pixel.getX(), pixel.getY(), pixel.getRed(),
                pixel.getGreen(), pixel.getBlue());
    }
}
