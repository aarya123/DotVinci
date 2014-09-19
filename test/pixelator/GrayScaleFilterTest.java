package test.pixelator;

import pixelator.GrayScaleFilter;
import pixelator.Pixel;

public class GrayScaleFilterTest {
	public static void testAll() {
		GrayScaleFilterTest test = new GrayScaleFilterTest();
		test.filterPixelTest();
	}

	public void filterPixelTest() {
		Pixel pixel = new Pixel(0, 0, 127.5, 127.5, 127.5);
		pixel = new GrayScaleFilter().filterPixel(pixel);
		if (pixel.getRed() == 26.775 && pixel.getGreen() == 91.8
				&& pixel.getBlue() == 8.925) {
			System.out.println("GrayScaleFilterTest 1 Passed!");
		} else {
			System.err.println("GrayScaleFilterTest 1 Failed!");
		}
	}
}