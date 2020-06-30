import java.awt.Graphics;
import java.awt.Rectangle;

public class Player extends Rectangle {
	
	private static final long serialVersionUID = 1L;

	public boolean left,right,up,down;
	private int speed=4;
	
	private int time=0, targetTime=10;
	public int imageIndex=0;
	
	private String lastDir="r";
	
	public Player(int x, int y) {
		setBounds(x,y,30,30); 
	}
	
	public void tick() {
		if(right && canMove(x+speed,y)) {
			x+=speed;
			lastDir="r";
		}
		if(left && canMove(x-speed,y)) {
			x-=speed;
			lastDir="l";
		}
		if(up && canMove(x,y-speed)) {
			y-=speed;
			lastDir="u";
		}
		if(down && canMove(x,y+speed)) {
			y+=speed;
			lastDir="d";
		}
	
		Level level = Game.level;
		
		//FOR EATING APPLES
		for(int i=0;i<level.apples.size();i++)
		{
			if(this.intersects(level.apples.get(i))) {
				level.apples.remove(i);
				break;
			}
		}
		
		//EXIT when all apples over : LEVEL UP !
		if(level.apples.size()==0) {
			Game.STATE=Game.WIN;
			return;
			
		}
		for(int i=0;i<Game.level.enemies.size();i++) {
			Enemy en = Game.level.enemies.get(i);
			if(en.intersects(this)) {
				//Menu system
				Game.STATE=Game.GAME_OVER;
				return;
			}
		}
		
		time++;
		
		if(time==targetTime) {
			time=0;
			imageIndex++;
		}
	}
	
	private boolean canMove(int nextx, int nexty) {
		
		Rectangle bounds = new Rectangle(nextx,nexty,width,height);
		Level level=Game.level;
		
		for(int xx=0;xx<level.tiles.length;xx++) {
			for(int yy=0;yy<level.tiles[0].length;yy++) {
				if(level.tiles[xx][yy]!=null) {
						if(bounds.intersects(level.tiles[xx][yy])) {
							// collision detected; we can't move
							return false;
						}
				}
			}
		}
		
		return true;
		
	}
	
	public void render(Graphics g) {
		//g.setColor(Color.yellow);
		g.fillRect(x, y,width,height);
		//SpriteSheet sheet = Game.pac;
		if(lastDir=="r")g.drawImage(Texture.player[imageIndex%2],x,y,width,height,null); 
			// using "%2" because image index can only be 0 or 1
		else if(lastDir=="l") g.drawImage(Texture.player[imageIndex%2],x+32,y,-width,height,null);
		else if(lastDir=="u") g.drawImage(Texture.player[imageIndex%2],x,y,width,height,null);
		else if(lastDir=="d") g.drawImage(Texture.player[imageIndex%2],x,y,width,height,null);
			
	}
}
