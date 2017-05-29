package it.polimi.ingsw.LM22.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Player;


public class MainGameController {

	private final Integer PERIOD_END_DEFINER = 2;
	
	private Integer TIMER_PER_MOVE; //caricabile da file
	private Game game;
	private VaticanReportManager vaticanReportManager;
	private TurnInizializator turnInizializator;
	private InitialConfigurator initialConfigurator;
	private MoveManager moveManager;
	
	public MainGameController(){
		this.initialConfigurator = new InitialConfigurator(game);
	}
	
	private void vaticanReport(){
		if (game.getPeriod()%PERIOD_END_DEFINER==0)
			vaticanReportManager.manageVaticanReport(game);
	}
	
	private void turnInit(){
		turnInizializator.initializeTurn(game);
	}
	
	/*
	 * permette di gestire tutta la fase di conteggio dei Punti Finali 
	 * */
	private void manageEndGame(Game game){
		
	}
	
	/*
	 * metodo che gestisce le scomuniche di terzo periodo
	 */
	private void manageFinalExCommunications(Game game){
		
	}
	
	/*
	 * metodo che gestisce l'attribuzione dei punti vittoria finali in base alla classifica dei punti militari
	 */
	private void manageMilitaryStandingPoints(Game game){
		sortMilitaryRanking(createMilitaryHashMap(game));
	}
	
	
	private HashMap<Player, Integer> createMilitaryHashMap(Game game){
		HashMap<Player, Integer> map = new HashMap<Player, Integer>();
		for (Player p: game.getPlayers()){
			map.put(p, p.getPersonalBoard().getResources().getMilitary());
		}
		return map;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void sortMilitaryRanking(HashMap<Player, Integer> hmap){
		  HashMap<Integer, Player> map = sortByValues(hmap);
	      Set set2 = map.entrySet();
	      Iterator iterator2 = set2.iterator();
	      while(iterator2.hasNext()) {
	           Map.Entry me2 = (Map.Entry)iterator2.next();
	      }
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HashMap sortByValues(HashMap<Player, Integer> map) { 
	       List list = new LinkedList(map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o2)).getValue())
	                  .compareTo(((Map.Entry) (o1)).getValue());
	            }
	       });
	       // Here I am copying the sorted list in HashMap
	       // using LinkedHashMap to preserve the insertion order
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }
	
	/*
	 * metodo che gestisce l'attribuzione dei punti vittoria in base al numero di carte
	 * territorio o personaggio che ogni player ha ottenuto 
	 * + attribuzione dei punti vittoria dati dalle VentureCards
	 * --> in questo caso devo fare il controllo su alcuni tipi di 
	 * scomuniche del terzo periodo
	 */
	private void manageVictoryPointDueToCards(){
		
	}
	
	/*
	 * metodo che gestisce l'attribuzione di punti vittoria in base al numero di risorse 
	 * possedute da ogni player (1 punto vittoria per ogni 5 risorse wood-stone-coins-servants
	 * del player contate tutte insieme )
	 * --> qui controllo la presenza di scomuniche del terzo periodo che non fanno prendere certi punti
	 */
	private void manageVictoryPointsDueToResource(){
		
	}
	
	
	private void electWinner(){
		
	}
	
}
