package it.unibo.ai.didattica.competition.tablut.AI;

import it.unibo.ai.didattica.competition.tablut.domain.State;

public class Heuristic_temp {

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
    private double weight[];
    private String playerColor; //il colore del client

    /****************const***********************/
    private final double CAPTURE = 1;
    private final double WIN = 1;
    private final double LOSS = 1;


    public Heuristic_temp(int initialBlack, int initialWhite, String playerColor) {
        this.initialBlack = initialBlack;
        this.initialBlack = initialWhite;
        this.playerColor = playerColor;

        this.weight = new double[6];
        weight[0] = 1;
        weight[1] = 1;
        weight[2] = 1;
        weight[3] = 1;
        weight[4] = 1;
        weight[5] = 1;
    }

    public Heuristic_temp(){
        this.initialWhite = 9;
        this.initialBlack = 16;
    }

    /*****************************************************/
    public double eval(State state){

        int sign = state.getTurn() == State.Turn.WHITE ? 1 : -1;
        int color = playerColor.equalsIgnoreCase("white") ? 1 : -1;

        double V =  weight[0] * kingManhattan() +
                    weight[1] * kingCapture() +
                    weight[2] * remainingPawns() +
                    weight[3] * pawsDifference() +
                    weight[4] * victoryPaths() +
                    weight[5] * eventuali();

        return V * sign * color;
    }

    /***********************FUNC*************************/
    //0
    private double kingManhattan(){
        return 0;
    }

    //1
    private double kingCapture(){
        return 0;
    }

    //2
    private double remainingPawns(){
        return 0;
    }

    //3
    private double pawsDifference(){
        return 0;
    }

    //4
    private double victoryPaths(){
        return 0;
    }

    //5
    private double eventuali(){
        return 0;
    }

    /******************************************************************/
}
