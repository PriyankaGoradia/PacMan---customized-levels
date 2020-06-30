import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable,KeyListener {

	
	private static final long serialVersionUID = 1L;
	private boolean isRunning=false;
	public static final int WIDTH=640,HEIGHT=480;
	public static final String TITLE = "Pac-Man";
	public static int choice = 0;
	private Thread thread;
	
	public static Player player;
	//object of level class is a static member of Game
	public static Level level;
	public static SpriteSheet pac;
	public static SpriteSheet enemy;
	public static final int START_SCREEN=0,GAME=1,GAME_OVER=2,WIN=3;
	public static int STATE =-1;
	public int ch=0;
	
	//for animated start, game and gameover screens
	private int time=0;
	private int targetFrames=50;
	private boolean showText=true;
	
	public Game() {
		
		Dimension dimension = new Dimension(Game.WIDTH, Game.HEIGHT);
		setPreferredSize(dimension);
		setMinimumSize(dimension);
		setMaximumSize(dimension);
		new Texture();
		
		addKeyListener(this);
		STATE=START_SCREEN;
		
		player=new Player(Game.WIDTH, Game.HEIGHT);
		enemy= new SpriteSheet("/sprites/enemies/0/ghost_0_0.png");

	}
	
	// start method is synchronized so that we don't have problems
	public synchronized void start() {
		if(isRunning) return;
		isRunning = true;
		thread =new Thread(this);
		thread.start();
		//goes to the main game loop
	}
	
	public synchronized void stop(){
		if(!isRunning) return;
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
	}
	
	
	private void tick(){
		
		if(STATE==GAME) {
			player.tick();
			level.tick();
		}
		
		else if(STATE==START_SCREEN) {
			time++;
			if (time==targetFrames){
				time=0;
				if(showText) {
					showText=false;
				}
				else {
					showText=true;
				}
			}
			if(ch!=0) {
				player=new Player(Game.WIDTH/2, Game.HEIGHT/2);
				if(ch==1) {
					level=new Level("/map/map.png");
				}
				else if(ch==2) {
					level=new Level("/map/map1.png");
				}
				STATE=GAME;
				ch=0;
			}
		}
		
		else if(STATE==GAME_OVER) {
			time++;
			if (time==targetFrames){
				time=0;
				if(showText) {
					showText=false;
				}
				else {
					showText=true;
				}
			}
			if(ch!=0) {
				player=new Player(Game.WIDTH/2, Game.HEIGHT/2);
				if(ch==1) {
					level=new Level("/map/map.png");
				}
				else if(ch==2) {
					level=new Level("/map/map1.png");
				}
				STATE=GAME;	
				ch=0;
			}
		}
	
		else if(STATE==WIN) {
			time++;
			if (time==targetFrames){
				time=0;
				if(showText) {
					showText=false;
				}
				else {
					showText=true;
				}
			}
			if(ch!=0) {
				player=new Player(Game.WIDTH/2, Game.HEIGHT/2);
				if(ch==1) {
					level=new Level("/map/map.png");
				}
				else if(ch==2) {
					level=new Level("/map/map1.png");
				}
				STATE=GAME;	
				ch=0;
			}
			
		}
		
	}
	
	
	private void render() {
		
		BufferStrategy bs= getBufferStrategy();
		
		if(bs==null) {
			// 3 means triple buffer
			createBufferStrategy(3);
			return;
		}
		
		Graphics g=bs.getDrawGraphics();
		
		g.setColor(Color.black);
		// position x=0 and y=0
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		if(STATE==GAME){
			player.render(g);
			level.render(g);
		}
		
		//MENU
		else if(STATE==START_SCREEN) {
			int boxWidth=500;
			int boxHeight=200;
			int xx=Game.WIDTH/2-boxWidth/2;
			int yy=Game.HEIGHT/2-boxHeight/2;
			
			g.setColor(new Color(0,0,150));
			g.fillRect(xx, yy, boxWidth, boxHeight);
			g.setColor(Color.white);
			g.setFont(new Font(Font.DIALOG,Font.BOLD,26));
			if(showText) {
				g.drawString("1) Press 1 for EASY level", xx+40, yy+80);
				g.drawString("2) Press 2 for HARD level", xx+40, yy+130);
			}
			
		}
		else if(STATE==GAME_OVER) {
			int boxWidth=500;
			int boxHeight=200;
			int xx=Game.WIDTH/2-boxWidth/2;
			int yy=Game.HEIGHT/2-boxHeight/2;
			
			g.setColor(new Color(0,0,150));
			g.fillRect(xx, yy, boxWidth, boxHeight);
			g.setColor(Color.white);
			g.setFont(new Font(Font.DIALOG,Font.BOLD,24));
			if(showText) {
				g.drawString("YOU LOSE :(", xx+40, yy+50);
				g.drawString("1) Press 1 for EASY level", xx+40, yy+80);
				g.drawString("2) Press 2 for HARD level", xx+40, yy+110);
				g.drawString("Press SPACEBAR to EXIT the game", xx+40, yy+140);
			}
			
		}
		else if(STATE==WIN) {
			int boxWidth=500;
			int boxHeight=200;
			int xx=Game.WIDTH/2-boxWidth/2;
			int yy=Game.HEIGHT/2-boxHeight/2;
			
			g.setColor(new Color(0,0,150));
			g.fillRect(xx, yy, boxWidth, boxHeight);
			g.setColor(Color.white);
			g.setFont(new Font(Font.DIALOG,Font.BOLD,24));
			if(showText) {
				g.drawString("YOU WIN !! :D ", xx+40, yy+50);
				g.drawString("1) Press 1 for EASY level", xx+40, yy+80);
				g.drawString("2) Press 2 for HARD level", xx+40, yy+110);
				g.drawString("Press SPACEBAR to EXIT the game", xx+40, yy+140);
			}
		}
		
		g.dispose();
		// for showing  what we have on screen
		bs.show();
	}
	
	@Override
	public void run() {
		
		//to focus on the screen where the game is played
		requestFocus();
		int fps =0;
		double timer= System.currentTimeMillis();
		long lastTime = System.nanoTime();
		double targetTick = 60.0;
		//WHY delta
		double delta=0;
		double ns = 1e9/targetTick;
		
		
		// game loop
		while(isRunning) {
			long now=System.nanoTime();
			delta+=(now-lastTime)/ns;
			lastTime =now;
			
			while(delta>=1) {
				tick();
				// render method can also be used outside the while loop
				// but this is preferred
				render();
				fps++;
				delta--;
			}
			
			if(System.currentTimeMillis()-timer>=1000) {
				System.out.println(fps);
				fps=0;
				timer+=1000;
			}
		}
		
		
		//when we want to finish the game
		stop();
	}
	
	public static void main(String[] args) {
		Game game=new Game();
		JFrame frame = new JFrame();
		
		//adding the class game
		frame.add(game);
		
		// keeps the window size same (perfect as specified)
		// no resizing
		frame.setResizable(false);
		
		frame.pack();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// this method sends the window to whatever frame is running the game
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
		
		// starts the main game loop calling our thread
		game.start();
	}

	
	
	// 3 functions of KeyListener class
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(STATE==GAME) {
		if(e.getKeyCode()==KeyEvent.VK_RIGHT)
			player.right=true;
		if(e.getKeyCode()==KeyEvent.VK_LEFT)
			player.left=true;
		if(e.getKeyCode()==KeyEvent.VK_UP)
			player.up=true;
		if(e.getKeyCode()==KeyEvent.VK_DOWN)
			player.down=true;}
		
		else if (STATE==START_SCREEN) {
			if(e.getKeyCode()==KeyEvent.VK_1) {
				ch=1;
			}
			else if(e.getKeyCode()==KeyEvent.VK_2) {
				ch=2;
			}
		}
		else if(STATE==GAME_OVER) {
			if(e.getKeyCode()==KeyEvent.VK_SPACE) {
					System.exit(0);
				}
			else if(e.getKeyCode()==KeyEvent.VK_1) {
				ch=1;
			}
			else if(e.getKeyCode()==KeyEvent.VK_2) {
				ch=2;
			}
		}
		else if(STATE==WIN) {
			if(e.getKeyCode()==KeyEvent.VK_SPACE) {
					System.exit(0);
				}
			else if(e.getKeyCode()==KeyEvent.VK_1) {
				ch=1;
			}
			else if(e.getKeyCode()==KeyEvent.VK_2) {
				ch=2;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_RIGHT)
			player.right=false;
		if(e.getKeyCode()==KeyEvent.VK_LEFT)
			player.left=false;
		if(e.getKeyCode()==KeyEvent.VK_UP)
			player.up=false;
		if(e.getKeyCode()==KeyEvent.VK_DOWN)
			player.down=false;
	}
}
