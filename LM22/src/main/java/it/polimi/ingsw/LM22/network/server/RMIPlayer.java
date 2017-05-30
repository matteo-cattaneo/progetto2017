package it.polimi.ingsw.LM22.network.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.ingsw.LM22.network.client.IClient;

public class RMIPlayer extends UnicastRemoteObject implements IPlayer {

	protected RMIPlayer() throws RemoteException {

	}

	@Override
	public String yourTurn() throws RemoteException {
		return null;
	}

	@Override
	public void showBoard(String move) throws RemoteException {
		
	}

	public void login(IClient client) throws RemoteException {
		
	}

	public IClient getClient() throws RemoteException {
		return null;
	}
}