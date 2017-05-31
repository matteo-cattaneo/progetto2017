package it.polimi.ingsw.LM22.network.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.network.client.IClient;

public class RMIPlayer extends UnicastRemoteObject implements IPlayer {
	private static final long serialVersionUID = 1L;
	IClient client = null;

	protected RMIPlayer() throws RemoteException {

	}

	@Override
	public String yourTurn() throws RemoteException {
		client.play();
		return client.getMove();
	}

	@Override
	public void showBoard(Game game) throws RemoteException {
//		client.print("_________________________");
//		client.print("| TABELLONE \t \t |");
//		client.print("| \t \t \t |");
//		client.print("|________________________|");
//		client.print("Ultima mossa: " + move);
	}

	public void login(IClient client) throws RemoteException {
		this.client = client;
	}

	public IClient getClient() throws RemoteException {
		return client; // server ottiene il client
	}
}