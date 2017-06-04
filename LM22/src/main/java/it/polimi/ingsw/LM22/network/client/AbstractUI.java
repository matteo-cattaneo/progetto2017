package it.polimi.ingsw.LM22.network.client;

import java.rmi.RemoteException;

import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Player;

//Classe astratta estesa da CLI e GUI interface
public abstract class AbstractUI {

	public abstract void printMoveMenu() throws RemoteException;

	public abstract void printMemberMoveMenu() throws RemoteException;

	public abstract void printCardMoveMenu() throws RemoteException;

	public abstract void printFamilyMemberMenu();

	public abstract void printServantsAddictionMenu() throws RemoteException;

	public abstract void printTowersMenu();

	public abstract void printLevelsMenu();

	public abstract void printMarketMoveMenu() throws RemoteException;

	public abstract void printWorkMoveMenu() throws RemoteException;

	public abstract void printWorkSelectionMenu();

	public abstract void printSellLeaderCardMenu() throws RemoteException;

	public abstract void printActivateLeaderCardMenu() throws RemoteException;

	public abstract void showLoginMenu();

	public abstract int showConnectionSelection();

	public abstract String getName();

	public abstract String getIP();

	public abstract void connectionOK();

	public abstract String getMove();

	public abstract void showMsg(String s);

	public abstract void showBoard(Game game) throws RemoteException;

	protected Player getPlayer(String name, Game game) throws RemoteException {
		for (Player p : game.getPlayers()) {
			if (p.getNickname().equals(name))
				return p;
		}
		return null;
	}

	public abstract void printMarketSelectionMenu() throws RemoteException;

	public abstract void printInvalidInput();
}
