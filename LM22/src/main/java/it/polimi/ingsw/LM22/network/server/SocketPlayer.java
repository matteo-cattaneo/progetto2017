package it.polimi.ingsw.LM22.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.network.client.IClient;

public class SocketPlayer implements IPlayer {
	ObjectOutputStream out;
	ObjectInputStream in;

	public SocketPlayer(Socket socket) throws IOException {
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
	}

	@Override
	public String yourTurn() throws ClassNotFoundException, IOException {
		out.writeChars("start");
		return in.readObject().toString();
	}

	@Override
	public void showBoard(Game game) throws IOException {
		out.writeObject(game);
	}

	@Override
	public void login(IClient client) {
		// TODO Auto-generated method stub
	}

}
