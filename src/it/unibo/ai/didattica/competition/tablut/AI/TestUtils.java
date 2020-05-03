package it.unibo.ai.didattica.competition.tablut.AI;

import com.google.gson.Gson;
import it.unibo.ai.didattica.competition.tablut.domain.*;
import it.unibo.ai.didattica.competition.tablut.util.StreamUtils;

import java.util.BitSet;
import java.util.List;

public class TestUtils {

    public static void main(String[] args) {
        System.out.println("Testing...");
        Utils u = new Utils(true);
        State state = new StateTablut();

        state.removePawn(4, 3);
        state.removePawn(4, 2);
        state.getBoard()[4][4] = State.Pawn.THRONE;
        state.getBoard()[4][1] = State.Pawn.EMPTY;
        state.getBoard()[1][4] = State.Pawn.EMPTY;
        state.getBoard()[2][4] = State.Pawn.EMPTY;
        state.getBoard()[2][2] = State.Pawn.KING;

        long start, end, delay1, delay2;

        /***pivoting***/

        start = System.currentTimeMillis();
        List<Action> l = u.getSuccessors(state);
        end = System.currentTimeMillis();

        delay1 = end - start;

        HeuristicFrittoMisto e = new HeuristicFrittoMisto(State.Turn.BLACK);
        start = System.currentTimeMillis();
        double n = e.victoryPaths(new Coord(2,2), state.getPieces().get(0), state.getPieces().get(1));
        end = System.currentTimeMillis();

        delay1 = end - start;
        System.out.println(n + "  ," + delay1);


    }



}
