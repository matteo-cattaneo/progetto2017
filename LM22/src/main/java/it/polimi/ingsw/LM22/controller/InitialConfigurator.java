package it.polimi.ingsw.LM22.controller;

public class InitialConfigurator extends TurnInizializator{

	/*
	 * costruttore che chiamerà uno dopo l'altro tutti i metodi privati
	 * che sono dichiarati successivamente all'interno di questa classe
	 */
	public InitialConfigurator(){

	}
	
	/*
	 * metodo che consente di istanziare tutto il model con i relativi caricamenti
	 * da file, in modo da ottenere tutti gli oggetti utili alla partita
	 * --> tendenzialmente una delle prime cose da fare alla creazione
	 * di una nuova partita
	 */
	private void gameCreation(){
		
	}
	
	/*
	 * metodo che costruisce il Model con i parametri principali per poter iniziare la partita
	 * */
	private void loadConfiguration(){
		
	}
	
	/*
	 * metodo che implementa la fase di scelta delle carte leader con relativo passaggio 
	 * al giocatore successivo delle carte rimanenti
	 * */
	private void leaderDistribution(){
		
	}
	
	/*
	 * consente di far scegliere ad ogni giocatore la carta leader e inserirla nella sua PersonalBoard 
	 */
	private void leaderSelection(){
		
	}
	
	
	/*
	 * metodo invocato direttamente nel costruttore di questa classe che permette di distribuire 
	 * le risorse con cui i player iniziano la partita in base all'ordine random generato 
	 * nella creazione della partita stessa
	 * */
	private void giveInitialResources(){
		/*
		 * potrebbe avere già le risorse memorizzate al suo interno 
		 * Integer FIVE_PLAYERS = 5;
		 * Resource[] initialResources = new Resource(FIVE_PLAYERS)
		 * --> modificare il costruttore in Resource in modo tale da avere anche un costruttore
		 * personalizzato nella classe permettendo di istanziare le risorse standard iniziali 
		 * direttamente in questa classe
		 */
	}
}
