package it.unibo.ai.didattica.competition.tablut.AI.Clients;

import it.unibo.ai.didattica.competition.tablut.AI.Clients.Utils.MetricsPartita_Genetic;
import it.unibo.ai.didattica.competition.tablut.AI.Minmax;
import it.unibo.ai.didattica.competition.tablut.client.TablutClient;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.Game;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.time.LocalTime;

/**
 * 
 * @author
 *
 */
public class TablutFrittoMistoClient extends TablutClient {

	private static final int NUMERO_INIZIALE_NERI = 16 ;
	private static final int NUMERO_INIZIALE_BIANCHI = 8; // + KING


	private static MetricsPartita_Genetic metrics;
	private final int timeOut = 58;
	private final int currDepthLimit = 2;
	private Game game;
	private static final String NAME = "FrittoMisto";

	//FOR GENETIC
	private long matchTime;

//	TODO: Presi dal random. Eventualmente da sistemare
//	public TablutFrittoMistoClient(String player, String name, int gameChosen, int timeout, String ipAddress) throws UnknownHostException, IOException {
//		super(player, name, timeout, ipAddress);
//		game = gameChosen;
//	}
//
//	public TablutFrittoMistoClient(String player, String name, int timeout, String ipAddress) throws UnknownHostException, IOException {
//		this(player, name, 4, timeout, ipAddress);
//	}
//
//	public TablutFrittoMistoClient(String player, int timeout, String ipAddress) throws UnknownHostException, IOException {
//		this(player, "random", 4, timeout, ipAddress);
//	}
//
//	public TablutFrittoMistoClient(String player) throws UnknownHostException, IOException {
//		this(player, "random", 4, 60, "localhost");
//	}


	public TablutFrittoMistoClient(String player) throws IOException {
		super(player, NAME);
		this.game = new GameAshtonTablut(99, 0, "garbage", "fake", "fake");
	}

	public static MetricsPartita_Genetic main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {

		if (args.length == 0) {
			System.out.println("You must specify which player you are (WHITE or BLACK)!");
			System.exit(-1);
		}
		System.out.println("Selected this: " + args[0]);

		TablutClient client = new TablutFrittoMistoClient(args[0]);

		client.run();

		//FOR GENETICS
		return metrics;

	}

