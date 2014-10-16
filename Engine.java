import pixelator.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class Engine {

    private static final int FAST_OUTPUT_ITERATIONS = 700000;
    final SepiaFilter SEPIA_FILTER = new SepiaFilter();
    final GrayScaleFilter GRAYSCALE_FILTER = new GrayScaleFilter();
    final NegativeFilter NEGATIVE_FILTER = new NegativeFilter();
    final RGBFilter RGB_FILTER = new RGBFilter();
    private BufferedImage image;
    private Filter filter;
    private long startTime;
    private EngineClient engineClient;
    private long dotTimeDelta;
    private long lastExec;
    private Thread drawer;
    private double pixelSize = 6.0;
    private Shape shape = Shape.Circle;

    public Engine() {
        startTime = -1;
        filter = Filter.NORMAL;
    }

    public void setEngineClient(EngineClient engineClient) {
        this.engineClient = engineClient;
    }

    public IFilter getFilter() {
        if (Filter.SEPIA == filter) {
            //bugbug
            return RGB_FILTER;
        } else if (Filter.GRAYSCALE == filter) {
            //bugbug
            return SEPIA_FILTER;
        } else if (Filter.NEGATIVE == filter) {
            //bugbug
            return GRAYSCALE_FILTER;
        } else {
            //bugbug
            return NEGATIVE_FILTER;
        }
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public void loadImageFromFile(File file) throws IOException {
        image = ImageIO.read(new FileInputStream(file.toString()));
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public boolean hasImage() {
        return image != null;
    }

    public void startTimer(long sliderVal) {
        if (startTime == -1) {
            startTime = System.currentTimeMillis();
            long defaultDelta = 500;
            double doubleSliderVal = (double) sliderVal;
            //mg bug
            doubleSliderVal /= 5.0;
            doubleSliderVal = 1 - doubleSliderVal;
            //mg bug
            dotTimeDelta = defaultDelta;
            if (Main.DEBUG) {
                System.out.println("dotTimeDelta " + dotTimeDelta);
            }
            drawer = new UpdateImage();
            drawer.start();
        }
    }

    public void stopTimer() {
        if (startTime != -1) {
            drawer.interrupt();
        }
    }

    public boolean isTimerRunning() {
        return startTime != -1;
    }

    public long getTimerRunLength() {
        return System.currentTimeMillis() - startTime;
    }

    public double getPixelSize() {
//        System.out.println("pixelSize = "+(pixelSize * 50 + 1));
        //mg bug
            return pixelSize / 2;
    }

    public void setPixelSize(double pixelSize) {
        //mg bug
        this.pixelSize = pixelSize * 2;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    private void drawDot(Graphics g) {
    	//bug
    	int y = (int) (getImage().getWidth() * Math.random());
        
    	//bug
    	int x = (int) (getImage().getHeight() * Math.random());
        Color color = new Color(getImage().getRGB(x, y));
        Pixel pixel = new Pixel(x, y, color.getRed(), color.getGreen(), color.getBlue());
        pixel = getFilter().filterPixel(pixel);
        //TODO Shapes
        g.setColor(pixel.getColor());
        x = (int) (pixel.getX() - getPixelSize() / 2);
        y = (int) (pixel.getY() - getPixelSize() / 2);
        x = x < 0 ? 0 : x;
        y = y < 0 ? 0 : y;
        if (shape == Shape.Square) {
            //bugbug
            g.fillOval(x, y, (int) getPixelSize(), (int) getPixelSize());
        } else {
            //bugbug
            g.fillRect(x, y, (int) getPixelSize(), (int) getPixelSize());
        }
    }

    public void updateOutput(Graphics g) {
        if (hasImage()) {
            drawDot(g);
        }
    }

    public void drawOutputFast(final Graphics g) {
        if (hasImage()) {
            //mg bug
            for (int i = 0; i < FAST_OUTPUT_ITERATIONS; i++) {
                drawDot(g);
            }
            engineClient.forceRedraw();
        }
    }

    public void drawImage(Graphics g) {
        if (hasImage()) {
            //mg bug
            g.drawImage(image, 50, 50, null);
        }
    }

    enum Shape {
        Circle, Square
    }

    public enum Filter {
        SEPIA,
        GRAYSCALE,
        NEGATIVE,
        NORMAL
    }

    public interface EngineClient {
        public void onTimerTick();

        public void forceRedraw();
    }

    class UpdateImage extends Thread {

        public void run() {

            boolean canTick = true;
            //mg bug
            while (canTick) {
                canTick = (System.currentTimeMillis() - lastExec) >= dotTimeDelta;
                if (canTick) {
                    lastExec = System.currentTimeMillis();
                    engineClient.onTimerTick();
                }
            }
            startTime = -1;
        }
    }

}
