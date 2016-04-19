package assignment4;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameStage extends JPanel implements Runnable {
	
	// Attribute
	private int currentScore;
	private int recordScore;
	private int winScore;
	private boolean stop;
	private int width;
	private int height;
	private Color color;
	
	private JLabel label;
	private Duck duck;
	private BG bg;
	private Ball ball;
	private Image win;
	
	// Getter Setter
	public void setCurrentScore(int s){	currentScore = s; 
		label.setText("Score : " + getCurrentScore());
	}
	public void setWinScore(int s){	winScore = s; }
	public void setWidth(int w){ width = w; }
	public void setHeight(int h){height = h; }
	public void setColor(Color c){color = c;}
	public void setStop(boolean b){stop = b;}
	
	public int	getCurrentScore(){	return currentScore;}
	public int 	getWinScore(){	return winScore;}
	public int getWidth(){	return width; }
	public int getHeight(){	return height;}
	public Color getColor(){return color;}
	public boolean getStop(){return stop;}
	
	// Constructor
	public GameStage(int w, int h, Color c)
	{
		// Basic setup
		setWidth(w);
		setHeight(h);
		setColor(c);
		this.setSize(getWidth(), getHeight());
		
		// Read Win picture
		try {
			win = ImageIO.read(new File("win.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Initialize component
		duck = new Duck(0,300,300);
		bg = new BG(0, 0);
		ball = new Ball(900, 300);
		label = new JLabel("Score : " + getCurrentScore());
		label.setFont(new Font(label.getName(), Font.PLAIN, 25));
		label.setBounds(310, 0, 200, 40);
		
		// Add component
		this.add(label);
		init();
	}
	
	// init the game
	public void init()
	{
		setStop(false);
		// Reset Score
		setCurrentScore(0);
		recordScore = getCurrentScore();
		setWinScore(20);
		
		// Reset position 
		duck.setDuckCurrentX(0);
		duck.setDuckCurrentY(300);
		bg.setBgX(0);
		bg.setBgY(0);
		ball.setBallCurrentX(900);
		ball.setBallCurrentY(300);
		
		repaint();
	}
	
	// Drawing method
	public void paintComponent(Graphics g)
	{
		this.setBackground(getColor());
		g.drawImage(bg.getImage(), bg.getBgX(), bg.getBgY(),this);
		g.drawImage(duck.getImage(), duck.getDuckCurrentX(), duck.getDuckCurrentY(), this);
		g.drawImage(ball.getImage(), ball.getBallCurrentX(), ball.getBallCurrentY(),this);
		
		// If win , Show win
		if(stop)
			g.drawImage(win, 180, 150, this);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!stop)
		{
			// Animation of Duck
			int duckY = duck.getDuckCurrentY();
			if(duck.getDuckDirection() == 1)// Up
			{
				if(duckY > duck.getDuckAnchorY() - 10)
					duck.setDuckCurrentY(duckY - 1);
				else
					duck.setDuckDirection(0);
			}
			else// Down
			{
				if(duckY < duck.getDuckAnchorY() + 10)
					duck.setDuckCurrentY(duckY + 1);
				else
					duck.setDuckDirection(1);
			}
			
			// If Score has change , Animation
			if(getCurrentScore() != recordScore)
			{
				recordScore = getCurrentScore();
				Thread scoreChange = new Thread(new Runnable(){
					public void run() {
						int cnt = 0;
						while(cnt < 40)
						{
							cnt++;
							// Move bg and ball
							if(getCurrentScore() < 9)
							{
								bg.setBgX(bg.getBgX()-1);
								ball.setBallCurrentX(ball.getBallCurrentX()-1);
							}
							// Move duck to ball
							else
								duck.setDuckCurrentX(duck.getDuckCurrentX() + 1);
							
							try {
								Thread.sleep(30);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							repaint();
						}
					}
					
				});
				scoreChange.start();
				// Check Win or not
				if(getCurrentScore() >= getWinScore())
				{
					try {
						scoreChange.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					stop = true;
					System.out.println("stop!!");
				}
			}
			repaint();
			
			
			
			try {
				Thread.sleep(80);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addScore()
	{
		// Add score
		if(getCurrentScore() < getWinScore())
			setCurrentScore(getCurrentScore() + 1);
		
		// Update JLabel
		label.setText("Score : " + getCurrentScore());

	}
}
