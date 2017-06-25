package it.polimi.ingsw.LM22.controller;

import java.io.IOException;

import it.polimi.ingsw.LM22.model.Effect;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.leader.ChurchSubstainEffect;

public class VaticanReportManager {
	private final Integer MAX_FAITH_GRID = 15;
	/**
	 * risorse richieste sono da caricare da file oppure no?
	 */
	private final Resource[] goal = { new Resource(0, 0, 0, 0, 3, 0, 0), new Resource(0, 0, 0, 0, 4, 0, 0),
			new Resource(0, 0, 0, 0, 5, 0, 0) };
	private final ResourceHandler resourceHandler = new ResourceHandler();
	private Game game;
	private MainGameController mainGame;

	/**
	 * metodo che controlla se un giocatore ha raggiunto i requisiti del periodo
	 * corrente per la Chiesa
	 */
	public boolean canGiveSupport(Player p, Integer period) {
		if (resourceHandler.enoughResources(p.getPersonalBoard().getResources(), goal[period - 1])) {
			return true;
		}
		return false;
	}

	/**
	 * metodo che conferisce al giocatore la scomunica e viene invocato: -
	 * automaticamente se il giocatore non ha i requisiti
	 * (giveSupport()-->false) - su richiesta del giocatore se askPlayer mi dice
	 * che il giocatore decide autonomamente di non dare il sostegno alla Chiesa
	 */
	public void exCommunicate(Player player, Integer period) {
		player.getEffects().add(game.getBoardgame().getFaithGrid().getExCommunication(period).getEffect());
	}

	/**
	 * metodo che distribuisce i reward derivati dall'aver supportato la chiesa
	 * o per essere scomunicati
	 */
	public void giveResourceDueToChurchSubstain(Player p) {
		Resource bonus;
		if (p.getPersonalBoard().getResources().getFaith() < MAX_FAITH_GRID)
			bonus = game.getBoardgame().getFaithGrid().getReward(p.getPersonalBoard().getResources().getFaith());
		else
			bonus = game.getBoardgame().getFaithGrid().getReward(MAX_FAITH_GRID);
		Resource faith = new Resource(0, 0, 0, 0, p.getPersonalBoard().getResources().getFaith(), 0, 0);
		resourceHandler.subResource(p.getPersonalBoard().getResources(), faith);
		resourceHandler.addResource(p.getPersonalBoard().getResources(), bonus);
	}

	/**
	 * metodo che gestisce tutta la fase di VaticanReport controllando volta per
	 * volta tutti i giocatori
	 */
	public void manageVaticanReport(Game game, MainGameController mainGame) throws IOException {
		this.game = game;
		this.mainGame = mainGame;
		Integer period = game.getPeriod();
		for (Player p : game.getPlayersOrder()) {
			if (canGiveSupport(p, period)) {
				// poichè può decidere se dare sostegno o no viene chiesto
				// e in base alla risposta si procede diversamente
				if (!mainGame.askSupport(p)) {
					exCommunicate(p, period);
				} else {
					giveResourceDueToChurchSubstain(p);
					for (Effect e : p.getEffects()) {
						if (e instanceof ChurchSubstainEffect)
							resourceHandler.addResource(p.getPersonalBoard().getResources(),
									((ChurchSubstainEffect) e).getReward());
					}
				}
			} else {
				exCommunicate(p, period);
				giveResourceDueToChurchSubstain(p);
			}
		}
	}

}
