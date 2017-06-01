package it.polimi.ingsw.LM22.network.client;

import java.io.FilterInputStream;
import java.rmi.RemoteException;
import java.util.Scanner;

public class StartClient {
	private AbstractUI UI;
	private IClient client;

	public static void main(String[] args) {
		StartClient start = new StartClient();
		// avvio il setup del client
		start.setup();
	}

	public void setup() {
		// stampo la selezione dell'intefaccia
		printUISelection();
		/*
		 * secondo il risulltato ottenuto prima richiedo il tipo di connessione
		 * e inizializzo con il giusto tipo dinamico
		 */
		switch (UI.showConnectionSelection()) {
		case 1:
			try {
				client = new RMIClient(UI);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			client = new SocketClient(UI);
			break;
		}
		/*
		 * con i metodi relativi alla prpria connessione e UI effettuo la
		 * connessione
		 */

		try {
			client.connect(UI.getName(), UI.getIP());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	/*
	 * permette la scelta dell' interfaccia da utilizzare durante la partita e
	 * inizializza con il giusto tipo dinamico
	 */

	public void printUISelection() {
		Scanner stdin = new Scanner(new FilterInputStream(System.in) {
			public void close() {
			}
		});
		int option;
		System.out.println("Choose you UI type:");
		System.out.println("1: GraphicalUserInterface");
		System.out.println("2: CommandLineInterface");
		option = Integer.parseInt(stdin.nextLine());
		switch (option) {
		case 1:
			this.UI = new GUIinterface();
			break;
		case 2:
			this.UI = new CLIinterface();
			break;
		default:
			System.out.println("Invalid input");
			printUISelection();
			break;
		}
		stdin.close();
	}

}
