 package model;

 /*
  * Class: Map
  * 
  * Authors: Steven Cleary, Alex Starks
  * 
  * Description:
  * This class contains all the information about the map.
  */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import view.RectangularTerritory;
import view.TerritoryShape;

/*
 * this class keeps a representation of the board that the risk game is played on, it should keep all data that is necessary for a board to hold
 * since we are using a computer screen rather than an actual board or board can change colors.
 */

public class Map {
	
	private ArrayList<TerritoryShape> map;
	private int width, height;
	private Point start;
	private int sides = 4;
	private static Color defaultMapColor = new Color(240,255,255);
	private List<Shape> neighborLines;
	private Color backGroundColor = Color.BLACK;
	
	public Color getBackGroundColor() {
		return backGroundColor;
	}

	public void setBackGroundColor(Color backGroundColor) {
		this.backGroundColor = backGroundColor;
	}

	public Map(Point start, Point size){
		map = new ArrayList<TerritoryShape>();
		this.start = start;
		this.width = size.x;
		this.height = size.y;
		map = new ArrayList<TerritoryShape>();
		neighborLines = new ArrayList<Shape>();
		
	}
	
	public Map(Point start, Point size, int number){
		map = new ArrayList<TerritoryShape>();
		this.start = start;
		this.width = size.x;
		this.height = size.y;
		if(number == 1)
			initMapOne();
		neighborLines = new ArrayList<Shape>();
		
	}
	
	public Map(Point start, Point size, int number, int sides){
		map = new ArrayList<TerritoryShape>();
		this.start = start;
		this.sides = sides;
		this.width = size.x;
		this.height = size.y;
		if(number == 1)
			initMapOne();
		neighborLines = new ArrayList<Shape>();
	}
	
	/*
	 * this generates a map that is in the form of a grid and uses sides to determine the number of sides
	 */
	private void initMapOne() {
		int count = 0;
		for(int i = 0; i < sides; i++){
			for(int j = 0; j < sides; j++){
				Territory temp = new Territory(count,Map.getDefaultColor());
				map.add(new RectangularTerritory(temp,Color.BLACK,new Point(start.x+i*(width/sides),start.y+j*(height/sides)),new Point(width/sides,height/sides)));
				count++;
			}
		}
		setUpNeighbors();
	}
	
	/*
	 * this method is a setter that sets the users onto the board
	 */
	public void setUpUsers(Player p1, ArrayList<Player> p2){
		System.out.println("setting up users: " +  map.size());
		ArrayList<Integer> terrIndexs = new ArrayList<Integer>();
		for(int i = 0; i < map.size();i++){
			terrIndexs.add(i);
		}
		Random rand = new Random();
		while(terrIndexs.size()>0){
			if(p1 != null){
				int next = rand.nextInt(terrIndexs.size());
				p1.addControlledTerritory(get(terrIndexs.get(next)));
				get(terrIndexs.get(next)).addArmy(p1);
				terrIndexs.remove(next);
			}
			for(int i = 0; i < p2.size(); i++){
				if(terrIndexs.size() < 1)
					break;
				int next = rand.nextInt(terrIndexs.size());
				p2.get(i).addControlledTerritory(get(terrIndexs.get(next)));
				get(terrIndexs.get(next)).addArmy(p2.get(i));
				terrIndexs.remove(next);
			}
		}
	}
	
	/*
	 * this connects makes it so all of the territories that should be neighbors are neighbors and should only be used by the initMapOne
	 */
	public void setUpNeighbors(){
		for(int i = 0; i < map.size(); i++){
			//System.out.print(i+" - ");
			if(i-sides >= 0){
				//System.out.print(i-sides+ " ");
				map.get(i).getTerritory().addNeighbor(map.get(i-sides).getTerritory());
			}if(i+sides <= map.size()-1){
				//System.out.print(i+sides + " ");
				map.get(i).getTerritory().addNeighbor(map.get(i+sides).getTerritory());
			}if(!(i%sides == sides-1)){
				//System.out.print(i+1+ " ");
				map.get(i).getTerritory().addNeighbor(map.get(i+1).getTerritory());
			}if(!(i%sides == 0)){
				//System.out.print(i-1+ " ");
				map.get(i).getTerritory().addNeighbor(map.get(i-1).getTerritory());
			}//System.out.println();
		}
	}
	
