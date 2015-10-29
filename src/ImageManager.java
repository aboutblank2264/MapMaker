import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageManager {
	private static BufferedImage WHITE;
	private static BufferedImage BLACK;
	
	public ImageManager() {
		try {
			WHITE = ImageIO.read(new File("../images/white.png"));
			BLACK = ImageIO.read(new File("../images/black.png"));
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
