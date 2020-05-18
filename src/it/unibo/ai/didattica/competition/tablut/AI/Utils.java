package it.unibo.ai.didattica.competition.tablut.AI;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class Utils {

    private boolean suppressPrint;

    public Utils(boolean suppressPrint){
        this.suppressPrint = suppressPrint;
    }

    /*****get-set****/
    public boolean isSuppressPrint() {
        return suppressPrint;
    }

    public void setSuppressPrint(boolean suppressPrint) {
        this.suppressPrint = suppressPrint;
    }


    /******CAMP OR KING********/
    public boolean isCastle(int r, int c) {
        return (r == 4 && c == 4);
    }

    public boolean isCastle(Coord c) {
        return (c.getRow() == 4 && c.getCol() == 4);
    }

    public boolean isCamp(int row, int col) {
        return (((row == 0 || row == 8) && (col >= 3 && col <= 5)) ||
                ((col == 0 || col == 8) && (row >= 3 && row <= 5)) ||
                ((row == 1 || row == 7) && (col == 4)) ||
                ((col == 1 || col == 7) && (row == 4)));
    }

    public boolean isCamp(Coord c) {
        int row=c.getRow(), col=c.getCol();
        return (((row == 0 || row == 8) && (col >= 3 && col <= 5)) ||
                ((col == 0 || col == 8) && (row >= 3 && row <= 5)) ||
                ((row == 1 || row == 7) && (col == 4)) ||
                ((col == 1 || col == 7) && (row == 4)));
    }

    /******CREAZIONE BIT-MAP******/
    public byte[][] w_map(State state, List<Coord> whitePieces) {
        //WHITE: w-map1 -->ciclo sullo stato, se incontro un pedone o campo/castello metto un 1 altrimenti uno 0
        State.Pawn[][] board = state.getBoard();
        byte[][] bytes = new byte[9][9];

        int r, c;
        for (r = 0; r < 9; r++) {
            for (c = 0; c < 9; c++) {
                if (board[r][c].equals(State.Pawn.EMPTY) && !isCamp(r, c) && !isCastle(r, c))
                    bytes[r][c] = 0;

                else {
                    bytes[r][c] = 1;

                    //mentre ciclo salvo le coordinate (utile classe ausiliaria coord) di tutti i pezzi del mio colore che incontro
                    if (board[r][c].equals(State.Pawn.WHITE))
                        whitePieces.add(new Coord(r, c));

                    //salvo il re
                    else if (board[r][c].equals(State.Pawn.KING))
                        whitePieces.add(0, new Coord(r, c));
                }
            }
        }

        return bytes;
    }

    public byte[][][] b_map(State state, List<Coord> blackPieces) {
        //ciclo sullo stato, se incontro un pedone o campo/castello metto un 1 altrimenti uno 0
        State.Pawn[][] board = state.getBoard();
        byte[][][] bytes = new byte[2][9][9]; //array di matrici. 0 table, 1 table_camp

        int r, c;
        for (r = 0; r < 9; r++) {
            for (c = 0; c < 9; c++) {
                if (board[r][c].equals(State.Pawn.EMPTY) /*&& !isCamp(r, c)*/ && !isCastle(r, c)) {
                    bytes[1][r][c] = 0;

                    if(!isCamp(r, c))
                        bytes[0][r][c] = 0;
                    else bytes[0][r][c] = 1;
                }
                else {
                    bytes[1][r][c] = 1;
                    bytes[0][r][c] = 1;


                    if(board[r][c].equals(State.Pawn.BLACK))
                        blackPieces.add(new Coord(r, c));
                }//else
            }//for
        }//for

        return bytes;
    }

    public void printboard(byte[][] a){
        for (int i=0; i<9; i++){
            for (int k=0; k<9; k++){
                System.out.print(a[i][k] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public List<Action> getSuccessors(State state) {

        //Coordinate dei pezzi
        List<Coord> whitePieces = new LinkedList<>();
        List<Coord> blackPieces = new ArrayList<>();
        //risultato
        List<Action> possibleActions = new LinkedList<>();

        //bitmap del campo per le azioni lecite
        byte[][] table = null;
        byte[][] table_camp = null;

        /***white***/
        if (state.getTurn().equals(State.Turn.WHITE)) {
            table = w_map(state, whitePieces);
            if(!suppressPrint) {System.out.println("_____TABLE_____");printboard(table);}

            for(Coord c : whitePieces){
                try {

                    //Mosse del re inserite prima
                    if(whitePieces.get(0).equals(c))
                        possibleActions.addAll(0, pivoting(c, table, state.getTurn()));
                    else
                        possibleActions.addAll(pivoting(c, table, state.getTurn()));

                }
                catch (Exception e){
                    System.out.println(e.getStackTrace());
                    System.out.println("Sono esploso in pivoting WHITE :)...");
                }
            }
        }


        /***black***/
        else {

            byte [][][] bytes = b_map(state, blackPieces);
            table = bytes[0];
            table_camp = bytes[1];

            if(!suppressPrint){System.out.println("_____TABLE_____");printboard(table);}
            if(!suppressPrint){System.out.println("_____TAB_C_____");printboard(table_camp);}
            for(Coord c : blackPieces){
                try {
                    if(isCamp(c))
                        possibleActions.addAll(pivoting(c, table_camp, state.getTurn()));
                    else possibleActions.addAll(pivoting(c, table, state.getTurn()));
                }
                catch (Exception e){
                    System.out.println(e.getStackTrace());
                    System.out.println("Sono esploso in pivoting BLACK :)...");
                }
            }
        }

        return possibleActions;
    }


    /****Pivoting***/
    //Torna un bitset ottenuto da una colonna
    private  BitSet getColumn(byte[][] array, int index){
        BitSet column = new BitSet(9); // Here I assume a rectangular 2D array!
        for(int i=0; i<array[0].length; i++){
             if(array[i][index] == 1)  column.set(i);
        }
        return column;
    }

    //Torna un bitset ottenuto da una riga
    private  BitSet getRow(byte[][] array, int index){
        BitSet row = new BitSet(9); // Here I assume a rectangular 2D array!
        for(int i=0; i<array[0].length; i++){
            if(array[index][i] == 1)  row.set(i);
        }
        return row;
    }

    //Presa da State per ottenere coordinate human-readable
    public String getBox(int row, int column) {
        String ret;
        char col = (char) (column + 97);
        ret = col + "" + (row + 1);
        return ret;
    }

    //places where you can move 8 steps, it's useful in pivoting
    private boolean place_8_steps(Coord c){
        return  (c.getRow() == 0 && (c.getCol() == 2 || c.getCol() == 6)) ||
                (c.getCol() == 8 && (c.getRow() == 2 || c.getRow() == 6)) ||
                (c.getRow() == 8 && (c.getCol() == 2 || c.getCol() == 6)) ||
                (c.getCol() == 0 && (c.getRow() == 2 || c.getCol() == 6));
    }

    //Ritorna l'insieme delle mosse possibili per il pedone in posizione Coord c
    private List<Action> pivoting(Coord c, byte[][] table, State.Turn turn) throws IOException {

        List<Action> res = new LinkedList<>();
        //BitSet array = null; //vettore originale
        BitSet vec = new BitSet(9); //vettore posizioni ammissibili, usato per debug


        /*****column*****/
        //array = getColumn(table, c.getCol()); --> table[i][c.getCol()]
        String from = getBox(c.getRow(), c.getCol());

        int pivot = c.getRow(); //pezzo da muovere
        //vec.set(pivot); //posizione pezzo

        //scorro la colonna verso su, mi muovo su una colonna cambiando righe
        for(int i=pivot+1; i<9; i++){
            if(/*!array.get(i)*/ table[i][c.getCol()] == 0) {
                if(i - pivot == 8 && !place_8_steps(c)) continue; //si potrebbe mettere break
                vec.set(i);
                String to = getBox(i, c.getCol());
                res.add(new Action(from, to, turn));
            }
            else {break;}
        }

        //vado verso giù, mi muovo su una colonna cambiando righe
        for(int k=pivot-1; k>=0; k--){
            if(/*!array.get(k)*/table[k][c.getCol()] == 0) {
                if(pivot - k == 8 && !place_8_steps(c)) continue; //si potrebbe mettere break
                vec.set(k);
                String to = getBox(k, c.getCol());
                res.add(new Action(from, to, turn));
            }
            else {break;}
        }
        //from!=to
        //vec.clear(pivot);

        //debug
        if(!suppressPrint) System.out.println("Col_VEC: " + vec.toString() +", for pawn " + getBox(c.getRow(), c.getCol()));

        /*****row****/
        //array = getRow(table, c.getRow()); --> array[index][i]
        pivot = c.getCol();

        vec.clear();
        //vec.set(pivot); //IMPORTANTE PER IL PRIMO CASO

        //vado verso destra sulla riga, mi muovo su una riga cambiando righe
        for(int i=pivot+1; i<9; i++){
            if(/*!array.get(i)*/table[c.getRow()][i] == 0) {
                if(i - pivot == 8 && !place_8_steps(c)) continue; //si potrebbe mettere break
                vec.set(i);
                String to = getBox(c.getRow(), i);
                res.add(new Action(from, to, turn));
            }
            else {break;}
        }

        //vado verso sinistra
        for(int k=pivot-1; k>=0; k--){
            if(/*!array.get(k)*/table[c.getRow()][k] == 0) {
                if((pivot - k == 8) && !place_8_steps(c)) continue; //si potrebbe mettere break
                vec.set(k);
                String to = getBox(c.getRow(), k);
                res.add(new Action(from, to, turn));
            }
            else {break;}
        }
        //from!=to
        //vec.clear(pivot);

        //debug
        if(!suppressPrint) System.out.println("Row_VEC: " + vec.toString() +", for pawn " + getBox(c.getRow(), c.getCol()));

        return res;
    }
}
