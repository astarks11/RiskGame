package tests;
/*
 * Class: RiskGUI
 * 
 * Authors: Steven Cleary, Matthew Drake, Alex Starks
 * 
 * Description:
 * This class tests all that is in the model. We have all of our
 * tests in one file.
 */
import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import model.*;
import view.GameView;


public class TestModel {

	@Test
	public void checkInit(){
		Player p1 = new Player(Color.RED,null);
		ArrayList<Player> p2 = new ArrayList<Player>();
		p2.add(new Player(Color.BLUE,new BeginnerAI()));
		p2.add(new Player(Color.RED,new BeginnerAI()));
		//p2.add(new Player(Color.RED,new IntermediateAI()));
		Map gm = new Map(new Point(0,0),new Point(1000,1000),1);
		RiskGame risk = new RiskGame(gm,p1,p2);
		gm.setUpUsers(p1,p2);
		assertEquals(p2,risk.getAiPlayers());
		assertEquals(p1,risk.getPlayer());
		assertEquals(gm,risk.getMap());
		System.out.println("armies per round" + risk.getArmiesPerRound());
	}
	
	@Test
	public void checkFlow(){
		Map gm = new Map(new Point(0,0),new Point(1000,1000),1);
		ArrayList<Player> p2 = new ArrayList<Player>();
		p2.add(new Player(Color.BLUE,new BeginnerAI()));
		p2.add(new Player(Color.RED,new BeginnerAI()));
		RiskGame risk = new RiskGame(gm,null,p2);
		risk.deSelectAll();
		risk.setCanBattle(true);
		risk.setCanFortify(true);
		risk.setCanTrade(true);
		risk.setCanDeploy(true);
		assertFalse(risk.getCanBattle());
		risk.setCanBattle(true);
		assertTrue(risk.getCanBattle());
	}
	
	@Test
	public void testFindTerritory(){
		Map gm = new Map(new Point(0,0),new Point(1000,1000),1,4);
		ArrayList<Player> p2 = new ArrayList<Player>();
		p2.add(new Player(Color.BLUE,new BeginnerAI()));
		p2.add(new Player(Color.RED,new BeginnerAI()));
		RiskGame risk = new RiskGame(gm,null,p2);
	}
	
	@Test
	public void testCanBattleTerr(){
		Map gm = new Map(new Point(0,0),new Point(1000,1000),1,4);
		Player hp = new Player(Color.GREEN,null);
		ArrayList<Player> p2 = new ArrayList<Player>();
		p2.add(new Player(Color.BLUE,new BeginnerAI()));
		p2.add(new Player(Color.RED,new BeginnerAI()));
		RiskGame risk = new RiskGame(gm,hp,p2);
		risk.setCanBattle(true);
	}
	
	@Test
	public void testCanAttack(){
		Map gm = new Map(new Point(0,0),new Point(1000,1000),1,4);
		Player p1 = new Player(Color.GREEN,null);
		ArrayList<Player> p2 = new ArrayList<Player>();
		p2.add(new Player(Color.BLUE,new BeginnerAI()));
		RiskGame risk = new RiskGame(gm,p1,p2);
		p1.addControlledTerritory(gm.get(0));
		gm.get(0).addArmy(p1);
		p2.get(0).addControlledTerritory(gm.get(1));
		gm.get(1).addArmy(p2.get(0));
		p2.get(0).addControlledTerritory(gm.get(4));
		gm.get(4).addArmy(p2.get(0));
		assertEquals(p1.getArmySize(),39);
		assertEquals(p2.get(0).getArmySize(),38);
		assertEquals(risk.getAttackingSize(),0);
		assertTrue(risk.canAttack(new Point(125,125),new Point(125,375)));
		assertFalse(risk.attackInProgress());
		assertFalse(risk.canAttack(new Point(125,125),new Point(375,375)));
		risk.setCanBattle(true);
	}
	
	@Test
	public void testDeploy(){
		Map gm = new Map(new Point(0,0),new Point(1000,1000),1,4);
		Player p1 = new Player(Color.GREEN,null);
		ArrayList<Player> p2 = new ArrayList<Player>();
		p2.add(new Player(Color.BLUE,new BeginnerAI()));
		RiskGame risk = new RiskGame(gm,p1,p2);
		p1.addControlledTerritory(gm.get(0));
		gm.get(0).addArmy(p1);
		//p2.get(0).addControlledTerritory(gm.get(1));
		//gm.get(1).addArmy2(p2.get(0));
		risk.selectTerritory(gm.get(0));
		risk.selectTerritory(gm.get(0));
		risk.setCanDeploy(true);
		risk.troopDeployed();
		risk.unDeploy();
		risk.unDeploy();
		for(int i = 0; i < p1.getArmySize() + 10; i++){
			risk.troopDeployed();
		}
		risk.setCanDeploy(false);
		risk.troopDeployed();
		risk.unDeploy();
	}
	
