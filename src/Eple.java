import java.awt.Color;
import java.awt.Point;



public class Eple extends Grafikkelement{
	
	public Point eplePoint;
	
	public Eple() {
		farge 	= Color.red;
		x		= 100;
		y		= 100;
		bredde	= 20;
		hoyde	= 20;
		eplePoint = new Point(x,y);
		
	}
	
	
	public void setPos (int x,int y) {
		this.x = x;
		this.y = y;
		
		eplePoint = new Point(x,y);
	}
	
	public Point getPos() {
		return eplePoint;
	}
}