package assignment4;

import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Typing extends JPanel implements Runnable{
	
	// Atrribute
	private int width;
	private int height;
	private Color color;
	private GameStage gs;
	private boolean stop;
	private int drawMode;// 0: waiting, 1: draw word picture , 2: wrong answer
	
	// Component
	private JTextField field;
	private JLabel waiting;
	private JLabel wrong;
	
	// Picture and position
	private Image picture2;
	private int picture2X;
	private int picture2Y;
	
	// Socket
	private Socket client;
    private PrintWriter writer;
    private BufferedReader reader;
    private static boolean serverSayStart;
	
	// Setter Getter
	public void setWidth(int w){ width = w; }
	public void setHeight(int h){height = h; }
	public void setColor(Color c){color = c;}
	public void setGameStage(GameStage g){gs = g;}
	public void setStop(boolean b){stop = b;}
	public static void  setServerSayStart(boolean b){serverSayStart = b;}

	public void setPicture2X(int x){picture2X = x;}
	public void setPicture2Y(int y){picture2Y = y;}
	
	public int getWidth(){	return width; }
	public int getHeight(){	return height;}
	public Color getColor(){return color;}
	public GameStage getGameStage(){return gs;}
	public boolean getStop(){return stop;}
	public static boolean getServerSayStart(){return serverSayStart;}
	
	public int getPicture2X(){	return picture2X;}
	public int getPicture2Y(){	return picture2Y;}
	
	// Constructor
	Typing(int w,int h,Color c,GameStage g)
	{
		// Basic setup
		setWidth(w);
		setHeight(h);
		setColor(c);
		setGameStage(g);
		
		// Initialize component
		waiting = new JLabel("Waiting");
		waiting.setFont(new Font(waiting.getName(), Font.PLAIN, 25));
		waiting.setBounds(5, 200, 200, 50);
		
		wrong = new JLabel("Wrong answer, please input again!");
		wrong.setFont(new Font(waiting.getName(), Font.PLAIN, 15));
		wrong.setBounds(5,300,200,50);
		
		field = new JTextField(20);
		field.setBounds(0, 535, 300, 30);
		field.setEditable(false);
		field.addActionListener( new ActionListener(){
			// When User input , send to server
		    public void actionPerformed(ActionEvent e) {
		        // Get userInput by Scanner through field
		    	String userInput2 = null;
		        Scanner scanner = new Scanner(field.getText());
		        if(scanner.hasNext())	userInput2 = scanner.next();
		        
		        // Send to server
		        sendMessage("User input");
		        if(userInput2 == null)	sendMessage("No word");
		        else{
		        	// If user input valid , wait for other's
		        	sendMessage(userInput2);
		        	field.setEditable(false);
		        	// Show waiting label
			        drawMode = 0;
		        }
		        
		        // Reset TextField
		        field.setText("");
		        scanner.close();
		    }
		});
		// Add Component
		this.add(field);
		this.add(waiting);
		this.add(wrong);

		// Socket
        try {
			client = new Socket("127.0.0.1",7777);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
			writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
		setServerSayStart(false);
		
		init();
	}
	
	// init the game
	public void init()
	{
		setStop(false);		
		repaint();
	}
	
	// Change picture2, Reset position
	public void changePicture2(String f2)
	{
		// Read Image
		try {
			picture2 = ImageIO.read(new File("img/unknown/" + f2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Set position
		setPicture2Y(0);
		setPicture2X(15);// Avoid two pictures cover each other
	}
	
	// Animation for backGround color
	public void changeBackground(boolean correct)
	{
		Color a = new Color(255, 36,0);
		Color b = new Color(142,252,0);
		Color origin = new Color(224,255,255);
		
		Thread changeColor = new Thread(new Runnable(){
			public void run() {
				int cnt = 0;
				while(cnt < 4)
				{
					if(cnt%2 == 0)
					{
						if(correct)	setColor(b);
						else	setColor(a);
					}
					else
						setColor(origin);
					cnt++;
					try {
						Thread.sleep(400);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		});
		changeColor.start();
	}
	
	// UserInput is right
	public void correct()
	{		
		getGameStage().addScore();
		changeBackground(true);
	}
	
	// Drawing method
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		this.setBackground(getColor());
		// Show waiting
		if(drawMode == 0)	
		{
			waiting.setVisible(true);
			wrong.setVisible(false);
		}
		// Show word picture
		else if(drawMode == 1)
		{
			g.drawImage(picture2, picture2X, picture2Y, this);
			waiting.setVisible(false);
			wrong.setVisible(false);
		}
		// Show wrong answer
		else if(drawMode == 2)
		{
			wrong.setVisible(true);
			waiting.setVisible(false);
		}
		field.setBounds(0, 535, 300, 30);
	}
	
	// Send message to server
    public void sendMessage(String message)
    {
        writer.println(message);
        writer.flush();
    }
	
	public void run() {
		// Waiting
		while(!serverSayStart)
		{
			String message;
            try {
                message = reader.readLine();
                System.out.println(message);

                // Server says start the game
                if(message.equals("Rock and Roll"))
                {
                	// Start Typing and GameStage
                	setServerSayStart(true);
                	Thread paintingThread = new Thread(gs);
                	paintingThread.start();
                	
                	// Let User can input
                	field.setEditable(true);
                }
                
                // Start drawing picture
                drawMode = 1;
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		}
		
		// Create a Thread for listening Server
		Thread fromServer = new Thread(new Runnable(){
			public void run() {
				while(true){
					// Read Instruction from server
					String act,f2;
					try {
						act = reader.readLine();
						// Change picture
						if(act.equals("changePicture"))
						{
							f2 = reader.readLine();
							System.out.println(f2);
							changePicture2(f2);
							
							// Show picture
							drawMode = 1;
						}
						// Wrong answer
						else if(act.equals("Wrong answer"))
						{
							changeBackground(false);
							// Show wrong for a while and show picture
							drawMode = 2;
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							drawMode = 1;
							
							// User can input again
							field.setEditable(true);
						}
						// Right answer
						else if(act.equals("Right answer"))
						{
							correct();
							// Show picture
							drawMode = 1;
							
							// User can input again
							field.setEditable(true);
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		fromServer.start();
		
		// Start Game		
		while(!stop)
		{
			
			// Animation for word
			setPicture2Y(getPicture2Y()+1);
			if(getPicture2Y() >= 500)	setPicture2Y(0);	

			field.setBounds(0, 535, 300, 30);
			repaint();

			// Check Win or not
			if(gs.getStop() == true)
				setStop(true);

			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}
}
