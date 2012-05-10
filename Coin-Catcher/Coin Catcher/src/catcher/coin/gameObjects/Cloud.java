package catcher.coin.gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Cloud extends GameObject {

	private boolean moveLeft 					= false;
	private boolean moveRight					= false;
	private volatile LinkedList<Coin> coins 	= new LinkedList<Coin>();		// Holds the cloud's coins
	private int maxCoinAmount;													// How many coins the cloud can drop
	private int coinsLeft						= 0;							// How many more coins the cloud can drop
	private int coinDelayFramesLeft				= 0;							// How many frames left to wait before the cloud can drop another coin
	private int maxCoinDelayFrames				= 30;							// How many frames the cloud must wait after dropping a coin
	private Coin deadCoin 						= null;							// A coin that has hit the ground
	private Random rand 						= new Random();					// Used to randomize a Coin object's start position
	
	public Cloud(int screenWidth, int screenHeight, int coinCount) {
		super(screenWidth, screenHeight);
		
		maxCoinAmount = coinCount;
		coinsLeft = maxCoinAmount;
		moveRight = true;
		moveSpeed = 2;
		
		try {
			image = ImageIO.read(new File("Resources\\snowcloud.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		width = image.getWidth();
		height = image.getHeight();
	}

	public void update(Player player)
	{
		if (coinDelayFramesLeft == 0)
		{
			rain();
			coinDelayFramesLeft = maxCoinDelayFrames;
		}
		
		move();
		
		// updates each coin the cloud has
		for (Coin coin : coins)
		{
			coin.update(player);
			
			if (coin.hitGround())
			{
				deadCoin = coin;
			}
		}
		
		// removes a coin that has hit the ground from the list
		if (deadCoin != null)
		{
			coins.remove(deadCoin);
			deadCoin = null;
		}
		
		coinDelayFramesLeft--;
	}
	
	// adds a Coin object to coins
	public void rain()
	{
		if (coinsLeft > 0)
		{
			// starts the Coin at a random position within the cloud
			int startX = rand.nextInt(width) + position.x;
			Coin coin = new Coin(gameScreenWidth, gameScreenHeight);
			coin.setStartPosition(startX, height/2);
			coins.add(coin);
			coinsLeft--;
		}
	}
	
	private void move()
	{
		if (position.x + width >= gameScreenWidth)
		{
			moveLeft = true;
			moveRight = false;
		}
		else if (position.x <= 0)
		{
			moveRight = true;
			moveLeft = false;
		}
		
		if (moveRight)
		{
			position.x += moveSpeed;
		}
		else if (moveLeft)
		{
			position.x -= moveSpeed;
		}
	}
	
	public int getMaxCoinAmount()
	{
		return maxCoinAmount;
	}
	
	public boolean isEmpty()
	{
		if (coins.size() == 0)
		{
			return true;
		}
		return false;
	}
	
	public int getCoinsLeft()
	{
		return coinsLeft;
	}
	
	// Paints cloud and its coins
	@Override
	public void paint(Graphics g) {
		
		g.drawImage(image, position.x, position.y, null);
		
		for (Coin coin : coins)
		{
			// If the coin is collected then value of the coin is shown on screen in its place.
			if (coin.isCollected())
			{
				g.setColor(Color.green);
				g.drawString("+" + coin.getValue(), coin.getPosition().x, coin.getPosition().y);
			}
			else
			{
				g.drawImage(coin.getImage(), coin.getPosition().x, coin.getPosition().y, null);
			}
		}
	}

}
