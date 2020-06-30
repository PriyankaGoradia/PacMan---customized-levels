// details about the level 
// color of different components on the map and relation to sprite images

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Level {
	
	public int width;
	public int height;
	
	//tiles is an object of class Tiles extending Rectangle
	public Tile[][] tiles;
	
	public List<Apple> apples;
	public List<Enemy> enemies;
	
	public Level(String path) {
		
		apples = new ArrayList<>();
		enemies=new ArrayList<>();
		
		try {
			
			BufferedImage map= ImageIO.read(getClass().getResource(path));

			this.width=map.getWidth();
			this.height=map.getHeight();
			int[] pixels=new int[width*height];
			tiles=new Tile[width][height];
			map.getRGB(0, 0, width, height, pixels,0,width);
		
			for( int xx=0; xx<width; xx++) {
				for(int yy=0; yy<height;yy++) {
					int val=pixels[xx+(yy*width)];
				
					if(val==0xFF000000) {
						// Tile  :BLACK
						tiles[xx][yy]=new Tile(xx*32,yy*32);
					}
					else if(val==0xFF0000FF) {
						//PLAYER  :
						Game.player.x=xx*30;
						Game.player.y=yy*30;
					}
					else if(val==0xFFFF0000) {
						//ENEMY :RED
						enemies.add(new Enemy(xx*32,yy*32));
					}
					else {
						apples.add(new Apple(xx*32,yy*32));
						
					}
					
				}
			}
		
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
				
	}
	
	
	public void tick() {
		for(int i=0;i<enemies.size();i++) {
			enemies.get(i).tick();
		}
	}
	
	public void render(Graphics g) {
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				if(tiles[x][y]!=null)
				tiles[x][y].render(g);
			}
		}
		
		for(int i=0;i<apples.size();i++) {
			apples.get(i).render(g);
		}
		for(int i=0;i<enemies.size();i++) {
			enemies.get(i).render(g);
		}
	}
}


