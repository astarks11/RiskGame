package model;
/*
 * Class: RiskGame
 * 
 * Authors: Steven Cleary, Matthew Drake, Alex Starks
 * 
 * Description:
 * This class is the main game controller, it asks AI's for their moves, and 
 * otherwise deals with everything game related, from trading cards, getting
 * reinforcements and so on.
 */
import java.awt.Point;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import javax.swing.JOptionPane;

/*
 * this class holds the most up to date and current version of the game, the idea behind it is to hold every piece of information about the game 
 * as current and up to date as possible, for example if we wanted to serialize this our goal is to have the user be able to pick up right where
 * he left off
 */
public class RiskGame extends Observable {

	// -------------------------------------
	// instance variables
	// -------------------------------------
	private List<Territory> territories; // list of territory objects
	private Player player; // human player object
	private List<Player> aiPlayers; // list of AI players
	private Battle battle; // set if battle occurs
	private CardDeck cardDeck; // risk card deck
	private int numberOfArmy;
	private Map map;
	private Dice firstRoll; // the dice object for the initial roll to see who goes first
	private Player winner; // winner of the game

	/*
	 * --------------------------------------------------------- Constructor:
	 * RiskGame Purpose: instantiate instance variables. Create game objects.
	 * 
	 * Parameters: map - a map object - player object ai - list of player
	 * objects ---------------------------------------------------------
	 */
	public RiskGame(Map map, Player player, List<Player> aiPlayers) {
		this.map = map;
		this.player = player;
		this.aiPlayers = aiPlayers;
		this.winner = null;
		territories = new ArrayList<Territory>();
		for (int i = 0; i < map.size(); i++) {
			territories.add(map.get(i));
		}
		this.cardDeck = new CardDeck(territories);
		initializeBoard();
	}

	/*
	 * ----------------------------------------------- 
	 * Method: initializeBoard()
	 * Purpose: To initialize the territories/armies
	 * ----------------------------------------------
	 */
	public void initializeBoard() {
		// -------------------------
		// number of army to deal
		// -------------------------
		int numberOfPlayers = 0;
		numberOfArmy = 0;
		if (player != null)
			numberOfPlayers++;
		numberOfPlayers += aiPlayers.size();
		if (numberOfPlayers == 2)// for testing purposes
			numberOfArmy = 40;
		if (numberOfPlayers == 3)
			numberOfArmy = 35;
		if (numberOfPlayers == 4)
			numberOfArmy = 30;
		if (numberOfPlayers == 5)
			numberOfArmy = 25;
		if (numberOfPlayers == 6)
			numberOfArmy = 20;

		// -----------------------------------
		// deal armies
		// ------------------------------------
		if (player != null)
			player.setArmy(numberOfArmy);
		for (Player p : aiPlayers) {
			p.setArmy(numberOfArmy);
		}
		notifyObservers();
	}

	/*
	 * --------------------------------------------- Method:getFirstMovePlayer()
	 * Purpose: This method generates random numbers for every player in the
	 * game and returns the object of the player who rolled the first highest
	 * number in the group ---------------------------------------------
	 */
	public Player getFirstMovePlayer() {

		firstRoll = new Dice();
		firstRoll.rollDie();
		Random rand = new Random();
		List<Integer> rolls = new ArrayList<Integer>();

		for (int i = 0; i < aiPlayers.size(); i++) {
			if (i == 0)
				rolls.add(firstRoll.getDice1());
			if (i == 1)
				rolls.add(firstRoll.getDice2());
			if (i == 2)
				rolls.add(firstRoll.getDice3());
			if (i == 3)
				rolls.add(firstRoll.getDice4());
			if (i == 4)
				rolls.add(firstRoll.getDice5());
			if (i == 5)
				rolls.add(firstRoll.getDice6());

		}
		if (player != null)
			rolls.add(firstRoll.getPlayerDice());

		for (int i = 0; i < rolls.size(); i++) {
			for (int j = 0; j < rolls.size(); j++) {
				if (rolls.get(i) < rolls.get(j))
					break;
				if (j == rolls.size() - 1) {
					if (j == aiPlayers.size() + 1)
						return player;
					return aiPlayers.get(i);
				}
			}
		}
		return null;
	}

