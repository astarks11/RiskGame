package view;
/*
 * Class: TestGameBoard
 * 
 * Authors: Alex Starks
 * 
 * Description:
 * This class allows the user to create their own map in the game, which will
 * then be playable for the user and AI after.
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.RiskGUI;
import model.Map;
import model.Territory;

public class TestGameBoard extends JPanel {


	GameBoard gameBoard;
	UserUI userUI;

	public TestGameBoard(double width, double height, RiskGUI riskGUI) {

		this.setLayout(new BorderLayout());
		// create jpanel classes
		gameBoard = new GameBoard(width,height);
		userUI = new UserUI(gameBoard,riskGUI, width * 0.2, height);
		// set backgrounds
		gameBoard.setBackground(Color.BLACK);
		// create split pane object
		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		sp.setResizeWeight(0.2);
		sp.setEnabled(true);
		sp.setDividerSize(0);
		// add jpanels to split pane
		sp.add(userUI);
		sp.add(gameBoard);
		// add split pane to jframe
		this.add(sp, BorderLayout.CENTER);
	}
	// getters
	public Map getMap() { return userUI.getMap(); }
}

class UserUI extends JPanel {

	JPanel user;
	JPanel interaction;
	GameBoard gb;
	RiskGUI riskGUI;
	double width;
	double height;
	final int sliderHeight;
	final int userPanelHeight = 350;
	Map map;
	String currentShape;

	public UserUI(GameBoard gameBoard,RiskGUI riskGUI, double wi, double hei) {

		this.width = wi;
		this.height = hei;
		this.sliderHeight = (int) height / 10;
		this.gb = gameBoard;
		this.riskGUI = riskGUI;
		this.setLayout(null);
		

		createUserPanel();
		createInteractionPanel();
		this.add(interaction);
		this.add(user);

	}
	
	
	/* Method: createInteractionPane
	 * Purpose: This method creates the interaction pane
	 * that contains the buttons to set territories, neighbors, and
	 * territory size
	 */
	private void createInteractionPanel()
	{
				interaction = new JPanel();
				interaction.setBackground(Color.WHITE);
				interaction.setLayout(null);
				interaction.setLocation(0,userPanelHeight);
				interaction.setSize((int)width,(int)(height-userPanelHeight));
				
				int min = 0;
				int max = 500;
				int initial = 200;
				/*
				JTextField sliderText = new JTextField("Size:");
				sliderText.setFont(sliderText.getFont().deriveFont(20f));
				sliderText.setEnabled(false);
				sliderText.setLocation(0, 0);
				sliderText.setSize((int) width, sliderHeight);
				sliderText.setForeground(Color.BLACK);
				*/

				JSlider territoryXsizeSlider = new JSlider(JSlider.HORIZONTAL, min, max, initial);
				territoryXsizeSlider.setLocation(0, 0);//sliderHeight);
				territoryXsizeSlider.setSize((int) width, sliderHeight);
				territoryXsizeSlider.setMajorTickSpacing(100);
				territoryXsizeSlider.setMinorTickSpacing(50);
				territoryXsizeSlider.setPaintTicks(true);
				territoryXsizeSlider.setPaintLabels(true);

				JSlider territoryYsizeSlider = new JSlider(JSlider.HORIZONTAL, min, max, initial);
				territoryYsizeSlider.setMajorTickSpacing(100);
				territoryYsizeSlider.setMinorTickSpacing(50);
				territoryYsizeSlider.setPaintTicks(true);
				territoryYsizeSlider.setPaintLabels(true);
				territoryYsizeSlider.setLocation(0, sliderHeight * 1);
				territoryYsizeSlider.setSize((int) width, sliderHeight);

				//interaction.add(sliderText);
				interaction.add(territoryXsizeSlider);
				interaction.add(territoryYsizeSlider);



				JButton setNeighbor = new JButton("Create Neighbors");
				setNeighbor.setLocation(0, sliderHeight * 2);
				setNeighbor.setSize((int) width, sliderHeight);
				interaction.add(setNeighbor);
				
			
				
				JButton setTerritories = new JButton("Create Territories");
				setTerritories.setLocation(0, sliderHeight * 3);
				setTerritories.setSize((int) width, sliderHeight);
				interaction.add(setTerritories);
				
				setTerritories.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						gb.setTerritory();
					}
				});
				
				
				
				JButton circle = new JButton("Ellipse");
				circle.setLocation(0, sliderHeight * 4);
				circle.setSize((int) (width/2), (int) (sliderHeight/2));
				interaction.add(circle);
				
				circle.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						gb.setCurrentShape("circle");
						gb.setTerritory();
					}
				});
				
				JButton square = new JButton("Rectangle");
				square.setLocation((int)(width/2), sliderHeight * 4);
				square.setSize((int) (width/2), (int) (sliderHeight/2));
				interaction.add(square);
				
				square.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						gb.setCurrentShape("square");
						gb.setTerritory();
					}
				});

				setNeighbor.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						gb.setNeighbor();
					}

				});

				territoryYsizeSlider.addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						gb.setSizeY(territoryYsizeSlider.getValue());
					}

				});

				territoryXsizeSlider.addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						gb.setSizeX(territoryXsizeSlider.getValue());
					}
				});
		
	}
	
	
	/* Method: createUserPanel()
	 * Purpose: this method creates the jpanel that contains the 
	 * build map option
	 */
	private void createUserPanel()
	{
				user = new JPanel();
				user.setLayout(null);
				user.setBackground(Color.WHITE);
				user.setSize((int)width,(int)height);
				user.setLocation(0, 0);
				
				
				JButton makeMapButton = new JButton("Build Map");
				makeMapButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						map = new Map(new Point(getInsets().left,getInsets().top), new Point((int)(width - width/gb.getWidth()),(int)height),1);
						map.setTerritories(gb.getTerrs());
						map.setNeighborLines(gb.getNeighborLines());
						riskGUI.setMap(map);
						riskGUI.setView(riskGUI.getMainMenuView());
						
					}
				});
				
				makeMapButton.setSize((int)width, sliderHeight);
				makeMapButton.setLocation(0,0);
				user.add(makeMapButton);
				
				
				JButton resetMap = new JButton("Reset");
				resetMap.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						gb.resetTerritories();
					}
				});
				
				resetMap.setSize((int)width, sliderHeight);
				resetMap.setLocation(0,sliderHeight);
				user.add(resetMap);
	}
	
	// getter
	public Map getMap() { return map; }
	public String getCurrentShape() { return currentShape; }

} // userUI

