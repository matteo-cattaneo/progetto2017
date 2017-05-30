package it.polimi.ingsw.LM22.network.client;

import java.io.FilterInputStream;
import java.rmi.RemoteException;
import java.util.Scanner;

public class StartClient {
	private AbstractUI UI;
	private IClient client;

	public static void main(String[] args) {
		StartClient start = new StartClient();
		start.setup();
	}

	public void setup() {
		printUISelection();
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
		try {
			client.connect(UI.getName(), UI.getIP());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

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
