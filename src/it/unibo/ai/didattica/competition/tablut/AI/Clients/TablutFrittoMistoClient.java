package it.unibo.ai.didattica.competition.tablut.AI.Clients;

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
import java.text.ParseException;

/**
 * 
 * @author E.Cerulo, V.M.Stanzione
 *
 */
public class TablutFrittoMistoClient extends TablutClient {

	private final int timeOut;
//	private final int currDepthLimit = 4;
	private final int currDepthLimit;
	private static final String NAME = "FrittoMisto";


	/***costruttori***/
	//gli argomenti devono essere il ruolo (White or Black), il timeout in secondi, e l'indirizzo IP del server.
	public TablutFrittoMistoClient(String player, int timeout, String ipAddress) throws UnknownHostException, IOException {
		super(player, NAME , timeout, ipAddress);
		this.timeOut = timeout;
		currDepthLimit = 4;
	}

	public TablutFrittoMistoClient(String player, int timeout, String ipAddress, int depth) throws UnknownHostException, IOException {
		super(player, NAME , timeout, ipAddress);
		this.timeOut = timeout;
		this.currDepthLimit = depth;
	}

	public TablutFrittoMistoClient(String player) throws IOException {
		super(player, NAME);
		this.timeOut = 58;
		currDepthLimit = 4;
	}


	/***main***/
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {

		if (args.length == 0) {
			System.out.println("You must specify which player you are (WHITE or BLACK)!");
			System.exit(-1);
		}
		System.out.println("Selected this: " + args[0]);

		TablutClient client = null;
		if(args.length == 1)
			//COLORE
			client = new TablutFrittoMistoClient(args[0]);
		else if(args.length == 3)
			//gli argomenti devono essere il ruolo (White or Black), il timeout in secondi, e l'indirizzo IP del server.
			client = new TablutFrittoMistoClient(args[0].toUpperCase(), Math.round(Float.parseFloat(args[1])) - 2, args[2]);
		else if (args.length == 4)
			//gli argomenti devono essere il ruolo (White or Black), il timeout in secondi, e l'indirizzo IP del server, depth
			client = new TablutFrittoMistoClient(args[0].toUpperCase(), Math.round(Float.parseFloat(args[1])) - 2, args[2], Integer.parseInt(args[3]));
		else {System.out.println("Usage: role timeout IP ; role"); System.exit(1);}

		client.run();

	}

	@Override
	public void run() {
		System.out.println("You are player " + this.getPlayer().toString() + "!");
		Action action;
		Minmax minmax = new Minmax(currDepthLimit, getPlayer());

		try {
			this.declareName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (this.getPlayer() == Turn.WHITE) {
			faiIlClient(minmax, Turn.WHITE, Turn.BLACK, "YOU WIN!", "YOU LOSE!");
			return;
		} else {

			faiIlClient(minmax, Turn.BLACK, Turn.WHITE, "YOU LOSE!", "YOU WIN!");
		}
	}

	private void faiIlClient(Minmax minmax, Turn turnoMio, Turn turnoNemico, String s, String s2) {

		Action action;
		while (true) {

			try{
				this.read();
				System.out.println("Current state:");
				System.out.println(this.getCurrentState().toString());
			} catch (Exception e) {
				System.out.println("Errore nella lettura nuovo stato");
				e.printStackTrace();
			}

			try {
				if (this.getCurrentState().getTurn().equals(turnoMio)) {

					/*****ACTION*****/
					long start = System.currentTimeMillis();

					action = minmax.makeDecision(timeOut, getCurrentState().clone());
					this.write(action);

					long end = System.currentTimeMillis();
					System.out.println("Ci ho messo " + (end - start) + " millisecs");
					/**************/

				} else if (this.getCurrentState().getTurn().equals(turnoNemico)) {
					System.out.println("Waiting for your opponent move... ");
				} else if (this.getCurrentState().getTurn().equals(Turn.WHITEWIN)) {
					System.out.println(s);
					System.exit(0);
				} else if (this.getCurrentState().getTurn().equals(Turn.BLACKWIN)) {
					System.out.println(s2);
					System.exit(0);
				} else if (this.getCurrentState().getTurn().equals(Turn.DRAW)) {
					System.out.println("DRAW!");
					System.exit(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

}
