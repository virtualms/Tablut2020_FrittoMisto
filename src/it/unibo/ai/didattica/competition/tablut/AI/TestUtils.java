package it.unibo.ai.didattica.competition.tablut.AI;

import it.unibo.ai.didattica.competition.tablut.domain.*;

import java.util.BitSet;
import java.util.List;

public class TestUtils {

    public static void main(String[] args) {
        System.out.println("Testing...");
        Utils u = new Utils(true);
        State state = new StateTablut();



        Game game = new GameAshtonTablut(state.clone(), 0, -1, "logs", "W", "B");

        long start, end, delay1, delay2;

        /***pivoting***/

        start = System.currentTimeMillis();
        List<Action> l = u.getSuccessors(state);
        end = System.currentTimeMillis();

        delay1 = end - start;
        int n = state.getNumberOf(State.Pawn.WHITE);

        System.out.println(n);

        State s = new StateTablut();
        State.Pawn [][] pawns = new State.Pawn[9][9];

        //TODO BOIAAAAAAAAAAAAAA FUNZIONA
        pawns[0][0] = State.Pawn.EMPTY;
        pawns[0][1] = State.Pawn.EMPTY;
        pawns[0][2] = State.Pawn.EMPTY;
        pawns[0][3] = State.Pawn.EMPTY;
        pawns[0][4] = State.Pawn.BLACK;
        pawns[0][5] = State.Pawn.EMPTY;
        pawns[0][6] = State.Pawn.EMPTY;
        pawns[0][7] = State.Pawn.EMPTY;
        pawns[0][8] = State.Pawn.EMPTY;

        pawns[1][0] = State.Pawn.EMPTY;
        pawns[1][1] = State.Pawn.EMPTY;
        pawns[1][2] = State.Pawn.EMPTY;
        pawns[1][3] = State.Pawn.EMPTY;
        pawns[1][4] = State.Pawn.BLACK;
        pawns[1][5] = State.Pawn.EMPTY;
        pawns[1][6] = State.Pawn.EMPTY;
        pawns[1][7] = State.Pawn.EMPTY;
        pawns[1][8] = State.Pawn.EMPTY;

        pawns[2][0] = State.Pawn.EMPTY;
        pawns[2][1] = State.Pawn.EMPTY;
        pawns[2][2] = State.Pawn.EMPTY;
        pawns[2][3] = State.Pawn.BLACK;
        pawns[2][4] = State.Pawn.EMPTY;
        pawns[2][5] = State.Pawn.EMPTY;
        pawns[2][6] = State.Pawn.EMPTY;
        pawns[2][7] = State.Pawn.EMPTY;
        pawns[2][8] = State.Pawn.EMPTY;

        pawns[3][0] = State.Pawn.EMPTY;
        pawns[3][1] = State.Pawn.EMPTY;
        pawns[3][2] = State.Pawn.EMPTY;
        pawns[3][3] = State.Pawn.BLACK;
        pawns[3][4] = State.Pawn.BLACK;
        pawns[3][5] = State.Pawn.BLACK;
        pawns[3][6] = State.Pawn.BLACK;
        pawns[3][7] = State.Pawn.EMPTY;
        pawns[3][8] = State.Pawn.EMPTY;

        pawns[4][0] = State.Pawn.BLACK;
        pawns[4][1] = State.Pawn.BLACK;
        pawns[4][2] = State.Pawn.EMPTY;
        pawns[4][3] = State.Pawn.EMPTY;
        pawns[4][4] = State.Pawn.KING;
        pawns[4][5] = State.Pawn.EMPTY;
        pawns[4][6] = State.Pawn.EMPTY;
        pawns[4][7] = State.Pawn.BLACK;
        pawns[4][8] = State.Pawn.BLACK;

        pawns[5][0] = State.Pawn.BLACK;
        pawns[5][1] = State.Pawn.EMPTY;
        pawns[5][2] = State.Pawn.EMPTY;
        pawns[5][3] = State.Pawn.WHITE;
        pawns[5][4] = State.Pawn.WHITE;
        pawns[5][5] = State.Pawn.BLACK;
        pawns[5][6] = State.Pawn.WHITE;
        pawns[5][7] = State.Pawn.EMPTY;
        pawns[5][8] = State.Pawn.BLACK;

        pawns[6][0] = State.Pawn.EMPTY;
        pawns[6][1] = State.Pawn.EMPTY;
        pawns[6][2] = State.Pawn.EMPTY;
        pawns[6][3] = State.Pawn.WHITE;
        pawns[6][4] = State.Pawn.EMPTY;
        pawns[6][5] = State.Pawn.EMPTY;
        pawns[6][6] = State.Pawn.EMPTY;
        pawns[6][7] = State.Pawn.EMPTY;
        pawns[6][8] = State.Pawn.EMPTY;

        pawns[7][0] = State.Pawn.EMPTY;
        pawns[7][1] = State.Pawn.EMPTY;
        pawns[7][2] = State.Pawn.WHITE;
        pawns[7][3] = State.Pawn.EMPTY;
        pawns[7][4] = State.Pawn.BLACK;
        pawns[7][5] = State.Pawn.EMPTY;
        pawns[7][6] = State.Pawn.EMPTY;
        pawns[7][7] = State.Pawn.EMPTY;
        pawns[7][8] = State.Pawn.EMPTY;

        pawns[8][0] = State.Pawn.EMPTY;
        pawns[8][1] = State.Pawn.EMPTY;
        pawns[8][2] = State.Pawn.EMPTY;
        pawns[8][3] = State.Pawn.BLACK;
        pawns[8][4] = State.Pawn.BLACK;
        pawns[8][5] = State.Pawn.EMPTY;
        pawns[8][6] = State.Pawn.EMPTY;
        pawns[8][7] = State.Pawn.EMPTY;
        pawns[8][8] = State.Pawn.EMPTY;

        s.setBoard(pawns);

        u.setSuppressPrint(false);
        u.getSuccessors(s);



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
        for(Action a: u.getSuccessors(state)){
            try {
                game.checkMove(state.clone(), a);
            }

            catch(Exception e){
                System.out.println(e.getStackTrace());
            }
        }

        delay2 = System.currentTimeMillis() - start;
        System.out.println("Delay " + delay2);

    */
    }


}
