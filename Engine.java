import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import pixelator.GrayScaleFilter;
import pixelator.IFilter;
import pixelator.NegativeFilter;
import pixelator.RGBFilter;
import pixelator.SepiaFilter;

import java.awt.Graphics;
import java.awt.Canvas;



public class Engine {

	private BufferedImage image;
	private Filter filter;
	private Timer timer;
	private long startTime;
	private long timeToRun;
	private EngineClient engineClient;
	final SepiaFilter SEPIA_FILTER=new SepiaFilter();
	final GrayScaleFilter GRAYSCALE_FILTER=new GrayScaleFilter();
	final NegativeFilter NEGATIVE_FILTER= new NegativeFilter();
	final RGBFilter RGB_FILTER=new RGBFilter();



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
	
	public IFilter getFilter()
	{
		
		if(Filter.SEPIA==filter)
		{
			return SEPIA_FILTER;
		}
		else if(Filter.GRAYSCALE==filter)
		{
			return GRAYSCALE_FILTER;
		}
		else if(Filter.NEGATIVE==filter)
		{
			return NEGATIVE_FILTER;
		}
		else{
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
		if(startTime == -1) {
			startTime = System.currentTimeMillis();
			long maxTimeToTake = 1000;
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

	public void updateOutput(Graphics g, Canvas canvas) {
		if (hasImage()) {
//			double x=getImage().getWidth()*Math.random();
//			double y=getImage().getHeight()*Math.random();
//			Pixel pixel=new Pixel(x,y);
//			Pixel pixel=getFilter().filterPixel(pixel);
//			g.drawImage(getImage(), 0, 0, canvas);
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

			if(getTimerRunLength() >= timeToRun) {
				System.out.println("timer end! " + getTimerRunLength());
				startTime = -1;
				cancel();
			}

		}

	}
}