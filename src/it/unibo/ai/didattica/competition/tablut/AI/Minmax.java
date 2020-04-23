package it.unibo.ai.didattica.competition.tablut.AI;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.Game;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.exceptions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

            //TODO COMMENTATO
            //new File("cane.txt").createNewFile();

            //importante altrimenti il thread che si occupa della call() continua ad eseguire
            //TODO ATTENZIONEEEEEE! NON SI TORNA UN RISOLTATO A VOLTE SE TIMEOUT TROPPO BASSO, BISOGNA TARARE BENE DEPTH O CAMBIARE
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

    //TODO BRUTTOOOOOOOOOOOOOO
    private Utils u = new Utils(true);

    @Override
    public Action call() throws Exception {
        result = null;
        double resultValue = Double.NEGATIVE_INFINITY;

        //TODO: Collection più performante?
        List<Action> azioni = null;

        //TODO: TESTING Utils

//        azioni = game.getAllLegalActions(currentState);
        azioni = u.getSuccessors(currentState);

        //TODO FACCIO SHUFFLE O LE PARTITE SONO TUTTE UGUALI (ANDREBBE FATTO IN UTILITY, DANDO PRIORITA' ALLE MOSSE DEL RE)
        Collections.shuffle(azioni);

        for (Action action : azioni) {

            //double value = minValue(game.checkMove(currentState.clone(), action), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
            //TODO CHECKMOVE HA ROTTO LA MINCHIA
            double value = minValue(this.movePawn(currentState.clone(), action), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);

            if (value > resultValue) {
                result = action;
                resultValue = value;
            }
        }
        return result;
    }

    public double maxValue(State state, double alpha, double beta, int depth) throws Exception{
        if (state.getTurn() == State.Turn.BLACKWIN || state.getTurn() == State.Turn.WHITEWIN || depth >= currDepthLimit)
            return Heuristic.eval(state, player);
        double value = Double.NEGATIVE_INFINITY;
        //TODO: DEVONO ESSERE RESTITUTE TUTTE LE AZIONI O SOLO QUELLE POSSIBILI PER UN DETERMINATO GIOCATORE??
        for (Action action : /*game.getAllLegalActions(state)*/u.getSuccessors(state)) {

            //TODO LA CHECK MI HA ROTTO IL CAZZO
            value = Math.max(value, minValue(this.movePawn(currentState.clone(), action), alpha, beta, depth + 1));
            if (value >= beta)
            return value;
            alpha = Math.max(alpha, value);
        }
        return value;
    }

    public double minValue(State state, double alpha, double beta, int depth) throws Exception{
        if (state.getTurn() == State.Turn.BLACKWIN || state.getTurn() == State.Turn.WHITEWIN || depth >= currDepthLimit)
            return Heuristic.eval(state, player);
        double value = Double.POSITIVE_INFINITY;
        //TODO: DEVONO ESSERE RESTITUTE TUTTE LE AZIONI O SOLO QUELLE POSSIBILI PER UN DETERMINATO GIOCATORE??
        for (Action action : /*game.getAllLegalActions(state)*/u.getSuccessors(state)) {

            //TODO LA CHECK MI HA ROTTO IL CAZZO
            value = Math.min(value, maxValue(this.movePawn(currentState.clone(), action), alpha, beta, depth + 1));
            if (value <= alpha)
                return value;
            beta = Math.min(beta, value);
        }
        return value;
    }



    /***********************++CHECK NON ROMPI CAZZO++**************************************/
    private State movePawn(State state, Action a) {
        State.Pawn pawn = state.getPawn(a.getRowFrom(), a.getColumnFrom());
        State.Pawn[][] newBoard = state.getBoard();
        // State newState = new State();
        // libero il trono o una casella qualunque
        if (a.getColumnFrom() == 4 && a.getRowFrom() == 4) {
            newBoard[a.getRowFrom()][a.getColumnFrom()] = State.Pawn.THRONE;
        } else {
            newBoard[a.getRowFrom()][a.getColumnFrom()] = State.Pawn.EMPTY;
        }

        // metto nel nuovo tabellone la pedina mossa
        newBoard[a.getRowTo()][a.getColumnTo()] = pawn;
        // aggiorno il tabellone
        state.setBoard(newBoard);
        // cambio il turno
        if (state.getTurn().equalsTurn(State.Turn.WHITE.toString())) {
            state.setTurn(State.Turn.BLACK);
        } else {
            state.setTurn(State.Turn.WHITE);
        }

        return state;
    }
}