	@Test
	public void testMakeAttack(){
		Map gm = new Map(new Point(0,0),new Point(1000,1000),1,4);
		Player p1 = new Player(Color.GREEN,null);
		ArrayList<Player> p2 = new ArrayList<Player>();
		p2.add(new Player(Color.BLUE,new BeginnerAI()));
		RiskGame risk = new RiskGame(gm,p1,p2);
		p1.addControlledTerritory(gm.get(0));
		gm.get(0).addArmy(p1);
		p2.get(0).addControlledTerritory(gm.get(1));
		gm.get(1).addArmy(p2.get(0));
		risk.selectTerritory(gm.get(0));
		risk.selectTerritory(gm.get(0));
		risk.setCanDeploy(true);
		risk.troopDeployed();//2
		risk.troopDeployed();//3
		risk.troopDeployed();//4
		risk.troopDeployed();//4
		assertTrue(risk.canAttack(new Point(125,125),new Point(125,375)));
		risk.addAttacker();//6
		risk.addAttacker();//7
		risk.subAttacker();//6
		assertEquals(risk.getAttackingSize(),5);
		assertEquals(risk.getAttackAmount(),1);
		risk.subAttacker();
		assertEquals(risk.getAttackAmount(),1);
		risk.addAttacker();
		assertEquals(risk.getAttackAmount(),2);
		risk.addAttacker();
		assertEquals(risk.getAttackAmount(),3);
		risk.addAttacker();
		assertEquals(risk.getAttackAmount(),4);
		risk.addAttacker();
		assertEquals(risk.getAttackAmount(),4);
		risk.makeAttack();
	}
	
	@Test
	public void AIMoves(){
		Map gm = new Map(new Point(0,0),new Point(1000,1000),1,4);
		Player p1 = new Player(Color.GREEN,null);
		ArrayList<Player> p2 = new ArrayList<Player>();
		p2.add(new Player(Color.BLUE,new BeginnerAI()));
		RiskGame risk = new RiskGame(gm,p1,p2);
		gm.setUpUsers(p1, p2);

	}
	

	@Test
	public void testMap(){
		Map gm = new Map(new Point(0,0),new Point(1000,1000),1,4);
		Player p1 = new Player(Color.GREEN,null);
		ArrayList<Player> p2 = new ArrayList<Player>();
		p2.add(new Player(Color.BLUE,new BeginnerAI()));
		RiskGame risk = new RiskGame(gm,p1,p2);
		gm.getList();
		assertEquals(gm.get(0),gm.getTerritory(125, 125));
		assertEquals(null,gm.getTerritory(125, 12332323));
		assertEquals(null,gm.getTerritory(125, 12332323));
		assertTrue(gm.setTerritoryHighlight(125, 125));
		assertTrue(gm.setTerritoryHighlight(125, 125));
		assertTrue(gm.setTerritoryHighlight(125, 375));
	}
	/*
	@Test
	public void AIloop(){//run6bots doesnt work
			Player p1 = null;
			ArrayList<Player> p2 = new ArrayList<Player>();
			p2.add(new Player(Color.BLUE,new BeginnerAI()));
			p2.add(new Player(Color.RED,new BeginnerAI()));
			p2.add(new Player(Color.BLACK, new BeginnerAI()));
			Map gm = new Map(new Point(0,0),new Point(1000,1000),1);
			
			RiskGame risk = new RiskGame(gm,p1,p2);
			gm.setUpUsers(p1, p2);
			System.out.println(risk.isGameOver());
			risk.startTestGameAI(false);
			risk.runTestAiLoop(false);
	}
	*/
	//12% more code coverage with botTest1 and botTest2
	
		
	@Test
	public void botTest2(){
		int beginnerWins = 0;
		int intermediateWins = 0;
		for(int i = 0; i < 1000; i++)
		{
			System.out.println("Game #" + i);
			Player p1 = null;
			ArrayList<Player> p2 = new ArrayList<Player>();
			p2.add(new Player(Color.BLUE,new BeginnerAI()));
			
			
			//p2.add(new Player(Color.RED,new BeginnerAI()));
			p2.add(new Player(Color.RED,new IntermediateAI()));
			p2.add(new Player(Color.GREEN,new BeginnerAI()));
			p2.add(new Player(Color.YELLOW,new IntermediateAI()));
			p2.add(new Player(Color.ORANGE,new BeginnerAI()));
			p2.add(new Player(Color.BLACK,new IntermediateAI()));
			Map gm = new Map(new Point(0,0),new Point(400 - 400/GameView.sidePanelSizeFactor(),400),1);

			RiskGame risk = new RiskGame(gm,p1,p2);
			gm.setUpUsers(p1, p2);

			risk.startGameAI(0);
			risk.runAiLoop(0);
			if(risk.getWinner() == p2.get(0) || risk.getWinner() == p2.get(2) || risk.getWinner() == p2.get(4))beginnerWins++;
			else intermediateWins++;
			System.out.println("Beginners Won: " + beginnerWins + "\nIntermediates Won: " + intermediateWins);
		}
	}

