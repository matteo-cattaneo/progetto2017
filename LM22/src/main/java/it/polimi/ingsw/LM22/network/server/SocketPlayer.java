package it.polimi.ingsw.LM22.network.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import it.polimi.ingsw.LM22.network.client.IClient;

public class SocketPlayer implements IPlayer {
	PrintWriter out;
	Scanner in;

	public SocketPlayer(Socket socket) throws IOException {
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new Scanner(socket.getInputStream());
	}

	@Override
	public String yourTurn() {
		out.println("start");
		return in.nextLine();
	}

	@Override
	public void showBoard(String msg) {
		out.println("_________________________");
		out.println("| TABELLONE \t \t |");
		out.println("| \t \t \t |");
		out.println("|________________________|");
		out.println("Ultima mossa: " + msg);
	}

	@Override
	public void login(IClient client) {
		// TODO Auto-generated method stub

	}

}
