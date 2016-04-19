package assignment4;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Duck {
	// Attribute
	private int duckCurrentX;
	private int duckCurrentY;
	private int duckAnchorY;
	private int duckDirection;// 0 for down , 1 for up
	private Image image;
	
	//	Setter Getter
	public void setDuckCurrentX(int x){	duckCurrentX = x;}
	public void setDuckCurrentY(int y){	duckCurrentY = y;}
	public void setDuckAnchorY(int y){duckAnchorY = y;}
	public void setDuckDirection(int d){duckDirection = d;}
	public void setImage(Image i){image = i;}
	
	public int getDuckCurrentX(){	return duckCurrentX;}
	public int getDuckCurrentY(){	return duckCurrentY;}
	public int getDuckAnchorY(){	return duckAnchorY;}
	public int getDuckDirection(){	return duckDirection;}
	public Image getImage(){return image;}
	
	// Constructor
	Duck(int x, int y, int anchorY)
	{
		setDuckCurrentX(x);
		setDuckCurrentY(y);
		setDuckAnchorY(anchorY);
		setDuckDirection(0);
		try {
			setImage(ImageIO.read(new File("duck.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
