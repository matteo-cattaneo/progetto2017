package it.polimi.ingsw.LM22.network.client;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Tower;
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

	private final String LEVEL_1 = "1";
	private final String LEVEL_2 = "2";
	private final String LEVEL_3 = "3";
	private final String LEVEL_4 = "4";

	private Scanner in = new Scanner(System.in);
	private String move = new String();
	private Game game;
	private String name;
	private boolean memberMove = false;

	/*
	 * costruisco la stringa mossa per poi poterla inviare al server
	 */
	public void setMove(String add) {
		move = move + add + "@";
	}

	/*
	 * restituisce al chiamante la mossa creata in precedenza tramite il menu
	 */
	@Override
	public String getMove() {
		return move;
	}

	/*
	 * menu principale da cui parte la costruzione guidata della mossa
	 */
	@Override
	public void printMoveMenu() throws RemoteException {
		move = "";
		showMsg("Choose your Move:");
		if (memberMove == false)
			showMsg("1: Move a Member");
		if (!getPlayer(name, game).getLeaderCards().isEmpty()) {
			showMsg("2: Sell a LeaderCard");
			showMsg("3: Activate a LeaderCard");
		}
		showMsg("4: End turn");
		int option = in.nextInt();
		switch (option) {
		case 2:
			if (!getPlayer(name, game).getLeaderCards().isEmpty())
				printSellLeaderCardMenu();
			else {
				printInvalidInput();
				printMoveMenu();
			}
			break;
		case 3:
			if (!getPlayer(name, game).getLeaderCards().isEmpty())
				printActivateLeaderCardMenu();
			else {
				printInvalidInput();
				printMoveMenu();
			}
			break;
		case 4:
			setMove("End");
			memberMove = false;
			break;
		case 1:
			if (memberMove == false) {
				printMemberMoveMenu();
				memberMove = true;
				break;
			}
		default:
			printInvalidInput();
			printMoveMenu();
			break;
		}
	}

	@Override
	public void printMemberMoveMenu() throws RemoteException {
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
		printServantsAddictionMenu();
		printTowersMenu();
		printLevelsMenu();
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
			break;
		}
	}

	@Override
	public void printServantsAddictionMenu() throws RemoteException {
		showMsg("Insert how many servants you want to use");
		showMsg("Please insert a positive number or zero");
		Integer servants = in.nextInt();
		if (servants >= 0 && servants <= getPlayer(name, game).getPersonalBoard().getResources().getServants()) {
			setMove(servants.toString());
		} else {
			printInvalidInput();
			printServantsAddictionMenu();
		}
	}

	@Override
	public void printTowersMenu() {
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
			setMove(String.valueOf(option - 1));
			break;
		default:
			printInvalidInput();
			printTowersMenu();
			break;
		}
	}

	@Override
	public void printLevelsMenu() {
		showMsg("Insert the level of the tower (1 - 2 - 3 - 4):");
		Integer servants = in.nextInt();
		switch (servants) {
		case 1:
			setMove(LEVEL_1);
			break;
		case 2:
			setMove(LEVEL_2);
			break;
		case 3:
			setMove(LEVEL_3);
			break;
		case 4:
			setMove(LEVEL_4);
			break;
		default:
			printInvalidInput();
			printLevelsMenu();
		}
	}

	@Override
	public void printMarketMoveMenu() throws RemoteException {
		setMove(MARKETMOVE);
		printFamilyMemberMenu();
		printServantsAddictionMenu();
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
		printServantsAddictionMenu();
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
		printServantsAddictionMenu();
	}

	// TODO test con carte leader caricate
	@Override
	public void printSellLeaderCardMenu() throws RemoteException {
		setMove("LeaderSell");
		showMsg("Choose the Leader card to sell:");
		int i, j;
		for (i = 0; i < getPlayer(name, game).getLeaderCards().size(); i++) {
			showMsg((i + 1) + ": " + getPlayer(name, game).getLeaderCards().get(i).getName());
		}
		// TODO le carte leader attivate si possono vendere?
		for (j = 0; j < getPlayer(name, game).getActivatedLeaderCards().size(); j++) {
			showMsg((j + 1 + i) + ": " + getPlayer(name, game).getActivatedLeaderCards().get(j).getName());
		}
		int option = in.nextInt();
		if (option <= i)
			setMove(getPlayer(name, game).getLeaderCards().get(option - 1).getName());
		else if (option > i && option < (j + i))
			setMove(getPlayer(name, game).getActivatedLeaderCards().get(option - 1).getName());
		else {
			printInvalidInput();
			printSellLeaderCardMenu();
		}
	}

	// TODO test con carte leader caricate
	@Override
	public void printActivateLeaderCardMenu() throws RemoteException {
		setMove("LeaderAct");
		showMsg("Choose the Leader card to activate:");
		for (int i = 0; i < getPlayer(name, game).getLeaderCards().size(); i++) {
			showMsg((i + 1) + ": " + getPlayer(name, game).getLeaderCards().get(i).getName());
		}
		int option = in.nextInt();
		if (option <= getPlayer(name, game).getLeaderCards().size())
			setMove(getPlayer(name, game).getLeaderCards().get(option).getName());
		else {
			printInvalidInput();
			printActivateLeaderCardMenu();
		}
		// TODO controlli su carte speciali per richiedere servitori(Integer) e
		// colore(String)
	}

	public void printInvalidInput() {
		showMsg("Invalid input");
	}

	@Override
	public void showLoginMenu() {
		// TODO Password & name
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
	@Override
	public void showBoard(Game game) throws RemoteException {
		this.game = game;
		showMsg("_________________________________");
		showMsg("| Period: " + game.getPeriod() + " \t\t\t|");
		showMsg("| Round: " + game.getRound() + "\t\t\t|");
		showMsg("| \t\t\t\t|");
		showMsg("| Dices:\t\t\t|");
		showMsg("| Orange: " + game.getBoardgame().getDice("Orange") + "\t\t\t|");
		showMsg("| Black: " + game.getBoardgame().getDice("Black") + " \t\t\t|");
		showMsg("| White: " + game.getBoardgame().getDice("White") + " \t\t\t|");
		showMsg("|_______________________________|");
		showMsg("| Family members:\t\t|");
		showMsg("| Orange: " + getPlayer(name, game).getMembers().get(0).getValue() + "\t\t\t|");
		showMsg("| Black: " + getPlayer(name, game).getMembers().get(1).getValue() + "\t\t\t|");
		showMsg("| White: " + getPlayer(name, game).getMembers().get(2).getValue() + "\t\t\t|");
		showMsg("| Uncolored: " + getPlayer(name, game).getMembers().get(3).getValue() + "\t\t\t|");
		showMsg("| \t\t\t\t|");
		showMsg("| Name: " + getPlayer(name, game).getNickname() + "\t\t\t|");
		showMsg("| Color: " + getPlayer(name, game).getColor() + " \t\t\t|");
		showMsg("| Coins: " + getPlayer(name, game).getPersonalBoard().getResources().getCoins() + "\t\t\t|");
		showMsg("| Wood: " + getPlayer(name, game).getPersonalBoard().getResources().getWood() + " \t\t\t|");
		showMsg("| Stone: " + getPlayer(name, game).getPersonalBoard().getResources().getStone() + " \t\t\t|");
		showMsg("| Servants: " + getPlayer(name, game).getPersonalBoard().getResources().getServants() + "\t\t\t|");
		showMsg("| Faith Points: " + getPlayer(name, game).getPersonalBoard().getResources().getFaith() + " \t\t|");
		showMsg("| Military Points: " + getPlayer(name, game).getPersonalBoard().getResources().getMilitary()
				+ "\t\t|");
		showMsg("| Victory Points: " + getPlayer(name, game).getPersonalBoard().getResources().getVictory() + "\t\t|");
		showMsg("|_______________________________|");
		// palazzo del consiglio
		showMsg("| Council Palace members: \t|");
		int i = 1;
		for (FamilyMember fm : game.getBoardgame().getCouncilPalace().getMembers()) {

			showMsg("| " + i + ":" + fm.getPlayer().getNickname() + "\t\t\t|");
			i++;
		}
		showMsg("|_______________________________|");
		// Towers
		for (int j = 0; j < 4; j++) {
			System.out.print("| ");
			// TODO rewards
			for (Tower t : game.getBoardgame().getTowers())
				System.out.print(t.getFloor()[j].getCard().getName() + " | ");
			showMsg("");
		}
		showMsg("|_______________________________|");
		// leaderCard
		showMsg("| Leader cards:\t\t\t|");
		for (LeaderCard ld : getPlayer(name, game).getLeaderCards())
			showMsg("| " + ld.getName() + "\t\t|");
		showMsg("|_______________________________|");
		showMsg("| Faith points track:\t\t|");
		// trovo il massimo victory
		int maxF = 0;
		for (Player p : game.getPlayers())
			if (p.getPersonalBoard().getResources().getFaith() > maxF)
				maxF = p.getPersonalBoard().getResources().getFaith();
		// stampo lista player
		for (int j = maxF; j >= 0; j--)
			for (Player p : game.getPlayers())
				if (p.getPersonalBoard().getResources().getFaith() == j)
					showMsg("| " + j + " " + p.getNickname() + "\t\t|");
		showMsg("|_______________________________|");
		showMsg("| Military points track:\t|");
		// trovo il massimo victory
		int maxM = 0;
		for (Player p : game.getPlayers())
			if (p.getPersonalBoard().getResources().getMilitary() > maxM)
				maxM = p.getPersonalBoard().getResources().getMilitary();
		// stampo lista player
		for (int j = maxM; j >= 0; j--)
			for (Player p : game.getPlayers())
				if (p.getPersonalBoard().getResources().getMilitary() == j)
					showMsg("| " + j + " " + p.getNickname() + "\t\t|");
		showMsg("|_______________________________|");
		showMsg("| Victory points track:\t\t|");
		// trovo il massimo victory
		int maxV = 0;
		for (Player p : game.getPlayers())
			if (p.getPersonalBoard().getResources().getVictory() > maxV)
				maxV = p.getPersonalBoard().getResources().getVictory();
		// stampo lista player
		for (int j = maxV; j >= 0; j--)
			for (Player p : game.getPlayers())
				if (p.getPersonalBoard().getResources().getVictory() == j)
					showMsg("| " + j + " " + p.getNickname() + "\t\t|");
		showMsg("|_______________________________|");

		/*
		 * personal board altri giocatori?
		 */
	}

	@Override
	public String councilRequest(Integer number) {
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
}
