package it.polimi.ingsw.LM22.network.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.network.client.IClient;
/*
 * classe utilizzata da remoto per le azioni sul client RMI
 */

public class RMIPlayer extends UnicastRemoteObject implements IPlayer {

	private static final long serialVersionUID = -2036349694420489903L;
	IClient client = null;
	String name;

	public RMIPlayer() throws RemoteException {

	}

	/*
	 * indica al client quando è il suo turno e restituisce la mossa da lui
	 * effettuata
	 */

	@Override
	public String yourTurn() throws RemoteException {
		client.play();
		return client.getMove();
	}

	// manda al client il model per poter visualizzare la board
	@Override
	public void showBoard(Game game) throws RemoteException {
		client.showBoard(game);
	}

	// riceve l'oggetto remoto del client
	public void login(IClient client) throws RemoteException {
		this.client = client;
		this.name = client.getName();
		// TODO verificare se ha gia iniziato un altra partita
	}

	// restituisce l'oggetto remoto del client
	public IClient getClient() throws RemoteException {
		return client;
	}

	public String getName() throws RemoteException {
		return name;
	}

	@Override
	public String councilRequest(Integer number) throws RemoteException {
		return client.councilRequest(number);
	}
}