package assignment4;

public class Word {
	// Storage for filename and word
	private String file;
	private String word;
	
	// Getter Setter
	public void setFile(String s){file = s;}
	public void setWord(String s){word = s;}
	
	public String getFile(){return file;}
	public String getWord(){return word;}
	
	// Constructor
	Word(String f, String w)
	{
		setFile(f);
		setWord(w);
	}
}
