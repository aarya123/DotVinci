import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;
import java.awt.Graphics;
import java.awt.Canvas;



public class Engine {

	private BufferedImage image;
	private Filter filter;
	private Timer timer;
	private long startTime;
	private long timeToRun;
	private EngineClient engineClient;



	public interface EngineClient {
		public void onTimerTick();
	}

	public Engine() {
		timer = new Timer();
		startTime = -1;
		timeToRun = -1;
	}

	public enum Filter {
		SEPIA,
		GRAYSCALE
	}

	public void setEngineClient(EngineClient engineClient) {
		this.engineClient = engineClient;
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

	public void updateOutput(Graphics g, Canvas canvas) {
		if (hasImage()) {
			g.drawImage(getImage(), 0, 0, canvas);
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

			if(System.currentTimeMillis() - startTime >= timeToRun) {
				System.out.println("timer end! " + (System.currentTimeMillis() - startTime));
				startTime = -1;
				cancel();
			}

		}

	}
}