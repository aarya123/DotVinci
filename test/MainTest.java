package test;

import test.pixelator.GrayScaleFilterTest;
import test.pixelator.NegativeFilterTest;
import test.pixelator.PixelTest;
import test.pixelator.RGBFilterTest;
import test.pixelator.SepiaFilterTest;

public class MainTest {

	public static void main(String[] args) {
		pixelatorTests();
	}
	public static void pixelatorTests(){
		PixelTest.testAll();
		GrayScaleFilterTest.testAll();
		NegativeFilterTest.testAll();
		RGBFilterTest.testAll();
		SepiaFilterTest.testAll();
	}
}