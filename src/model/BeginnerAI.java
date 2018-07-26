package model;
/*
 * Class: BeginnerAI
 * 
 * Authors: Matthew Drake
 * 
 * Description:
 * This class contains the intelligence for the beginner AI.
 * It extends the abstract class AI, and makes decisions.
 */
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BeginnerAI extends AI
{
	private Random r;

	private boolean flag;
	public BeginnerAI(){///Color color){
		//super(color);
		flag = false;
	}

	@Override
	public int getDiceRoll(int attackingArmies, int defendingArmies, boolean attacker)
	{
		if(attacker && attackingArmies < 4)
		{
			if(!flag)
			{
				flag = true;
			}
			return attackingArmies;
		}
		if(!attacker && defendingArmies < 3)return defendingArmies;
		r = new Random();
		int dice = r.nextInt(2)+1;
		if(dice == 3 && !attacker)return 2;
		return dice;
	}

	@Override
	public Move getMove(Player player)
	{
		
		if(player.getMoves().size() == 0)return null;
		r = new Random();
		return player.getMoves().get(r.nextInt(player.getMoves().size()));
	}

	@Override
	public Fortification getFortification(Player player)
	{
		r = new Random();
		if(player.getControlledTerritories().size() == 0)return null;
		Territory movingTerritory = player.getControlledTerritories().get((r.nextInt(player.getControlledTerritories().size())));
		if(movingTerritory.getArmySize() == 1)
		{
			return new Fortification(player, movingTerritory, movingTerritory, 0);
		}
		ArrayList<Territory> neighbors = new ArrayList<Territory>();
		for(int i = 0; i < movingTerritory.getNeighbors().size(); i++)
		{
			if(movingTerritory.getOwnerColor().getRGB() == movingTerritory.getNeighbors().get(i).getOwnerColor().getRGB())
			{
				neighbors.add(movingTerritory.getNeighbors().get(i));
			}
		}
		if(neighbors.size() == 0)
		{
			return new Fortification(player, movingTerritory, movingTerritory, 0);
		}
			Territory receivingTerritory = neighbors.get(r.nextInt(neighbors.size()));
		
		int amount = movingTerritory.getArmySize() - 1;
		if(amount > 0)amount = r.nextInt(amount);
		return new Fortification(player, movingTerritory, receivingTerritory, amount);
	}

	
	@Override
	public boolean endTurn(Player player)
	{
		r = new Random();
		
		if(player.getMoves().size() != 0)return r.nextBoolean();
		
		return true;
	}

	@Override
	public void getReinforcements(Player player)
	{
		ArrayList<Territory> territories = (ArrayList<Territory>) player.getControlledTerritories();
		if(territories.size() == 0)return;
		while(player.getArmySize() != 0){
			r = new Random();
			int amountOfArmies = r.nextInt(player.getArmySize()+1);
			if(territories.size() == 0)return;
			int territoryNum = r.nextInt(territories.size());

			player.reinforce(territories.get(territoryNum),amountOfArmies);
		}

		return;
	}

}
