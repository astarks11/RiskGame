package view;
/*
 * Class: TerritoryShape
 * 
 * Authors: Steven Cleary
 * 
 * Description:
 * This class allows the territories to be drawn
 * on the game GUI.
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import model.Territory;

/*
 * this class is used for drawing the territories to the screen 
 */
public class TerritoryShape {
	private ArrayList<Shape> myShapes;
	private Color border, hoverColor;
	private Point p1,p2;
	private Territory territory;
	private boolean hovered = false;
	private boolean selected = false;
	private Point oldSize;
	private Point translate = null;
	private boolean iamcopy = false;
	
	public TerritoryShape(Territory territory,Color border, Point p1, Point p2){
		this.territory = territory;
		this.border = border;
		this.p1 = p1;
		this.p2 = p2;
		myShapes = new ArrayList<Shape>();
	}
	
	public TerritoryShape(TerritoryShape ts, Point p1, Point p2){
		iamcopy = true;
		this.territory = ts.getTerritory();
		this.border = ts.getBorder();
		this.p1 = p1;
		this.p2 = p2;
		oldSize = ts.getP2();
		this.myShapes = new ArrayList<Shape>();
		
		//translate = new Point((int)xdiff,(int)ydiff);
		
		relocate(ts);
	}
	
	public ArrayList<Shape> getShapes() {
		return myShapes;
	}
	
	private void relocate(TerritoryShape old){
		double xdiff = p1.x - old.getP1().x;
		double ydiff = p1.y - old.getP1().y;
		for(int i = 0; i < old.getShapes().size(); i++){
			Shape s = old.getShapes().get(i);
			Shape ns = null;
			double newX = s.getBounds().getX() + xdiff;
			double newY = s.getBounds().getY() + ydiff;
			System.out.println(s);
			if(s instanceof Rectangle){
				ns = new Rectangle((int)newX, (int)newY, (int)(p2.x*s.getBounds().getWidth()/oldSize.x), (int)(p2.y*s.getBounds().getHeight()/oldSize.y));
			}
			if(s instanceof Ellipse2D.Double){
				ns = new Ellipse2D.Double((int)newX, (int)newY, (int)(p2.x*s.getBounds().getWidth()/oldSize.x), (int)(p2.y*s.getBounds().getHeight()/oldSize.y));
			}
			addShape(ns);
		}

	}

	private void resizeAndLocate() {
		for(Shape s: myShapes){
			//s.set
		}
	}

	public Territory getTerritory() {
		return territory;
	}

	public void setTerritory(Territory territory) {
		this.territory = territory;
	}

	public boolean contains(double x, double y){
		for(int i = 0; i < myShapes.size();i++){
			if(myShapes.get(i).contains(x, y))
				return true;
		}
		return false;
	}
	
	public void addShape(Shape newShape){
		myShapes.add(newShape);
	}
	
	public void draw(Graphics g){
		
		Graphics2D g2 = (Graphics2D)g;
		//if(translate != null){
			//AffineTransform at = new AffineTransform();
			//at.scale(0.5,0.5);
			//at.translate(translate.x, translate.y);
			//g2.setTransform(at);
			//g2.translate(translate.x, translate.y);
		//}
		g2.setColor(border);
		g2.fill(myShapes.get(0));
		g2.setColor(territory.getOwnerColor());
		if(selected == true)
			g2.setColor(g2.getColor().darker());
		for(int i = 1; i < myShapes.size(); i++){
			//System.out.println("size:"+myShapes.get(i).getBounds());
			g2.fill(myShapes.get(i));

		}
		Font font = new Font("Arial Black", Font.PLAIN, 32);
		g2.setFont(font);
		FontMetrics fm = g.getFontMetrics(font);
		String armies = Integer.toString(territory.getArmySize());
		int stringWidth = fm.stringWidth(armies);
		int stringHeight = fm.getHeight();
		g2.setColor(Color.BLACK);
		g2.fillRect(p1.x + p2.x/2 - (stringWidth+20)/2, p1.y + p2.y/2 - (stringHeight+20)/2, stringWidth + 20, stringHeight + 20);
		g2.setColor(Color.WHITE);
		g2.fillRect(p1.x + p2.x/2 - (stringWidth+10)/2, p1.y + p2.y/2 - (stringHeight+10)/2, stringWidth + 10, stringHeight + 10);
		g2.setColor(Color.BLACK);
		g.drawString(armies, p1.x + p2.x/2 - stringWidth/2, p1.y + p2.y/2 + stringHeight/4);
		if(hoverColor != null){
			g2.setColor(hoverColor);
			g2.fill(myShapes.get(0));
		}
		//if(translate!=null){
			//System.out.println("g2 reset");
			//g2.setTransform(new AffineTransform());
		//}
	}



	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}

	public void setHovered(boolean b) {
		hovered = b;
	}

	public boolean getHovered() {
		return hovered;
	}
	public void setSelected(boolean b) {
		selected = b;
	}

	public boolean getSelected() {
		return selected;
	}
	
	public void setHoverColor(Color color){
		hoverColor = color;
	}

	public Color getHoverColor() {
		return hoverColor;
	}
	public Color getBorder(){
		return border;
	}

	
}
