import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Engine {

	private BufferedImage image;

	public Engine() {

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
}