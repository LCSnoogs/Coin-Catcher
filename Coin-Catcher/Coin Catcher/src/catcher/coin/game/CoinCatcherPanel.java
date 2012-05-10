package catcher.coin.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import catcher.coin.gameObjects.*;

public class CoinCatcherPanel extends JPanel implements Runnable, KeyListener{

	private static final long serialVersionUID 	= 1L;
	private boolean run 						= true;		// Game Loop runs as long as this is true
	private Player player;
	private Dog dog;
	private Cloud cloud;
	private Font font;
	private boolean intro 						= true;		// If this is true, the Introduction is painted. If this is false, the games starts
	private BufferedImage introImage;
	private Game game;
	
	public CoinCatcherPanel(Game window)
	{
		game = window;
		setBackground(new Color(135,206,250));
		addKeyListener(this);
		
		try {
			introImage = ImageIO.read(new File("Resources\\cc_intro.png"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	// initializes member variables and starts thread
	public void setup()
	{
		font = new Font("Comic Sans MS", 20, 20);
		player = new Player(this.getWidth(), this.getHeight());
		player.setStartPosition(0, this.getHeight() - player.getHeight());
		dog = new Dog(this.getWidth(), this.getHeight());
		dog.setStartPosition(this.getWidth() - dog.getWidth(), this.getHeight() - dog.getHeight());
		cloud = new Cloud(this.getWidth(), this.getHeight(), 30);
		cloud.setStartPosition(0, 20);
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setFont(font);
		
		if (intro)
		{					
			g.drawImage(introImage, 0, 0, null);
		}
		else
		{
			player.paint(g);
			dog.paint(g);
			cloud.paint(g);
			
			paintUI(g);
		}
	}
	
	// Paints the user interface
	private void paintUI(Graphics g)
	{
		g.setColor(Color.black);
		g.drawString("Coins Left: " + cloud.getCoinsLeft(), this.getWidth()/2 - this.getWidth()/4, 15);
		g.drawString("Coins Collected: " + player.getCoinsCollected(), this.getWidth()/2 + this.getWidth()/4, 15);
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		
		switch(arg0.getKeyCode()){
        
            case KeyEvent.VK_A:
                player.setMoveLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setMoveRight(true);
                break;
            case KeyEvent.VK_SPACE:
            	player.setJumping(true);
            	break;
         }    
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
		switch(arg0.getKeyCode()){
        
            case KeyEvent.VK_A:
                player.setMoveLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setMoveRight(false);
                break;
            case KeyEvent.VK_ENTER:
            	intro = false;
            	break;
         } 
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		
		while (run)
		{
			if (!intro)
			{
				update();
				
				if (cloud.isEmpty())
				{
					run = false;
				}
			}
			
			repaint();
			
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.requestFocus();
		}
		
		shutdown();
	}
	
	// Updates all game objects
	private void update()
	{
		player.update(dog);
		dog.update(player);
		cloud.update(player);
	}
	
	// Starts the Dog Fighter part of the game when Coin Catcher part ends
	private void shutdown()
	{
		game.startDogFigther(player.getCoinsCollected());
	}

}
