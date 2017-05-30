package it.polimi.ingsw.LM22.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.MarketSpace;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Tower;

public class TurnInizializator {
	
	private final Integer DICE_NUMBER = 3;
	private final Integer DICE_MAX = 6;
	private final Integer DICE_MIN = 1;
	protected final String[] colors = {"ORANGE" ,"BLACK", "WHITE"};
	private final Integer WORKSPACES = 2;
	private final String[] workType = {"PRODUCTION", "HARVEST"};
	private final Integer TOWERS_NUM = 4;
	
	/*
	 * metodo generale che gestisce tutta la parte iniziale di preparazione di un nuovo turno
	 * --> invocherà tutti i metodi minori in grado di svolgere le varie funzionalità richieste
	 * per la preparazione di un turno e la pulizia della Board :
	 * - distribuzione carte (di 4 tipi)
	 * - lancio dei dadi
	 * - calcolo nuovo ordine di turno
	 * - 
	 * */
	public void initializeTurn(Game game){
		setNewPlayersOrder(game);
		cleanBoardGame(game);
		throwDices(game);
		distributeDevelopmentCards(game);
	}

	/*
	 * metodo che ritira tutte le carte non prese
	 * e ritira tutti i familiari
	 */
	private void cleanBoardGame(Game game){
		for (Tower tower: game.getBoardgame().getTowers()){
			tower.setOccupied(false);
			//settaggio a null dello spazio delle carte 
		}
		retireMembers(game);
	}	
	
	/*
	 * metodo che consente di distribuire tutte e carte sviluppo
	 */
	protected void distributeDevelopmentCards(Game game){
		Integer period = game.getPeriod();
		distributeTerritoryCards(period);
		distributeCharacterCards(period);
		distributeBuildingCards(period);
		distributeVentureCards(period);
	}
	
	protected void distributeTerritoryCards(Integer period){
		
	}
	
	protected void distributeCharacterCards(Integer period){
		
	}
	
	protected void distributeBuildingCards(Integer period){
		
	}
	
	protected void distributeVentureCards(Integer period){
		
	}
	
	/*
	 * metodo che reinizializza i valori dei dadi
	 */
	protected void throwDices(Game game){
		Random random = new Random();
		for (int cont=0; cont<DICE_NUMBER; cont++){
			game.getBoardgame().setDice(colors[cont], (random.nextInt(DICE_MAX)+DICE_MIN));
		}
	}
	
	/*
	 * metodo che in base al valore dei dadi tirati conferisce ai familiari il loro valore
	 * --> qui avviene anche il controllo per le scomuniche che danno dei malus ai dadi 
	 * e per le carte leader che modificano il valore dei dadi
	 */
	protected void setFamilyMembersValue(Game game){
		
	}
	
	/*
	 * metodo che presi in ingresso l'ordine del turno appena finito e la lista 
	 * dei famiiari inseriti nel CouncilSpace e genera l'ordine per il nuovo turno
	 * pulendo già il CouncilSpace
	 */
	protected void setNewPlayersOrder(Game game){
		List<FamilyMember> members= game.getBoardgame().getCouncilPalace().getMembers();
		List<Player> newOrder = new ArrayList<Player>();
		for (FamilyMember m: members){
			if (!newOrder.contains(m.getPlayer()))
				newOrder.add(m.getPlayer());
			m.setUsed(false);
		}
			
		game.getBoardgame().getCouncilPalace().setMembers(null);
		for (Player p: game.getPlayersOrder()){
			if (!newOrder.contains(p)){
				newOrder.add(p);
			}
		}
		game.setPlayersOrder(newOrder);
	}
	
	/*
	 * metodo che si occupa di riportare tutti i familiari al Player
	 * e settare il loro flag used a false 
	 * pulendo tutti i vari AbstractSpace
	 */
	private void retireMembers(Game game){
		for (Player p: game.getPlayers()){
			for (FamilyMember m: p.getMembers()){
				m.setUsed(false);
			}
		}
		for (MarketSpace space: game.getBoardgame().getMarket()){
			space.setMember(null);
		}
		for (int cont = 0; cont < WORKSPACES; cont++){
			game.getBoardgame().getWorkSpace(workType[cont]).setMembers(null);
		}
	}
	
	/*
	 * metodo ipotizzato per la modalità con il quinto giocatore
	 * --> IDEA
	 * sarebe quella di distribuire delle risorse in modo prefissato e decrescente
	 * in base all'ordine inverso di turno di quello che è stato appena calcolato
	 * per il nuovo turno che deve iniziare
	 * (il quinto giocatore prende molte risorse, il quarto meno e così via fino al primo)
	 */
	private void distributeNewResources(){
		
	}
}
