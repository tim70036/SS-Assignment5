package assignment4;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MyWindow extends JFrame {
	// Attribute
	private int width;
	private int height;
	
	private GameStage stage;
	private Typing typing;
	
	// Getter
	public GameStage getStage(){return stage;}
	public Typing getTyping(){return typing;}
	
	// Constructor
	MyWindow(int w,int h)
	{
		width = w;
		height = h;

		// Basic setup
		this.setSize(width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLayout(null);

		// Add GameStage
		stage = new GameStage(700,600,new Color(135,206,250));
		this.add(stage);
		stage.setBounds(300, 0, 700, 600);

		// Add Typing
		typing = new Typing(300,600,new Color(224,255,255), stage);
		this.add(typing);
		typing.setBounds(0, 0, 300, 600);



			stage.init();
			typing.init();
			// Control of Game thread
			Thread typingThread = new Thread(typing);
			typingThread.start();
	}
}