	/*
	 * ----------------------------------------------- Method: tradeCards()
	 * Purpose: this method will take a player and three cards and determine if
	 * the players is able to receive any reinforcements from the cards. Then
	 * will return the number of reinforcements gained from the trade in.
	 * ----------------------------------------------
	 */
	public int tradeCards(Player player, Card card1, Card card2, Card card3) {
		return cardDeck.tradeCards(player, card1, card2, card3);
	}

	/*
	 * ---------------------------------------------------------
	 * Method:isGameOver() Purpose: To check all of the territories and see if
	 * only one person owns all of the territories, if so true is returned as
	 * the game is over. If game is over, winner var is set.
	 * ------------------------------------------------------
	 */
	public boolean isGameOver() {

		Player owner1 = map.get(0).getOwner(); // owner of first territories
		winner = null;

		for (int i = 0; i < territories.size(); i++) {

			if (territories.get(i).getOwner() != owner1) // &&
															// map.get(i).getOwner()
															// != null)
				return false;
		}
		winner = owner1;
		return true;
	}

	/*
	 * 
	 * // getter. public Battle getBattle() { return battle; } public Card
	 * getCard() { return cardDeck.getCard(); } public CardDeck
	 * testGetCardDeck() { return cardDeck; } public Dice getInitialRoll() {
	 * return firstRoll; }
	 */
	public Player getPlayer() {
		return player;
	}

	public List<Player> getAiPlayers() {
		return aiPlayers;
	}

	public Map getMap() {
		return map;
	}

	public int getArmiesPerRound() {
		return numberOfArmy;
	}

	public Player getWinner() {
		return winner;
	}

	/*
	 * --------------------------------------------- Method: runAILoop()
	 * Purpose: This method is called on an all AI game to run the game till
	 * completion ----------------------------------------------
	 */
	public void runAiLoop(int shouldSleep) {

		int counter = 0;
		while (!this.isGameOver()) {

			for (Player p : aiPlayers) {

				if (this.isGameOver())
					return;

				// ---------------------- reinforcements ---------------------
				p.calculateReinforcements();
				p.getAI().getReinforcements(p);
				if (p.getAI().tradeInCards(p, this))
					p.getAI().getReinforcements(p);

				setChanged();
				notifyObservers();
				int sleepAmount = 0;
				// ---------------------- reinforcements ---------------------
				try {

					sleepAmount = shouldSleep;
					Thread.sleep(sleepAmount);
				} catch (Exception e) {
				}
				// ---------------------- battle -----------------------------
				boolean earnedCard = false;
				int temp = 0;
				while (p.getAI().endTurn(p)) {
					if (temp++ == 30)
						break;
					Move move = p.getAI().getMove(p);
					if (move == null)
						break;

					while (move.getAttackingTerritory().getArmySize() != 1
							&& move.getDefendingTerritory().getArmySize() != 0) {

						int adiceAmount = p.getAI().getDiceRoll(move.getAttackingTerritory().getArmySize() - 1,
								move.getDefendingTerritory().getArmySize(), true);
						int ddiceAmount = (move.getDefendingTerritory().getOwner().getAI().getDiceRoll(
								move.getAttackingTerritory().getArmySize() - 1,
								move.getDefendingTerritory().getArmySize(), false));
						Battle battle = new Battle(move.getAttackingTerritory(), move.getDefendingTerritory());
						if (!battle.engageCarnage(adiceAmount, ddiceAmount,
								move.getAttackingTerritory().getArmySize() - 1))
							break;
						setChanged();
						notifyObservers();
					}

					if (move.getDefendingTerritory().getOwner() == p) {
						earnedCard = true;
					}
					setChanged();
					notifyObservers();

					try {
						Thread.sleep(sleepAmount);
					} catch (Exception e) {

					}

				}

				if (earnedCard) {
					p.addCardToPlayersHand(cardDeck.getCard());
				}

				try {
					sleepAmount = shouldSleep * 10;
					Thread.sleep(sleepAmount);

					Fortification fortification = p.getAI().getFortification(p);
					if (fortification != null)
						fortification.fortify();
					setChanged();
					notifyObservers();
					// -------------------- fortify --------------------------

					sleepAmount = shouldSleep;
					Thread.sleep(sleepAmount);
				} catch (Exception e) {
					System.out.println("thread sleep exception");
				}
			}
		}
	}

