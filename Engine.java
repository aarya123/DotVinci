import pixelator.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class Engine {

    private BufferedImage image;
    private Filter filter;
    private Timer timer;
    private long startTime;
    private long timeToRun;
    private EngineClient engineClient;
    final SepiaFilter SEPIA_FILTER = new SepiaFilter();
    final GrayScaleFilter GRAYSCALE_FILTER = new GrayScaleFilter();
    final NegativeFilter NEGATIVE_FILTER = new NegativeFilter();
    final RGBFilter RGB_FILTER = new RGBFilter();


    public interface EngineClient {
        public void onTimerTick();
    }

    public Engine() {
        timer = new Timer();
        startTime = -1;
        timeToRun = -1;
        filter = Filter.NORMAL;
    }

    public enum Filter {
        SEPIA,
        GRAYSCALE,
        NEGATIVE,
        NORMAL
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

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void loadImageFromFile(File file) throws IOException {
        image = ImageIO.read(new FileInputStream(file.toString()));
    }

    public BufferedImage getImage() {
        return image;
    }

    public boolean hasImage() {
        return image != null;
    }

    public void startTimer(long sliderVal) {
        if (startTime == -1) {
            startTime = System.currentTimeMillis();
            long maxTimeToTake = 1000000;
            sliderVal *= 10;
            timeToRun = maxTimeToTake - sliderVal;
            timer.scheduleAtFixedRate(new UpdateImage(), 0, 10);
        }
    }

    public boolean isTimerRunning() {
        return startTime != -1;
    }

    public long getTimerRunLength() {
        return System.currentTimeMillis() - startTime;
    }

    public void updateOutput(Graphics g, JPanel canvas) {
        if (hasImage()) {
            int x = (int) (getImage().getWidth() * Math.random());
            int y = (int) (getImage().getHeight() * Math.random());
            Color color = new Color(getImage().getRGB(x, y));
            Pixel pixel = new Pixel(x, y, color.getRed(), color.getGreen(), color.getBlue());
            pixel = getFilter().filterPixel(pixel);
            //TODO Shapes
            //TODO Radius
            g.setColor(pixel.getColor());
            x = (int) (pixel.getX() - 5);
            y = (int) (pixel.getY() - 5);
            x = x < 0 ? 0 : x;
            y = y < 0 ? 0 : y;
            g.fillOval(x, y, 10, 10);
//			g.drawImage(getImage(), 0, 0, canvas);
        }
    }

    public void drawImage(Graphics g, JPanel canvas) {
        if(hasImage()) {
            g.drawImage(image, 0, 0, null);
        }
    }

    class UpdateImage extends TimerTask {

        @Override
        public void run() {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    engineClient.onTimerTick();
                }

            });

            if (getTimerRunLength() >= timeToRun) {
                System.out.println("timer end! " + getTimerRunLength());
                startTime = -1;
                cancel();
            }

        }

    }
}