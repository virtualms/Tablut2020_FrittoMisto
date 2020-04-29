package it.unibo.ai.didattica.competition.tablut.AI;

import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HeuristicFrittoMisto implements Heuristic{

    /* FUNZIONE DI VALUTAZIONE V.
     * Funzione di valutazione a somma zero( F_bianco + F_nero deve risultare uguale a 0 o si rischiano squilibri ).
     * V=sum(f_i * w_i)
     *
     * 0. Distanza del re da punti di vittoria (distanza di manhattan)
     * 1. Lati accerchiati del re
     * 2. Pedine iniziali - pedine finali
     * 3. Differenza di pedine fra nero e bianco p_b - p_n*(16/9)
     * 4. Vie di vittoria per il re (corridoi liberi che portano alla vittoria
     * 5. Altre idee?
     *
     * Funzione per calcolare la lista dei pezzi da uno stato.
     * Distanza (in Coord) da punti di vittoria
     */

    private int initialBlack;
    private int initialWhite;
    private Coord castle;
    private List<Coord> citadels;
    private List<Coord> winPos;
    private double weight[];
    private State.Turn playerColor; //il colore del client

    /****************const***********************/
    private final double CAPTURE = 1;
    private final double WIN = 5000;
    private final double LOSS = -5000;


    public HeuristicFrittoMisto(int initialBlack, int initialWhite, State.Turn playerColor) {
        this.initialBlack = initialBlack;
        this.initialBlack = initialWhite;
        this.playerColor = playerColor;

        initWeights();
        initPos();
    }

    public HeuristicFrittoMisto(State.Turn playerColor){
        this.initialWhite = 9;
        this.initialBlack = 16;
        this.playerColor = playerColor;

        initWeights();
        initPos();
    }

    private void initWeights(){
        this.weight = new double[7];
        weight[0] = 50;  //manhattan
        weight[1] = -100;  //king capture
        weight[2] = 100;  //lost pawns
        weight[3] = 100 * (16/9); //white pieces difference
        weight[4] = 500;  //victory path
        weight[5] = WIN;  //victory
        weight[6] = -100; //black pieces
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

    /*****************************************************/
    public double eval(State state){

        int color = ((playerColor == State.Turn.WHITE || playerColor == State.Turn.WHITEWIN) ? 1 : -1); //eval fatta su bianco, per il nero è a specchio (somma zero)

        //Liste globali (?) l'assunto che heuristic non sia chiamata più volte in contemporanea è giusto?
        List<List<Coord>> pieces = state.getPieces();

        List<Coord> blackPieces = pieces.get(state.BLACK);
        List<Coord> whitePieces = pieces.get(state.WHITE);
        Coord king = pieces.get(state.KING).get(0);

        double V =  weight[0] * kingManhattan(king)                                    +
                    weight[1] * kingCapture(king, blackPieces)                         +
                    weight[2] * lostPaws(blackPieces, whitePieces, state.getTurn())    +
                    weight[3] * whitePieces.size()                                     +
                    weight[4] * victoryPaths(king, blackPieces, whitePieces)           +
                    weight[5] * winCondition(state.getTurn())                          +
                    weight[6] * blackPieces.size();

        return V * color;
    }

    /******************************************************/

    /***********************FUNC*************************/
    //0
    private double kingManhattan(Coord king){
        //6 massima distanza da win position
        return 6 - winPos.stream().
                mapToDouble(c -> c.distanceFrom(king)).
                min().
                getAsDouble();
    }

    //1
    private double kingCapture(Coord king, List<Coord> blackPieces){
        double count=0;

        count = blackPieces.stream().filter(b -> king.closeTo(b)).count();

        count = count + citadels.stream().filter(c -> king.closeTo(c)).count();

        if(king.closeTo(castle)) count++;

        return count;
    }

    //2
    //TODO DA RENDERE UNA SOMMA SENZA COEFF COME PER GLI ALTRI
    private double lostPaws(List<Coord> black, List<Coord> white, State.Turn turn){
        double coeff = 16/9; //per equilibrare la situazione di pezzi
        return - (coeff)*(initialWhite - white.size()) + (initialBlack - black.size());
    }

//    //3
//    private double pawsDifference(int blackPieces, int whitePieces){
//        double coeff = 16/9; //per equilibrare la situazione di pezzi
//        return whitePieces * coeff - blackPieces;
//    }

    //4
    private double victoryPaths(Coord king, List<Coord> blackPieces, List<Coord> whitePieces) {

        List<Coord> victoryPos = isVictoryRoad(king);

        if(victoryPos.isEmpty()) return 0;

        double paths=0;
        Stream<Coord> s = Stream.concat(blackPieces.stream(), whitePieces.stream());
        Optional<Coord> o;
        for(Coord victory : victoryPos){
            s = Stream.concat(blackPieces.stream(), whitePieces.stream());
            o = s.filter(p -> isBetween(p, victory, king)).findAny();

            if(!o.isPresent()) paths++;
        }
        return paths;
    }

    private boolean isBetween(Coord piece, Coord victory, Coord king){
        double max, min;

        if(victory.getCol() == king.getCol()){
            max = (victory.getRow() >= king.getRow() ? victory.getRow() : king.getRow());
            min = (victory.getRow() < king.getRow() ? victory.getRow() : king.getRow());

            return piece.getRow() >= min && piece.getRow() <= max;
        }

        else if(victory.getRow() == king.getRow()){
            max = (victory.getCol() >= king.getCol() ? victory.getCol() : king.getCol());
            min = (victory.getCol() < king.getCol() ? victory.getCol() : king.getCol());

            return piece.getCol() >= min && piece.getCol() <= max;
        }

        return false;
    }

    private List<Coord> isVictoryRoad(Coord king){
        List<Coord> posSelected = winPos.stream().
                filter(w -> king.getRow() == w.getRow() || king.getCol() == w.getCol()).
                collect(Collectors.toList());

        return posSelected;
    }

    //5
    private double winCondition(State.Turn turn){
        if(turn == State.Turn.WHITEWIN)
            return 1;
        if(turn == State.Turn.BLACKWIN)
            return -1;

        return 0;
    }

    /******************************************************************/
}
