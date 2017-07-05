package it.polimi.ingsw.LM22.controller;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.LM22.model.BuildingCard;
import it.polimi.ingsw.LM22.model.CardActionEffect;
import it.polimi.ingsw.LM22.model.DoubleChangeEffect;
import it.polimi.ingsw.LM22.model.Effect;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.VentureCard;
import it.polimi.ingsw.LM22.model.excommunication.FinalCardCostMalusEx;
import it.polimi.ingsw.LM22.model.excommunication.FinalResourceMalusEx;
import it.polimi.ingsw.LM22.model.excommunication.NoFinalCardPointsEx;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;
import it.polimi.ingsw.LM22.network.NetContrAdapter;
import it.polimi.ingsw.LM22.network.server.IPlayer;
import it.polimi.ingsw.LM22.network.server.PlayerInfo;

public class MainGameController implements Runnable {
	private static final Resource NOTHING = new Resource(0, 0, 0, 0, 0, 0, 0);
	private static final HashMap<String, Resource> councilResource = initializeCouncilMap();

	private static final String DISCONNECTED = " has been disconnected";

	private static final Integer END_DEFINER = 2;
	private static final Integer LAST_PERIOD = 3;

	private static final Integer TERRITORY = 0;
	private static final Resource[] territoryReward = { NOTHING, NOTHING, NOTHING, new Resource(0, 0, 0, 0, 0, 0, 1),
			new Resource(0, 0, 0, 0, 0, 0, 4), new Resource(0, 0, 0, 0, 0, 0, 10), new Resource(0, 0, 0, 0, 0, 0, 20) };
	private static final Integer CHARACTER = 1;
	private static final Resource[] characterReward = { NOTHING, new Resource(0, 0, 0, 0, 0, 0, 1),
			new Resource(0, 0, 0, 0, 0, 0, 3), new Resource(0, 0, 0, 0, 0, 0, 6), new Resource(0, 0, 0, 0, 0, 0, 10),
			new Resource(0, 0, 0, 0, 0, 0, 15), new Resource(0, 0, 0, 0, 0, 0, 21) };
	private static final Integer BUILDING = 2;
	private static final Integer VENTURE = 3;
	private static final Resource FIVE_VICTORY = new Resource(0, 0, 0, 0, 0, 0, 5);
	private static final Resource TWO_VICTORY = new Resource(0, 0, 0, 0, 0, 0, 2);

	private static final Logger LOGGER = Logger.getLogger(MainGameController.class.getClass().getSimpleName());
	private Game game = new Game();
	private List<PlayerInfo> playerRoom;
	private VaticanReportManager vaticanReportManager = new VaticanReportManager();
	private ResourceHandler resourceHandler = new ResourceHandler();
	private MoveManager moveManager = new MoveManager(game, this);
	private EffectManager effectManager = new EffectManager(moveManager);
	private InitialConfigurator initConf;
	private TurnInizializator turnInizializator = new TurnInizializator(effectManager, this);
	private NetContrAdapter netContrAdapter = new NetContrAdapter();

	/**
	 * salvo la lista di player passata come parametro e inizializzo la partita
	 * tramite l'initialConfigurator
	 */
	public MainGameController(List<PlayerInfo> playerRoom) throws RemoteException {
		this.playerRoom = playerRoom;
		initConf = new InitialConfigurator(playerRoom, effectManager, this);
		initConf.initializeTurn(game);
	}

	@Override
	public void run() {
		// distribuzione personal tile
		personalTileSelection();
		// distribuzione carte leader
		leaderSelection();
		// inizio della partita
		showMsgAll("GAME STARTED!!!");
		startGame();
	}

	/**
	 * permetto al giocatore di scegliere una personal bonus tile in ordine di
	 * gioco inverso
	 */
	private void personalTileSelection() {
		for (int i = game.getPlayersOrder().size() - 1; i >= 0; i--) {
			Player p = game.getPlayersOrder().get(i);
			int selection;
			if (checkPlayer(p))
				try {
					selection = getIPlayer(p).selectPersonalTile(game);
					p.getPersonalBoard().setBonusBoard(game.getPersonalBonusTile()[selection]);
					game.getPersonalBonusTile()[selection] = null;
				} catch (IOException e) {
					disconnectPlayer(p);
					LOGGER.log(Level.INFO, p.getNickname() + DISCONNECTED, e);
				}
		}
	}

