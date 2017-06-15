package it.polimi.ingsw.LM22.network.client;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import it.polimi.ingsw.LM22.model.BuildingCard;
import it.polimi.ingsw.LM22.model.CharacterCard;
import it.polimi.ingsw.LM22.model.DevelopmentCard;
import it.polimi.ingsw.LM22.model.DoubleChangeEffect;
import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.MarketSpace;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.TerritoryCard;
import it.polimi.ingsw.LM22.model.Tower;
import it.polimi.ingsw.LM22.model.VentureCard;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;

/*
 * Classe che contiene tutti i metodi che permettono all'utente 
di interfacciarsi con la CLI
*/
public class CLIinterface extends AbstractUI {

	private final String DEFAULT_IP = "localhost";
	// Colori familiari
	private final String[] MEMBER_COLOR = { "Orange", "Black", "White", "Uncolored" };

	private final String CARDMOVE = "Card";
	private final String MARKETMOVE = "Market";
	private final String WORKMOVE = "Work";
	private final String COUNCILMOVE = "Council";

	private final String TERRITORY = "TERRITORY";
	private final String CHARACTER = "CHARACTER";
	private final String BUILDING = "BUILDING";
	private final String VENTURE = "VENTURE";

	private final String PRODUCTION = "PRODUCTION";
	private final String HARVEST = "HARVEST";

	private Scanner in = new Scanner(System.in);
	private String move = new String();
	private Game game;
	private String name;
	private boolean memberMove = false;
	private long timeout = 0;
	private long time;

	/*
	 * costruisco la stringa mossa per poi poterla inviare al server
	 */
	private void setMove(String add) {
		move = move + add + "@";
	}

	/*
	 * restituisce al chiamante la mossa creata in precedenza tramite il menu
	 */
	@Override
	public String getMove() throws RemoteException {
		return move;
	}

