package it.unibo.ai.didattica.competition.tablut.AI.Clients;

import it.unibo.ai.didattica.competition.tablut.AI.Minmax;
import it.unibo.ai.didattica.competition.tablut.client.TablutClient;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.Game;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

/**
 * 
 * @author
 *
 */
public class TablutFrittoMistoClient extends TablutClient {

	private final int timeOut = 58;
	private final int currDepthLimit = 3;
	private Game game;
	private static final String NAME = "FrittoMisto";

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

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {

		if (args.length == 0) {
			System.out.println("You must specify which player you are (WHITE or BLACK)!");
			System.exit(-1);
		}
		System.out.println("Selected this: " + args[0]);

		TablutClient client = new TablutFrittoMistoClient(args[0]);

		client.run();

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
			System.out.println("You are player " + this.getPlayer().toString() + "!");
			while (true) {
				try {
					this.read();

					System.out.println("Current state:");
					System.out.println(this.getCurrentState().toString());
					if (this.getCurrentState().getTurn().equals(Turn.WHITE)) {

						/*****AGGIUNTE*****/
						action = minmax.makeDecision(timeOut, getCurrentState());
						System.out.println("From: "+ action.getFrom() + ", to=" + action.getTo());

						this.write(action);
					} else if (this.getCurrentState().getTurn().equals(Turn.BLACK)) {
						System.out.println("Waiting for your opponent move... ");
					} else if (this.getCurrentState().getTurn().equals(Turn.WHITEWIN)) {
						System.out.println("YOU WIN!");
						System.exit(0);
					} else if (this.getCurrentState().getTurn().equals(Turn.BLACKWIN)) {
						System.out.println("YOU LOSE!");
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
		} else {
			System.out.println("You are player " + this.getPlayer().toString() + "!");
			while (true) {
				try {
					this.read();
					System.out.println("Current state:");
					System.out.println(this.getCurrentState().toString());
					if (this.getCurrentState().getTurn().equals(Turn.BLACK)) {


						action = minmax.makeDecision(timeOut, getCurrentState());
						System.out.println("From: "+ action.getFrom() + ", to=" + action.getTo());


						this.write(action);
					} else if (this.getCurrentState().getTurn().equals(Turn.WHITE)) {
						System.out.println("Waiting for your opponent move... ");
					} else if (this.getCurrentState().getTurn().equals(Turn.WHITEWIN)) {
						System.out.println("YOU LOSE!");
						System.exit(0);
					} else if (this.getCurrentState().getTurn().equals(Turn.BLACKWIN)) {
						System.out.println("YOU WIN!");
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

}
