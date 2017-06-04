package it.polimi.ingsw.LM22.controller;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.polimi.ingsw.LM22.model.BuildingCard;
import it.polimi.ingsw.LM22.model.CharacterCard;
import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.TerritoryCard;
import it.polimi.ingsw.LM22.model.VentureCard;
import it.polimi.ingsw.LM22.network.NetContrAdapter;
import it.polimi.ingsw.LM22.network.server.IPlayer;

public class MainGameController implements Runnable {
	// Colori giocatori
	private final String[] PLAYER_COLOR = { "Blue", "Green", "Red", "Yellow" };
	// Colori familiari
	private final String[] MEMBER_COLOR = { "Orange", "Black", "White", "Uncolored" };

	private final Integer PERIOD_END_DEFINER = 2;

	private Integer TIMER_PER_MOVE; // caricabile da file
	private Game game = new Game();
	private IPlayer iplayer[];
	private int[] ordine;
	private int nPlayers;
	private VaticanReportManager vaticanReportManager;
	private TurnInizializator turnInizializator;
	private InitialConfigurator initialConfigurator;
	private MoveManager moveManager = new MoveManager(game, this);
	private NetContrAdapter netContrAdapter = new NetContrAdapter();

	public MainGameController(IPlayer iplayer[], int[] ordine, int nPlayer) throws RemoteException {
		this.nPlayers = nPlayer;
		this.iplayer = iplayer;
		this.ordine = ordine;
		setupPlayers();
		this.initialConfigurator = new InitialConfigurator(game);
	}

	@Override
	public void run() {
		String sMove;
		AbstractMove aMove;
		int i = 0;
		try {
			iplayer[ordine[i]].showBoard(game);
			while (true) {
				sMove = iplayer[ordine[i]].yourTurn();
				aMove = netContrAdapter.moveParser(getPlayer(iplayer[ordine[i]]), sMove);
				// moveManager.manageMove(aMove);
				System.out.println(sMove);
				sendAll();// invio a tutti il nuovo model
				if (ordine[i + 1] == 4) {
					i = 0;
					// vaticano
				} else {
					i++;
				}
				// turn initializzator
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Partita terminata!");
		}
	}

	/*
	 * inizializzo i giocatori con i dati forniti dal network
	 */
	private void setupPlayers() throws RemoteException {
		Player players[] = new Player[nPlayers];
		game.setPlayers(players);
		int i = 0;
		for (Player p : game.getPlayers()) {
			List<FamilyMember> members = new ArrayList<FamilyMember>();
			for (int j = 0; j < 4; j++) {
				FamilyMember fm = new FamilyMember(p, MEMBER_COLOR[j]);
				// personalBoard: personal tile & dev cards
				members.add(fm);
			}
			players[i] = new Player(iplayer[i].getName(), PLAYER_COLOR[i], members);
			i++;
		}
	}

	/*
	 * fornisco il giocatore corrispondente al client fornito
	 */
	private Player getPlayer(IPlayer ip) throws RemoteException {
		for (Player p : game.getPlayers()) {
			if (p.getNickname().equals(ip.getName()))
				return p;
		}
		return null;
	}

	private void sendAll() throws IOException {
		for (int j = 0; j < nPlayers; j++) {
			iplayer[j].showBoard(game);
		}
	}

	private void vaticanReport() {
		if (game.getPeriod() % PERIOD_END_DEFINER == 0)
			vaticanReportManager.manageVaticanReport(game);
	}

	private void turnInit() {
		turnInizializator.initializeTurn(game);
	}

	/*
	 * permette di gestire tutta la fase di conteggio dei Punti Finali
	 */
	private void manageEndGame(Game game) {

	}

	/*
	 * metodo che gestisce le scomuniche di terzo periodo
	 */
	private void manageFinalExCommunications(Game game) {

	}

	/*
	 * metodo che gestisce l'attribuzione dei punti vittoria finali in base alla
	 * classifica dei punti militari
	 */
	private void manageMilitaryStandingPoints(Game game) {
		sortMilitaryRanking(createMilitaryHashMap(game));
	}

	private HashMap<Player, Integer> createMilitaryHashMap(Game game) {
		HashMap<Player, Integer> map = new HashMap<Player, Integer>();
		for (Player p : game.getPlayers()) {
			map.put(p, p.getPersonalBoard().getResources().getMilitary());
		}
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void sortMilitaryRanking(HashMap<Player, Integer> hmap) {
		HashMap<Integer, Player> map = sortByValues(hmap);
		Set set2 = map.entrySet();
		Iterator iterator2 = set2.iterator();
		while (iterator2.hasNext()) {
			Map.Entry me2 = (Map.Entry) iterator2.next();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HashMap sortByValues(HashMap<Player, Integer> map) {
		List list = new LinkedList(map.entrySet());
		// Defined Custom Comparator here
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
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
	 * metodo che viene invocato ogni volta che ottengo un effetto comprendente
	 * x councilPrivilege e permette di scegliere x councilPrivilege diversi, si
	 * avrà un ciclo che permette di scegliere tra le varie possibilità e al
	 * ciclo dopo si toglie il tipo di risorsa già scelto
	 */
	public Resource selectCouncilPrivilege(Integer councilNumber) {
		return null;
		// TODO
	}

	/*
	 * metodo che gestisce l'attribuzione dei punti vittoria in base al numero
	 * di carte territorio o personaggio che ogni player ha ottenuto +
	 * attribuzione dei punti vittoria dati dalle VentureCards --> in questo
	 * caso devo fare il controllo su alcuni tipi di scomuniche del terzo
	 * periodo
	 */
	private void manageVictoryPointDueToCards() {

	}

	/*
	 * metodo che gestisce l'attribuzione di punti vittoria in base al numero di
	 * risorse possedute da ogni player (1 punto vittoria per ogni 5 risorse
	 * wood-stone-coins-servants del player contate tutte insieme ) --> qui
	 * controllo la presenza di scomuniche del terzo periodo che non fanno
	 * prendere certi punti
	 */
	private void manageVictoryPointsDueToResource() {

	}

	private void electWinner() {

	}

}
