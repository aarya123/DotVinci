package test.pixelator;

import java.awt.*;

import pixelator.Pixel;

public class PixelTest {

	public static void testAll() {
		PixelTest pixelTest = new PixelTest();
		pixelTest.getColorTest();
	}

	public void getColorTest() {
		Pixel pixel = new Pixel(0, 0, 127.5, 127.5, 127.5);
		if (pixel.getColor().equals(
				new Color((int) 127.5, (int) 127.5, (int) 127.5))) {
			System.out.println("PixelTest 1 Passed!");
		} else {
			System.err.println("PixelTest 1 Failed!");
		}
	}
}