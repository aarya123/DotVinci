package pixelator;

public class GrayScaleFilter implements IFilter {

    static final double RED_MULTIPLE = 0.21;
    static final double GREEN_MULTIPLE = 0.72;
    static final double BLUE_MULTIPLE = 0.07;

    public Pixel filterPixel(Pixel pixel) {
        double newRed = RED_MULTIPLE * pixel.getRed();			/*Defect 003*/
        double newGreen = GREEN_MULTIPLE * pixel.getGreen();
        double newBlue = BLUE_MULTIPLE * pixel.getBlue();
        
        /*double newRed = (pixel.getRed() + pixel.getBlue()+pixel.getGreen())/3;
        double newGreen = newRed;
        double newBlue = newRed;*/
        return new Pixel(pixel.getX(), pixel.getY(), newRed, newGreen, newBlue);
    }
}