package it.polimi.ingsw.LM22.network.server;



import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import it.polimi.ingsw.LM22.network.client.IClient;


public class SocketPlayer implements IPlayer {
	PrintWriter out;
	Scanner in;

	public SocketPlayer(Socket socket) throws IOException  {
		out = new PrintWriter(socket.getOutputStream());
		in = new Scanner(socket.getInputStream());
	}

	@Override
	public String yourTurn() {
		return null;	
	}

	@Override
	public void showBoard(String msg) {
		
	}

	@Override
	public void login(IClient client) {
		// TODO Auto-generated method stub
	}

	
}