	public void aiTurns(int sleep) {
		for (Player p : aiPlayers) {
			if (this.isGameOver())
				return;
			// ---------------------- reinforcements ---------------------
			p.calculateReinforcements();
			p.getAI().getReinforcements(p);
			if (p.getAI().tradeInCards(p, this))
				p.getAI().getReinforcements(p);
			setChanged();
			notifyObservers();
			// ---------------------- reinforcements ---------------------
			try {
				Thread.sleep(sleep);
				// ---------------------- battle -----------------------------
				boolean earnedCard = false;
				int temp = 0;
				while (p.getAI().endTurn(p)) {
					if (temp++ == 30)
						break;
					Move move = p.getAI().getMove(p);
					if (move == null)
						break;
					System.out.println("test");
					while (move.getAttackingTerritory().getArmySize() != 1
							&& move.getDefendingTerritory().getArmySize() != 0) {
						int adiceAmount = p.getAI().getDiceRoll(move.getAttackingTerritory().getArmySize() - 1,
								move.getDefendingTerritory().getArmySize(), true);
						int ddiceAmount = 0;
						if (move.getDefendingTerritory().getOwner().getAI() != null)
							ddiceAmount = (move.getDefendingTerritory().getOwner().getAI().getDiceRoll(
									move.getAttackingTerritory().getArmySize() - 1,
									move.getDefendingTerritory().getArmySize(), false));
						else {
							if (move.getDefendingTerritory().getArmySize() == 1) {
								ddiceAmount = 1;
							} else {
								int userInput = JOptionPane.showConfirmDialog(null, "Defend With Two Troops?");
								if (userInput == JOptionPane.YES_OPTION) {
									ddiceAmount = 2;
								} else {
									ddiceAmount = 1;
								}
							}
						}
						Battle battle = new Battle(move.getAttackingTerritory(), move.getDefendingTerritory());
						if (!battle.engageCarnage(adiceAmount, ddiceAmount,
								move.getAttackingTerritory().getArmySize() - 1))
							break;
						setChanged();
						notifyObservers();
					}
					if (move.getDefendingTerritory().getOwner() == p) {
						earnedCard = true;
					}
					setChanged();
					notifyObservers();

					Thread.sleep(sleep);

				}
				if (earnedCard) {
					p.addCardToPlayersHand(cardDeck.getCard());
				}
				Thread.sleep(sleep);
				Fortification fortification = p.getAI().getFortification(p);
				if (fortification != null)
					fortification.fortify();
				setChanged();
				notifyObservers();
				// -------------------- fortify --------------------------
				Thread.sleep(sleep);
			} catch (Exception e) {
			}
		}

	}

	// these variables record the current state of the game
	private Territory selected = null;
	private boolean canDeploy = false;
	private boolean canBattle = false;
	private boolean canFortify = false;
	private boolean canTrade = false;

	/*
	 * increments the players army
	 */
	public void troopDeployed() {
		if (selected == null || !canDeploy)
			return;
		selected.addArmy(player);
		setChanged();
		notifyObservers();
	}

	/*
	 * sets the selcted territory to the territory passed in the paramater
	 */
	public void selectTerritory(Territory t) {
		if (selected != null) {
			map.unSelect(selected);
		}
		System.out.println("selected");
		selected = t;
		map.select(t);
		setChanged();
		notifyObservers();
	}

	/*
	 * decrements the players army
	 */
	public void unDeploy() {
		if (selected == null || !canDeploy)
			return;
		selected.decrementArmy();
		setChanged();
		notifyObservers();

	}

	/*
	 * sets the canDeploy boolean to false
	 */
	public void setCanDeploy(boolean b) {
		canBattle = false;
		canFortify = false;
		canTrade = false;
		canDeploy = b;
	}

