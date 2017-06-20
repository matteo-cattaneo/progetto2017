package it.polimi.ingsw.LM22.network.client;

import java.rmi.RemoteException;

/*
 * Interfaccia estesa da RMIClient e SOCKETclient
 * interfaccia dedicata esclusivamente alla gestione della comunicazione lato clinet
 */
public interface IConnection {

	public void connect(String name, String ip) throws RemoteException;

	public void play() throws RemoteException;

}
