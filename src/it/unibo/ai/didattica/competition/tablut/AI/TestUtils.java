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
        state.removePawn(0, 3);

        long start, end, delay1, delay2;

        /***pivoting***/

        start = System.currentTimeMillis();
        u.setSuppressPrint(false);
        List<Action> l = u.getSuccessors(state);
        end = System.currentTimeMillis();

        delay1 = end - start;

        HeuristicFrittoMisto e = new HeuristicFrittoMisto(State.Turn.BLACK);
        start = System.currentTimeMillis();
        end = System.currentTimeMillis();

        delay1 = end - start;


    }



}
