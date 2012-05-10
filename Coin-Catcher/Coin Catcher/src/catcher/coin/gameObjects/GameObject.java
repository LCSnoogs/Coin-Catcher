package catcher.coin.gameObjects;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public abstract class GameObject {

	protected int gameScreenWidth;				// Set as the width of the screen
	protected int gameScreenHeight;				// Set as the height of the screen
	protected int moveSpeed;					// How many pixels the GameObject moves in each frame
	protected Point position;					// The x and y coordinates of the GameObject
	protected BufferedImage image = null;		// The image of the GameObject
	protected int height;						// The height of the GameObject
	protected int width;						// The width of the GameObject
	protected Rectangle collisionBox;			// Used to find out if the GameObject is intersected by another GameObject
	
	public GameObject(int screenWidth, int screenHeight)
	{
		gameScreenWidth = screenWidth;
		gameScreenHeight = screenHeight;
		collisionBox = new Rectangle();
	}
	
	// Used to set the starting position of the GameObject at the start of the game
	public void setStartPosition(int x, int y)
	{
		position = new Point(x, y);
		collisionBox.setBounds(x, y, width, height);
	}
	
	public Point getPosition()
	{
		return position;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public Rectangle getCollisionBox()
	{
		return collisionBox;
	}
	
	public void setScreenWidth(int width)
	{
		gameScreenWidth = width;
	}
	
	public void setScreenHeight(int height)
	{
		gameScreenHeight = height;
	}
	
	// Used to paint GameObject on screen
	public abstract void paint(Graphics g);
}
