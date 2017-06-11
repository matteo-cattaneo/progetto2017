package it.polimi.ingsw.LM22.controller;

import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;

public class VaticanReportManager {

	private final Integer[] goal = { 3, 4, 5 };
	private final ResourceHandler resourceHandler = new ResourceHandler();

	/*
	 * metodo che controlla se un giocatore ha raggiunto i requisiti del periodo
	 * corrente per la Chiesa
	 */
	public boolean canGiveSupport(Player p, Integer period) {
		if (p.getPersonalBoard().getResources().getFaith() >= goal[period - 1]) {
			return true;
		}
		return false;
	}

	/*
	 * metodo che conferisce al giocatore la scomunica e viene invocato: -
	 * automaticamente se il giocatore non ha i requisiti
	 * (giveSupport()-->false) - su richiesta del giocatore se askPlayer mi dice
	 * che il giocatore decide autonomamente di non dare il sostegno alla Chiesa
	 */
	public void exCommunicate(Player player) {
		// TODO
	}

	/*
	 * metodo invocato se giveSupport() restituisce true e chiede al giocatore
	 * se desidera dare o no il sostegno alla Chiesa --> se si allora invochiamo
	 * il metodo che toglie i punti fede del giocatore e gli dà i corrispettivi
	 * punti vittoria
	 * + deve controllare se ha Sisto VI attivato
	 */
	public void askVatican(Player player) {
		// TODO sposterei il metodo nel mainGameController
	}

	/*
	 * metodo che si occupa dettagliatamente del recupero della scomunica e
	 * della copia nei vari giocatori scomunicati
	 */
	public void giveExTile() {
		// TODO
	}

	public void giveVictoryPointsDueToChurch(Game game, Player p) {
		Resource bonus = game.getBoardgame().getFaithGrid().getReward(p.getPersonalBoard().getResources().getFaith());
		Resource faith = new Resource(0, 0, 0, 0, p.getPersonalBoard().getResources().getFaith(), 0, 0);
		resourceHandler.subResource(p.getPersonalBoard().getResources(), faith);
		resourceHandler.addResource(p.getPersonalBoard().getResources(), bonus);
	}

	/*
	 * metodo che gestisce tutta la fase di VaticanReport controllando volta per
	 * volta tutti i giocatori
	 */
	public void manageVaticanReport(Game game) {
		for (Player p : game.getPlayersOrder()) {
			if (canGiveSupport(p, game.getPeriod())) {
				askVatican(p);
			} else
				exCommunicate(p);
		}
	}

}
