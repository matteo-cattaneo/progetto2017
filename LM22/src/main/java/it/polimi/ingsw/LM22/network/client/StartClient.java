package it.polimi.ingsw.LM22.network.client;

import java.io.FilterInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.Scanner;

public class StartClient {

	public static void main(String[] args) {
		StartClient start = new StartClient();
		// avvio il setup del client
		start.setup();
	}

	public void setup() {
		// visualizzo splash screen caricandola da file
		try {
			System.out.println(
					new String(Files.readAllBytes(Paths.get(".\\JSON\\SplashScreen.txt")), StandardCharsets.UTF_8));
		} catch (IOException e) {
		}
		// stampo la selezione dell'intefaccia
		AbstractUI UI = printUISelection();
		// richiedo tipo connessione
		IClient client = selectConnectionType(UI);
		/*
		 * con i metodi relativi alla prpria connessione e UI, effettuo la
		 * connessione
		 */

		try {
			client.connect(UI.getName(), UI.getIP());
		} catch (RemoteException e) {
			UI.showMsg("Connection server error!");
		}
	}
	/*
	 * permette la scelta dell' interfaccia da utilizzare durante la partita e
	 * inizializza con il giusto tipo dinamico
	 */

	private AbstractUI printUISelection() {
		AbstractUI UI = null;
		Scanner stdin = new Scanner(new FilterInputStream(System.in) {
			public void close() {
			}
		});
		int option;
		// richiedo di selezionare la UI
		System.out.println("Choose you UI type:");
		System.out.println("1: GUI");
		System.out.println("2: CLI");
		option = Integer.parseInt(stdin.nextLine());
		switch (option) {
		case 1:
			System.err.println("GUI - WIP");
			UI = new GUIinterface();
			break;
		case 2:
			UI = new CLIinterface();
			break;
		default:
			System.out.println("Invalid input");
			UI = printUISelection();
			break;
		}
		stdin.close();
		return UI;
	}

	private IClient selectConnectionType(AbstractUI UI) {
		IClient client = null;
		/*
		 * secondo il risulltato ottenuto prima richiedo il tipo di connessione
		 * e inizializzo con il giusto tipo dinamico
		 */
		switch (UI.showConnectionSelection()) {
		case 1:
			try {
				client = new RMIClient(UI);
			} catch (RemoteException e) {
				UI.showMsg("Error RMI!");
			}
			break;
		case 2:
			client = new SocketClient(UI);
			break;
		default:
			UI.printInvalidInput();
			client = selectConnectionType(UI);
		}
		return client;
	}

}
