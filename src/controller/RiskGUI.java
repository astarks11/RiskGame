package controller;
/*
 * Class: RiskGUI
 * 
 * Authors: Steven Cleary, Matthew Drake, Alex Starks
 * 
 * Description:
 * This class is the controller for all of the main screens. It will allow the user
 * to interact with the risk game. 
 */
import java.awt.Color;
import java.awt.Insets;

import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;


import view.*;
import model.*;
/*
 * this acts the controller for the Risk game
 * in iteration 2 it will become more controller like when we add a way to switch between views inside the gui
 */
public class RiskGUI extends JFrame{
	
	public static void main(String[] args){
		RiskGUI rg = new RiskGUI();
		rg.setVisible(true);
		//rg.start();
	}
	
	private int width,height;
	private MainMenu mainMenu;
	private GameView gameView;
	private RiskGame risk;
	private JPanel currentView;
	private Insets insets;
	private Player humanPlayer;
	private ArrayList<Player> aiPlayers;
	private Map gameMap; 
	private TestGameBoard temp;
	private GameOver gameOver;
	
	public RiskGUI(){
		
		// ------------------------- set jframe -----------------------------
		width = Toolkit.getDefaultToolkit().getScreenSize().width;
		height = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(true);
		this.setSize(width, height);
		insets = getInsets();		
		gameMap = null;
		// ------------------------- call initialize methods -----------------------
		//createMapAndPlayers();
		//initializeNewGame();
		initViews();
		//addObservers();
		setView(mainMenu);

		}
	
	
	/*
	 * starts the ai loop if the human player is not playing
	 */
	public void start(){
		createMapAndPlayers();
		initializeNewGame();
		addObservers();
		setView(gameView);
		
		if(humanPlayer ==  null && currentView == gameView){
			risk.startGameAI(1000);
			risk.runAiLoop(1000);
			System.out.println(risk.getWinner().getColor()+" wins!!" + risk.getWinner().getAI().getClass());
			System.exit(0);
		}
		
	}
	
	private void initViews(){
		temp = new TestGameBoard(Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height,this);
		
		mainMenu = new MainMenu(insets, width,height,this);
		temp.setBounds(insets.left,insets.top,width,height);
		mainMenu.setBounds(insets.left,insets.top,width,height);

	
	}

	/*
	 * adds things that need to observer the game to the game
	 */
	private void addObservers() {
		risk.addObserver(mainMenu);
		risk.addObserver(gameView);
		risk.addObserver(gameOver);
	}

	/*
	 * changes the view
	 */
	public void setView(JPanel newView) {
		if (currentView != null)
			remove(currentView);		
		currentView = newView;
		//((Observer)newView).update(risk,"");
		add(currentView);
		currentView.repaint();
		validate();
		revalidate();
		System.out.println("view set");
	}
	
	public void setMap(Map map) { 
		this.gameMap = map;
	}

	public JPanel getGameView() {
		return gameView;
	}

	public void endGame(){
		setView(gameOver);
	}
		
	public TestGameBoard getMapCreator(){
		return temp;
	}
	public JPanel getMainMenuView() {
		return mainMenu;
	}
	public void setPlayer(Player p)
	{
		humanPlayer = p;
	}
	public void setAI(ArrayList<Player> ai)
	{
		aiPlayers = ai;
		//initializeNewGame();
	}
	/* ---------------------------------------------
	 * Method: initializeGame()
	 * Purpose: Called before start is run, this creates a new game object with the
	 * set map, humanPlayers, aiPlayers. Also, create new gameView. sets gameMaps users
	 * -------------------------------------------- */
	public void initializeNewGame() {
		risk = new RiskGame(gameMap,humanPlayer,aiPlayers);
		gameMap.setUpUsers(humanPlayer, aiPlayers);
		gameView = new GameView(new Point(insets.left,insets.top),new Point(width,height),risk,this);
		gameView.setBounds(insets.left,insets.top,width,height);
		gameOver = new GameOver(risk,new Point(insets.left,insets.top),new Point(width,height));
		gameOver.setBounds(insets.left, insets.top, width, height);
	}
	/* --------------------------------------------
	 * Method: createMapAndPlayers()
	 * Purpose: this is called in the constructor to initialize the values of
	 * human, aiPlayers, and Map
	 * -------------------------------------------*/
	private void createMapAndPlayers() {
		
		// create map

		if (gameMap == null)
			this.gameMap = new Map(new Point(insets.left,insets.top),new Point(width- width/GameView.sidePanelSizeFactor(),height),1);		

		// create players
		//humanPlayer = null; 
		if (aiPlayers == null) {
		aiPlayers = new ArrayList<Player>();
		aiPlayers.add(new Player(Color.GREEN, new BeginnerAI()));
		aiPlayers.add(new Player(Color.YELLOW, new BeginnerAI()));
		aiPlayers.add(new Player(Color.RED, new ExpertAI()));
		}
		// reset game
		//setPlayer(null);
		setAI(aiPlayers);
	}
	
	public void removePlayer()
	{
		humanPlayer = null;
	}



}
