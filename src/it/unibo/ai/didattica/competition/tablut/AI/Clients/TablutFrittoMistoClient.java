package it.unibo.ai.didattica.competition.tablut.AI.Clients;

import it.unibo.ai.didattica.competition.tablut.AI.Clients.Utils.MetricsPartita_Genetic;
import it.unibo.ai.didattica.competition.tablut.AI.Minmax;
import it.unibo.ai.didattica.competition.tablut.client.TablutClient;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.Game;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * 
 * @author
 *
 */
public class TablutFrittoMistoClient extends TablutClient {

	private static final int BLACKNUMBER = 16;
	private static final int WHITENUMBER = 8;
	public static final int MAX_NUM_EXCEPTIONS = 10;
	//	private final int timeOut = 30;
	private final int timeOut = 58;
//	private final int currDepthLimit = 4;
	private final int currDepthLimit = 1;
	private Game game;
	private static final String NAME = "FrittoMisto";

	private static MetricsPartita_Genetic metrics;

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

//		metrics = new MetricsPartita_Genetic();
		metrics = null;

		client.run();

		return metrics;
	}

	@Override
	public void run() {
		System.out.println("You are player " + this.getPlayer().toString() + "!");
		Action action;
		Minmax minmax = new Minmax(game, currDepthLimit, getPlayer(), true);

		try {
			this.declareName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (this.getPlayer() == Turn.WHITE) {
			//TODO. INSERITO PER NON AVERE CODICE DIVERSO FRA LE PROVE DEL NERO E DEL BIANCO. UNA VOLTA CAPITO PERCHè C'è
			// QUELL'ERRORE CON DEPTH 4 POSSIAMO ANCHE ELIMINARE IL METODO faiIlClient
			faiIlClient(minmax, Turn.WHITE, Turn.BLACK, "YOU WIN!", "YOU LOSE!");
			return;
		} else {
			faiIlClient(minmax, Turn.BLACK, Turn.WHITE, "YOU LOSE!", "YOU WIN!");
		}
	}

	private void faiIlClient(Minmax minmax, Turn turnoMio, Turn turnoNemico, String s, String s2) {

		long matchStart = System.currentTimeMillis();
		int numeberOfException = 0;

		Action action;
		while (true) {

			try{
				this.read();
				System.out.println("Current state:");
				System.out.println(this.getCurrentState().toString());
			} catch (Exception e) {
				System.out.println("Errore nella lettura nuovo stato");
				e.printStackTrace();
				if (numeberOfException > MAX_NUM_EXCEPTIONS){
					return;
				}
				numeberOfException++;
			}

			try {
				if (this.getCurrentState().getTurn().equals(turnoMio)) {

					/*****AGGIUNTE*****/

					long start = System.currentTimeMillis();

					action = minmax.makeDecision(timeOut, getCurrentState().clone());
					this.write(action);

					long end = System.currentTimeMillis();
					System.out.println("Ci ho messo " + (end - start) + " millisecs");
					/*******FINE*******/

				} else if (this.getCurrentState().getTurn().equals(turnoNemico)) {
					System.out.println("Waiting for your opponent move... ");
				} else if (this.getCurrentState().getTurn().equals(Turn.WHITEWIN)) {

					aggiornaMetrics(this.getCurrentState(), matchStart, false);

					System.out.println(s);
					return; //System.exit(0);
				} else if (this.getCurrentState().getTurn().equals(Turn.BLACKWIN)) {

					aggiornaMetrics(this.getCurrentState(), matchStart, false);

					System.out.println(s2);
					return; //System.exit(0);
				} else if (this.getCurrentState().getTurn().equals(Turn.DRAW)) {

					aggiornaMetrics(this.getCurrentState(), matchStart, true);

					System.out.println("DRAW!");
					return; //System.exit(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return; //System.exit(1);
			}
		}
	}

	private void aggiornaMetrics(State currentState, long matchStart, boolean isDraw) {

		metrics = new MetricsPartita_Genetic();

		long endMatch = System.currentTimeMillis();

		metrics.setTime(endMatch - matchStart);

		metrics.setVictory(
				getPlayer().equals(Turn.WHITE) ?
						currentState.getTurn().equals(Turn.WHITEWIN) :
						currentState.getTurn().equals(Turn.BLACKWIN)
		);

		metrics.setDraw(isDraw);


		if (getPlayer().equals(Turn.BLACK)) {

			metrics.setOpponentPawsEaten(
					WHITENUMBER - currentState.getNumberOf(State.Pawn.WHITE)
			);
			metrics.setMinePawsLosts(
					BLACKNUMBER - currentState.getNumberOf(State.Pawn.BLACK)
			);

		} else {
			metrics.setOpponentPawsEaten(
					BLACKNUMBER - currentState.getNumberOf(State.Pawn.BLACK)
			);

			metrics.setMinePawsLosts(
					WHITENUMBER - currentState.getNumberOf(State.Pawn.WHITE)
			);
		}




	}

}
