package it.unibo.ai.didattica.competition.tablut.AI.test;

public class TestTimer {


    //NB. non termina subito per quanto scritto nel metodo makeDecision di MinMax, leggi li.
    ////executorService.shutdownNow();
    public static void main(String[] args) {

        Test_BlockedTimer minmax = new Test_BlockedTimer();

        //cambiare il timeout tra 1 e 20
        String resultTest = minmax.makeDecision(1);

        System.out.println("risultato= _____" + resultTest + "_____");
    }

}