	public void drawMap(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		for (int i = 0; i < neighborLines.size(); i++){
			g2.draw(neighborLines.get(i));
		}
		for(int i = 0; i < map.size(); i++){
			map.get(i).draw(g);
		}
		
	}

	public int size(){
		return map.size();
	}
	
	/*
	 * returns the ArrayList of territory shapes so the view can draw the model
	 */
	public ArrayList<TerritoryShape> getList(){
		return map;
	}
	
	/*
	 * returns the territory at a specific place on the board
	 */
	public Territory getTerritory(int x, int y) {
		for(int i = 0; i < map.size(); i++){
			if(map.get(i).contains(x,y)){
				return map.get(i).getTerritory();
			}
		}
		return null;
	}
	
	/*
	 * returns the territory at a specific index in the list of territories
	 */
	public Territory get(int i){
		return map.get(i).getTerritory();
	}
	
	/*
	 * this is a setter that changes sets the highlight on a territory
	 */
	public boolean setTerritoryHighlight(int x, int y) {
		boolean success = false;
		for(int i = 0; i < map.size(); i++){
			if(map.get(i).contains(x,y)){
				map.get(i).setHoverColor(new Color(0,0,255,120 ));
				map.get(i).setHovered(true);
				success = true;
			}else if(map.get(i).getHovered()){
				map.get(i).setHovered(false);
				success = true;
			}
		}
		return success;
	}
	
	/*
	 * this returns the default map color
	 */
	public static Color getDefaultColor(){
		return defaultMapColor;
	}

	/*
	 * this sets the selected attribute in a territoryShape to false
	 */
	public void unSelect(Territory selected) {
		for(TerritoryShape t : map){
			if(t.getTerritory() == selected){
				t.setSelected(false);
			}
		}
		
	}

	/*
	 * sets the specific territory to selected
	 */
	public void select(Territory selected) {
		for(TerritoryShape t : map){
			if(t.getTerritory() == selected){
				t.setSelected(true);
			}
		}
		
	}

	/*
	 * sets all territories to unselected
	 */
	public void deselectAll() {
		for(TerritoryShape t : map){
			t.setSelected(false);
		}
		
	}

	/*
	 * returns whether the game board holds the specific location
	 */
	public boolean contains(int x, int y) {
		boolean contains = false;
		for(int i = 0; i < map.size(); i++){
			if(map.get(i).contains(x,y))
				contains = true;
		}
		return contains;
	}

	/*
	 * returns the territory at a specific location
	 */
	public Territory getTerritory(Point terr){
		return getTerritory(terr.x,terr.y);
	}
	
	/*
	 * sets the specified territory to the color passed in the paramater
	 */
	public void setTerritoryColor(Territory selected, Color color) {
		for(TerritoryShape t : map){
			if(t.getTerritory() == selected){
				t.setHoverColor(color);
			}
		}	
	}

	/*
	 * returns a boolean to tell whether the specific territory is the color
	 */
	public boolean isColor(Territory selected, Color highlight) {
		for(TerritoryShape t: map){
			if(t.getTerritory() == selected && t.getHoverColor() == highlight)
				return true;
		}
		return false;
	}
	
	/*
	 * sets the highlight of all territories to nothing 
	 */
	public void setNoHighlightAll(){
		for(TerritoryShape t: map){
			t.setHoverColor(null);
		}
	}


	public TerritoryShape getTerrShape(int index){
		return map.get(index);
	}

	
	public void setNeighborLines(List<Shape> lines) { neighborLines = lines; }
	public void setTerritories(ArrayList<TerritoryShape> terrs) { map = terrs; }

	
}
