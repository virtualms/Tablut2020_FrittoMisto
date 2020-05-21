package it.unibo.ai.didattica.competition.tablut.AI;

import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author E.Cerulo, V.M.Stanzione
 *
 */

public class HeuristicFrittoMisto implements Heuristic{

    /***cost***/
    public static final int KING_MANHATTAN = 0;
    public static final int KING_CAPTURED_SIDES = 1;
    public static final int PAWNS_DIFFERENCE = 2;
    public static final int PAWNS_WHITE = 3;
    public static final int PAWNS_BLACK = 6;
    public static final int VICTORY = 5;
    public static final int VICTORY_PATH = 4;

    private final int depthLimit;
    private final double initialBlack;
    private final double initialWhite;
    private Coord castle;
    private List<Coord> citadels;
    private List<Coord> winPos;
    private static double [] weight;

    /***color***/
    private final State.Turn playerColor; //il colore del client
    private final double color;

    /****************WIN***********************/

    public HeuristicFrittoMisto(int initialBlack, int initialWhite, State.Turn playerColor, int depthLimit) {
        this.initialBlack = initialBlack;
        this.initialWhite = initialWhite;
        this.playerColor = playerColor;
        this.depthLimit = depthLimit;

        this.color = ((playerColor == State.Turn.WHITE || playerColor == State.Turn.WHITEWIN) ? 1 : -1);

        initWeights();
        initPos();
    }

    public HeuristicFrittoMisto(State.Turn playerColor, int depthLimit){
        this.initialWhite = 9;
        this.initialBlack = 16;
        this.playerColor = playerColor;
        this.depthLimit = depthLimit;

        this.color = ((playerColor == State.Turn.WHITE || playerColor == State.Turn.WHITEWIN) ? 1 : -1); //eval fatta su bianco, per il nero è a specchio (somma zero)

        initWeights();
        initPos();
    }

    public static void setWeight(double[] weight) {
        HeuristicFrittoMisto.weight = weight;
    }

    private void initWeights(){
        weight = new double[7];

        //double pawnsCoef = (initialBlack) / initialWhite; //(16.0/9.0)

        /*
        weight[KING_MANHATTAN] = 50;  //manhattan
        weight[KING_CAPTURED_SIDES] = -100;  //king capture
        weight[PAWNS_DIFFERENCE] = 100;  //lost pawns
        weight[PAWNS_WHITE] = 100 * pawnsCoef; //white pieces (difference ?)
        weight[VICTORY_PATH] = 300;  //victory path
        weight[VICTORY] = 5000;  //victory
        weight[PAWNS_BLACK] = -100; //black pieces
        */

        //POST GENETIC
        weight[KING_MANHATTAN] = 42;  //manhattan
        weight[KING_CAPTURED_SIDES] = -147;  //king capture
        weight[PAWNS_DIFFERENCE] = -22;  //lost pawns
        weight[PAWNS_WHITE] = 250; //white pieces (difference ?)
        weight[VICTORY_PATH] = 195;  //victory path
        weight[VICTORY] = 5000;  //victory
        weight[PAWNS_BLACK] = -164; //black pieces
    }

    private void initPos(){
        this.winPos = new ArrayList<>();

        this.winPos.add(new Coord(0, 1));
        this.winPos.add(new Coord(0, 2));
        this.winPos.add(new Coord(0, 6));
        this.winPos.add(new Coord(0, 7));

        this.winPos.add(new Coord(8, 1));
        this.winPos.add(new Coord(8, 2));
        this.winPos.add(new Coord(8, 6));
        this.winPos.add(new Coord(8, 7));

        this.winPos.add(new Coord(1, 0));
        this.winPos.add(new Coord(2, 0));
        this.winPos.add(new Coord(6, 0));
        this.winPos.add(new Coord(7, 0));

        this.winPos.add(new Coord(1, 8));
        this.winPos.add(new Coord(2, 8));
        this.winPos.add(new Coord(6, 8));
        this.winPos.add(new Coord(7, 8));

        //castle
        this.castle = new Coord(4,4);

        //citadels
        this.citadels = new ArrayList<>();

        this.citadels.add(new Coord(0, 3));
        this.citadels.add(new Coord(0, 4));
        this.citadels.add(new Coord(1, 4));
        this.citadels.add(new Coord(0, 5));

        this.citadels.add(new Coord(3, 8));
        this.citadels.add(new Coord(4, 8));
        this.citadels.add(new Coord(4, 7));
        this.citadels.add(new Coord(5, 8));

        this.citadels.add(new Coord(8, 3));
        this.citadels.add(new Coord(8, 4));
        this.citadels.add(new Coord(7, 4));
        this.citadels.add(new Coord(8, 5));

        this.citadels.add(new Coord(3, 0));
        this.citadels.add(new Coord(4, 0));
        this.citadels.add(new Coord(4, 1));
        this.citadels.add(new Coord(5, 0));
    }