	/**
	 * consente ad ogni giocatore di scegliere le proprire carte leader
	 */
	private void leaderSelection() {
		// per tutte le 4 carte leader
		for (int i = 0; i < 3;) {
			// mostro a tutti i player la scelta della prima carta
			// la seguente operazione non è bloccante per il server
			playersLeaderCard();
			// per ogni player
			int counter = 0;
			String leaderSelected[] = new String[4];
			for (int j = 0; j < game.getPlayersOrder().size(); j++) {
				Player p = game.getPlayersOrder().get(j);
				// se il giocatore è connesso e non ha ancora scelto
				if (checkPlayer(p))
					try {
						// ottengo il valore della carta >la carta selezionata
						leaderSelected[j] = getIPlayer(p).getLeaderCard();
						counter++;
					} catch (IOException e) {
						LOGGER.log(Level.INFO, p.getNickname() + DISCONNECTED, e);
						disconnectPlayer(p);
					}
			}
			// se tutti i player hanno selezionato la carta
			if (counter == game.getPlayersOrder().size()) {
				// applico le modifiche alle liste delle carte
				for (int j = 0; j < game.getPlayersOrder().size(); j++) {
					Player p = game.getPlayersOrder().get(j);
					// sposto la carta selezionata nella lista HandLeaderCard
					p.getHandLeaderCards().add(leaderByName(leaderSelected[j], p));
					p.getLeaderCards().remove(leaderByName(leaderSelected[j], p));
					// player successivo
					Player pNext;
					if (game.getPlayersOrder().size() == j + 1)
						pNext = game.getPlayersOrder().get(0);
					else
						pNext = game.getPlayersOrder().get(j + 1);
					// inserisco temporanemente nella lista activatedLeaderCards
					// le carte rimanenti
					pNext.getActivatedLeaderCards().addAll(p.getLeaderCards());
					// svuoto la lista LeaderCards
					p.setLeaderCards(new ArrayList<LeaderCard>());
				}
				// trasferico le carte dalla lista leaderCards alla lista
				// ActivateLeaderCard per ogni Player
				for (Player p : game.getPlayersOrder()) {
					p.setLeaderCards(p.getActivatedLeaderCards());
					p.setActivatedLeaderCards(new ArrayList<LeaderCard>());
				}
				// mostro di nuovo il menu di selezione delle carte
				i++;
			}
		}
		// assegno d'ufficio l'ultima carta al player
		for (Player p : game.getPlayersOrder()) {
			p.getHandLeaderCards().addAll(p.getLeaderCards());
			p.setLeaderCards(new ArrayList<LeaderCard>());
		}
	}

	private LeaderCard leaderByName(String cardName, Player p) {
		for (LeaderCard ld : p.getLeaderCards())
			if (ld.getName().toLowerCase().startsWith(cardName.toLowerCase()))
				return ld;
		return null;
	}

	private void playersLeaderCard() {
		for (Player p : game.getPlayersOrder()) {
			if (checkPlayer(p))
				try {
					getIPlayer(p).selectLeaderCard(game);
				} catch (IOException e) {
					LOGGER.log(Level.INFO, p.getNickname() + DISCONNECTED, e);
					disconnectPlayer(p);
				}
		}
	}

	/**
	 * vero inizio della partita, viene visualizzata la board e permette di
	 * giocare al primo giocatore
	 */
	private void startGame() {
		// invio a tutti il nuovo model
		sendAll();
		for (int countTurn = 0; countTurn < 4; countTurn++) {
			// inizio turno
			for (Player p : game.getPlayersOrder()) {
				// inizio turno di un giocatore
				if (checkPlayer(p))
					playTurn(p);
				// fine turno di un giocatore
			}
			// fine turno
		}
		vaticanReport();
		// verifico se è la fine del gioco
		if (game.getPeriod().equals(LAST_PERIOD) && game.getRound().equals(END_DEFINER))
			manageEndGame(game);
		else {
			turnInit();
			startGame();
		}
	}

