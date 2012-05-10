package catcher.coin.gameObjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Coin extends GameObject {

	private int value 			= 1;			// The value of the coin
	private boolean hitGround	= false;		// If this is true, the coin has hit the ground
	private boolean collected 	= false;		// If this is true, a Player object has collected the coin
	
	public Coin(int screenWidth, int screenHeight) {
		super(screenWidth, screenHeight);
		moveSpeed = 5;
		try {
			image = ImageIO.read(new File("Resources\\coin_button.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		width = image.getWidth();
		height = image.getHeight();
	}

	protected void move()
	{
		if (position.y < gameScreenHeight)
		{
			position.y += moveSpeed;
			collisionBox.y = position.y;
		}
		else
		{
			hitGround = true;
		}
	}

	// Coins are painted in Cloud object's paint method to avoid Concurrent Modification Error
	@Override
	public void paint(Graphics g) {
		
	}
	
	public boolean hitGround()
	{
		return hitGround;
	}

	public void checkIfHit(Player player)
	{
		// If the coin has hit the player's basket then it has been collected and player's coinsCollected increases
		if (collisionBox.intersects(player.getBasket()))
		{
			if (!collected)
			{
				player.incrementCoinsCollected(value);
			}
			collected = true;
		}
	}

	public void update(Player player)
	{
		move();
		if (!player.isInvincible())		// When the player is invincible, it cannot collect coins
		{
			checkIfHit(player);
		}
	}

	public boolean isCollected() {
		// TODO Auto-generated method stub
		return collected;
	}

	public int getValue() {
		// TODO Auto-generated method stub
		return value;
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
}
