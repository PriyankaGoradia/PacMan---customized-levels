import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {

	private BufferedImage sheet;
	
	public SpriteSheet(String path) {
	
		try {
			sheet=ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("failed to load image !!");
		}
	
	}
	
	public BufferedImage getSprite() {
		return sheet;
	}

	
}