	/**
	 * turno di un giocatore: può fare tutte la mosse che vuole finche non
	 * seleziona la mossa End
	 */
	private void playTurn(Player p) {
		AbstractMove aMove;
		for (String sMove = ""; !sMove.startsWith("End@");) {
			// inizio di una mossa
			try {
				// richiedo mossa a player
				sMove = getIPlayer(p).yourTurn();
			} catch (IOException e) {
				LOGGER.log(Level.INFO, "Lost connection with " + p.getNickname(), e);
				// ho perso la connessione con il client
				sMove = "End@Disconnect@";
			}
			// ottengo informazioni dalla mossa ricevuta
			aMove = netContrAdapter.moveParser(p, sMove);
			// provo ad eseguire la mossa richiesta
			try {
				moveManager.manageMove(aMove);
				sendAll();
			} catch (InvalidMoveException e) {
				LOGGER.log(Level.INFO, p.getNickname() + " has done an invalid move", e);
				// il player ha fatto una mossa non valida
				try {
					if (sMove.startsWith("Leader"))
						getIPlayer(p).showMsg("Invalid leader move!!!");
					else
						getIPlayer(p).showMsg("Invalid member move!!!");
				} catch (IOException e1) {
					// player mossa errata + client disconnesso
					LOGGER.log(Level.INFO, p.getNickname() + DISCONNECTED, e1);
					disconnectPlayer(p);
				}
			}
			// fine di una mossa
		}
	}

	/**
	 * verifico se il player in questione è connesso
	 */
	private boolean checkPlayer(Player p) {
		for (int j = 0; j < playerRoom.size(); j++)
			if (p.getNickname().equals(playerRoom.get(j).getName()))
				return playerRoom.get(j).getConnected();
		return false;
	}

	/**
	 * restituisco il client newtwork corrispondente al giocatore fornito
	 */
	public IPlayer getIPlayer(Player p) throws RemoteException {
		for (int j = 0; j < playerRoom.size(); j++)
			if (p.getNickname().equals(playerRoom.get(j).getName()))
				return playerRoom.get(j).getIplayer();
		return null;
	}

