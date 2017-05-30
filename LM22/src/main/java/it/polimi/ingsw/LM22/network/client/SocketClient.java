package it.polimi.ingsw.LM22.network.client;

import java.net.Socket;
import java.rmi.RemoteException;

public class SocketClient implements IClient {
	private final int SOCKET_PORT = 1337;
	private Socket socket;
	private AbstractUI UI;
	private String name;

	public SocketClient(AbstractUI UI) {
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