	@Test 
	public void testGetFirstMovePlayer()
	{
		Player p1 = null;
		ArrayList<Player> p2 = new ArrayList<Player>();
		p2.add(new Player(Color.BLUE,new BeginnerAI()));
		p2.add(new Player(Color.RED,new IntermediateAI()));
		p2.add(new Player(Color.GREEN,new IntermediateAI()));
		p2.add(new Player(Color.BLACK,new IntermediateAI()));
		p2.add(new Player(Color.ORANGE,new IntermediateAI()));
		p2.add(new Player(Color.GRAY,new IntermediateAI()));

		Map gm = new Map(new Point(0,0),new Point(400 - 400/GameView.sidePanelSizeFactor(),400),1);

		RiskGame risk = new RiskGame(gm,p1,p2);
		gm.setUpUsers(p1, p2);
		
		for (int i = 0; i < 100; i++)
		{
			boolean found = false;
			Player p = risk.getFirstMovePlayer();
			for (Player curr : p2)
			{
				if (curr == p)
					found = true;
			}
			assertTrue(found);
		}
	}
	
	@Test
	public void testCardDeckTradeIns()
	{
		List<Territory> terrs = new ArrayList<Territory>();
		for (int i = 0; i < 16; i++)
		{
			terrs.add(new Territory(0,Color.RED));
		}
		CardDeck cd = new CardDeck(terrs);
		Player p = new Player(Color.RED);
		
		assertTrue(cd.getSize()-2 == terrs.size());
		assertTrue(cd.getNumberOfTradeIns() == 0);
		
		Card card1 = new Card(0,Army.INFANTRY);
		Card card2 = new Card(1,Army.INFANTRY);
		Card card3 = new Card(2,Army.INFANTRY);
		p.addCardToPlayersHand(card1);
		p.addCardToPlayersHand(card2);
		p.addCardToPlayersHand(card3);

		
		Card card4 = new Card(3,Army.INFANTRY);
		Card card5 = new Card(4,Army.INFANTRY);
		Card card6 = new Card(5,Army.INFANTRY);
		p.addCardToPlayersHand(card4);
		p.addCardToPlayersHand(card5);
		p.addCardToPlayersHand(card6);


		Card card7 = new Card(6,Army.INFANTRY);
		Card card8 = new Card(7,Army.INFANTRY);
		Card card9 = new Card(8,Army.INFANTRY);
		p.addCardToPlayersHand(card7);
		p.addCardToPlayersHand(card8);
		p.addCardToPlayersHand(card9);

		Card card10 = new Card(9,Army.INFANTRY);
		Card card11 = new Card(10,Army.INFANTRY);
		Card card12 = new Card(11,Army.INFANTRY);
		p.addCardToPlayersHand(card10);
		p.addCardToPlayersHand(card11);
		p.addCardToPlayersHand(card12);


		Card card13 = new Card(12,Army.INFANTRY);
		Card card14 = new Card(13,Army.INFANTRY);
		Card card15 = new Card(14,Army.INFANTRY);
		p.addCardToPlayersHand(card13);
		p.addCardToPlayersHand(card14);
		p.addCardToPlayersHand(card15);

		Card card16 = new Card(15,Army.INFANTRY);
		Card card17 = new Card(16,Army.INFANTRY);
		Card card18 = new Card(17,Army.INFANTRY);
		p.addCardToPlayersHand(card16);
		p.addCardToPlayersHand(card17);
		p.addCardToPlayersHand(card18);

		cd.tradeCards(p, card1, card2, card3);
		
		cd.tradeCards(p, card4, card5, card6);
		cd.tradeCards(p, card7, card8, card9);
		cd.tradeCards(p, card10, card11, card12);
		cd.tradeCards(p, card13, card14, card15);
		cd.tradeCards(p, card16, card17, card18);
		
		

		
		}	
	

