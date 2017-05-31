package it.polimi.ingsw.LM22.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import it.polimi.ingsw.LM22.model.Game;

public class SocketClient implements IClient {
	private final int SOCKET_PORT = 1337;
	private Socket socket;
	private AbstractUI UI;
	private String name;

	public SocketClient(AbstractUI UI) {
		this.UI = UI;
	}

	@SuppressWarnings("resource")
	public void connect(String name, String ip) {
		this.name = name;
		try {
			socket = new Socket(ip, SOCKET_PORT);
		} catch (IOException e) {
			UI.showMsg("Socket connection error!");
		}
		UI.connectionOK();
		try {
			ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
			while (true) {
				String socketLine = socketIn.readLine();
				if (socketLine.equals("start")) {
					UI.showBoard((Game) socketIn.readObject());
					UI.printMoveMenu();
					socketOut.writeObject(UI.getMove());
				} else
					UI.showMsg(socketLine);
			}
		} catch (ClassNotFoundException | IOException e) {
			UI.showMsg("Connessione chiusa");
		}

	}

	public void play() {

	}

	public void print(String move) {
		// TODO Auto-generated method stub

	}

	public String getMove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showBoard(Game game) {
		// TODO Auto-generated method stub
		
	}
}
