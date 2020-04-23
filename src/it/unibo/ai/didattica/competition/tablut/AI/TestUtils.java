package it.unibo.ai.didattica.competition.tablut.AI;

import it.unibo.ai.didattica.competition.tablut.domain.*;

import java.util.BitSet;
import java.util.List;

public class TestUtils {

    public static void main(String[] args) {
        System.out.println("Testing...");
        Utils u = new Utils(false);
        State state = new StateTablut();



        Game game = new GameAshtonTablut(state.clone(), 0, -1, "logs", "W", "B");

        long start, end, delay1, delay2;

        /***pivoting***/

        start = System.currentTimeMillis();
        List<Action> l = u.getSuccessors(state);
        end = System.currentTimeMillis();


        delay1 = end - start;
        System.out.println("PIVOTING___Done in " + delay1 + ". Count " + l.size());

        //TODO BOIAAAAAAAAAAAAAA FUNZIONA

        /****************Arcangelo**********/

    /*
        start = System.currentTimeMillis();
        List<Action> their = u.getAllLegalActions(game);
        end = System.currentTimeMillis();

        delay2 = end - start;
        System.out.println("PIVOTING___Done in " + delay1 + ". Count " + u.getA().size());
        System.out.println("ARCANGELO___Done in " + delay2 + ". Count " + their.size());

        System.out.println("\n\n\nDIFF\n\n" + "their_l=" + their.size() + ", mine=" + u.getA().size());
        for(Action a : their){
            if(!u.getA().contains(a))
                System.out.println("NOT FOUND: from=" + a.getFrom() +", to=" + a.getTo());
        }
*/
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
