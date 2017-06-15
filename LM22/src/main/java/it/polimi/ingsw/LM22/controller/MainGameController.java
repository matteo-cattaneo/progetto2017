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

import it.polimi.ingsw.LM22.model.CardActionEffect;
import it.polimi.ingsw.LM22.model.DoubleChangeEffect;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.VentureCard;
import it.polimi.ingsw.LM22.network.NetContrAdapter;
import it.polimi.ingsw.LM22.network.server.IPlayer;
import it.polimi.ingsw.LM22.network.server.PlayerInfo;

public class MainGameController implements Runnable {
	private Resource NOTHING = new Resource(0, 0, 0, 0, 0, 0, 0);
	private final HashMap<String, Resource> councilResource = initializeCouncilMap();

	private final Integer END_DEFINER = 2;

	private static final Logger LOGGER = Logger.getLogger(MainGameController.class.getClass().getSimpleName());
	private Game game = new Game();
	private ArrayList<PlayerInfo> playerRoom;
	private InitialConfigurator initialConfigurator;
	private VaticanReportManager vaticanReportManager = new VaticanReportManager();
	private ResourceHandler resourceHandler = new ResourceHandler();
	private MoveManager moveManager = new MoveManager(game, this);
	private EffectManager effectManager = new EffectManager(moveManager);
	private TurnInizializator turnInizializator = new TurnInizializator(effectManager, resourceHandler);
	private NetContrAdapter netContrAdapter = new NetContrAdapter();

	public MainGameController(ArrayList<PlayerInfo> playerRoom) throws RemoteException {
		this.playerRoom = playerRoom;
		initialConfigurator = new InitialConfigurator(game, playerRoom, resourceHandler, effectManager);
	}

