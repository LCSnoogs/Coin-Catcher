package catcher.coin.gameObjects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;



public class Player extends GameObject{

	protected boolean moveLeft			= false;				// If this is true, the player moves to the left
	protected boolean moveRight			= false;				// If this is true, the player moves to the right
	protected boolean jumping			= false;				// If this is true, the player jumps
	protected boolean isHit				= false;				// If this is true, the player has been hit by the dog
	protected int maxInvincibleFrames	= 100;					// How many frames the player will be invincible after being hit
	protected int invincibleFramesLeft	= 0;					// How many frames until the player will be vulnerable again
	protected int maxPauseFrames		= 20;					// How many frames the player cannot move after being hit
	protected int pauseFramesLeft		= 0;					// How many frames until the player can move again
	protected int maxJumpSpeed			= 20;					// The maximum amount of pixels the player can move up
	protected int jumpSpeed				= maxJumpSpeed;			// How many pixels the player is moving vertically
	protected int gravity				= -1;					// How many pixels the player is pushed down
	protected int coinsCollected		= 0;					// How many coins the player has collected in its basket
	protected Rectangle basket;									// The boundaries of the basket
	protected Rectangle hand;									// The boundaries of the player's hand
	
	public Player(int screenWidth, int screenHeight)
	{
		super(screenWidth, screenHeight);
		moveSpeed = 5;
		width = 50;
		height = 70;
	}

	public void setStartPosition(int x, int y)
	{
		super.setStartPosition(x, y);
		hand = new Rectangle(position.x + width/2 - 10, position.y - 20, 20, 20);
		basket = new Rectangle(position.x, hand.y + hand.height/2 - 50, 50, 50);
	}
	
	public void move()
	{
		if (moveRight && basket.x + basket.width <= gameScreenWidth)	// makes sure that the player cannot go farther than the game boundaries
		{
			position.x += moveSpeed;
		}
		else if (moveLeft && basket.x >= 0)
		{
			position.x -= moveSpeed;
		}
		
		// moving the collisionBox, basket, and hand with the player
		collisionBox.x = position.x;
		basket.x = position.x;
		hand.x = position.x + width/2 - hand.width/2;
		hand.y = position.y - hand.height;
		basket.y = hand.y + hand.height/2 - basket.height;
	}
	
	public void jump()
	{
		if (jumping)
		{
			position.y -= jumpSpeed;
			jumpSpeed += gravity;									// decreases jumpSpeed until player is back on the ground
			
			if (position.y >= gameScreenHeight - height)			// checks if the player has hit the ground
			{
				jumping = false;
				position.y = gameScreenHeight - height;
				jumpSpeed = maxJumpSpeed;
			}
			
			// moves collisionBox, hand, and basket with the player
			collisionBox.y = position.y;
			hand.y = position.y - hand.height;
			basket.y = hand.y + hand.height/2 - basket.height;
		}
	}
	
	public void paint(Graphics g)
	{
		if (invincibleFramesLeft % 2 == 0)									// makes the player's image flash when it is invincible
		{
			g.setColor(Color.black);
			g.fillRect(position.x, position.y, width, height);
			g.setColor(Color.yellow);
			g.fillRect(basket.x, basket.y, basket.width, basket.height);
			g.setColor(Color.darkGray);
			g.fillRect(hand.x, hand.y, hand.width, hand.height);
		}
	}

	// called during every frame to move the player and check for collision with a Dog object
	public void update(Dog dog) {
		
		if (pauseFramesLeft == 0)					// keeps the player from moving during pause phase
		{
			move();
			jump();
		}
		
		if (!isHit)									// does not need to check if player has been hit if it has already been hit
		{
			checkIfHit(dog);
		}
		
		if (isHit && pauseFramesLeft > 0)			// player can move when pauseFramesLeft is 0
		{
			pauseFramesLeft--;
		}
		if (isHit && invincibleFramesLeft > 0)		// player is vulnerable when invincibleFramesLeft is 0
		{
			invincibleFramesLeft--;
		}
		else
		{
			isHit = false;							// player can be hit again when pauseFramesLeft and invincibleFramesLeft is 0
		}
	}

	// Checks if player has been hit by a Dog object
	public void checkIfHit(Dog dog)
	{
		if (collisionBox.intersects(dog.getCollisionBox()))
		{
			// player has been hit and pause and invincible phase starts
			isHit = true;
			pauseFramesLeft = maxPauseFrames;
			invincibleFramesLeft = maxInvincibleFrames;
		}
		
	}
	
	public void setMoveRight(boolean move)
	{
		moveRight = move;
	}
	
	public void setMoveLeft(boolean move)
	{
		moveLeft = move;
	}
	
	public void setJumping(boolean jump)
	{
		jumping = jump;
	}
	
	public boolean isInvincible() {
		
		if (invincibleFramesLeft > 0)
		{
			return true;
		}
		return false;
	}
	
	public Rectangle getBasket()
	{
		return basket;
	}

	public int getCoinsCollected() {
		
		return coinsCollected;
	}
	
	public void incrementCoinsCollected(int value)
	{
		coinsCollected += value;
	}
	
	public void setIsHit(boolean hit)
	{
		isHit = hit;
		if (isHit)
		{
			pauseFramesLeft = maxPauseFrames;
		}
	}
	
	public boolean isHit()
	{
		return isHit;
	}
}
