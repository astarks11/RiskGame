package model;

/*
 * Class: IntermediateAI
 * 
 * Authors: Matthew Drake
 * 
 * Description:
 * This class contains the intelligence for the intermediate AI.
 * It extends teh abstract class AI, and makes decisions.
 */
import java.util.ArrayList;
import java.util.Random;

public class IntermediateAI extends AI
{
	private ArrayList<Move> smartMoves;
	public IntermediateAI()
	{
		smartMoves = new ArrayList<Move>();
	}
	@Override
	public int getDiceRoll(int attackingArmies, int defendingArmies, boolean attacker)
	{
		if(attacker)return Math.min(attackingArmies, 3);
		else return Math.min(defendingArmies, 2);
	}

	@Override
	public Move getMove(Player player)
	{
		if(player.getMoves().size() == 0)return null;
		smartMoves = updateSmartMoves(player.getMoves());
		if(smartMoves.size() == 0)return null;
		
		Move retVal = smartMoves.get(0);
		return retVal;
	}

	private ArrayList<Move> updateSmartMoves(ArrayList<Move> availableMoves)
	{
		smartMoves = new ArrayList<Move>();
		//System.out.println(availableMoves.size());
		for(int i = 0; i < availableMoves.size();i++)
		{
			if(availableMoves.get(i).getAttackingTerritory().getArmySize()-1 >  availableMoves.get(i).getDefendingTerritory().getArmySize())
			{
				smartMoves.add(availableMoves.get(i));
			}
		}
		return smartMoves;

	}

	@Override
	public Fortification getFortification(Player player)
	{
		return null;
	}

	

	@Override
	public boolean endTurn(Player player)
	{
		Random r = new Random();
		
		if(player.getMoves().size() != 0)return r.nextBoolean();
		
		return true;
	}

	@Override
	public void getReinforcements(Player player)
	{
		
		Random r;
		ArrayList<Territory> territories = (ArrayList<Territory>) player.getControlledTerritories();
		int counter = 0;
		int armySize = player.getArmySize();
		while(armySize != 0){
			counter++;
			r = new Random();
			int amountOfArmies = r.nextInt(armySize+1);
			armySize -= amountOfArmies;
			if(territories.size() == 0)return;
			int territoryNum = r.nextInt(territories.size());
			if(territories.get(territoryNum).getArmySize() > 10 && counter < 10)continue;
			player.reinforce(territories.get(territoryNum),amountOfArmies);
		}

		return;
		

	}

}
