import java.awt.Color;
import java.awt.Graphics;


public class Grafikkelement {
	
	int x=250,y=250,bredde=5,hoyde=5;
	
	Color farge = Color.black;
	
	public void drawMe(Graphics g) {
		g.setColor(farge);
		g.fillRect(x,y,bredde,hoyde);
	}
}
