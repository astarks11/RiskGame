package model;
/*
 * Class: ExpertAI
 * 
 * Authors: Matthew Drake
 * 
 * Description:
 * This class contains the intelligence for the expert AI.
 * It extends the abstract class AI, and makes decisions.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExpertAI extends AI
{
	ArrayList<Move> smartMoves;
	ArrayList<Territory> friendlyLockedTerritory;
	public ExpertAI()
	{
		smartMoves = new ArrayList<Move>();
		friendlyLockedTerritory = new ArrayList<Territory>();
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

			//System.out.println(availableMoves.get(i).getAttackingTerritory().getArmySize()-1 + " - " + availableMoves.get(i).getDefendingTerritory().getArmySize());
			if(availableMoves.get(i).getAttackingTerritory().getArmySize()-1 >  availableMoves.get(i).getDefendingTerritory().getArmySize())
			{
				smartMoves.add(availableMoves.get(i));
				//System.out.println(smartMoves.size());
			}
		}
		return smartMoves;

	}
	@Override
	public Fortification getFortification(Player player)
	{
		// = new ArrayList<Territory>();
		updateLockedTerritories(player.getControlledTerritories());
		if(friendlyLockedTerritory.size() != 0)
		{
			for(int i = 0; i < friendlyLockedTerritory.size();i++)
			{
				for(int j = 0; j < friendlyLockedTerritory.get(i).getNeighbors().size(); j++)
				{
					boolean flag = true;
					for(int k = 0; k < friendlyLockedTerritory.size();k++)
					{
						
						if(friendlyLockedTerritory.get(i).getNeighbors().get(j) == friendlyLockedTerritory.get(k))
						{
							flag = false;
						}
					}
					if(flag)
					{
						return new Fortification(player, friendlyLockedTerritory.get(i), friendlyLockedTerritory.get(i).getNeighbors().get(j),friendlyLockedTerritory.get(i).getArmySize()-1);
					}

				}
			}
		}
		return null;
	}

	private void updateLockedTerritories(List<Territory> controlledTerritories)
	{
		friendlyLockedTerritory = new ArrayList<Territory>();
		for(int i = 0; i < controlledTerritories.size(); i++)
		{
			if(controlledTerritories.get(i).getArmySize() > 1)
			{
				boolean flag = false;
				for(int j = 0; j < controlledTerritories.get(i).getNeighbors().size(); j++)
				{
					if(controlledTerritories.get(i).getNeighbors().get(j).getOwner() != controlledTerritories.get(i).getOwner())
					{
						flag = true;
						break;
					}
				}
				if(!flag)friendlyLockedTerritory.add(controlledTerritories.get(1));
			}

		}

	}
	

	@Override
	public boolean endTurn(Player player)
	{
		smartMoves = updateSmartMoves(player.getMoves());
		if(smartMoves.size() == 0)return false;
		return true;
	}

	@Override
	public void getReinforcements(Player player)
	{
		Random r;
		ArrayList<Territory> territories = (ArrayList<Territory>) player.getControlledTerritories();
		int counter = 0;
		while(player.getArmySize() != 0){
			counter++;
			
			r = new Random();
			int amountOfArmies = r.nextInt(player.getArmySize()+1);
			if(territories.size() == 0)return;
			int territoryNum = r.nextInt(territories.size());//
			if(territories.get(territoryNum).getArmySize() > 10 && counter < 10)continue;
			player.reinforce(territories.get(territoryNum),amountOfArmies);
		}

		return;

	}

}
