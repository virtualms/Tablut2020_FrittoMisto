package it.unibo.ai.didattica.competition.tablut.AI;

import it.unibo.ai.didattica.competition.tablut.domain.Game;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.sql.ResultSet;

public class Heuristic {




    public Heuristic() {
    }

    // "state" -> futuro eventuale
    // "player"  -> per chi sto valutando lo stato
    public static double eval(State state, State.Turn player) {

        double result = 0;
        if (player == State.Turn.WHITE)
            result = calcolaWhite(state);

        if (player == State.Turn.BLACK)
            result = calcolaBlack(state);

        if (player == State.Turn.WHITE && state.getTurn() == State.Turn.WHITEWIN)
            result = Double.POSITIVE_INFINITY;

        if (player == State.Turn.WHITE && state.getTurn() == State.Turn.BLACKWIN)
            result = Double.NEGATIVE_INFINITY;

        if (player == State.Turn.BLACK && state.getTurn() == State.Turn.BLACKWIN)
            result = Double.POSITIVE_INFINITY;

        if (player == State.Turn.BLACK && state.getTurn() == State.Turn.WHITEWIN)
            result = Double.NEGATIVE_INFINITY;

        return result;

    }

    private static double calcolaWhite(State state) {
        return state.getNumberOf(State.Pawn.WHITE) - state.getNumberOf(State.Pawn.BLACK);
    }

    private static double calcolaBlack(State state) {
        return state.getNumberOf(State.Pawn.BLACK) - state.getNumberOf(State.Pawn.WHITE);
    }
}
