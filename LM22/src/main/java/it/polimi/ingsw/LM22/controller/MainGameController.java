package it.polimi.ingsw.LM22.controller;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
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
import it.polimi.ingsw.LM22.network.server.PlayerInfo;

public class MainGameController implements Runnable {

	private final HashMap<String, Resource> councilResource = initializeCouncilMap();

	private final Integer END_DEFINER = 2;

	private static final Logger LOGGER = Logger.getLogger(MainGameController.class.getClass().getSimpleName());
	private Game game = new Game();
	private ArrayList<PlayerInfo> playerRoom;
	private VaticanReportManager vaticanReportManager = new VaticanReportManager();;
	private EffectManager effectManager = new EffectManager();
	private ResourceHandler resourceHandler = new ResourceHandler();
	private TurnInizializator turnInizializator = new TurnInizializator(effectManager, resourceHandler);;
	private MoveManager moveManager = new MoveManager(game, this);
	private NetContrAdapter netContrAdapter = new NetContrAdapter();
	private int i = 0;

	public MainGameController(ArrayList<PlayerInfo> playerRoom) throws RemoteException {
		this.playerRoom = playerRoom;
		new InitialConfigurator(game, playerRoom, resourceHandler, effectManager);
	}

	@Override
	public void run() {
		AbstractMove aMove;
		// inizio turno
		i = 0;
		Player p;
		while (i < game.getPlayersOrder().size()) {
			// inizio turno di un giocatore
			p = game.getPlayersOrder().get(i);
			for (String sMove = ""; !sMove.startsWith("End@");) {
				try {
					// invio a tutti il nuovo model
					sendAll();
					// richiedo mossa a player
					sMove = getIPlayer(p).yourTurn();
				} catch (ClassNotFoundException | IOException e) {
					// ho perso la connessione con il client
					sMove = "End@Disconnect@";
				}
				System.out.println(sMove);
				// ottengo informazioni dalla mossa ricevuta
				aMove = netContrAdapter.moveParser(p, sMove);
				// provo ad eseguire la mossa
				try {
					moveManager.manageMove(aMove);
				} catch (InvalidMoveException e) {
					// segnalo al client che non può fare questa mossa
				}
			}
			i++;
			// fine turno di un giocatore
		}
		// fine turno/periodo
		// vaticanReport();
		// turnInizializator.initializeTurn(game);
		// verifico se è la fine del gioco
		// if (game.getPeriod().equals(END_DEFINER) &&
		// game.getRound().equals(END_DEFINER))
		// manageEndGame(game);
		// else
		if (!game.getPlayersOrder().isEmpty())
			run();
		else
			manageEndGame(game);
	}

	/*
	 * restituisco il giocatore corrispondente al client newtwork fornito
	 */
	private Player getPlayer(IPlayer ip) throws RemoteException {
		for (Player p : game.getPlayers()) {
			if (p.getNickname().equals(ip.getName()))
				return p;
		}
		return null;
	}

	/*
	 * restituisco il client newtwork corrispondente al giocatore fornito
	 */
	private IPlayer getIPlayer(Player p) throws RemoteException {
		for (int j = 0; j < playerRoom.size(); j++) {
			if (p.getNickname().equals(playerRoom.get(j).getName()))
				return playerRoom.get(j).getIplayer();
		}
		return null;
	}

	/*
	 * invio il model a tutti i clients connessi
	 */
	private void sendAll() throws IOException {
		for (int j = 0; j < playerRoom.size(); j++) {
			if (game.getPlayersOrder().contains(getPlayer(playerRoom.get(j).getIplayer())))
				playerRoom.get(j).getIplayer().showBoard(game);
		}
	}

	private void vaticanReport() {
		// solo alla fine del periodo
		if (game.getRound().equals(END_DEFINER))
			vaticanReportManager.manageVaticanReport(game);
	}

	private void turnInit() {
		turnInizializator.initializeTurn(game);
	}

	/*
	 * permette di gestire tutta la fase di conteggio dei Punti Finali
	 */
	private void manageEndGame(Game game) {
		LOGGER.log(Level.INFO, "Game ended");
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

	public void disconnectPlayer(Player player) {
		game.getPlayersOrder().remove(player);
		// TODO informare tutti
		System.out.println("Player " + player.getNickname() + " disconnected!");
		// TODO impostare connected a false o remove player from list
	}
}