package it.polimi.ingsw.LM22.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.network.NetContrAdapter;
import it.polimi.ingsw.LM22.network.server.IPlayer;

public class MainGameController implements Runnable {

	private final HashMap<String, Resource> councilResource = initializeCouncilMap();

	private final Integer PERIOD_END_DEFINER = 2;

	private static final Logger LOGGER = Logger.getLogger(MainGameController.class.getClass().getSimpleName());
	private Integer TIMER_PER_MOVE; // caricabile da file
	private Game game = new Game();
	private IPlayer iplayer[];
	private int nPlayers;
	private VaticanReportManager vaticanReportManager;
	private TurnInizializator turnInizializator;
	private InitialConfigurator initialConfigurator;
	private MoveManager moveManager = new MoveManager(game, this);
	private NetContrAdapter netContrAdapter = new NetContrAdapter();
	private ResourceHandler resourceHandler = new ResourceHandler();

	public MainGameController(IPlayer iplayer[], int nPlayer) throws RemoteException {
		this.nPlayers = nPlayer;
		this.iplayer = iplayer;
		try {
			this.initialConfigurator = new InitialConfigurator(game, iplayer, nPlayer);
		} catch (IOException e) {
			System.err.println("Errore nel caricamento dei file JSON");
		}
	}

	@Override
	public void run() {
		AbstractMove aMove;
		try {
			// inizio turno
			for (Player p : game.getPlayersOrder()) {
				// turno di un giocatore
				for (String sMove = ""; !sMove.equals("End@");) {
					sendAll();// invio a tutti il model
					FileOutputStream fout = new FileOutputStream("C:\\Users\\Matteo\\Desktop\\server.txt");
					ObjectOutputStream oos = new ObjectOutputStream(fout);
					oos.writeObject(game);
					oos.close();
					fout.close();
					sMove = getIPlayer(p).yourTurn();
					System.out.println(sMove);
					aMove = netContrAdapter.moveParser(p, sMove);
					moveManager.manageMove(aMove);
				}
				// fine turno di un giocatore
			}
			// fine turno/periodo
			// vaticano
			// turn initializzator
			if (game.getPeriod().equals(2) && game.getRound().equals(2))
				manageEndGame(game);
			else
				run();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			System.err.println("Room crashed!");
		}
	}

	/*
	 * fornisco il giocatore corrispondente al client fornito
	 */
	// private Player getPlayer(IPlayer ip) throws RemoteException {
	// for (Player p : game.getPlayers()) {
	// if (p.getNickname().equals(ip.getName()))
	// return p;
	// }
	// return null;
	// }

	private IPlayer getIPlayer(Player p) throws RemoteException {
		for (IPlayer ip : iplayer) {
			if (p.getNickname().equals(ip.getName()))
				return ip;
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
	public Resource selectCouncilPrivilege(Integer councilNumber, Player player) throws IOException {
		Resource resource = new Resource(0, 0, 0, 0, 0, 0, 0);
		String result = getIPlayer(player).councilRequest(councilNumber);
		String[] cp = result.split("@");
		for (String c : cp) {
			resourceHandler.addResource(resource, councilResource.get(c));
		}
		return resource;
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

	private HashMap<String, Resource> initializeCouncilMap() {
		HashMap<String, Resource> map = new HashMap<String, Resource>();
		map.put("wood&stone", new Resource(1, 1, 0, 0, 0, 0, 0));
		map.put("servants", new Resource(0, 0, 2, 0, 0, 0, 0));
		map.put("coins", new Resource(0, 0, 0, 2, 0, 0, 0));
		map.put("military", new Resource(0, 0, 0, 0, 0, 2, 0));
		map.put("faith", new Resource(0, 0, 0, 0, 1, 0, 0));
		map.put("", new Resource(0, 0, 0, 0, 0, 0, 0));
		return map;
	}
}
