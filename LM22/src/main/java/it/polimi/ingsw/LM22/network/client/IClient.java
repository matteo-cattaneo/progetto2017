package it.polimi.ingsw.LM22.network.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.LM22.model.Game;

/*
 * Interfaccia estesa da RMI e SOCKET client
 * necessaria alla gestione lato cliente delle connessioni
 */
public interface IClient extends Remote {

	public void connect(String name, String ip) throws RemoteException;

	public void play() throws RemoteException;

	public String getMove() throws RemoteException;

	public void showBoard(Game game) throws RemoteException;

	public String getName() throws RemoteException;

	public String councilRequest(Integer number) throws RemoteException;

	public String servantsRequest() throws RemoteException;

	public String towerRequest() throws RemoteException;

	public String floorRequest() throws RemoteException;
	
	public void showMsg(String msg) throws RemoteException;
}
