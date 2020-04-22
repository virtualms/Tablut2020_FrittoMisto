package it.unibo.ai.didattica.competition.tablut.AI;

import it.unibo.ai.didattica.competition.tablut.domain.*;

import java.util.BitSet;
import java.util.List;

public class TestUtils {

    public static void main(String [] args){
        System.out.println("Testing...");
        Utils u = new Utils();
        State state = new StateTablut();

        long start, end, delay;

        /***pivoting***/
        start = System.currentTimeMillis();
        u.getSuccessorFirstLv_proto(state);
        end = System.currentTimeMillis();

        delay = end - start;
        System.out.println("PIVOTING___Done in " + delay + ". Count " + u.getA().size());
        //TODO BOIAAAAAAAAAAAAAA FUNZIONA

/*

        Game game = new GameAshtonTablut(state.clone(), 0, -1, "logs", "W", "B");
        start = System.currentTimeMillis();
        List<Action>  l = game.getAllLegalActions(state);
        end = System.currentTimeMillis();
        delay = end - start;
        System.out.println("OTHER___Done in " + delay + ". Count " + l.size());
*/


/*
        Game game = new GameAshtonTablut(state.clone(), 0, -1, "logs", "W", "B");
        for(Action a: u.getA()){
            try {
                u.movePawn(game, state.clone(), a);
                System.out.println("Found from="+ a.getFrom() +" ,to=" +a.getTo());
            }

            catch(Exception e){
                System.out.println(e.getStackTrace());
            }
        }
*/

    }
}
