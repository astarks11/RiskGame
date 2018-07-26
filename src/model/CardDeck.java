package model;
/*
 * Class: CardDeck
 * 
 * Authors: Alex Starks, Matthew Drake
 * 
 * Description:
 * This class contains the cards that get used in the game
 * It gives a random card to a player if they take over a
 * territory.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardDeck {

	// -------------------------------------
	// instance variables
	// -------------------------------------
	private List<Card> cardDeck; // the RISK card deck
	private List<Territory> terrs; // list of territories
	private int tradeIns; // number of trade - ins
	private int lastTradeInReward; // number of the last tradeins reward
	
	/*--------------------------------------------------
	 * Constructor: Creates each type of army type
	 * for each individual territory. Then adds 2 wildcards.
	 * 
	 * NOTES: This constructor assume each territory in the 
	 * list is unique. A deck is made based on the number
	 * of 
	 * ------------------------------------------------ */
	public CardDeck(List<Territory> terrs)
	{
		this.terrs = terrs;
		this.cardDeck = new ArrayList<Card>();
		this.tradeIns = 0;
		this.lastTradeInReward = 0;
		
		
		
		putBackCards();
		
	}
	
	
	
	/* ------------------------------------------
	 * Method: getCard()
	 * Purpose: This method uses javas Random class
	 * to pick a random card out of a non-shuffled
	 * deck. This is done to mimic a draw from
	 * a shuffled deck.
	 * -----------------------------------------*/
	public Card getCard() {
		Random rand = new Random();
		int pick = rand.nextInt(cardDeck.size());
		Card retVal = cardDeck.get(pick);
		cardDeck.remove(pick);
		if(cardDeck.size() == 0)
		{
			putBackCards();
		}
		return retVal;
	}
	public void putBackCards()
	{
		cardDeck = new ArrayList<Card>();
		Random rand = new Random();
		int num = 0; // random number
		int lastNum = 0; // last number generated
		int numBefore = 0; // number before last num
		for (Territory t : terrs)
		{
			// ensure each card made will be different than the last two
			// this ensures that there are an even amount of types among the territories
			while (num == lastNum || num == numBefore)
			{
				num = rand.nextInt(3)+1;
			}
			numBefore = lastNum;
			lastNum = num;
			
			if (num == 1)
				cardDeck.add(new Card(t.getID(),Army.INFANTRY));
			if (num == 2)
				cardDeck.add(new Card(t.getID(),Army.CALVARY));
			if (num == 3)
				cardDeck.add(new Card(t.getID(),Army.ARTILLERY));
		}
		cardDeck.add(new Card(-1,Army.WILDCARD));
		cardDeck.add(new Card(-1,Army.WILDCARD));
	}
	
	
	/*
	 * ----------------------------------------------- 
	 * Method: tradeCards()
	 * Purpose: this method will take a player and three cards and determine if
	 * the players is able to receive any reinforcements from the cards. Then
	 * will return the number of reinforcements gained from the trade in.
	 * ----------------------------------------------
	 */
	public int tradeCards(Player player, Card card1, Card card2, Card card3) {
		int infantryEarned = 0;
		int sameTypes = 0;

		if (card1 == null || card2 == null || card3 == null)
			return 0;
		// if card1 == card2
		if (card1.getType().equals(card2.getType()))
			sameTypes++;
		// if card1 == card3
		if (card1.getType().equals(card3.getType()))
			sameTypes++;
		// if card2 == card3
		if (card2.getType().equals(card3.getType()))
			sameTypes++;
		// if a card is same as wildcard
		if (card1.getType().equals(Army.WILDCARD))
			sameTypes++;
		if (card2.getType().equals(Army.WILDCARD))
			sameTypes++;
		if (card3.getType().equals(Army.WILDCARD))
			sameTypes++;
		
		if (sameTypes != 3 && sameTypes != 0)
			return 0;
			
		// 1st gets 4
		if (tradeIns == 0)
		{
			tradeIns++;
			infantryEarned = 4;
		}
		// 2nd gets 6
		if (tradeIns == 1)
		{
			tradeIns++;
			infantryEarned = 6;
		}
		// 3rd gets 8
		if (tradeIns == 2)
		{
			tradeIns++;
			infantryEarned = 8;
		}
		// 4th gets 10
		if (tradeIns == 4)
		{
			tradeIns++;
			infantryEarned = 10;
		}
		// 5th gets 12
		if (tradeIns == 5)
		{
			tradeIns++;
			infantryEarned = 12;
		}
		// 6th gets 15
		if (tradeIns == 6)
		{
			tradeIns++;
			infantryEarned = 15;
			lastTradeInReward += 15;
		}
		// 7th gets 20 etc..
		if (tradeIns >= 7)
		{
			tradeIns++;
			lastTradeInReward += 5;
			infantryEarned = lastTradeInReward;
		}

			// if any of the three cards in a territory owned by the player, give 2 more !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			List<Territory> playersTerrs = player.getControlledTerritories();
			for (Territory t : playersTerrs) {
				if (t.getID() == card1.getTerritoryID() || t.getID() == card2.getTerritoryID()
						|| t.getID() == card3.getTerritoryID()) {
					player.addArmy();
					player.addArmy();
					break;
				}
			}
			
			for (int i = 0; i < infantryEarned; i++)
				player.addArmy();
			
			player.removeCardFromPlayersHand(card1);
			player.removeCardFromPlayersHand(card2);
			player.removeCardFromPlayersHand(card3);
			
			tradeIns++;
		return infantryEarned;
	}
	
	// getter
	//public List<Card> testGetCardList() { return cardDeck; }
	public int getNumberOfTradeIns() { return tradeIns; }
	public int getSize() { return cardDeck.size(); }
	
	
	public void printDeck() {
		for (Card c : cardDeck)
			System.out.println(c.getTerritoryID() + "  " + c.getType());
	}
 }

	