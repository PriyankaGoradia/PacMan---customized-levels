import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

// contains different pac and ghost images for animation

public class Texture {

	public static BufferedImage[] player;
	public BufferedImage[] spritesheet;
	public static int i=-1;
	
	public Texture() {
		try {
			spritesheet=new BufferedImage[5];
			spritesheet[0]=ImageIO.read(getClass().getResource("/sprites/pac/pacman_0_0.png"));
			spritesheet[1]=ImageIO.read(getClass().getResource("/sprites/pac/pacman_0_1.png"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		player=new BufferedImage[2];
		
		player[0]=getSprite();
		player[1]=getSprite();
	}
	
	public BufferedImage getSprite(){
		return spritesheet[++i];
	}
}
