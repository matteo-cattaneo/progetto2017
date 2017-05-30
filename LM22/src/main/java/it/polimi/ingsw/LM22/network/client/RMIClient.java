package it.polimi.ingsw.LM22.network.client;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;


public class RMIClient extends UnicastRemoteObject implements IClient {
	private String move;
	private String name;
	private AbstractUI UI;

	public RMIClient(AbstractUI UI) throws RemoteException {
		this.UI = UI;
	}

	@Override
	public void connect(String name, String ip) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void play() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void print(String move) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMove() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
}