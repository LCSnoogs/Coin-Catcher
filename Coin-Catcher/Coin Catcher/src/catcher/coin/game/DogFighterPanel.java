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

public class DogFighterPanel extends JPanel implements Runnable, KeyListener{

	private static final long serialVersionUID = 1L;
	private boolean run;
	private FighterPlayer player = null;
	private FighterDog dog = null;
	private Font font;
	private boolean intro = true;
	private BufferedImage introImage;
	
	public DogFighterPanel()
	{
		setBackground(new Color(135,206,250));
		addKeyListener(this);
		
		try {
			introImage = ImageIO.read(new File("Resources\\dg_intro.png"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void setup(int attackPower)
	{
		font = new Font("Comic Sans MS", 20, 20);
		
		// Done to fix the problem where this.getHeight() would sometimes return 0
		while (this.getHeight() <= 0)
		{
			System.out.println(this.getHeight());
		}
		
		player = new FighterPlayer(this.getWidth(), this.getHeight(), attackPower);
		player.setStartPosition(0, this.getHeight() - player.getHeight());
		dog = new FighterDog(this.getWidth(), this.getHeight());
		dog.setStartPosition(this.getWidth() - dog.getWidth(), this.getHeight() - dog.getHeight());
		Thread thread = new Thread(this);
		run = true;
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
			
			paintUI(g);
		}
	}
	
	private void paintUI(Graphics g)
	{
		g.setColor(Color.black);
		g.drawString("Player Health: " + player.getHealth(), this.getWidth()/2 - this.getWidth()/4, 15);
		g.drawString("Dog Health: " + dog.getHealth(), this.getWidth()/2 + this.getWidth()/4, 15);
		if (!run)
		{
			if (dog.getHealth() <= 0)
			{
				g.drawString("Game Over: You Win!", this.getWidth()/2, this.getHeight()/2);
			}
			else
			{
				g.drawString("Game Over: You Lose!", this.getWidth()/2, this.getHeight()/2);
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		
		switch(arg0.getKeyCode()){
        // Player movement
            case KeyEvent.VK_A:
                player.setMoveLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setMoveRight(true);
                break;
            case KeyEvent.VK_SPACE:
            	player.setJumping(true);
            	break;
            case KeyEvent.VK_CONTROL:
            	player.setAttacking(true);
            	break;
         }    
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
		switch(arg0.getKeyCode()){
        // Player movement
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
		// TODO Auto-generated method stub
		while (run)
		{
			if (!intro)
			{
				player.update(dog);
				dog.update(player);
				
				if (dog.getHealth() <= 0 || player.getHealth() <= 0)	// The game is over when the player's or dog's health is 0
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
	}

}
