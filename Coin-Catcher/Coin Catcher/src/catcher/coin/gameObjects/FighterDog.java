package catcher.coin.gameObjects;

import java.awt.Graphics;

public class FighterDog extends Dog {

	private int attackPower = 1;
	private int maxInvincibleFrames = 100;
	private int invincibleFramesLeft = 0;
	private int health = 100;
	
	public FighterDog(int screenWidth, int screenHeight) {
		super(screenWidth, screenHeight);
		
	}

	public void paint(Graphics g) {
		
		if (spriteColumn >= numOfColumns)
		{
			spriteColumn = startingSpriteColumn;
		}
		
		image = sprites[runSpriteRow][spriteColumn];
		
		spriteFramesLeft--;
		
		if (invincibleFramesLeft % 2 == 0)
		{
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
		}
		if (spriteFramesLeft <= 0)
		{
			spriteColumn++;
			spriteFramesLeft = maxSpriteFrames;
		}
	}
	
	private void checkIfHit(FighterPlayer player)
	{
		if (invincibleFramesLeft == 0)
		{
			if (collisionBox.intersects(player.getBasket()) && !player.isInvincible())		// checks if dog is hit by basket
			{
				health -= player.getAttackPower();
				invincibleFramesLeft = maxInvincibleFrames;
			}
		}
		else
		{
			invincibleFramesLeft--;
		}
	}
	
	public void update(FighterPlayer player) {
		
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
	
	public boolean isInvincible()
	{
		if (invincibleFramesLeft > 0)
		{
			return true;
		}
		return false;
	}

	public void decreaseHealth(int attackPower) {
		
		health -= attackPower;
	}

	public void setIsHit(boolean b) {
		
		isHit = b;
		
		if (isHit)
		{
			pauseFramesLeft = maxPauseFrames;
		}
	}

	public int getAttackPower() {
		
		return attackPower;
	}

	public int getHealth() {
		
		if (health <= 0)
		{
			return 0;
		}
		return health;
	}
}