	@Override
	public void run() {
		AbstractMove aMove;
		for (int countTurn = 0; countTurn < 4; countTurn++) {
			// inizio turno
			for (Player p : game.getPlayersOrder()) {
				// inizio turno di un giocatore
				if (checkPlayer(p))
					for (String sMove = ""; !sMove.startsWith("End@");) {
						try {
							// invio a tutti il nuovo model
							sendAll();
							// richiedo mossa a player
							sMove = getIPlayer(p).yourTurn();
						} catch (IOException e) {
							// ho perso la connessione con il client
							sMove = "End@Disconnect@";
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
						System.out.println(p.getNickname() + ": " + sMove);
						// ottengo informazioni dalla mossa ricevuta
						aMove = netContrAdapter.moveParser(p, sMove);
						// provo ad eseguire la mossa
						try {
							moveManager.manageMove(aMove);
						} catch (InvalidMoveException e) {
							e.printStackTrace();
						}
					}
				// fine turno di un giocatore
			}
			// fine turno
		}

		vaticanReport();
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
	 * verifico se il player in questione è connesso
	 */
	private boolean checkPlayer(Player p) {
		for (int j = 0; j < playerRoom.size(); j++)
			if (p.getNickname().equals(playerRoom.get(j).getName()))
				return playerRoom.get(j).getConnected();
		return false;
	}

	/*
	 * restituisco il giocatore corrispondente al client newtwork fornito
	 */
	private Player getPlayer(IPlayer ip) throws RemoteException {
		for (Player p : game.getPlayers())
			if (p.getNickname().equals(ip.getName()))
				return p;
		return null;
	}

	/*
	 * restituisco il client newtwork corrispondente al giocatore fornito
	 */
	private IPlayer getIPlayer(Player p) throws RemoteException {
		for (int j = 0; j < playerRoom.size(); j++)
			if (p.getNickname().equals(playerRoom.get(j).getName()))
				return playerRoom.get(j).getIplayer();
		return null;
	}

	/*
	 * invio il model a tutti i clients connessi
	 */
	private void sendAll() throws IOException {
		for (int j = 0; j < playerRoom.size(); j++) {
			if (checkPlayer(getPlayer(playerRoom.get(j).getIplayer()))
					&& game.getPlayersOrder().contains(getPlayer(playerRoom.get(j).getIplayer())))
				playerRoom.get(j).getIplayer().showBoard(game);
		}
	}

	/*
	 * metodo chiamato dal controller per gestire il rapporto con il Vaticano
	 */
	private void vaticanReport() {
		// solo alla fine del periodo
		if (game.getRound().equals(END_DEFINER))
			try {
				vaticanReportManager.manageVaticanReport(game, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		map.put("", NOTHING.clone());
		return map;
	}

	/*
	 * sospendo i client che sono disconnessi o non hanno effettuato la mossa in
	 * tempo utile
	 */
	public void disconnectPlayer(Player player) {
		// imposto a false l attibuto connected, cosi da segnalare che i player
		// è disconnesso
		for (int j = 0; j < playerRoom.size(); j++)
			if (player.getNickname().equals(playerRoom.get(j).getName()))
				playerRoom.get(j).setConnected(false);
		showMsgAll(player.getNickname() + " disconnected!");
	}

	/*
	 * visualizzo un messaggio sul display di tutti i client
	 */
	private void showMsgAll(String msg) {
		for (int j = 0; j < playerRoom.size(); j++)
			try {
				if (checkPlayer(getPlayer(playerRoom.get(j).getIplayer())))
					playerRoom.get(j).getIplayer().showMsg(msg);
			} catch (IOException e) {
			}
		System.out.println(msg);
	}

	/*
	 * metodo che gestisce la richiesta del numero di servitori che il player
	 * vuole aggiungere ad un effetto (sia di cardAction che di WorkAction)
	 */
	public Resource askForServants(Player player) throws IOException {
		Resource servants = NOTHING.clone();
		String result = getIPlayer(player).servantsRequest();
		servants.setServants(Integer.parseInt(result));
		return servants;
	}

	/*
	 * metodo che chiede torre e piano per una nuova mossa ritorna come primo
	 * parametro la torre e come secondo il floor
	 */
	public Integer[] askForCardSpace(Player player, CardActionEffect effect) throws IOException {
		Integer[] param = new Integer[2];
		try {
			if (effect.getCardType().equals(-1))
				param[0] = Integer.parseInt(getIPlayer(player).towerRequest());
			else
				param[0] = effect.getCardType();
			param[1] = Integer.parseInt(getIPlayer(player).floorRequest());
		} catch (NumberFormatException e) {
		}
		return param;
	}

	/*
	 * metodo che chiama il player giocante e chiede se vuole effettuare questo
	 * scambio (nel caso abbia effettivamente le risorse disponibili)
	 */
	public boolean askChangeToPlayer(Player p, Resource[] exchange) throws IOException {
		return getIPlayer(p).changeRequest(exchange);
	}

	/*
	 * metodo invocato se giveSupport() restituisce true e chiede al giocatore
	 * se desidera dare o no il sostegno alla Chiesa --> se si allora invochiamo
	 * il metodo che toglie i punti fede del giocatore e gli dà i corrispettivi
	 * punti vittoria + deve controllare se ha Sisto VI attivato
	 */
	public boolean askSupport(Player player) throws IOException {
		return getIPlayer(player).supportRequest();
	}

	/*
	 * metodo che richiede al player il colore su cui vuole attivare un
	 * determinato effetto
	 */
	public String askForColor(Player player) throws IOException {
		return getIPlayer(player).colorRequest();
	}

	/*
	 * Chiedo al player quale costo vuole usare: 0 -> primo costo(risorse), 1 ->
	 * secondo costo (punti militari)
	 */
	public Integer askForCost(CardMove cardMove) throws IOException {
		VentureCard vc = (VentureCard) game.getBoardgame().getTowers()[cardMove.getTowerSelected()].getFloor()[cardMove
				.getLevelSelected()].getCard();
		return getIPlayer(cardMove.getPlayer()).ventureCostRequest(vc);
	}

	/*
	 * Chiedo al player quale effetto vuole attivare: 1 -> primo effetto, 2 ->
	 * secondo effetto
	 */
	public Integer askForDoubleChange(Player player, DoubleChangeEffect effect) throws IOException {
		return getIPlayer(player).doubleChangeRequest(effect);
	}

}