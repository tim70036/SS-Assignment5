package assignment4;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class Server extends JFrame {
	private ServerSocket server;
	private JTextArea textArea;
	
	private ArrayList<ConnectionThread>	connectionPool = new ArrayList<ConnectionThread>();
	
	// Data structure for picture
	private ArrayList<String>	unknownWordFile;
	private ArrayList<Integer>	usedIndex2;
	private int unknownWordFileIndex;
	private Random r;
	
	// Output data structure
	private String userInput2;
	private ArrayList<String>	answer = new ArrayList<String>();
	private ArrayList<Word> toOutput;
	// Continue to create socket to accept each client
	public void runServer()
	{
		addText("Server started at " + LocalDateTime.now());
		
		int cnt = 0;
		while(true)
		{
			try {
				// Accept a request
				addText("Server is waiting for connection....");
				Socket client = server.accept();
				ConnectionThread connect = new ConnectionThread(client);
				
				// Show message
				addText("Server is connect!");
				addText("Player" + cnt + "'s IP address is" + client.getInetAddress());
				cnt++;
				
				// Add to List
				connectionPool.add(connect);
				
				// If at least two players, Start the game
				addText("Pool has " + connectionPool.size() + " connection");
				if(connectionPool.size() >= 2)
					clientStart();
				
				// Start connectionThread
				connect.start();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class ConnectionThread extends Thread{
		private Socket client;
		private PrintWriter writer;
		private BufferedReader reader;
		
		ConnectionThread(Socket c)
		{
			client = c;
			try {
				writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
				reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Send message to client
		public void sendMessage(String message)
		{
			writer.println(message);
			writer.flush();
		}
		
		// Keep Reading from client
		public void run() {
			String clientMessage;
			while(true)
			{
				try {
					// Read instruction
					clientMessage = reader.readLine();
					addText(clientMessage + ": ");
					
					// User input 
					if(clientMessage.equals("User input"))
					{
						userInput2 = reader.readLine();
						addText(userInput2);
						// Check whether all UserInput match
				        if(!userInput2.equals("No word"))// user must input
				        {
				        	answer.add(userInput2);// Add to answer pool
				        	if(answer.size() >= connectionPool.size())// All user has inputed, Check answer
				        	{
				        		boolean same = true;
				        		String s = answer.get(0);
				        		for(String i : answer)
				        		{
				        			if(!s.equals(i))
				        			{
				        				same = false;
				        				break;
				        			}
				        		}
				        		// If all same , than correct
				        		if(same)
				        			correct(true);
				        		else
				        			correct(false);
				        		// Clear answer pool
				        		answer.clear();
				        	}
				        }
				        
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
//			// GameOver output the file
//			try {
//				PrintWriter w = new PrintWriter("output.txt");
//				for(Word cur : toOutput)
//					w.println(cur.getFile() + " " + cur.getWord());
//				w.close();
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
		}		
	}
	
	// Tell all client to start game and send first image filename
	public void clientStart()
	{
		int cnt = 0;
		for(ConnectionThread i : connectionPool)
		{
			i.sendMessage("Rock and Roll");
			addText("send start signal to Player" + cnt++);
		}
		changePicture();
	}
	
	// Add text to textArea
	public void addText(String clientMessage) {
		if(clientMessage != null)
			textArea.append(clientMessage + "\n");
	}
	Server(int port)
	{
		// GUI Setup
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Initialize textArea
		this.textArea = new JTextArea();
		this.textArea.setEditable(false);
		this.textArea.setPreferredSize(new Dimension(500,550));
		JScrollPane scrollPane = new JScrollPane(this.textArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    this.add(scrollPane);
	    
	    // Create Server Socket
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.pack();
		this.setVisible(true);
		
		// Deal with data for word
		r = new Random();
		unknownWordFile = new ArrayList<String>();
		usedIndex2 = new ArrayList<Integer>();
		toOutput =  new ArrayList<Word>();
		try {
			// Read the unknown word's file name into ArrayList
			Scanner scanner = new Scanner(new File("unknown_words.txt"));
			while(scanner.hasNext())
				unknownWordFile.add(scanner.next());
			scanner.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void changePicture()
	{
		// Find a non use picture
		while(usedIndex2.contains(unknownWordFileIndex))
			unknownWordFileIndex = r.nextInt(unknownWordFile.size());
		
		// Send to all
		for(ConnectionThread i : connectionPool)
		{
			i.sendMessage("changePicture");
			i.sendMessage(unknownWordFile.get(unknownWordFileIndex));
		}
			
	}
	
	public void correct(boolean b)
	{
		if(b)
		{
			// Add to used
			usedIndex2.add(new Integer(unknownWordFileIndex));
			
			// Add to toOutput
			toOutput.add(new Word(unknownWordFile.get(unknownWordFileIndex) ,  userInput2));
			
			// Send to all
			for(ConnectionThread i : connectionPool)
				i.sendMessage("Right answer");
			
			changePicture();
		}
		// Not correct
		else
		{
			// Send to all
			for(ConnectionThread i : connectionPool)
				i.sendMessage("Wrong answer");
		}
	}
	
	public static void main(String[] args) {
		Server server = new Server(7777);
		server.runServer();
	}

}