class GameBoard extends JPanel {

	List<MyShape> terrs;
	String currentShape;
	ArrayList<TerritoryShape> territories;
	int size = 200;
	double width;
	double height;
	double terr1Size;
	double terr2Size;
	Shape ghostShape;
	double userInputSizeX;
	double userInputSizeY;
	boolean isExited;
	boolean setNeighbor;
	int terrIDcount;
	int mouseX;
	int mouseY;
	List<Shape> neighborLines;

	public GameBoard(double wid, double hei) {
		this.width = ((wid - (wid * 0.2)));
		//this.width = wid;
		this.height = hei;
		this.isExited = false;
		this.setNeighbor = false;
		this.terrIDcount = 0;
		this.mouseX = 0;
		this.mouseY = 0;
		this.currentShape = "circle";
		terr1Size = width / 8;
		terr2Size = width / 10;
		userInputSizeX = terr1Size;
		userInputSizeY = terr1Size;
		ghostShape = new Ellipse2D.Double(0, 0, terr1Size, terr1Size);
		repaint();
		terrs = new ArrayList<MyShape>();
		territories = new ArrayList<TerritoryShape>();
		neighborLines = new ArrayList<Shape>();
		MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
		this.addMouseListener(myMouseAdapter);
		this.addMouseMotionListener(myMouseAdapter);

	}
	
