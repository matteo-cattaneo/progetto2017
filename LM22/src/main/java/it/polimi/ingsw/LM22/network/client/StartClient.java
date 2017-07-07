package it.polimi.ingsw.LM22.network.client;

import java.io.FilterInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * classe principale del client, chiede all'utente le informazioni necessarie
 * alla connessione e all'inizio della partita
 */
public class StartClient {
	public static final Logger logger = Logger.getLogger("Client");

	private StartClient() {
		// costruttore vuoto privato
	}

	public static void main(String[] args) {
		// configuro il logger per stampare su file e non sulla console
		try {
			Handler fh = new FileHandler("./LIMclient.log", true);
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);
			logger.setUseParentHandlers(false);
		} catch (SecurityException | IOException e) {
			logger.log(Level.SEVERE, "Logger initialization failed!", e);
		}

		// avvio il setup del client
		setup();
	}

	public static void setup() {
		// visualizzo splash screen caricandola da file
		try {
			System.out.println(
					new String(Files.readAllBytes(Paths.get(".//JSON//SplashScreen.txt")), StandardCharsets.UTF_8));
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Splash screen not found!", e);
			System.out.println("Splash screen not found!");
		}
		// stampo la selezione dell'intefaccia
		AbstractUI ui = printUISelection();
		// richiedo tipo connessione
		IConnection client = selectConnectionType(ui);
		/**
		 * con i metodi relativi alla prpria connessione e UI, effettuo la connessione
		 */

		try {
			client.connect(ui.getName(), ui.getPassword(), ui.getIP());
		} catch (RemoteException e) {
			logger.log(Level.SEVERE, "Connection server error!", e);
			System.out.println("Connection server error!");
		}
	}

	/**
	 * permette la scelta dell' interfaccia da utilizzare durante la partita e
	 * inizializza con il giusto tipo dinamico
	 */

	private static AbstractUI printUISelection() {
		AbstractUI ui;
		Scanner stdin = new Scanner(new FilterInputStream(System.in) {
			@Override
			public void close() {
				// chiudo lo stream ma non System.in
			}
		});
		// richiedo di selezionare la UI
		System.out.println("Choose you UI type:");
		System.out.println("1: GUI");
		System.out.println("2: CLI");
		int option = -1;
		while (option == -1)
			try {
				option = Integer.parseInt(stdin.nextLine());
			} catch (NumberFormatException e) {
				logger.log(Level.SEVERE, "Invalid input", e);
				System.out.println("Invalid input");
				option = -1;
			}
		switch (option) {
		case 1:
			System.err.println("GUI - WIP");
			System.err.println("CLI interface automatically selected!");
			ui = new CLIinterface();
			break;
		case 2:
			ui = new CLIinterface();
			break;
		default:
			System.out.println("Invalid input");
			ui = printUISelection();
			break;
		}
		stdin.close();
		return ui;
	}

	private static IConnection selectConnectionType(AbstractUI ui) {
		IConnection client;
		/**
		 * secondo il risulltato ottenuto prima richiedo il tipo di connessione e
		 * inizializzo con il giusto tipo dinamico
		 */
		switch (ui.showConnectionSelection()) {
		case 1:
			try {
				client = new RMIClient(ui);
			} catch (RemoteException e) {
				logger.log(Level.SEVERE, "Error RMI!", e);
				ui.printInvalidInput();
				client = selectConnectionType(ui);
			}
			break;
		case 2:
			client = new SocketClient(ui);
			break;
		default:
			ui.printInvalidInput();
			client = selectConnectionType(ui);
		}
		return client;
	}

}
