package catcher.coin.gameObjects;

public class FighterPlayer extends Player {

	private int attackPower 			= 1;		// How much damage a player can deal to a Dog object's health
	private int health 					= 3;		// How much health a player has
	private int maxAttackFrames 		= 10;		// How many frames the player stays attacking
	private int attackFramesLeft 		= 0;		// How many frames left before the player stops attacking
	private int maxAttackDelayFrames 	= 10;		// How many frames the player must wait before attacking again
	private int attackDelayFramesLeft 	= 0;		// How many frames left before the player can attack again
	private boolean attacking 			= false;	// If true, the player is attacking
	
	public FighterPlayer(int screenWidth, int screenHeight, int attackPower) {
		super(screenWidth, screenHeight);
		
		// Makes sure that the player has an attackPower value of at least 1
		if (attackPower > 1)
		{
			this.attackPower = attackPower;
		}
	}

	public int getHealth()
	{
		// Makes sure health never presented as a negative number
		if (health <= 0)
		{
			return 0;
		}
		return health;
	}
	
	public int getAttackPower()
	{
		return attackPower;
	}
	
	public void update(FighterDog dog)
	{
		if (pauseFramesLeft == 0)
		{
			move();
			jump();
		}
		
		if (!isHit)
		{
			checkIfHit(dog);
		}
		
		if (isHit && pauseFramesLeft > 0)
		{
			pauseFramesLeft--;
		}
		if (isHit && invincibleFramesLeft > 0)
		{
			invincibleFramesLeft--;
		}
		else
		{
			isHit = false;
		}
		
		if (attacking)
		{
			attack(dog);
		}
	}
	
	// Starts attack phase
	public void attack(FighterDog dog)
	{
		if (attackDelayFramesLeft == 0 && invincibleFramesLeft == 0)	// Players cannot attack while they are invincible
		{
			attackFramesLeft = maxAttackFrames;
			attackDelayFramesLeft = maxAttackDelayFrames;
		}
		
		if (attackFramesLeft > 0)
		{
			if (dog.getPosition().x < position.x)						// Makes sure the player is attacking in the direction of the dog
			{
				// Sets the player's hand and basket to attack positions
				hand.x = position.x - hand.width;
				hand.y = position.y + height/2;
				basket.x = hand.x - basket.width + hand.width/2;
				basket.y = hand.y - hand.height/2;
			}
			else if (dog.getPosition().x > position.x)
			{
				hand.x = position.x + width;
				hand.y = position.y + height/2;
				basket.x = hand.x + hand.width/2;
				basket.y = hand.y - hand.height/2;
			}
			
			attackFramesLeft--;
		}
		else if (attackDelayFramesLeft > 0)
		{
			attackDelayFramesLeft--;
			if (attackDelayFramesLeft == 0)
			{
				attacking = false;
			}
		}
	}
	
	public void setAttacking(boolean attacking)
	{
		this.attacking = attacking;
	}
	
	public void checkIfHit(FighterDog dog)
	{
		if (!dog.isInvincible())									// Player cannot be hit by an invincible dog
		{
			if (collisionBox.intersects(dog.getCollisionBox()))
			{
				isHit = true;
				pauseFramesLeft = maxPauseFrames;
				invincibleFramesLeft = maxInvincibleFrames;
				health -= dog.getAttackPower();						// Player loses health when hit by dog
				dog.setIsHit(true);									// Makes dog pause with player after being hit
			}
		}
	}

	public void decreaseHealth(int attackPower) {
		
		health -= attackPower;
	}
}
