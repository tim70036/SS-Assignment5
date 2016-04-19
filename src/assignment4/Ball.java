package assignment4;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Ball{
	// Attribute
	private int ballCurrentX;
	private int ballCurrentY;
	private Image image;
	
	// Setter Getter
	public void setBallCurrentX(int x){	ballCurrentX = x;}
	public void setBallCurrentY(int y){ ballCurrentY = y;}
	public void setImage(Image i){image = i;}
	
	public int getBallCurrentX(){	return ballCurrentX;}
	public int getBallCurrentY(){ return ballCurrentY;}
	public Image getImage(){return image;}
	
	// Constructor
	Ball(int x, int y)
	{
		setBallCurrentX(x);
		setBallCurrentY(y);
		try {
			setImage(ImageIO.read(new File("b.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
