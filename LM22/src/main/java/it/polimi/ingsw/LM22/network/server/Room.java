package it.polimi.ingsw.LM22.network.server;

import java.io.IOException;
import java.rmi.RemoteException;

public class Room implements Runnable {
	IPlayer player[];
	int[] ordine;
	int n;

	public Room(IPlayer player[], int[] ordine, int n) throws IOException {
		this.n = n;
		this.player = player;
		this.ordine = ordine;
	}

	@Override
	public void run() {
		int i = 0;
		try {
			player[ordine[i]].showBoard("#Primo turno#");
			while (true) {
				sendAll(player[ordine[i]].yourTurn());

				if (ordine[i + 1] == 4) {
					i = 0;
				} else {
					i++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Partita terminata!");
		}
	}

	public void sendAll(String msg) throws RemoteException {
		for (int j = 0; j < n; j++) {
			player[j].showBoard(msg);
		}
	}
}
