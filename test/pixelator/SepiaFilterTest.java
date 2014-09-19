package test.pixelator;

import pixelator.Pixel;
import pixelator.SepiaFilter;

public class SepiaFilterTest {

	public static void testAll() {
		SepiaFilterTest test = new SepiaFilterTest();
		test.filterPixelTest();
	}

	public void filterPixelTest() {
		Pixel pixel = new Pixel(0, 0, 127.5, 127.5, 127.5);
		pixel = new SepiaFilter().filterPixel(pixel);
		if ((int) pixel.getRed() == 172 && (int) pixel.getGreen() == 153
				&&  (int)pixel.getBlue() == 119) {
			System.out.println("SepiaFilterTest 1 Passed!");
		} else {
			System.err.println("SepiaFilterTest 1 Failed!");
		}
	}
}