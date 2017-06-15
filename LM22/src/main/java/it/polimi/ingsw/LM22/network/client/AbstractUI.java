package it.polimi.ingsw.LM22.network.client;

import java.rmi.RemoteException;

import it.polimi.ingsw.LM22.model.DoubleChangeEffect;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.VentureCard;

//Classe astratta estesa da CLI e GUI interface
public abstract class AbstractUI {

	public abstract void printMoveMenu() throws RemoteException;

	public abstract void printMemberMoveMenu() throws RemoteException;

	public abstract void printCardMoveMenu() throws RemoteException;

	public abstract void printFamilyMemberMenu() throws RemoteException;

	public abstract String printServantsAddictionMenu() throws RemoteException;

	public abstract String printTowersMenu() throws RemoteException;

	public abstract String printLevelsMenu() throws RemoteException;

	public abstract void printMarketMoveMenu() throws RemoteException;

	public abstract void printWorkMoveMenu() throws RemoteException;

	public abstract void printWorkSelectionMenu() throws RemoteException;

	public abstract void printSellLeaderCardMenu() throws RemoteException;

	public abstract void printActivateLeaderCardMenu() throws RemoteException;

	public abstract int showConnectionSelection();

	public abstract String getName() throws RemoteException;

	public abstract String getIP();

	public abstract void connectionOK();

	public abstract String getMove() throws RemoteException;

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

	public abstract String councilRequest(Integer number) throws RemoteException;

	public abstract boolean printSupportMenu() throws RemoteException;

	public abstract String printColorMenu() throws RemoteException;

	public abstract Integer printVentureCostMenu(VentureCard vc) throws RemoteException;

	public abstract boolean printChangeMenu(Resource[] exchange) throws RemoteException;

	public abstract Integer printDoubleChangeMenu(DoubleChangeEffect effect) throws RemoteException;

	public abstract void printCouncilMoveMenu() throws RemoteException;

}
