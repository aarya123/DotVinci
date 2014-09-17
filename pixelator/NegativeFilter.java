package pixelator;

public class NegativeFilter implements IFilter {

	static final double MAX_PIXEL = 255.0;

	public Pixel filterPixel(Pixel pixel) {
		double newRed = MAX_PIXEL - pixel.getRed();
		double newGreen = MAX_PIXEL - pixel.getGreen();
		double newBlue = MAX_PIXEL - pixel.getBlue();
		return new Pixel(pixel.getX(), pixel.getY(), newRed, newGreen, newBlue);
	}

}
