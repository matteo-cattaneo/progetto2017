package it.polimi.ingsw.LM22.network.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

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
			Scanner socketIn = new Scanner(socket.getInputStream());
			PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
			while (true) {
				String socketLine = socketIn.nextLine();
				if (socketLine.equals("start")) {
					UI.printMoveMenu();
					socketOut.println(UI.getMove());
				} else
					UI.showMsg(socketLine);
			}
		} catch (NoSuchElementException | IOException e) {
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

}
