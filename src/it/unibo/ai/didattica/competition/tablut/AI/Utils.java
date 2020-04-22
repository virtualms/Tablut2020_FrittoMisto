package it.unibo.ai.didattica.competition.tablut.AI;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.Game;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class Utils {

    //Qualche classe sui bit set, le metto qui per non perderle
    //1- https://docs.oracle.com/javase/7/docs/api/java/util/BitSet.html
    //2- http://simul.iro.umontreal.ca/ssj-2/doc/html/umontreal/iro/lecuyer/util/BitVector.html
    //Per ora meglio la 1. Possibile anche byte[][], ma non ha utility così come boolean[]


    /*
     Idea: genero una bitMap per capire che mosse posso fare in ogni stato per ogni pedone.
     Necessarie:
       -funzione che restituisce una bitmap da uno stato, in base al colore del player.
        (Se nero posso muovermi nel campo. Problema --> se sono nel campo posso muovermi, appena esco non posso più rientrare!
        Possibile funzione che fa check zone campo e castello)

       -funzione Apply che applica un'azione ad uno stato (copiare movePawn da gameAshtonTablut)

       -funzione che dia la posizione del campo

       -costante del castello

       -funzione controllo isKing

      Possibile sviluppo modulo in C JNI che restituisce insieme di action codificate con [from, to, turn]. Java si limita a prenderle,
      convertirle in Action ed applicarle.
     */

    //Restituisco, nel primo livello, sia stati successori che mosse possibili da fare
    public Map<State, Action> getSuccessorFirstLv(State state) {
        Map<State, Action> res = null;
        // byte[][] table
        //WHITE: w-map1 -->ciclo sullo stato, se incontro un pedone o campo/castello metto un 1 altrimenti uno 0
        //BLACK: b-map1 -->ciclo sullo stato, se incontro un pedone o castello metto un 1 altrimenti uno 0. b-map2 -->idem a w-map1.
        //mentre ciclo salvo le coordinate (utile classe ausiliaria coord) di tutti i pezzi del mio colore che incontro
        //ACTION WHITE: per ogni pezzo(pivot) faccio l'array selezionato negato * array ottenuto come pensato (CellaN = CellaN-1*). Gli uno sono
        //le coordinate delle mosse ammissibili (opero su w-map1)
        //ACTION BLACK: stessa azione di action white. Se il pivot è nel campo opero su b-map1, altrimenti su b-map2

        //Ottengo quindi un Action; from=pivot selezionato, to=coordinata di un uno su array calcolato(quindi punto finale)
        //Apply(movePawn) dell'action sullo stato

        //Esempio algoritmo
        //byte[]  array = |0|1|0|0|1|0|0|1|0| eg.
        //Prendo come pivot p=5; |0|1|0|0|'1'|0|0|1|0|
        /*
        p=5
        byte[] vec = new byte[9]

        //check se vado fuori
        for(i=p+1; i<9; i++){
              vec[i] = NOT vec[i-1];
        }

        //check se vado fuori
        for(k=p-1; k>0; i--){
            vec[k] = NOT vec[k+1];
        }

        res_vec = (NOT array AND vec)

        for(j=0; j<9; j++){
            if(res_vec[j] == 1)
                action = new Action(p, j, Turn)    //from - to
        }

        map.add(movePawn(state_father, action), action) oppure list.add(movePawn(state_father, action)

         */
        return res;
    }

    public List<State> getSuccessor(State state) {
        List<State> res = null;

        return res;
    }

    public Utils(){}

    /******************Ausiliari*********************/
    //liste da inserire sopra, clean a ogni interazione
    private List<Coord> whitePieces = new ArrayList<>();
    private List<Coord> blackPieces = new ArrayList<>();
    private List<Action> possibleActions = new LinkedList<>();
    private List<State> possibleStates = new LinkedList<>();

    public boolean isCastle(int r, int c) {
        return (r == 4 && c == 4);
    }

    /******CAMP OR KING********/
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
    public byte[][] w_map1(State state) {
        //WHITE: w-map1 -->ciclo sullo stato, se incontro un pedone o campo/castello metto un 1 altrimenti uno 0
        State.Pawn[][] board = state.getBoard();
        byte[][] bytes = new byte[9][9];

        int r, c;
        for (r = 0; r < 9; r++) {
            for (c = 0; c < 9; c++) {
                if (board[r][c].equals(State.Pawn.EMPTY) && !isCamp(r, c))
                    bytes[r][c] = 0;
                else {
                    bytes[r][c] = 1;

                    //mentre ciclo salvo le coordinate (utile classe ausiliaria coord) di tutti i pezzi del mio colore che incontro
                    if (/*state.getTurn().equals(State.Turn.WHITE) &&*/ board[r][c].equals(State.Pawn.WHITE) || board[r][c].equals(State.Pawn.KING))
                        whitePieces.add(new Coord(r, c));
                }
            }
        }

        return bytes;
    }

    public byte[][] b_map2_camp(State state) {
        //BLACK: b-map1 -->ciclo sullo stato, se incontro un pedone o castello T metto un 1 altrimenti uno 0
        State.Pawn[][] board = state.getBoard();
        byte[][] bytes = new byte[9][9];

        int r, c;
        for (r = 0; r < 9; r++) {
            for (c = 0; c < 9; c++) {
                if (board[r][c].equals(State.Pawn.EMPTY))
                    //TODO NON E' VERO, POSSO MUOVERMI SOLO NEL MIO CAMPO!!! Posso aggiungere un controllo che la distanza da from to < 8
                    //impossibile per qualsiasi mossa, anche per le pedine centrali
                    bytes[r][c] = 0;
                else {
                    bytes[r][c] = 1;

                    if (/*state.getTurn().equals(State.Turn.WHITE) &&*/ board[r][c].equals(State.Pawn.BLACK))
                        blackPieces.add(new Coord(r, c));
                }
            }
        }

        return bytes;
    }

    //idem w_map1
    public byte[][] b_map1(State state) {
        //ciclo sullo stato, se incontro un pedone o campo/castello metto un 1 altrimenti uno 0
        State.Pawn[][] board = state.getBoard();
        byte[][] bytes = new byte[9][9];

        int r, c;
        for (r = 0; r < 9; r++) {
            for (c = 0; c < 9; c++) {
                if (board[r][c].equals(State.Pawn.EMPTY) && !isCamp(r, c))
                    bytes[r][c] = 0;
                else {
                    bytes[r][c] = 1;

                    //già fatto
                    //if(/*state.getTurn().equals(State.Turn.WHITE) &&*/ board[r][c].equals("B"))
                    //    whitePieces.add(new Coord(r,c));
                }
            }
        }

        return bytes;
    }


    public void printboard( byte[][] a){
        for (int i=0; i<9; i++){
            for (int k=0; k<9; k++){
                System.out.print(a[i][k] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }
    public void getSuccessorFirstLv_proto(State state) {
        //ACTION WHITE: per ogni pezzo(pivot) faccio l'array selezionato negato * array ottenuto come pensato (CellaN = CellaN-1*). Gli uno sono
        //le coordinate delle mosse ammissibili (opero su w-map1)
        whitePieces.clear();
        blackPieces.clear();
        possibleActions.clear();
        possibleStates.clear();

        byte[][] table = null;
        byte[][] table_camp = null;
        /***white***/
        if (state.getTurn().equals(State.Turn.WHITE)) {
            table = w_map1(state);
            printboard(table);

            for(Coord c : whitePieces){
                try {
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
            //TODO COMPATTARLE
            table_camp = b_map2_camp(state);
            table = b_map1(state);

            printboard(table);
            printboard(table_camp);
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

    }


    /****Pivoting***/
    private  BitSet getColumn(byte[][] array, int index){
        BitSet column = new BitSet(9); // Here I assume a rectangular 2D array!
        for(int i=0; i<array[0].length; i++){
             if(array[i][index] == 1)  column.set(i);
        }
        return column;
    }

    private  BitSet getRow(byte[][] array, int index){
        BitSet row = new BitSet(9); // Here I assume a rectangular 2D array!
        for(int i=0; i<array[0].length; i++){
            if(array[index][i] == 1)  row.set(i);
        }
        return row;
    }

    //Presa da State
    public String getBox(int row, int column) {
        String ret;
        char col = (char) (column + 97);
        ret = col + "" + (row + 1);
        return ret;
    }

    //places where you can move 8 steps
    private boolean place_8_steps(Coord c){
        return  (c.getRow() == 2 && c.getCol() == 0) ||
                (c.getRow() == 0 && c.getCol() == 2) ||
                (c.getRow() == 6 && c.getCol() == 0) ||
                (c.getRow() == 8 && c.getCol() == 2) ||
                (c.getRow() == 8 && c.getCol() == 6) ||
                (c.getRow() == 6 && c.getCol() == 8) ||
                (c.getRow() == 2 && c.getCol() == 8) ||
                (c.getRow() == 0 && c.getCol() == 6);
    }

    private List<Action> pivoting(Coord c, byte[][] table, State.Turn turn) throws IOException {

        List<Action> res = new LinkedList<>();
        //vettore originale
        BitSet array = null;
        //vettore risultati
        BitSet vec = new BitSet(9);
        int pivot = -1;

        /*****column*****/
        array = getColumn(table, c.getCol());
        String from = getBox(c.getRow(), c.getCol());
        //System.out.println("       PIVOTING.   With c=" + c.toString() + " and array=" + array.toString());

        pivot = c.getRow();
        vec.set(pivot); //IMPORTANTE PER IL PRIMO CASO

        //vado verso su, mi muovo su una colonna cambiando righe
        for(int i=pivot+1; i<9; i++){
            if(/*vec.get(i-1) &&*/ !array.get(i)) {
                if(i - pivot == 8 && !place_8_steps(c)) continue; //TODO E' UNA MERDA MA SI VEDA b_map2_camp, serve ad evitare alcune mosse illegali/
                vec.set(i);
                String to = getBox(i, c.getCol());
                res.add(new Action(from, to, turn));
            }
            else {break;}
        }

        //vado verso giù, mi muovo su una colonna cambiando righe
        for(int k=pivot-1; k>0; k--){
            if(/*vec.get(k+1) &&*/ !array.get(k)) {
                if(pivot - k == 8 && !place_8_steps(c)) continue; //TODO E' UNA MERDA MA SI VEDA b_map2_camp, serve ad evitare alcune mosse illegali/
                vec.set(k);
                String to = getBox(k, c.getCol());
                res.add(new Action(from, to, turn));
            }
            else {break;}
        }
        //from!=to
        vec.clear(pivot);

        System.out.println("C_VEC: " + vec.toString() +", for pawn " + getBox(c.getRow(), c.getCol()));
        /*****row****/
        array = getRow(table, c.getRow());
        pivot = c.getCol();

        vec.clear();
        vec.set(pivot); //IMPORTANTE PER IL PRIMO CASO

        //vado verso destra, mi muovo su una colonna cambiando righe
        for(int i=pivot+1; i<9; i++){
            if(/*vec.get(i-1) &&*/ !array.get(i)) {
                if(i - pivot == 8 && !place_8_steps(c)) continue; //TODO E' UNA MERDA MA SI VEDA b_map2_camp, serve ad evitare alcune mosse illegali/
                vec.set(i);
                String to = getBox(c.getRow(), i);
                res.add(new Action(from, to, turn));
            }
            else {break;}
        }

        //vado verso sinistra, mi muovo su una colonna cambiando righe
        for(int k=pivot-1; k>0; k--){
            if(/*vec.get(k+1) &&*/ !array.get(k)) {
                if(pivot - k == 8 && !place_8_steps(c)) continue; //TODO E' UNA MERDA MA SI VEDA b_map2_camp, serve ad evitare alcune mosse illegali/
                vec.set(k);
                String to = getBox(c.getRow(), k);
                res.add(new Action(from, to, turn));
            }
            else {break;}
        }
        //from!=to
        vec.clear(pivot);

        System.out.println("r_VEC: " + vec.toString() +", for pawn " + getBox(c.getRow(), c.getCol()));
        //finito, torno il risultato per il pedone il coordinate c

        return res;
    }

    public State movePawn(Game game, State state, Action a) throws Exception{
        game.checkMove(state.clone(), a);
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

    public List<Action> getA(){
        return this.possibleActions;
    }
}