	/*
	 * menu principale da cui parte la costruzione guidata della mossa
	 */
	@Override
	public void printMoveMenu() throws RemoteException {
		move = "";
		timeout = game.getMoveTimer();
		time = System.currentTimeMillis() / 1000;
		/*
		 * Verifico che la mossa venga eseguita nel tempo prestabilito. Se non è
		 * trascorsa l'intera durata del tempo concesso, la mossa è valida
		 */
		ExecutorService exe = Executors.newFixedThreadPool(1);
		Future<?> future = exe.submit(() -> {
			try {
				showPrincipalMenu();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		});
		try {
			// eseguo l interfaccia di comunicazione in un thread con timeout
			// allo scadere del timeout termino il thread e disconnetto il
			// client
			future.get(timeout, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			move = "End@Disconnect@";
			System.out.println("Move time expired, now you have been disconnected!");
			// System.exit(0);
		}
	}

	private void showPrincipalMenu() throws RemoteException {
		timeout = (timeout - (System.currentTimeMillis() / 1000 - time));
		System.out.println("Timeout: " + timeout);
		showMsg("Choose your Move:");
		if (memberMove == false)
			showMsg("1: Move a Member");
		showMsg("2: Show card");
		if (!getPlayer(name, game).getHandLeaderCards().isEmpty())
			showMsg("3: Sell a LeaderCard");
		if (!getPlayer(name, game).getLeaderCards().isEmpty() || !getPlayer(name, game).getHandLeaderCards().isEmpty())
			showMsg("4: Activate a LeaderCard");
		showMsg("5: End turn");
		int option;
		option = in.nextInt();
		switch (option) {
		case 1:
			if (memberMove == false) {
				printMemberMoveMenu();
				break;
			} else {
				printInvalidInput();
				showPrincipalMenu();
			}
		case 2:
			showCard();
			showPrincipalMenu();
			break;
		case 3:
			if (!getPlayer(name, game).getHandLeaderCards().isEmpty())
				printSellLeaderCardMenu();
			else {
				printInvalidInput();
				showPrincipalMenu();
			}
			break;
		case 4:
			if (!getPlayer(name, game).getLeaderCards().isEmpty()
					|| !getPlayer(name, game).getHandLeaderCards().isEmpty())
				printActivateLeaderCardMenu();
			else {
				printInvalidInput();
				showPrincipalMenu();
			}
			break;
		case 5:
			setMove("End");
			memberMove = false;
			break;
		default:
			printInvalidInput();
			showPrincipalMenu();
			break;
		}
		// calcolo il timeout da cui dovrò partire alla possima mossa
		timeout = (timeout - (System.currentTimeMillis() / 1000 - time));
	}

	private void showCard() throws RemoteException {
		showMsg("Insert the card name: (no case sensitive)");
		in.nextLine();
		String name = in.nextLine();
		DevelopmentCard card = null;
		for (int j = 0; j < 4; j++)
			for (Tower t : game.getBoardgame().getTowers())
				if (t.getFloor()[j].getCard().getName().toLowerCase().equals(name.toLowerCase()))
					card = t.getFloor()[j].getCard();
		showMsg("");
		if (card != null) {
			showMsg("Name: " + card.getName());
			showMsg("Period: " + card.getPeriod());
			if (card instanceof TerritoryCard) {
				showMsg("Immediate effect: ");
				System.out.printf(card.getImmediateEffect().getInfo());
				showMsg("Permament effect:");
				System.out.printf("Requirement: " + ((TerritoryCard) card).getRequirement().toString() + "%n"
						+ ((TerritoryCard) card).getPermanentEffect().getInfo());
				showMsg("Card type: Territory");
			}
			if (card instanceof CharacterCard) {
				showMsg("Card cost: ");
				System.out.printf(((CharacterCard) card).getCost().getInfo());
				showMsg("Immediate effect: ");
				System.out.printf(card.getImmediateEffect().getInfo());
				showMsg("Permament effect:");
				System.out.printf(((CharacterCard) card).getPermanentEffect().getInfo());
				showMsg("Card type: Character");
			}
			if (card instanceof BuildingCard) {
				showMsg("Card cost: ");
				System.out.printf(((BuildingCard) card).getCost().getInfo());
				showMsg("Immediate effect: ");
				System.out.printf(card.getImmediateEffect().getInfo());
				showMsg("Permament effect:");
				System.out.printf(((BuildingCard) card).getPermanentEffect().getInfo());
				showMsg("Card type: Building");
			}
			if (card instanceof VentureCard) {
				showMsg("Card cost: ");
				System.out.printf(((VentureCard) card).getCardCost1().getInfo());
				showMsg("Second card cost: ");
				showMsg("Require: ");
				System.out.printf(((VentureCard) card).getCardCost2()[0].getInfo());
				showMsg("Cost: ");
				System.out.printf(((VentureCard) card).getCardCost2()[1].getInfo());
				showMsg("Immediate effect: ");
				System.out.printf(card.getImmediateEffect().getInfo());
				showMsg("Permament effect:");
				System.out.printf(((VentureCard) card).getPermanentEffect().getInfo());
				showMsg("Card type: Venture");
			}
		} else
			showMsg("Card not found!");

		showMsg("");

	}

	@Override
	public void printMemberMoveMenu() throws RemoteException {
		memberMove = true;
		showMsg("Choose your move:");
		showMsg("1: Card");
		showMsg("2: Market");
		showMsg("3: Work");
		showMsg("4: Council");
		int option = in.nextInt();
		switch (option) {
		case 1:
			printCardMoveMenu();
			break;
		case 2:
			printMarketMoveMenu();
			break;
		case 3:
			printWorkMoveMenu();
			break;
		case 4:
			printCouncilMoveMenu();
			break;
		default:
			printInvalidInput();
			printMemberMoveMenu();
			break;
		}
	}

	@Override
	public void printCardMoveMenu() throws RemoteException {
		setMove(CARDMOVE);
		printFamilyMemberMenu();
		setMove(printServantsAddictionMenu());
		setMove(printTowersMenu());
		setMove(printLevelsMenu());
	}

	@Override
	public void printFamilyMemberMenu() throws RemoteException {
		showMsg("Choose your Family Member you want to move:");
		int i = 0;
		for (FamilyMember fm : getPlayer(name, game).getMembers()) {
			if (!fm.isUsed())
				showMsg((i + 1) + ": " + MEMBER_COLOR[i]);
			i++;
		}
		int option = in.nextInt();
		switch (option) {
		case 1:
		case 2:
		case 3:
		case 4:
			if (!getPlayer(name, game).getMembers().get(option - 1).isUsed()) {
				setMove(MEMBER_COLOR[option - 1]);
				break;
			}
		default:
			printInvalidInput();
			printFamilyMemberMenu();
		}
	}

	public String printServantsAddictionMenu() throws RemoteException {
		showMsg("Insert how many servants you want to use");
		showMsg("Please insert a positive number or zero");
		Integer servants = in.nextInt();
		if (servants >= 0 && servants <= getPlayer(name, game).getPersonalBoard().getResources().getServants()) {
			return servants.toString();
		} else {
			printInvalidInput();
			return printServantsAddictionMenu();
		}
	}

	@Override
	public String printTowersMenu() throws RemoteException {
		showMsg("Choose the Tower:");
		showMsg("1: " + TERRITORY);
		showMsg("2: " + CHARACTER);
		showMsg("3: " + BUILDING);
		showMsg("4: " + VENTURE);
		int option = in.nextInt();
		switch (option) {
		case 1:
		case 2:
		case 3:
		case 4:
			return String.valueOf(option - 1);
		default:
			printInvalidInput();
			return printTowersMenu();
		}
	}

	@Override
	public String printLevelsMenu() throws RemoteException {
		showMsg("Insert the level of the tower (1 - 2 - 3 - 4):");
		Integer level = in.nextInt();
		switch (level) {
		case 1:
		case 2:
		case 3:
		case 4:
			return String.valueOf(level - 1);
		default:
			printInvalidInput();
			return printLevelsMenu();
		}
	}

	@Override
	public void printMarketMoveMenu() throws RemoteException {
		setMove(MARKETMOVE);
		printFamilyMemberMenu();
		setMove(printServantsAddictionMenu());
		printMarketSelectionMenu();
	}

	@Override
	public void printMarketSelectionMenu() throws RemoteException {
		showMsg("Choose the Market space:");
		showMsg("1: Five coins");
		showMsg("2: Five servants");
		if (game.getPlayers().length == 4) {
			showMsg("3: Three military points + two coins");
			showMsg("4: Two different counsil privilege");
		}
		int option = in.nextInt();
		switch (option) {
		case 1:
		case 2:
			setMove(String.valueOf(option - 1));
			break;
		case 3:
		case 4:
			if (game.getPlayers().length == 4) {
				setMove(String.valueOf(option - 1));
				break;
			}
		default:
			printInvalidInput();
			printMarketSelectionMenu();
			break;
		}
	}

	@Override
	public void printWorkMoveMenu() throws RemoteException {
		setMove(WORKMOVE);
		printFamilyMemberMenu();
		setMove(printServantsAddictionMenu());
		printWorkSelectionMenu();
	}

	@Override
	public void printWorkSelectionMenu() {
		showMsg("Choose the Work type:");
		showMsg("1: Production");
		showMsg("2: Harvest");
		int option = in.nextInt();
		switch (option) {
		case 1:
			setMove(PRODUCTION);
			break;
		case 2:
			setMove(HARVEST);
			break;
		default:
			printInvalidInput();
			printWorkSelectionMenu();
			break;
		}
	}

	@Override
	public void printCouncilMoveMenu() throws RemoteException {
		setMove(COUNCILMOVE);
		printFamilyMemberMenu();
		setMove(printServantsAddictionMenu());
	}

	@Override
	public void printSellLeaderCardMenu() throws RemoteException {
		setMove("LeaderSell");
		showMsg("Choose the Leader card to sell:");
		int i;
		for (i = 0; i < getPlayer(name, game).getHandLeaderCards().size(); i++) {
			showMsg((i + 1) + ": " + getPlayer(name, game).getHandLeaderCards().get(i).getName());
		}
		int option = in.nextInt();
		if (option <= i)
			setMove(getPlayer(name, game).getHandLeaderCards().get(option - 1).getName());
		else {
			printInvalidInput();
			printSellLeaderCardMenu();
		}
	}

	@Override
	public void printActivateLeaderCardMenu() throws RemoteException {
		ArrayList<LeaderCard> ld = new ArrayList<LeaderCard>();
		Collections.copy(ld, getPlayer(name, game).getLeaderCards());
		setMove("LeaderAct");
		showMsg("Choose the Leader card to activate:");
		int i;
		for (i = 0; i < getPlayer(name, game).getLeaderCards().size(); i++) {
			showMsg((i + 1) + ": " + getPlayer(name, game).getLeaderCards().get(i).getName());
		}
		int option = in.nextInt();
		if (option <= i)
			setMove(getPlayer(name, game).getLeaderCards().get(option).getName());
		else {
			printInvalidInput();
			printActivateLeaderCardMenu();
		}
	}

	@Override
	public void printInvalidInput() {
		showMsg("Invalid input");
	}

	/*
	 * permette di selezionare la connessione desiderata
	 */
	@Override
	public int showConnectionSelection() {
		int connType = 0;
		while (connType != 1 && connType != 2) {
			showMsg("Connection type: ");
			showMsg("1: RMI");
			showMsg("2: Socket");
			connType = Integer.parseInt(in.nextLine());
		}
		return connType;
	}

	/*
	 * richiede il nome del giocatore e lo restituisce al chiamante
	 */
	@Override
	public String getName() {
		String name;
		showMsg("Username(Player): ");
		name = in.nextLine();
		if (name.equals(""))
			name = "Player";
		this.name = name;
		return name;
	}

	/*
	 * richiede l'IP del server a cui connettersi e lo restituisce al chiamante
	 */
	@Override
	public String getIP() {
		String ip;
		showMsg("Indirizzo ip server(" + DEFAULT_IP + "): ");
		ip = in.nextLine();
		if (ip.equals(""))
			ip = DEFAULT_IP;
		return ip;
	}

	/*
	 * metodo che semplicemente permette di visualizzare messaggi all'utente
	 */
	@Override
	public void showMsg(String s) {
		System.out.println(s);
	}

	@Override
	public void connectionOK() {
		showMsg("Connessione stabilita!");
		showMsg("Attendi il tuo turno...");
	}

	/*
	 * ricevuto il model lo analizza e visualizza il tabellone e le informazioni
	 * necessarie all'utente per poter giocare
	 * 
	 * WIP
	 */
	private void showPersonalBoard(Game game) throws RemoteException {
		System.out.printf("%-30s|%n", "| Period: " + game.getPeriod());
		System.out.printf("%-30s|%n", "| Round: " + game.getRound());
		System.out.printf("%-30s|%n", "| ");
		System.out.printf("%-30s|%n", "| Dices:");
		System.out.printf("%-30s|%n", "| Orange: " + game.getBoardgame().getDice("Orange"));
		System.out.printf("%-30s|%n", "| Black: " + game.getBoardgame().getDice("Black"));
		System.out.printf("%-30s|%n", "| White: " + game.getBoardgame().getDice("White"));
		showMsg("|_____________________________|");
		System.out.printf("%-30s|%n", "| Family members:");
		System.out.printf("%-30s|%n", "| Orange: " + getPlayer(name, game).getMembers().get(0).getValue());
		System.out.printf("%-30s|%n", "| Black: " + getPlayer(name, game).getMembers().get(1).getValue());
		System.out.printf("%-30s|%n", "| White: " + getPlayer(name, game).getMembers().get(2).getValue());
		System.out.printf("%-30s|%n", "| Uncolored: " + getPlayer(name, game).getMembers().get(3).getValue());
		System.out.printf("%-30s|%n", "| ");
		System.out.printf("%-30s|%n", "| Name: " + getPlayer(name, game).getNickname());
		System.out.printf("%-30s|%n", "| Color: " + getPlayer(name, game).getColor());
		System.out.printf("%-30s|%n", "| Coins: " + getPlayer(name, game).getPersonalBoard().getResources().getCoins());
		System.out.printf("%-30s|%n", "| Wood: " + getPlayer(name, game).getPersonalBoard().getResources().getWood());
		System.out.printf("%-30s|%n", "| Stone: " + getPlayer(name, game).getPersonalBoard().getResources().getStone());
		System.out.printf("%-30s|%n",
				"| Servants: " + getPlayer(name, game).getPersonalBoard().getResources().getServants());
		System.out.printf("%-30s|%n",
				"| Faith Points: " + getPlayer(name, game).getPersonalBoard().getResources().getFaith());
		System.out.printf("%-30s|%n",
				"| Military Points: " + getPlayer(name, game).getPersonalBoard().getResources().getMilitary());
		System.out.printf("%-30s|%n",
				"| Victory Points: " + getPlayer(name, game).getPersonalBoard().getResources().getVictory());
		showMsg("|_____________________________|");
		// Hand LeaderCard
		System.out.printf("%-30s|%n", "| Hand leader cards:");
		for (LeaderCard ld : getPlayer(name, game).getHandLeaderCards())
			System.out.printf("%-30s|%n", "| " + ld.getName());
		showMsg("|_____________________________|");
		// Table LeaderCard
		System.out.printf("%-30s|%n", "| Table leader cards:");
		for (LeaderCard ld : getPlayer(name, game).getLeaderCards())
			System.out.printf("%-30s|%n", "| " + ld.getName());
		for (LeaderCard ld : getPlayer(name, game).getActivatedLeaderCards())
			System.out.printf("%-30s|%n", "| " + ld.getName());
		showMsg("|_____________________________|");
		// player dev cards
		System.out.printf("%-30s|%n", "| Development cards:");

		if (!getPlayer(name, game).getPersonalBoard().getBuildingsCards().isEmpty()) {
			for (BuildingCard c : getPlayer(name, game).getPersonalBoard().getBuildingsCards())
				System.out.printf("%-30s| ", "| " + c.getName());
			showMsg("");
		}
		if (!getPlayer(name, game).getPersonalBoard().getTerritoriesCards().isEmpty()) {
			for (TerritoryCard c : getPlayer(name, game).getPersonalBoard().getTerritoriesCards())
				System.out.printf("%-30s| ", "| " + c.getName());
			showMsg("");
		}
		if (!getPlayer(name, game).getPersonalBoard().getCharactersCards().isEmpty()) {
			for (CharacterCard c : getPlayer(name, game).getPersonalBoard().getCharactersCards())
				System.out.printf("%-30s| ", "| " + c.getName());
			showMsg("");
		}
		if (!getPlayer(name, game).getPersonalBoard().getVenturesCards().isEmpty()) {
			for (VentureCard c : getPlayer(name, game).getPersonalBoard().getVenturesCards())
				System.out.printf("%-30s| ", "| " + c.getName());
			showMsg("");
		}
	}

	private void showBoardTracks(Game game) throws RemoteException {
		System.out.printf("%-30s|%n", "| Faith points track:");
		// trovo il massimo victory
		int maxF = 0;
		for (Player p : game.getPlayers())
			if (p.getPersonalBoard().getResources().getFaith() > maxF)
				maxF = p.getPersonalBoard().getResources().getFaith();
		// stampo lista player
		for (int j = maxF; j >= 0; j--)
			for (Player p : game.getPlayers())
				if (p.getPersonalBoard().getResources().getFaith() == j)
					System.out.printf("%-30s|%n", "| " + j + " " + p.getNickname());
		showMsg("|_____________________________|");
		System.out.printf("%-30s|%n", "| Military points track:");
		// trovo il massimo victory
		int maxM = 0;
		for (Player p : game.getPlayers())
			if (p.getPersonalBoard().getResources().getMilitary() > maxM)
				maxM = p.getPersonalBoard().getResources().getMilitary();
		// stampo lista player
		for (int j = maxM; j >= 0; j--)
			for (Player p : game.getPlayers())
				if (p.getPersonalBoard().getResources().getMilitary() == j)
					System.out.printf("%-30s|%n", "| " + j + " " + p.getNickname());
		showMsg("|_____________________________|");
		System.out.printf("%-30s|%n", "| Victory points track:");
		// trovo il massimo victory
		int maxV = 0;
		for (Player p : game.getPlayers())
			if (p.getPersonalBoard().getResources().getVictory() > maxV)
				maxV = p.getPersonalBoard().getResources().getVictory();
		// stampo lista player
		for (int j = maxV; j >= 0; j--)
			for (Player p : game.getPlayers())
				if (p.getPersonalBoard().getResources().getVictory() == j)
					System.out.printf("%-30s|%n", "| " + j + " " + p.getNickname());
	}

	@Override
	public void showBoard(Game game) throws RemoteException {
		this.game = game;
		// Towers
		showMsg("______________________________");
		System.out.printf("%-30s|%n", "| Towers: ");
		showMsg("|_____________________________|");
		showMsg("        _________________________________________________________________________________________________");
		for (int j = 3; j >= 0; j--) {
			System.out.print("Dice " + game.getBoardgame().getTowers()[0].getFloor()[j].getSpace().getSpaceRequirement()
					+ ": | ");
			for (Tower t : game.getBoardgame().getTowers())
				if (t.getFloor()[j].getCard().getName() != null)
					System.out.printf("%-22s| ", t.getFloor()[j].getCard().getName());
				else
					System.out.printf("%-22s| ",
							"#Name: " + t.getFloor()[j].getSpace().getMember().getPlayer().getNickname());
			showMsg("");
			System.out.print("Reward: | ");
			for (Tower t : game.getBoardgame().getTowers())
				if (t.getFloor()[j].getCard().getName() != null)
					System.out.printf("%-22s| ",
							t.getFloor()[j].getSpace().getReward().getInfo().replaceAll("%n", " "));
				else
					System.out.printf("%-22s| ",
							"#Color: " + t.getFloor()[j].getSpace().getMember().getPlayer().getColor());
			showMsg("");
			showMsg("        |_______________________|_______________________|_______________________|_______________________|");

		}
		// palazzo del consiglio
		showMsg("");
		showMsg("______________________________");
		System.out.printf("%-30s|%n", "| Council Palace members: ");
		int i = 1;
		for (FamilyMember fm : game.getBoardgame().getCouncilPalace().getMembers()) {
			System.out.printf("%-30s|%n", "| " + i + ":" + fm.getPlayer().getNickname());
			i++;
		}
		// mercato
		showMsg("|_____________________________|");
		showMsg("______________________________");
		System.out.printf("%-30s|%n", "| Market spaces: ");
		showMsg("|_____________________________|");
		for (MarketSpace ms : game.getBoardgame().getMarket()) {
			if (ms.getMember() == null) {
				System.out.printf("%-30s|%n", "| Reward:");
				System.out.printf("- " + ms.getReward().getInfo().replaceAll("%n", "%n- "));
				System.out.printf("PdC: " + ms.getCouncilPrivilege() + "%n");
				System.out.printf("%-30s|%n", "| Requirement:" + ms.getSpaceRequirement());
			} else
				System.out.printf("%-30s|%n", "| " + ms.getMember().getPlayer().getNickname());
			showMsg("|_____________________________|");
		}
		showMsg("______________________________");
		showBoardTracks(game);
		showMsg("|_____________________________|");
		showMsg("______________________________");
		showPersonalBoard(game);
		showMsg("|_____________________________|");
		showMsg("");
		/*
		 * personal board altri giocatori?
		 */
	}

	@Override
	public String councilRequest(Integer number) throws RemoteException {
		ArrayList<String> list = new ArrayList<String>();
		String result = new String();
		String[] council = { "wood&stone", "servants", "coins", "military", "faith" };
		for (int k = 0; k < number;) {
			showMsg("Choose the Council Privilege reward:");
			if (!list.contains("wood&stone"))
				showMsg("1: one stone & one wood");
			if (!list.contains("servants"))
				showMsg("2: two servants");
			if (!list.contains("coins"))
				showMsg("3: two coins");
			if (!list.contains("military"))
				showMsg("4: two military points");
			if (!list.contains("faith"))
				showMsg("5: one faith point");
			int option = in.nextInt();

			if (!list.contains(council[option - 1])) {
				list.add(council[option - 1]);
				k++;
			} else
				printInvalidInput();
		}
		for (String s : list)
			result = result + "@" + s;

		return result;
	}

	@Override
	public boolean printSupportMenu() throws RemoteException {
		showMsg("Do you want support the Vatican?: ");
		showMsg("1: Yes");
		showMsg("2: No");
		Integer option = in.nextInt();
		switch (option) {
		case 1:
			return true;
		case 2:
			return false;
		default:
			printInvalidInput();
			return printSupportMenu();
		}
	}

	@Override
	public String printColorMenu() throws RemoteException {
		showMsg("Choose the family member that you want improve: ");
		for (int i = 0; i < 3; i++) {
			showMsg((i + 1) + ": " + MEMBER_COLOR[i]);
		}
		Integer option = in.nextInt();
		switch (option) {
		case 1:
		case 2:
		case 3:
			return MEMBER_COLOR[option - 1];
		default:
			printInvalidInput();
			return printColorMenu();
		}
	}

	@Override
	public Integer printVentureCostMenu(VentureCard vc) throws RemoteException {
		showMsg("Which cost do you want pay?: ");
		showMsg("1: Resource cost");
		System.out.printf(vc.getCardCost1().getInfo());
		showMsg("2: Military points cost");
		System.out.printf("Requirement: " + vc.getCardCost2()[0].getInfo());
		System.out.printf("Cost: " + vc.getCardCost2()[1].getInfo());
		Integer option = in.nextInt();
		switch (option) {
		case 1:
		case 2:
			return (option - 1);
		default:
			printInvalidInput();
			return printVentureCostMenu(vc);
		}
	}

	@Override
	public boolean printChangeMenu(Resource[] exchange) throws RemoteException {
		showMsg("Do you want change");
		System.out.printf(exchange[0].getInfo());
		showMsg("with");
		System.out.printf(exchange[1].getInfo());
		showMsg("1: Yes");
		showMsg("2: No");
		Integer option = in.nextInt();
		switch (option) {
		case 1:
			return true;
		case 2:
			return false;
		default:
			printInvalidInput();
			return printChangeMenu(exchange);
		}
	}

	@Override
	public Integer printDoubleChangeMenu(DoubleChangeEffect effect) throws RemoteException {
		showMsg("Choose the effect that you want activate: ");
		showMsg("1: Change");
		System.out.printf(effect.getExchangeEffect1()[0].getInfo());
		showMsg("With");
		System.out.printf(effect.getExchangeEffect1()[1].getInfo());
		showMsg("OR");
		showMsg("2: Change");
		System.out.printf(effect.getExchangeEffect2()[0].getInfo());
		showMsg("With");
		System.out.printf(effect.getExchangeEffect2()[1].getInfo());
		Integer option = in.nextInt();
		switch (option) {
		case 1:
		case 2:
			return option;
		default:
			printInvalidInput();
			return printDoubleChangeMenu(effect);
		}
	}
}
