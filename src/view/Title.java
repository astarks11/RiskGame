package view;
/*
 * Class: Title
 * 
 * Authors: Steven Cleary
 * 
 * Description:
 * This class draws the title for the main menu 
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
/*
 * iteration 2
 */
public class Title {
	private int width, height;
	private Point init;
	public Title(Point init, int width, int height){
		this.width = width;
		this.height = height;
		this.init = init;
	}

	public void draw(Graphics g){

		Font font = new Font("Arial Black", Font.BOLD, 240);
		g.setFont(font);

		FontMetrics fm = g.getFontMetrics(font);
		int stringWidth = fm.stringWidth("RISK");
		int stringHeight = fm.getHeight();
		
		g.setColor(Color.YELLOW);
		g.drawString("RISK", init.x+(width/2)-(stringWidth/2), init.y+(height/2)+(stringHeight/4));

	}
}
