package view;
/*
 * Class: RiskGUI
 * 
 * Authors: Steven Cleary
 * 
 * Description:
 * This class allows an arrow to be drawn.
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;

public class Arrow {
	private Point start, end;
	private int width = 20;

	public Arrow( Point start, Point end) {
		this.start = start;
		this.end = end;
	}

	public void setEndPoint(Point end) {
		//System.out.println("in arrow: ");
		this.end = end;
	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
	
		g2.setColor(Color.GRAY);
		try {
			double hyp = Math.sqrt((end.x - start.x) * (end.x - start.x) + (end.y - start.y) * (end.y - start.y));
			double rotate = (Math.atan2((double)-(end.x - start.x) , (end.y - start.y)));
			g2.rotate(rotate,start.x+width/2,start.y);
			g2.fillRect(start.x, start.y, width,(int) hyp-25);
			g2.fillPolygon(new int[] {start.x-30+width/2, start.x+width/2, start.x + 30 + width/2}, new int[] {(int)(start.y + hyp-50), (int)(start.y+hyp), (int)(start.y+hyp-50)}, 3);
			g2.rotate(-rotate,start.x+width/2,start.y);
		} catch (ArithmeticException ae) {
			// dont draw
			System.out.println("Arithmetic Exception caught");
		}
	}
	
	public Point getAttackingPoint(){
		return start;
	}
	
	public Point getDefendingPoint(){
		return end;
	}
	
	public int endX(){
		return end.x;
	}
	
	public int endY(){
		return end.y;
	}
}
