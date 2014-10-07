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
            return SEPIA_FILTER;
        } else if (Filter.GRAYSCALE == filter) {
            return GRAYSCALE_FILTER;
        } else if (Filter.NEGATIVE == filter) {
            return NEGATIVE_FILTER;
        } else {
            return RGB_FILTER;
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
            doubleSliderVal /= 100.0;
            doubleSliderVal = 1 - doubleSliderVal;
            dotTimeDelta = (long) (defaultDelta * doubleSliderVal);
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
        return pixelSize;
    }

    public void setPixelSize(double pixelSize) {
        this.pixelSize = pixelSize;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    private void drawDot(Graphics g) {
        int x = (int) (getImage().getWidth() * Math.random());
        int y = (int) (getImage().getHeight() * Math.random());
        Color color = new Color(getImage().getRGB(x, y));
        Pixel pixel = new Pixel(x, y, color.getRed(), color.getGreen(), color.getBlue());
        pixel = getFilter().filterPixel(pixel);
        //TODO Shapes
        g.setColor(pixel.getColor());
        x = (int) (pixel.getX() - getPixelSize() / 2);
        y = (int) (pixel.getY() - getPixelSize() / 2);
        x = x < 0 ? 0 : x;
        y = y < 0 ? 0 : y;
        if (shape == Shape.Triangle) {

        } else if (shape == Shape.Square) {
            g.fillRect(x, y, (int) getPixelSize(), (int) getPixelSize());
        } else {
            g.fillOval(x, y, (int) getPixelSize(), (int) getPixelSize());
        }
    }

    public void updateOutput(Graphics g) {
        if (hasImage()) {
            drawDot(g);
            //			g.drawImage(getImage(), 0, 0, canvas);
        }
    }

    public void drawOutputFast(final Graphics g) {
        if (hasImage()) {
            new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < FAST_OUTPUT_ITERATIONS; i++) {
                        drawDot(g);
                    }
                    engineClient.forceRedraw();

                }
            }.start();
        }
    }

    public void drawImage(Graphics g) {
        if (hasImage()) {
            g.drawImage(image, 0, 0, null);
        }
    }

    enum Shape {
        Circle, Square, Triangle
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
        @Override
        public void run() {

            while (!isInterrupted()) {
                boolean canTick = (System.currentTimeMillis() - lastExec) >= dotTimeDelta;
                if (canTick) {
                    lastExec = System.currentTimeMillis();
                    engineClient.onTimerTick();
                }
            }
            startTime = -1;
        }
    }

}
