package catcher.coin.game;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Game extends JFrame {

	private static final long serialVersionUID = 1L;
	private static CoinCatcherPanel ccPanel;
	private static DogFighterPanel dgPanel;
	public Game()
	{
		super("Coin Catcher");
		this.setResizable(false);
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        ccPanel = new CoinCatcherPanel(this);
        ccPanel.setLayout(new BorderLayout());
        
        dgPanel = new DogFighterPanel();
        dgPanel.setLayout(new BorderLayout());
        
        add(ccPanel, BorderLayout.CENTER);
	}
	
	public static void main(String[] args) {
        Game window = new Game();
        window.setVisible(true);
        
        ccPanel.setup();
    }

	// Starts the second part of the game
	public void startDogFigther(int coinsCollected) {
		
		ccPanel.setVisible(false);
		add(dgPanel, BorderLayout.CENTER);
		dgPanel.setup(coinsCollected);
	}
}