	/**
	 * invio il model a tutti i clients connessi
	 */
	private void sendAll() {
		try {
			for (int j = 0; j < playerRoom.size(); j++) {
				if (playerRoom.get(j).getConnected())
					playerRoom.get(j).getIplayer().showBoard(game);
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Connection Problems due to IOException", e);
		}
	}

	public Game getGame() {
		return game;
	}

	/**
	 * metodo chiamato dal controller per gestire il rapporto con il Vaticano
	 */
	private void vaticanReport() {
		// solo alla fine del periodo
		if (game.getRound().equals(END_DEFINER))
			try {
				showMsgAll("Vatican Report!");
				vaticanReportManager.manageVaticanReport(game, this);
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Vatican Report error!", e);
			}
	}

	/**
	 * avvia l'inizializzazione del contesto di gioco per il prossimo turno
	 */
	private void turnInit() {
		try {
			turnInizializator.initializeTurn(game);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Turn initializing error!", e);
		}
	}

	/**
	 * permette di gestire tutta la fase di conteggio dei Punti Finali
	 */
	private void manageEndGame(Game game) {
		manageMilitaryStandingPoints(game);
		manageVictoryPointDueToCards(game);
		electWinner(game);
		LOGGER.log(Level.INFO, "Game ended");
		disconnectRoomPlayers();
	}

	/**
	 * gestisce la fase di rimozione dei player dalla lista della room che
	 * conteneva la partita terminata
	 */
	private void disconnectRoomPlayers() {
		for (int i = 0; i < playerRoom.size();) {
			try {
				playerRoom.get(i).getIplayer().close();
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Player already disconnected!", e);
			}
			playerRoom.remove(i);
		}

	}

	/**
	 * metodo che gestisce l'attribuzione dei punti vittoria finali in base alla
	 * classifica dei punti militari
	 */
	private void manageMilitaryStandingPoints(Game game) {
		int max1 = 0;
		int max2 = 0;
		for (Player p : game.getPlayersOrder()) {
			if (p.getPersonalBoard().getResources().getMilitary() > max1) {
				max2 = max1;
				max1 = p.getPersonalBoard().getResources().getMilitary();
			} else if (p.getPersonalBoard().getResources().getMilitary() < max1
					&& p.getPersonalBoard().getResources().getMilitary() > max2)
				max2 = p.getPersonalBoard().getResources().getMilitary();
		}
		int cont1 = 0;
		for (Player p : game.getPlayersOrder()) {
			if (p.getPersonalBoard().getResources().getMilitary().equals(max1)) {
				resourceHandler.addResource(p.getPersonalBoard().getResources(),
						resourceHandler.calculateResource(FIVE_VICTORY, p, false));
				cont1++;
			}
		}
		if (cont1 > 1)
			return;
		for (Player p : game.getPlayersOrder()) {
			if (p.getPersonalBoard().getResources().getMilitary().equals(max2)) {
				resourceHandler.addResource(p.getPersonalBoard().getResources(),
						resourceHandler.calculateResource(TWO_VICTORY, p, false));
			}
		}
		return;
	}

	/**
	 * metodo che gestisce l'attribuzione dei punti vittoria in base al numero
	 * di carte territorio o personaggio che ogni player ha ottenuto +
	 * attribuzione dei punti vittoria dati dalle VentureCards --> in questo
	 * caso devo fare il controllo su alcuni tipi di scomuniche del terzo
	 * periodo
	 */
	private void manageVictoryPointDueToCards(Game game) {
		for (Player p : game.getPlayersOrder()) {
			manageVictoryPointsDueToResource(p);
			manageFinalTerritoryCards(p);
			manageFinalCharacterCards(p);
			manageFinalBuildingCards(p);
			manageFinalVentureCards(p);
		}
	}

	private void manageFinalVentureCards(Player p) {
		for (Effect e : p.getEffects()) {
			if (e instanceof NoFinalCardPointsEx && ((NoFinalCardPointsEx) e).getCardType().equals(VENTURE))
				return;
		}
		Resource total = NOTHING.copy();
		for (VentureCard card : p.getPersonalBoard().getVenturesCards()) {
			resourceHandler.addResource(total,
					resourceHandler.calculateResource(card.getPermanentEffect().copy(), p, false));
		}
		resourceHandler.addResource(p.getPersonalBoard().getResources(), total);
	}

	private void manageFinalBuildingCards(Player p) {
		for (Effect e : p.getEffects())
			if (e instanceof FinalCardCostMalusEx && ((FinalCardCostMalusEx) e).getCardType().equals(BUILDING)) {
				Integer total = 0;
				for (BuildingCard card : p.getPersonalBoard().getBuildingsCards()) {
					total = total + card.getCost().getStone() + card.getCost().getStone();
				}
				resourceHandler.subResource(p.getPersonalBoard().getResources(), new Resource(0, 0, 0, 0, 0, 0, total));
			}
	}

	private void manageFinalCharacterCards(Player p) {
		for (Effect e : p.getEffects()) {
			if (e instanceof NoFinalCardPointsEx && ((NoFinalCardPointsEx) e).getCardType().equals(CHARACTER))
				return;
		}
		resourceHandler.addResource(p.getPersonalBoard().getResources(), resourceHandler
				.calculateResource(characterReward[p.getPersonalBoard().getCharactersCards().size()].copy(), p, false));
	}

	private void manageFinalTerritoryCards(Player p) {
		for (Effect e : p.getEffects()) {
			if (e instanceof NoFinalCardPointsEx && ((NoFinalCardPointsEx) e).getCardType().equals(TERRITORY))
				return;
		}
		resourceHandler.addResource(p.getPersonalBoard().getResources(), resourceHandler.calculateResource(
				territoryReward[p.getPersonalBoard().getTerritoriesCards().size()].copy(), p, false));
	}

	/**
	 * metodo che gestisce l'attribuzione di punti vittoria in base al numero di
	 * risorse possedute da ogni player (1 punto vittoria per ogni 5 risorse
	 * wood-stone-coins-servants del player contate tutte insieme ) --> qui
	 * controllo la presenza di scomuniche del terzo periodo che non fanno
	 * prendere certi punti
	 */
	private void manageVictoryPointsDueToResource(Player p) {
		for (Effect e : p.getEffects()) {
			if (e instanceof FinalResourceMalusEx && ((FinalResourceMalusEx) e).getResource().getMilitary().equals(1)) {
				resourceHandler.subResource(p.getPersonalBoard().getResources(),
						new Resource(0, 0, 0, 0, 0, 0, p.getPersonalBoard().getResources().getMilitary()));
			}
			if (e instanceof FinalResourceMalusEx && ((FinalResourceMalusEx) e).getResource().getVictory().equals(5)) {
				resourceHandler.subResource(p.getPersonalBoard().getResources(),
						new Resource(0, 0, 0, 0, 0, 0, p.getPersonalBoard().getResources().getVictory() / 5));
			}
			if (e instanceof FinalResourceMalusEx && resourceHandler
					.equalResources(((FinalResourceMalusEx) e).getResource(), new Resource(1, 1, 1, 1, 0, 0, 0))) {
				Integer total = ((FinalResourceMalusEx) e).getResource().getWood()
						+ ((FinalResourceMalusEx) e).getResource().getStone()
						+ ((FinalResourceMalusEx) e).getResource().getCoins()
						+ ((FinalResourceMalusEx) e).getResource().getServants();
				resourceHandler.subResource(p.getPersonalBoard().getResources(), new Resource(0, 0, 0, 0, 0, 0, total));
			}
		}
		Integer total = p.getPersonalBoard().getResources().getWood() + p.getPersonalBoard().getResources().getStone()
				+ p.getPersonalBoard().getResources().getCoins() + p.getPersonalBoard().getResources().getServants();
		resourceHandler.addResource(p.getPersonalBoard().getResources(), new Resource(0, 0, 0, 0, 0, 0, total / 5));
	}

	/**
	 * metodo che viene invocato ogni volta che ottengo un effetto comprendente
	 * x councilPrivilege e permette di scegliere x councilPrivilege diversi, si
	 * avrà un ciclo che permette di scegliere tra le varie possibilità e al
	 * ciclo dopo si toglie il tipo di risorsa già scelto
	 */
	public Resource selectCouncilPrivilege(Integer councilNumber, Player player) throws IOException {
		Resource resource = new Resource(0, 0, 0, 0, 0, 0, 0);
		if (councilNumber == 0)
			return resource;
		String result = getIPlayer(player).councilRequest(councilNumber);
		String[] cp = result.split("@");
		for (String c : cp) {
			resourceHandler.addResource(resource, councilResource.get(c));
		}
		return resource;
	}

	/**
	 * show the message to all the players of the game
	 */
	private void electWinner(Game game) {
		Integer max = 0;
		for (Player p : game.getPlayersOrder()) {
			if (p.getPersonalBoard().getResources().getVictory() > max)
				max = p.getPersonalBoard().getResources().getVictory();
		}
		for (Player p : game.getPlayersOrder()) {
			if (p.getPersonalBoard().getResources().getVictory().equals(max)) {
				showMsgAll("The winner is " + p.getNickname() + " with a total of "
						+ p.getPersonalBoard().getResources().getVictory() + " victory Points!!");
			}
		}
		for (Player p : game.getPlayersOrder()) {
			showMsgAll(p.getNickname() + " has ended the game with "
					+ p.getPersonalBoard().getResources().getInfo().replace("%n", " "));
		}
	}

	/**
	 * inizializzo HashMap con i valori predefiniti del Privilegio del consiglio
	 */
	private static HashMap<String, Resource> initializeCouncilMap() {
		HashMap<String, Resource> map = new HashMap<String, Resource>();
		map.put("wood&stone", new Resource(1, 1, 0, 0, 0, 0, 0));
		map.put("servants", new Resource(0, 0, 2, 0, 0, 0, 0));
		map.put("coins", new Resource(0, 0, 0, 2, 0, 0, 0));
		map.put("military", new Resource(0, 0, 0, 0, 0, 2, 0));
		map.put("faith", new Resource(0, 0, 0, 0, 1, 0, 0));
		map.put("", NOTHING.copy());
		return map;
	}

	/**
	 * sospendo i client che sono disconnessi o non hanno effettuato la mossa in
	 * tempo utile
	 */
	public void disconnectPlayer(Player player) {
		// chiudo le connessioni e imposto a false l'attibuto connected
		// cosi da segnalare che i player è disconnesso
		for (int j = 0; j < playerRoom.size(); j++)
			if (player.getNickname().equals(playerRoom.get(j).getName())) {
				try {
					playerRoom.get(j).getIplayer().close();
				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, "Player already disconnected!", e);
				}
				playerRoom.get(j).setConnected(false);
			}

		showMsgAll(player.getNickname() + " disconnected!");
	}

