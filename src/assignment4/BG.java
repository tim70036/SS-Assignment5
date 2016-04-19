package assignment4;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BG {
	// Atrrtibute
	private int bgX;
	private int bgY;
	private Image image;
	
	//	Setter Getter
	public void setBgX(int x){	bgX = x;}
	public void setBgY(int y){ bgY = y;}
	public void setImage(Image i){image = i;}
	
	public int getBgX(){	return bgX;}
	public int getBgY(){ return bgY;}
	public Image getImage(){return image;}
	
	// Constructor
	BG(int x, int y)
	{
		setBgX(x);
		setBgY(y);
		try {
			setImage(ImageIO.read(new File("h.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
