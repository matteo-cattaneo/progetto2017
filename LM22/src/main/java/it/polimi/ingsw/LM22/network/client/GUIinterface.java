package it.polimi.ingsw.LM22.network.client;

import java.rmi.RemoteException;
import java.util.List;

import it.polimi.ingsw.LM22.model.DoubleChangeEffect;
import it.polimi.ingsw.LM22.model.Game;
/*
 * Classe che contiene tutti i metodi che permettono all'utente 
di interfacciarsi con la GUI 
*/
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.VentureCard;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;

//WIP
public class GUIinterface extends AbstractUI {

	@Override
	public void printMoveMenu() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void printMemberMoveMenu() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void printCardMoveMenu() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void printFamilyMemberMenu() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public String printServantsAddictionMenu() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String printTowersMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String printLevelsMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printMarketMoveMenu() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void printWorkMoveMenu() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void printWorkSelectionMenu() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printSellLeaderCardMenu() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void printActivateLeaderCardMenu() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public int showConnectionSelection() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIP() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void connectionOK() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getMove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showMsg(String s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showBoard(Game game) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void printMarketSelectionMenu() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void printInvalidInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public String councilRequest(Integer number) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printCouncilMoveMenu() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean printSupportMenu() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String printColorMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer printVentureCostMenu(VentureCard vc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean printChangeMenu(Resource[] exchange) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Integer printDoubleChangeMenu(DoubleChangeEffect effect) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String askToPlayerForEffectToCopy(List<LeaderCard> lcards) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer selectPersonalTile(Game game) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