	/**
	 * visualizzo un messaggio sul display di tutti i client (e sul server per
	 * tracciabilità)
	 */
	private void showMsgAll(String msg) {
		for (int j = 0; j < playerRoom.size(); j++)
			try {
				if (playerRoom.get(j).getConnected())
					playerRoom.get(j).getIplayer().showMsg(msg);
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Error sending '" + msg + "' to all!", e);
			}
		LOGGER.log(Level.INFO, msg);
	}

	/**
	 * metodo che gestisce la richiesta del numero di servitori che il player
	 * vuole aggiungere ad un effetto (sia di cardAction che di WorkAction)
	 */
	public Resource askForServants(Player player) throws IOException {
		Resource servants = NOTHING.copy();
		String result = getIPlayer(player).servantsRequest();
		servants.setServants(Integer.parseInt(result));
		return servants;
	}

	/**
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
			LOGGER.log(Level.SEVERE, "Error Number Format Exception!", e);
		}
		return param;
	}

	/**
	 * metodo che chiama il player giocante e chiede se vuole effettuare questo
	 * scambio (nel caso abbia effettivamente le risorse disponibili)
	 */
	public boolean askChangeToPlayer(Player p, Resource[] exchange) throws IOException {
		return getIPlayer(p).changeRequest(exchange);
	}

