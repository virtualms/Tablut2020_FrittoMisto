package it.unibo.ai.didattica.competition.tablut.AI;

import it.unibo.ai.didattica.competition.tablut.domain.*;

import java.util.BitSet;
import java.util.List;

public class TestUtils {

    public static void main(String[] args) {
        System.out.println("Testing...");
        Utils u = new Utils();
        State state = new StateTablut();
        //state.removePawn(8, 3);
        Game game = new GameAshtonTablut(state.clone(), 0, -1, "logs", "W", "B");

        long start, end, delay;

        /***pivoting***/

        start = System.currentTimeMillis();
        u.getSuccessorFirstLv_proto(state);
        List<Action> mine = u.getA();
        end = System.currentTimeMillis();

        delay = end - start;
        System.out.println("PIVOTING___Done in " + delay + ". Count " + u.getA().size());


        //TODO BOIAAAAAAAAAAAAAA FUNZIONA

        /****************Arcangelo**********/


        start = System.currentTimeMillis();
        List<Action> their = u.getAllLegalActions(game);
        end = System.currentTimeMillis();

        delay = end - start;
        //System.out.println("ARCANGELO___Done in " + delay + ". Count " + l.size());

        System.out.println("\n\n\nDIFF\n\n" + "their_l=" + their.size() + ", mine=" + mine.size());
        for(Action a : their){
            if(!mine.contains(a))
                System.out.println("NOT FOUND: from=" + a.getFrom() +", to=" + a.getTo());
        }

/*

        Game game = new GameAshtonTablut(state.clone(), 0, -1, "logs", "W", "B");
        start = System.currentTimeMillis();
        List<Action>  l = game.getAllLegalActions(state);
        end = System.currentTimeMillis();
        delay = end - start;
        System.out.println("OTHER___Done in " + delay + ". Count " + l.size());
*/

/*

        for(Action a: u.getA()){
            try {
                //game.checkMove(state.clone(), a);
                System.out.println("Found from="+ a.getFrom() +" ,to=" +a.getTo());
            }

            catch(Exception e){
                System.out.println(e.getStackTrace());
            }
        }

*/
    }


}