	@Override
	public void run() {

		Long startTimeMills = System.currentTimeMillis();

		System.out.println("You are player " + this.getPlayer().toString() + "!");
		Action action;
		Minmax minmax = new Minmax(game, currDepthLimit, getPlayer(), true);

		try {
			this.declareName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (this.getPlayer() == Turn.WHITE) {
			System.out.println("You are player " + this.getPlayer().toString() + "!");
			while (true) {
				try {
					this.read();

					System.out.println("Current state:");
					System.out.println(this.getCurrentState().toString());
					if (this.getCurrentState().getTurn().equals(Turn.WHITE)) {


						Long start = System.currentTimeMillis();

						/*****AGGIUNTE*****/
						action = minmax.makeDecision(timeOut, getCurrentState());

						Long end = System.currentTimeMillis();

						System.out.println("tempo impegato: __" + (end - start) + "__ millisecondi");

						this.write(action);
					} else if (this.getCurrentState().getTurn().equals(Turn.BLACK)) {
						System.out.println("Waiting for your opponent move... ");
					} else if (this.getCurrentState().getTurn().equals(Turn.WHITEWIN)) {


						System.out.println("YOU WIN!");
						//added
						printMatchTime(startTimeMills);
						aggiornaMetrics(getCurrentState());
						
						/*System.exit(0)*/ return;
					} else if (this.getCurrentState().getTurn().equals(Turn.BLACKWIN)) {

						System.out.println("YOU LOSE!");
						//added
						printMatchTime(startTimeMills);
						aggiornaMetrics(getCurrentState());


						/*System.exit(0)*/ return;
					} else if (this.getCurrentState().getTurn().equals(Turn.DRAW)) {


						System.out.println("DRAW!");
						//added
						printMatchTime(startTimeMills);
						aggiornaMetrics(getCurrentState());

						/*System.exit(0)*/ return;
					}

				} catch (Exception e) {
					e.printStackTrace();
					/*System.exit(1)*/ return;
				}
			}
		} else {
			System.out.println("You are player " + this.getPlayer().toString() + "!");
			while (true) {
				try {
					this.read();
					System.out.println("Current state:");
					System.out.println(this.getCurrentState().toString());
					if (this.getCurrentState().getTurn().equals(Turn.BLACK)) {


						/*****AGGIUNTE*****/
						Long start = System.currentTimeMillis();

						action = minmax.makeDecision(timeOut, getCurrentState());

						Long end = System.currentTimeMillis();

						System.out.println("tempo impegato: __" + (end - start) + "__ millisecondi");


						this.write(action);
					} else if (this.getCurrentState().getTurn().equals(Turn.WHITE)) {
						System.out.println("Waiting for your opponent move... ");
					} else if (this.getCurrentState().getTurn().equals(Turn.WHITEWIN)) {
						System.out.println("YOU LOSE!");

						//added
						printMatchTime(startTimeMills);
						aggiornaMetrics(getCurrentState());

						/*System.exit(0)*/ return;
					} else if (this.getCurrentState().getTurn().equals(Turn.BLACKWIN)) {
						System.out.println("YOU WIN!");

						//added
						printMatchTime(startTimeMills);
						aggiornaMetrics(getCurrentState());

						/*System.exit(0)*/ return;
					} else if (this.getCurrentState().getTurn().equals(Turn.DRAW)) {
						System.out.println("DRAW!");

						//added
						printMatchTime(startTimeMills);
						aggiornaMetrics(getCurrentState());

						/*System.exit(0)*/ return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					/*System.exit(1)*/ return;
				}
			}
		}
	}

	private void aggiornaMetrics(State currentState) {

		metrics = new MetricsPartita_Genetic();

		boolean victory = false;
		double opponentPawsEaten = 0;
		double minePawsLosts = 0;

		if(super.getPlayer() == Turn.BLACK){

			minePawsLosts = (NUMERO_INIZIALE_NERI - currentState.getNumberOf(State.Pawn.BLACK)); //TODO CONTROLLARE CHE NON SIA DISPONIBILE GIA' QUESTA CONSTANTE
			//percentuale
			minePawsLosts = minePawsLosts/NUMERO_INIZIALE_NERI * 100.0;

			opponentPawsEaten = (NUMERO_INIZIALE_BIANCHI - currentState.getNumberOf(State.Pawn.WHITE));
			opponentPawsEaten = opponentPawsEaten/NUMERO_INIZIALE_BIANCHI * 100.0;

			victory = currentState.getTurn().equals(Turn.BLACKWIN);

		}

		if(super.getPlayer() == Turn.WHITE){

			minePawsLosts = (NUMERO_INIZIALE_BIANCHI - currentState.getNumberOf(State.Pawn.WHITE)); //TODO CONTROLLARE CHE NON SIA DISPONIBILE GIA' QUESTA CONSTANTE
			//percentuale
			minePawsLosts = minePawsLosts/NUMERO_INIZIALE_BIANCHI * 100.0;

			opponentPawsEaten = (NUMERO_INIZIALE_NERI - currentState.getNumberOf(State.Pawn.BLACK));
			opponentPawsEaten = opponentPawsEaten/NUMERO_INIZIALE_NERI * 100.0;

			victory = currentState.getTurn().equals(Turn.WHITEWIN);
		}

		metrics.setDraw(currentState.getTurn().equals(Turn.DRAW));
		metrics.setTime(matchTime);
		metrics.setMinePawsLosts(minePawsLosts);
		metrics.setOpponentPawsEaten(opponentPawsEaten);
		metrics.setVictory(victory);
	}

	private void printMatchTime(Long startTimeMills) {
		Long endTimeMills = System.currentTimeMillis();
		
		matchTime = (endTimeMills - startTimeMills);
		
		System.out.println("PARTITA FINITA IN __" + (endTimeMills - startTimeMills) / 1000.0 + "__ seconds");
	}

}