	@Test
	public void botTest3(){
		int beginnerWins = 0;
		int intermediateWins = 0;
		for(int i = 0; i < 1000; i++)
		{
			System.out.println("Game #" + i);
			Player p1 = null;
			ArrayList<Player> p2 = new ArrayList<Player>();
			p2.add(new Player(Color.BLUE,new BeginnerAI()));
			
			
			//p2.add(new Player(Color.RED,new BeginnerAI()));
			p2.add(new Player(Color.RED,new IntermediateAI()));
			p2.add(new Player(Color.GREEN,new BeginnerAI()));
			p2.add(new Player(Color.YELLOW,new IntermediateAI()));
			p2.add(new Player(Color.ORANGE,new BeginnerAI()));
			p2.add(new Player(Color.BLACK,new IntermediateAI()));
			Map gm = new Map(new Point(0,0),new Point(400 - 400/GameView.sidePanelSizeFactor(),400),1);

			RiskGame risk = new RiskGame(gm,p1,p2);
			gm.setUpUsers(p1, p2);
			
			risk.startGameAI(0);
			risk.runAiLoop(0);
			if(risk.getWinner() == p2.get(0) || risk.getWinner() == p2.get(2) || risk.getWinner() == p2.get(4))beginnerWins++;
			else intermediateWins++;
			System.out.println("Beginners Won: " + beginnerWins + "\nIntermediates Won: " + intermediateWins);
		}	
	}
	
	@Test
	public void testTerritory(){
		Map gm = new Map(new Point(0,0),new Point(1000,1000),1,4);
		Player p1 = new Player(Color.GREEN,null);
		ArrayList<Player> p2 = new ArrayList<Player>();
		p2.add(new Player(Color.BLUE,new BeginnerAI()));
		RiskGame risk = new RiskGame(gm,p1,p2);
		gm.setTerritoryColor(gm.get(0), Color.RED);
		assertTrue(gm.isColor(gm.get(0), Color.RED));
		assertFalse(gm.isColor(gm.get(0), Color.BLUE));
		assertTrue(gm.contains(125, 125));
		gm.setNoHighlightAll();
		assertFalse(gm.contains(100000, 100000));
		Territory t = new Territory(1);
		assertEquals(t.getID(),1);
	}
	
	@Test
	public void testFourPlayers()
	{
		Player p1 = null;
		ArrayList<Player> p2 = new ArrayList<Player>();
		p2.add(new Player(Color.BLUE,new BeginnerAI()));
		
		
		//p2.add(new Player(Color.RED,new BeginnerAI()));
		p2.add(new Player(Color.RED,new IntermediateAI()));
		p2.add(new Player(Color.GREEN,new BeginnerAI()));
		p2.add(new Player(Color.YELLOW,new IntermediateAI()));

		Map gm = new Map(new Point(0,0),new Point(400 - 400/GameView.sidePanelSizeFactor(),400),1);

		RiskGame risk = new RiskGame(gm,p1,p2);
		gm.setUpUsers(p1, p2);
		
		risk.startGameAI(0);
		risk.runAiLoop(0);
	}
	
	@Test
	public void testFivePlayers()
	{
		Player p1 = null;
		ArrayList<Player> p2 = new ArrayList<Player>();
		p2.add(new Player(Color.BLUE,new BeginnerAI()));
		
		
		p2.add(new Player(Color.ORANGE,new BeginnerAI()));
		p2.add(new Player(Color.RED,new IntermediateAI()));
		p2.add(new Player(Color.GREEN,new BeginnerAI()));
		p2.add(new Player(Color.YELLOW,new IntermediateAI()));

		Map gm = new Map(new Point(0,0),new Point(400 - 400/GameView.sidePanelSizeFactor(),400),1);

		RiskGame risk = new RiskGame(gm,p1,p2);
		gm.setUpUsers(p1, p2);
		
		risk.startGameAI(0);
		risk.runAiLoop(0);
	}
}