	public boolean getCanDeploy() {
		return canDeploy;
	}

	/*
	 * sets the can battle boolean to false
	 */
	public void setCanBattle(boolean b) {
		canDeploy = false;
		canFortify = false;
		canTrade = false;
		canBattle = b;
	}

	/*
	 * gets the can battle boolean
	 */
	public boolean getCanBattle() {
		return canBattle;
	}

	/*
	 * sets the can fortify boolean to false
	 */
	public void setCanFortify(boolean b) {

		if (getCard)
			player.addCardToPlayersHand(cardDeck.getCard());

		canBattle = false;
		canDeploy = false;
		canTrade = false;
		canFortify = b;
	}

	/*
	 * sets the can trade boolean to false
	 */
	public void setCanTrade(boolean b) {
		canDeploy = false;
		canBattle = false;
		canFortify = false;
		canTrade = b;
	}

	/*
	 * sets the map to unselect all
	 */
	public void deSelectAll() {
		map.deselectAll();
		setChanged();
		notifyObservers();
	}

	private Territory attackingTerritory = null, defendingTerritory = null;
	private int attackAmount = 0;

	/*
	 * returns whether an attack can be made, and can change colors on the
	 * territory if an attack can be made
	 */
	public boolean canAttack(Point attackingPoint, Point defendingPoint) {
		attackingTerritory = map.getTerritory(attackingPoint);
		defendingTerritory = map.getTerritory(defendingPoint);
		if (attackingTerritory.isNeighbor(defendingTerritory))
			if (player.canAttack(defendingTerritory)) {
				setChanged();
				notifyObservers();
				return true;
			}
		return false;
	}

	/*
	 * returns the amount of players on the territory that is attacking
	 */
	public int getAttackingSize() {
		if (attackingTerritory == null)
			return 0;
		return attackingTerritory.getArmySize();
	}

	/*
	 * returns the amount of armies that the player is attacking with
	 */
	public int getAttackAmount() {
		return attackAmount;
	}

	/*
	 * ai loop for run6bots
	 */
	public void startGameAI(int shouldSleep) {
		int temp = aiPlayers.get(0).getArmySize();
		int sleepAmount = shouldSleep;
		for (int i = 0; i < temp; i++) {
			for (Player p : aiPlayers) {
				try {
					Thread.sleep(sleepAmount);
				} catch (Exception e) {
				}

				p.setArmy(1);
				p.getAI().getReinforcements(p);
				setChanged();
				notifyObservers();
			}
		}
	}

	/*
	 * adds an attacker to the board
	 */
	public void addAttacker() {
		if(attackingTerritory == null)
			return;
		if (attackAmount >= 3 || attackAmount + armiesToAdd >= attackingTerritory.getArmySize())
			return;
		attackAmount++;
		setChanged();
		notifyObservers();
	}

	/*
	 * subtracts an attacker from the board
	 */
	public void subAttacker() {
		if(attackingTerritory == null)
			return;
		if (attackAmount < 1)
			return;
		attackAmount--;
		setChanged();
		notifyObservers();
	}

	private boolean getCard = false;

	/*
	 * updates the board after the player makes an attack this does not affect
	 * iteration 1, this is hard coded to test the gui for iteration 2 run6bots
	 * does not use this
	 */
	public void makeAttack() {
		if(attackingTerritory == null)
			return;
		Battle battle = new Battle(attackingTerritory, defendingTerritory);
		int d = defendingTerritory.getOwner().getAI().getDiceRoll(attackingTerritory.getArmySize() - 1,
				defendingTerritory.getArmySize(), false);
		if(armiesToAdd == 0)
			battle.engageCarnage(attackAmount, d, 1);
		else
			battle.engageCarnage(attackAmount, d, armiesToAdd);
		if (defendingTerritory.getOwner() == player)
			getCard = true;
		attackAmount = 0;
		armiesToAdd = 0;
		attackingTerritory = null;
		defendingTerritory = null;
		setChanged();
		notifyObservers();

	}

	private Territory firstTerritory, secondTerritory;
	private int moveAmount = 0;