	public ArrayList<TerritoryShape> getTerrs()
	{ 
		
		for (TerritoryShape s : territories)
		{
			Territory t = s.getTerritory();
			System.out.print("ter:"+t.getID() + "\t");
			List<Territory> x = t.getNeighbors();
			for (Territory n : x)
			{
				System.out.print("n:"+n.getID()+" ");
			}
			System.out.println("\n");
		}
		return territories; 
	}
	public List<Shape> getNeighborLines() { return neighborLines;}

	@Override
	protected void paintComponent(Graphics grphcs) {
		super.paintComponent(grphcs);
		Graphics2D g2d = (Graphics2D) grphcs;

		 g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//System.out.println("----------------------TerrNeighborCheck--------------------------------");
		for (MyShape s : terrs) {
			//System.out.println(s.getTerritory().getID());
			for (Territory t : s.getTerritory().getNeighbors()) {
				//System.out.println(" " + t.getID());
			}
		}
		//System.out.println("----------------------TerrNeighborCheck--------------------------------");

		for (Shape s : neighborLines) {
			g2d.setColor(Color.GRAY);
			g2d.fill(s);
			g2d.draw(s);
		}

		for (MyShape s : terrs) {

			if (setNeighbor && s.isClicked) {
				g2d.setColor(Color.BLUE);
				g2d.fill(s.getShape());
				g2d.draw(s.getShape());
			} else {
				g2d.setColor(Color.GRAY);
				g2d.fill(s.getShape());
				g2d.draw(s.getShape());
			}

		}

		if (isExited)
			g2d.setColor(Color.WHITE);
		else
			g2d.setColor(Color.GRAY);
		g2d.fill(ghostShape);
		g2d.draw(ghostShape);

	}