    /*********************eval********************************/
    public double eval(State state, int depth){

        //double color = ((playerColor == State.Turn.WHITE || playerColor == State.Turn.WHITEWIN) ? 1 : -1);

        //pawns
        List<List<Coord>> pieces = state.getPieces();

        List<Coord> blackPieces = pieces.get(state.BLACK);
        List<Coord> whitePieces = pieces.get(state.WHITE);
        Coord king = pieces.get(state.KING).get(0);

        double V =  weight[KING_MANHATTAN]      * kingManhattan(king)                                   +
                    weight[KING_CAPTURED_SIDES] * kingCapture(king, blackPieces)                        +
                    //weight[PAWNS_DIFFERENCE]    * lostPaws(blackPieces, whitePieces, state.getTurn())   +
                    weight[PAWNS_WHITE]         * whitePieces.size()                                    +
                    weight[VICTORY_PATH]        * victoryPaths(king, blackPieces, whitePieces)          +
                    weight[VICTORY]             * winCondition(state.getTurn(), depth)                  +
                    weight[PAWNS_BLACK]         * blackPieces.size();

        return V * color;
    }

    /******************************************************/



    /**********************FUNC*************************/
    /**0**/
    private double kingManhattan(Coord king){
        //6 massima distanza da win position
        return 6 - winPos.stream().
                mapToDouble(c -> c.distanceFrom(king)).
                min().
                getAsDouble();
    }

    /**1**/
    //Lati accerchiati del re
    private double kingCapture(Coord king, List<Coord> blackPieces){
        double count=0;

        Stream<Coord> s = Stream.concat(blackPieces.stream(), citadels.stream());

        count = s.filter(king::closeTo).count();

        if(king.closeTo(castle)) count++;

        return count;
    }

    /**2**/
    private double lostPaws(List<Coord> black, List<Coord> white, State.Turn turn){
//        double coeff = 16/9; //per equilibrare la situazione di pezzi
//        return - (coeff)*(initialWhite - white.size()) + (initialBlack - black.size());
        return 0;
    }

    /**3**/
    //pezzi bianchi

    /**4**/
    //strade aperte di vittoria per il re
    public double victoryPaths(Coord king, List<Coord> blackPieces, List<Coord> whitePieces) {

        List<Coord> victoryPos = victoryRoads(king);

        if(victoryPos.isEmpty()) return 0;

        double paths=0;
        Optional<Coord> o;
        Supplier<Stream<Coord>> sup1, sup2;

        sup1 = () -> Stream.concat(blackPieces.stream(), whitePieces.stream());
        sup2 = () -> Stream.concat(sup1.get(), citadels.stream());

        //controllo che non ci siano pezzi/cittadelle fra il re e le victorypos
        for(Coord victory : victoryPos){
            o = sup2.get().filter(p -> isBetween(p, victory, king)).findAny();

            if(!o.isPresent()) paths++;
        }
        return paths;
    }

    //piece - victory - king
    private boolean isBetween(Coord p, Coord victory, Coord king){
        double max, min;

        if(p.equals((Coord)king)) return false;
        if(p.equals((Coord)victory)) return true;

        //controllo le colonne
        if(victory.getCol() == king.getCol()){

            if(p.getCol() != king.getCol()) return false;

            max = (victory.getRow() >= king.getRow() ? victory.getRow() : king.getRow());
            min = (victory.getRow() < king.getRow() ? victory.getRow() : king.getRow());

            return p.getRow() > min && p.getRow() < max;
        }

        //controllo le righe
        else if(victory.getRow() == king.getRow()){

            if(p.getRow() != king.getRow()) return false;

            max = (victory.getCol() >= king.getCol() ? victory.getCol() : king.getCol());
            min = (victory.getCol() < king.getCol() ? victory.getCol() : king.getCol());

            return p.getCol() > min && p.getCol() < max;
        }

        return false;
    }

    private List<Coord> victoryRoads(Coord king){

        //tutte le victory pos accessibili dal re
        return winPos.stream().
                filter(w -> (king.getRow() == w.getRow() || king.getCol() == w.getCol())).
                collect(Collectors.toList());
    }

    /***5***/
    //vittoria o sconfitta
    private double winCondition(State.Turn turn, int depth){
        if(turn == State.Turn.WHITEWIN)
            return 1.0 * depthBonus(depth);
        if(turn == State.Turn.BLACKWIN)
            return -1.0 * depthBonus(depth);

        return 0;
    }

    //bonus su distanza da root
    private double depthBonus(int depth){
        //return depth == 0 ? 2 : 1;
        return (double)(depthLimit - depth)/(double)depthLimit + 1.0;
    }

    /**6**/
    //pezzi neri
}