	public boolean getCanFortify() {
		return canFortify;
	}

	public boolean canFortify(Point firstPoint, Point defendingPoint) {
		firstTerritory = map.getTerritory(firstPoint);
		secondTerritory = map.getTerritory(defendingPoint);
		if (firstTerritory.isNeighbor(secondTerritory)) {
			if (player.ownsTerritory(secondTerritory)) {
				setChanged();
				notifyObservers();
				return true;
			}
		}
		return false;
	}

	public void addFortifier() {
		if (moveAmount >= firstTerritory.getArmySize() - 1)
			return;
		moveAmount++;
		setChanged();
		notifyObservers();
	}

	public void subFortifier() {
		if (moveAmount < 1)
			return;
		moveAmount--;
		setChanged();
		notifyObservers();
	}

	private boolean arrowToRemove = false;

	public void makeFortification() {
		System.out.println(moveAmount);
		Fortification fortifier = new Fortification(player, firstTerritory, secondTerritory, moveAmount);
		fortifier.fortify();
		firstTerritory = null;
		secondTerritory = null;
		moveAmount = 0;
		canFortify = false;
		arrowToRemove = true;
		setChanged();
		notifyObservers();
	}

	public boolean shouldRemoveArrow() {
		return arrowToRemove;
	}

	/*
	 * returns whether the player is making an attack
	 */
	public boolean attackInProgress() {
		return (attackingTerritory == null || defendingTerritory == null);
	}

	public boolean fortifyInProgress() {
		return (firstTerritory == null || secondTerritory == null);
	}

	public String getFortifyAmount() {
		return "" + moveAmount;
	}

	public int getFortifySize() {
		if (firstTerritory == null)
			return 0;
		return firstTerritory.getArmySize();
	}

	private boolean cursorIsOnMap = false;

	public boolean isCursorOnMap() {
		return cursorIsOnMap;
	}

	public void cursorIsOffMap() {
		cursorIsOnMap = false;
		setChanged();
		notifyObservers();
	}

	public void cursorIsOnMap() {
		cursorIsOnMap = true;
	}

	private boolean startingGame = true;

	public boolean getStartingGame() {
		return startingGame;
	}

	public void setStartingGame(boolean b) {
		startingGame = b;
		setChanged();
		notifyObservers();
	}

	public void removeArrow(boolean b) {
		arrowToRemove = false;

	}

	public void aiStart(int sleepAmount) {
		int temp = aiPlayers.get(0).getArmySize();

		for (Player p : aiPlayers) {
			try {
				Thread.sleep(sleepAmount);
			} catch (Exception e) {
			}

			p.setArmy(1);
			p.getAI().getReinforcements(p);
			setChanged();
			notifyObservers();
		}
	}

	private int armiesToAdd = 0;;
	
	public int getArmiesToAdd() {
		return armiesToAdd;
	}

	public int possibleDiceRolls() {
		if(attackingTerritory == null)
			return 0;
		if(attackingTerritory.getArmySize() > 3)
			return 3;
		if(attackingTerritory.getArmySize() > 2)
			return 2;
		if(attackingTerritory.getArmySize() > 1)
			return 1;
		return 0;
	}

	public int getArmiesToAddPossibly() {
		if(attackingTerritory == null)
			return 0;
		return attackingTerritory.getArmySize() - attackAmount;
	}

	/*
	 * adds an armytoadd to the board
	 */
	public void addArmieToAdd() {
		if(attackingTerritory == null)
			return;
		if (armiesToAdd + attackAmount >= attackingTerritory.getArmySize())
			return;
		armiesToAdd++;
		setChanged();
		notifyObservers();
	}

	/*
	 * subtracts an armytoadd from the board
	 */
	public void subArmyToAdd() {
		if(attackingTerritory == null)
			return;
		if (armiesToAdd < 1)
			return;
		armiesToAdd--;
		setChanged();
		notifyObservers();
	}

	public void addAllPossibleArmies() {
		armiesToAdd = getArmiesToAddPossibly();
		setChanged();
		notifyObservers();
	}
	
}
