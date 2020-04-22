package it.unibo.ai.didattica.competition.tablut.AI;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.Game;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;


public final class Minmax implements Callable<Action> {

    //TODO: Controllare, per avere conferma, che questo executor è quello più performante.
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public static Game game;
    protected int currDepthLimit;
    private State.Turn player;

    //TODO: è per limitare, eventualmente, la ricerca soltanto al primo livello
    private boolean iterative;

//  Come parametri per la call
    private State currentState;
    private Action result;

    //TODO: Questa versione non ha il limite di profondità.
//     Può essere inserito oppure no, vediamo come si comporta il timeout
    public Minmax(Game game, int currDepthLimit, State.Turn player, boolean iterative) {
        this.game = game;
        this.currDepthLimit = currDepthLimit;
        this.player = player;
        this.iterative = iterative;
    }


    public Action makeDecision(int timeOut, State stato) throws IOException {

        //In questo modo per passare i parametri a call()
        currentState = stato;

        //metto in esecuzione il task (viene chiamata "call()")
        Future<Action> risultato = executorService.submit(this);

        //TODO: Come inizializzare una azione coerente qui? Per non metterla a null
        result = null;

        try {
            result = risultato.get(timeOut, TimeUnit.SECONDS);
        } catch (TimeoutException e) {

            new File("cane.txt").createNewFile();

            //importante altrimenti il thread che si occupa della call() continua ad eseguire
            risultato.cancel(true);

            //Questo è il metodo che fa terminare il thread che lavora sui callable.
            //Decommentato altrimenti non potrebbero esserci chiamate successive di questo metodo.
            //In alternativa si potrebbe instanziare un ExecutorService ad ogni chiamata, ma per me è inutile,
            //inoltre in questo modo si ha sempre un pool di thread pronti ad eseguire. Se venisse instanziato ogni volta,
            //i thread verrebbero inizializzati ogni volta con perdita di prestazioni.
            //executorService.shutdownNow();

            System.out.println("time_out scattato");
            return result;

        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Action call() throws Exception {
        result = null;
        double resultValue = Double.NEGATIVE_INFINITY;

        //TODO: Collection più performante?
        ArrayList<Action> azioni = null;

        azioni = game.getAllLegalActions(currentState);

        for (Action action : azioni) {

            double value = minValue(game.checkMove(currentState.clone(), action), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if (value > resultValue) {
                result = action;
                resultValue = value;
            }
        }
        return result;
    }

    public double maxValue(State state, double alpha, double beta) throws Exception{
        if (state.getTurn() == State.Turn.BLACKWIN || state.getTurn() == State.Turn.WHITEWIN)
            return Heuristic.eval(state, player);
        double value = Double.NEGATIVE_INFINITY;
        //TODO: DEVONO ESSERE RESTITUTE TUTTE LE AZIONI O SOLO QUELLE POSSIBILI PER UN DETERMINATO GIOCATORE??
        for (Action action : game.getAllLegalActions(state)) {
            value = Math.max(value, minValue(game.checkMove(state.clone(), action), alpha, beta));
            if (value >= beta)
            return value;
            alpha = Math.max(alpha, value);
        }
        return value;
    }

    public double minValue(State state, double alpha, double beta) throws Exception{
        if (state.getTurn() == State.Turn.BLACKWIN || state.getTurn() == State.Turn.WHITEWIN)
            return Heuristic.eval(state, player);
        double value = Double.POSITIVE_INFINITY;
        //TODO: DEVONO ESSERE RESTITUTE TUTTE LE AZIONI O SOLO QUELLE POSSIBILI PER UN DETERMINATO GIOCATORE??
        for (Action action : game.getAllLegalActions(state)) {
            value = Math.min(value, maxValue(game.checkMove(state.clone(), action), alpha, beta));
            if (value <= alpha)
                return value;
            beta = Math.min(beta, value);
        }
        return value;
    }

}