	/**
	 * metodo invocato se giveSupport() restituisce true e chiede al giocatore
	 * se desidera dare o no il sostegno alla Chiesa --> se si allora invochiamo
	 * il metodo che toglie i punti fede del giocatore e gli dà i corrispettivi
	 * punti vittoria + deve controllare se ha Sisto VI attivato
	 */
	public boolean askSupport(Player player) throws IOException {
		return getIPlayer(player).supportRequest();
	}

	/**
	 * metodo che richiede al player il colore su cui vuole attivare un
	 * determinato effetto
	 */
	public String askForColor(Player player) throws IOException {
		return getIPlayer(player).colorRequest();
	}

	/**
	 * Chiedo al player quale costo vuole usare: 0 -> primo costo(risorse), 1 ->
	 * secondo costo (punti militari)
	 */
	public Integer askForCost(CardMove cardMove) throws IOException {
		VentureCard vc = (VentureCard) game.getBoardgame().getTowers()[cardMove.getTowerSelected()].getFloor()[cardMove
				.getLevelSelected()].getCard();
		if ("No resource%n".equals(vc.getCardCost1().getInfo()))
			return 1;
		else if ("No resource%n".equals(vc.getCardCost2()[0].getInfo()))
			return 0;
		else
			return getIPlayer(cardMove.getPlayer()).ventureCostRequest(vc);
	}

	/**
	 * Chiedo al player quale effetto vuole attivare: 1 -> primo effetto, 2 ->
	 * secondo effetto
	 */
	public Integer askForDoubleChange(Player player, DoubleChangeEffect effect) throws IOException {
		return getIPlayer(player).doubleChangeRequest(effect);
	}

	/**
	 * chiedo al player da quale card vuole copiare l'effetto e restituisco il
	 * nome
	 */
	public String askToPlayerForEffectToCopy(Player player, List<LeaderCard> lcards) throws IOException {
		return getIPlayer(player).askToPlayerForEffectToCopy(lcards);
	}

}