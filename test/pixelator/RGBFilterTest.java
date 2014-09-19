package test.pixelator;

import pixelator.Pixel;
import pixelator.RGBFilter;

public class RGBFilterTest {

	public static void testAll() {
		RGBFilterTest test = new RGBFilterTest();
		test.filterPixelTest();
	}

	public void filterPixelTest() {
		Pixel pixel = new Pixel(0, 0, 127.5, 127.5, 127.5);
		pixel = new RGBFilter().filterPixel(pixel);
		if (pixel.getRed() == 127.5 && pixel.getGreen() == 127.5
				&& pixel.getBlue() == 127.5) {
			System.out.println("NegativeFilterTest 1 Passed!");
		} else {
			System.err.println("NegativeFilterTest 1 Failed!");
		}
	}
}
