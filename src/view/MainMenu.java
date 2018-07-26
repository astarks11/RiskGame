package view;
/*
 * Class: RiskGUI
 * 
 * Authors: Steven Cleary, Matthew Drake, Alex Starks
 * 
 * Description:
 * This class is main menu for the game, it allows users to change options before the game 
 * begins, from choosing their color, to setting the other players, and changing the map. 
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import controller.*;
import model.*;

/*
 * iteration 2
 */
public class MainMenu extends JPanel implements Observer {

	private Title title;
	private Insets insets;
	private int width, height, borderx, bordery;
	private Point titleInit, buttonSize, buttonsStart;
	private JButton start, settings, otherPlayers, map, chooseColor, removePlayer;
	private ArrayList<JButton> buttons;
	private RiskGUI risk;
	private MainMenu hack;
	private boolean firstStart;
	private SetPlayerColor setPlayerGui;
	private SetArtificialPlayers setAIplayers;
	public boolean playerAdded;

	public MainMenu(Insets insets, int width, int height, RiskGUI risk) {
		hack = this;
		playerAdded = false;
		this.insets = insets;
		titleInit = new Point(insets.left + width / 3, insets.top + height / 3);
		this.width = width;
		this.height = height;
		
		this.risk = risk;
		borderx = width / 50;
		bordery = height / 40;
		buttonSize = new Point(insets.left + width / 10, insets.top + height / 20);
		buttonsStart = new Point(insets.left + borderx, titleInit.y + bordery);
		setLayout(null);
		title = new Title(new Point(insets.left + borderx, insets.top + bordery), width - 2 * borderx,
				titleInit.y - insets.top);

		firstStart = true;
		buttons = new ArrayList<JButton>();

		start = new JButton("Start");
		buttons.add(start);

		settings = new JButton("Create Map");
		buttons.add(settings);
		
		map = new JButton("Reset Map");
		buttons.add(map);

		otherPlayers = new JButton("Set AI");
		buttons.add(otherPlayers);
		
		chooseColor = new JButton("Player Color");
		buttons.add(chooseColor);
		
		removePlayer = new JButton("Remove Player");
		buttons.add(removePlayer);

		drawButtons();

	}

	private void drawButtons() {
		ButtonListener bl = new ButtonListener();
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).addActionListener(bl);
			buttons.get(i).setBounds(buttonsStart.x, buttonsStart.y + (i * (bordery + buttonSize.y) + bordery),
					buttonSize.x, buttonSize.y);
			buttons.get(i).setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.RED));
			add(buttons.get(i));
		}

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		this.setBackground(Color.BLUE);
		g.setColor(new Color(0, 191, 255));
		g.fillRect(insets.left + borderx, insets.top + bordery, width - 2 * borderx, titleInit.y - insets.top);
		title.draw(g);
		start.repaint();
	}

	@Override
	public void update(Observable arg0, Object arg1) {

	}

	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton buttonClicked = (JButton) e.getSource();
			if (buttonClicked == start) {
				startGame();
			} else if (buttonClicked == settings) {
				risk.setView(risk.getMapCreator());
			} else if (buttonClicked == otherPlayers) {
				setAIplayers = new SetArtificialPlayers(risk,otherPlayers,playerAdded);
			} else if (buttonClicked == map) {
				Map gameMap = new Map(new Point(insets.left, insets.top),
						new Point(width - width / GameView.sidePanelSizeFactor(), height), 1);
				risk.setMap(gameMap);
			} else if (buttonClicked == chooseColor) {
				playerAdded = true;
				setPlayerGui = new SetPlayerColor(risk);
				
			} else if (buttonClicked == removePlayer) {
				playerAdded = false;
				risk.removePlayer();
			}

		}

	}
	private void startGame() {
		//risk.setView(risk.getGameView());

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				risk.start();
			}

		});
		t.start();
	}

}
