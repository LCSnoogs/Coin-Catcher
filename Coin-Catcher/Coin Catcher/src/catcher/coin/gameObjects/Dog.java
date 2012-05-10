package catcher.coin.gameObjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Dog extends GameObject{

	protected boolean moveRight 				= false;
	protected boolean moveLeft 					= false;
	protected boolean isHit 					= false;
	protected int maxPauseFrames 				= 20;
	protected int pauseFramesLeft				= 0;
	protected final int numOfRows 				= 8;											// Number of rows in the sprite sheet
	protected final int numOfColumns 			= 8;											// Number of columns in the sprite sheet
	protected final int runSpriteRow 			= 2;											// The row that holds the sprites for running
	protected final int startingSpriteColumn 	= 3;											// The column where the running sprites start
	protected int spriteColumn 					= startingSpriteColumn;							// Current sprite column
	protected final int maxSpriteFrames 		= 10;											// How many frames to stay at current sprite before moving to the next sprite
	protected int spriteFramesLeft 				= 0;											// How many frames to wait before switching to another sprite
	protected BufferedImage[][] sprites 		= new BufferedImage[numOfRows][numOfColumns];	// Each individual sprite will be an item in the array
	
	public Dog(int screenWidth, int screenHeight) {
		super(screenWidth, screenHeight);
		moveSpeed = 10;
		
		BufferedImage spriteSheet = null;
		try {
			spriteSheet = ImageIO.read(new File("Resources\\8bit_dog_spritesheet.png"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		// Adds each individual sprite to the sprites array
		for (int i = 0; i < numOfRows; i++)
		{
			for (int j = 0; j < numOfColumns; j++)
			{
				sprites[i][j] = spriteSheet.getSubimage(j*(spriteSheet.getWidth()/numOfColumns), i*(spriteSheet.getHeight()/numOfRows), 
						spriteSheet.getWidth()/numOfColumns, spriteSheet.getHeight()/numOfRows);
			}
		}
		
		image = sprites[runSpriteRow][startingSpriteColumn];
		width = image.getWidth();
		height = image.getHeight();
	}

	public void update(Player player) {
		
		if (pauseFramesLeft == 0)
		{
			move();
		}
		
		if (!isHit)
		{
			checkIfHit(player);
		}
		if (isHit && pauseFramesLeft > 0)
		{
			pauseFramesLeft--;
		}
		else
		{
			isHit = false;
		}
	}

	protected void move() {
		
		if (position.x + width >= gameScreenWidth)		// makes dog move in the opposite direction when it reaches game boundaries
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
			collisionBox.x = position.x;
		}
		else if (moveLeft)
		{
			position.x -= moveSpeed;
			collisionBox.x = position.x;
		}
	}

	// Checks if Dog has been hit by a Player object
	private void checkIfHit(Player player)
	{
		if (collisionBox.intersects((Rectangle)player.getCollisionBox()) && !player.isInvincible())
		{
			isHit = true;
			pauseFramesLeft = maxPauseFrames;
			player.setIsHit(true);					// Makes Player pause with the dog
		}
	}
	
	@Override
	public void paint(Graphics g) {
		
		// Used to repeat the animation
		if (spriteColumn >= numOfColumns)
		{
			spriteColumn = startingSpriteColumn;
		}

		image = sprites[runSpriteRow][spriteColumn];
		spriteFramesLeft--;
		
		// Flips the image so the dog faces the direction of movement
		if (moveLeft)
		{
			g.drawImage (image, position.x + width, position.y, position.x, position.y + height,
	             0, 0, width, height, null);
		}
		else if (moveRight)
		{
			g.drawImage (image, position.x, position.y, position.x + width, position.y + height,
		             0, 0, width, height, null);
		}
		
		// Sets the image to the next sprite
		if (spriteFramesLeft <= 0)
		{
			spriteColumn++;
			spriteFramesLeft = maxSpriteFrames;
		}
	}
}
