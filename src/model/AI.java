package model;
/*
 * Class: AI
 * 
 * Authors: Matthew Drake
 * 
 * Description:
 * This class contains the methods that each AI must implement, as well as a
 * catch all method for trading in cards whenever possible.
 */
import java.util.ArrayList;
import java.util.List;

public abstract class AI
{
	
	public abstract int getDiceRoll(int attackingArmies, int defendingArmies, boolean attacker);
	public abstract Move getMove(Player player);
	public abstract Fortification getFortification(Player player);
	public abstract boolean endTurn(Player player); // return true if turn is over, false otherwise
	public abstract void getReinforcements(Player player);


	public boolean tradeInCards(Player player, RiskGame game)
	{
		List<Card> listOfCards = player.getCards();

		List<Card> infantryCards =  new ArrayList<Card>(); 
		List<Card> calvaryCards = new ArrayList<Card>();
		List<Card> artilleryCards = new ArrayList<Card>();
		List<Card> wildCards = new ArrayList<Card>(); 
		if(listOfCards.size() > 2)
		{
			for(int i = 0; i < listOfCards.size(); i++)
			{
				if(listOfCards.get(i).getType() == Army.INFANTRY)infantryCards.add(listOfCards.get(i));
				else if(listOfCards.get(i).getType() == Army.CALVARY)calvaryCards.add(listOfCards.get(i));
				else if(listOfCards.get(i).getType() == Army.ARTILLERY)artilleryCards.add(listOfCards.get(0));
				else if(listOfCards.get(i).getType() == Army.WILDCARD)wildCards.add(listOfCards.get(0));
			}
			Card[] addedCards = new Card[3];
			if(infantryCards.size() + wildCards.size() == 3)
			{	
				for(int i = 0; i < addedCards.length; i++)
				{
					if(infantryCards.size() > 0)
					{
						addedCards[i] = infantryCards.get(0);
						infantryCards.remove(0);
					}
					else
					{
						addedCards[i] = wildCards.get(0);
						wildCards.remove(0);
					}
				}
				game.tradeCards(player, addedCards[0], addedCards[1], addedCards[2]);
				return true;
			}
			else if(calvaryCards.size() + wildCards.size() == 3)
			{
				for(int i = 0; i < addedCards.length; i++)
				{
					if(calvaryCards.size() > 0)
					{
						addedCards[i] = calvaryCards.get(0);
						calvaryCards.remove(0);
					}
					else
					{
						addedCards[i] = wildCards.get(0);
						wildCards.remove(0);
					}
				}
				game.tradeCards(player, addedCards[0], addedCards[1], addedCards[2]);
				return true;
			}
			else if(artilleryCards.size() + wildCards.size() == 3)
			{
				for(int i = 0; i < addedCards.length; i++)
				{
					if(artilleryCards.size() > 0)
					{
						addedCards[i] = artilleryCards.get(0);
						artilleryCards.remove(0);
					}
					else
					{
						addedCards[i] = wildCards.get(0);
						wildCards.remove(0);
					}
				}
				game.tradeCards(player, addedCards[0], addedCards[1], addedCards[2]);
				return true;
			}
			else if((artilleryCards.size() > 0 && calvaryCards.size() > 0 && infantryCards.size() > 0) ||
					(wildCards.size() > 0 && calvaryCards.size() > 0 && infantryCards.size() > 0) ||
					(artilleryCards.size() > 0 && wildCards.size() > 0 && infantryCards.size() > 0)||
					(artilleryCards.size() > 0 && calvaryCards.size() > 0 && wildCards.size() > 0)||
					(artilleryCards.size() > 0 && wildCards.size() > 1)||
					(calvaryCards.size() > 0 && wildCards.size() > 1)||
					(infantryCards.size() > 0 && wildCards.size() > 1))
			{
				if(artilleryCards.size() > 0)
				{
					addedCards[0] = artilleryCards.get(0);
				}
				else
				{
					addedCards[0] = wildCards.get(0);
					wildCards.remove(0);
				}
				if(calvaryCards.size() > 0)
				{
					addedCards[1] = calvaryCards.get(0);
				}
				else
				{
					addedCards[1] = wildCards.get(0);
					wildCards.remove(0);
				}
				if(infantryCards.size() > 0)
				{
					addedCards[2] = infantryCards.get(0);
				}
				else
				{
					addedCards[2] = wildCards.get(0);
					wildCards.remove(0);
				}
				game.tradeCards(player, addedCards[0], addedCards[1], addedCards[2]);
				return true;
			}
			
			

		}
		return false;
	}
}
