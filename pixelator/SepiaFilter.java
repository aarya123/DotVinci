package pixelator;

public class SepiaFilter implements IFilter {

	static final double[] RED_MULTIPLES = { 0.393, 0.769, 0.189 };
	static final double[] GREEN_MULTIPLES = { 0.349, 0.686, 0.168 };
	static final double[] BLUE_MULTIPLES = { 0.272, 0.534, 0.131 };

	public Pixel filterPixel(Pixel pixel) {
		double red = pixel.getRed(), green = pixel.getGreen(), blue = pixel
				.getBlue();
		double newRed = RED_MULTIPLES[0] * red + RED_MULTIPLES[1] * green
				+ RED_MULTIPLES[2] * blue;
		double newGreen = GREEN_MULTIPLES[0] * red + GREEN_MULTIPLES[1] * green
				+ GREEN_MULTIPLES[2] * blue;
		double newBlue = BLUE_MULTIPLES[0] * red + BLUE_MULTIPLES[1] * green
				+ BLUE_MULTIPLES[2] * blue;
		newRed = newRed > 255 ? 255 : newRed;
		newGreen = newGreen > 255 ? 255 : newGreen;
		newBlue = newBlue > 255 ? 255 : newBlue;
		return new Pixel(pixel.getX(), pixel.getY(), newRed, newGreen, newBlue);
	}
}