	class MyMouseAdapter extends MouseAdapter {
		@Override
		public void mouseExited(MouseEvent e) {
			if (!setNeighbor) {
				if (currentShape.equals("circle"))
					ghostShape = new Ellipse2D.Double(width / 2, height / 2, userInputSizeX, userInputSizeY);
				else
					ghostShape = new Rectangle2D.Double(width / 2, height / 2, userInputSizeX, userInputSizeY);

				repaint();
				isExited = true;
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {

			Territory t = new Territory(terrIDcount++,Color.BLACK);
			if (!setNeighbor) {
				if (currentShape.equals("circle"))
					terrs.add(new MyShape(new Ellipse2D.Double(e.getX(), e.getY(), userInputSizeX, userInputSizeY),
						t));
				else
					terrs.add(new MyShape(new Rectangle2D.Double(e.getX(), e.getY(), userInputSizeX, userInputSizeY),
							t));
				

				TerritoryShape s;// = new TerritoryShape(t,Color.WHITE,new Point(e.getX(),e.getY()),new Point((int)userInputSizeX,(int)userInputSizeY));
				if (currentShape.equals("circle"))
					 s = new EllipseTerritory(t,Color.WHITE,new Point(e.getX(),e.getY()),new Point((int)userInputSizeX,(int)userInputSizeY));
					//s.addShape(new Ellipse2D.Double(e.getX(), e.getY(), userInputSizeX, userInputSizeY));
				else
					 s = new RectangularTerritory(t,Color.WHITE,new Point(e.getX(),e.getY()),new Point((int)userInputSizeX,(int)userInputSizeY));
					//s.addShape(new Rectangle2D.Double(e.getX(), e.getY(), userInputSizeX, userInputSizeY));

				territories.add(s);
				Double x = width / e.getX();
				Double y = height / e.getY();
				System.out.println("X:" + x + "  Y" + y);
				repaint();
			}
			if (setNeighbor) {
				Territory clickedTerr = null;
				Territory otherTerr = null;
				for (MyShape s : terrs) {
					if (s.getShape().contains(e.getX(), e.getY())) {
						otherTerr = s.getTerritory();
						if (clickedTerr != null) {
							if (!clickedTerr.isNeighbor(otherTerr)) {
								clickedTerr.addNeighbor(otherTerr);
							}
							if (!otherTerr.isNeighbor(clickedTerr)) {
								otherTerr.addNeighbor(clickedTerr);
								neighborLines.add(new Line2D.Double(mouseX, mouseY, e.getX(), e.getY()));
							}
						}
					}
					if (mouseX != 0 && mouseY != 0 && s.getShape().contains(mouseX, mouseY)) {
						clickedTerr = s.getTerritory();
						if (otherTerr != null) {
							if (!otherTerr.isNeighbor(clickedTerr)) {
								otherTerr.addNeighbor(clickedTerr);
							}
							if (!clickedTerr.isNeighbor(otherTerr)) {
								clickedTerr.addNeighbor(otherTerr);
								neighborLines.add(new Line2D.Double(mouseX, mouseY, e.getX(), e.getY()));
							}
						}
					}
				}

				if (mouseX == 0 && mouseY == 0) {
					mouseX = e.getX();
					mouseY = e.getY();
				} else {

					mouseX = 0;
					mouseY = 0;
				}
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (!setNeighbor) {
				if (currentShape.equals("circle"))
					ghostShape = new Ellipse2D.Double(e.getX(), e.getY(), userInputSizeX, userInputSizeY);
				else
					ghostShape = new Rectangle2D.Double(e.getX(), e.getY(), userInputSizeX, userInputSizeY);
			}
			if (setNeighbor && mouseX != 0 && mouseY != 0) {
				ghostShape = new Line2D.Double(mouseX, mouseY, e.getX(), e.getY());
			}
			
			for (MyShape s : terrs)
			{
				if (s.getShape().contains(e.getX(),e.getY()))
					s.setClicked();
				else
					s.notClicked();
			}
			isExited = false;
			repaint();

		}

	}

	class MyShape {

		Shape shape;
		boolean isClicked;
		int clicked;
		Territory terr;

		public MyShape(Shape s, Territory ter) {
			clicked = 0;
			shape = s;
			isClicked = false;
			terr = ter;
		}

		public void setClicked() { isClicked = true; }
		public boolean isClicked() { return isClicked; }
		public void notClicked() { isClicked = false;}
		public Shape getShape() { return shape; }
		public int getNumClicked() { return clicked; }
		public void incrementClick() { clicked++; }
		public List<MyShape> getMyShapeList() { return terrs; }
		public Territory getTerritory() {return terr; }
	}

	public void setSizeX(double x) {
		userInputSizeX = x;
		ghostShape =  getCurrentShape();
		repaint();
	}

	public void setSizeY(double y) {
		userInputSizeY = y;
		ghostShape =  getCurrentShape();
		repaint();
	}

	public void setNeighbor() {
		setNeighbor = true;
		ghostShape = new Line2D.Double(0, 0, 0, 0);
		repaint();
	}

	public void setTerritory() {
		setNeighbor = false;
		ghostShape = getCurrentShape();
		isExited = true;
		repaint();
	}
	
	public void resetTerritories()
	{
		terrs = new ArrayList<MyShape>();
		territories = new ArrayList<TerritoryShape>();
		neighborLines = new ArrayList<Shape>();
		ghostShape = new Ellipse2D.Double(0,0,0,0);
		repaint();
	}
	
	
	public void setCurrentShape(String s) { currentShape = s; }
	private Shape getCurrentShape() 
	{
		if (currentShape.equals("circle"))
			return new Ellipse2D.Double(width / 2, height / 2, userInputSizeX, userInputSizeY);
		else
			return 
					new Rectangle2D.Double(width / 2, height / 2, userInputSizeX, userInputSizeY);
	}

}
