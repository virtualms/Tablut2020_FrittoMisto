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
        state.setTurn(State.Turn.WHITE);
        state.getBoard()[1][4] = State.Pawn.EMPTY;
        state.getBoard()[1][8] = State.Pawn.BLACK;
        state.getBoard()[2][4] = State.Pawn.EMPTY;
        state.getBoard()[2][3] = State.Pawn.WHITE;

        long start, end, delay1, delay2;

        /***pivoting***/

        start = System.currentTimeMillis();
        u.setSuppressPrint(false);
        List<Action> l = u.getSuccessors(state);
        end = System.currentTimeMillis();

    }

//d1 to d3

}
