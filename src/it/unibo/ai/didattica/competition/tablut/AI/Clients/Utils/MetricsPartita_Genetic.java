package it.unibo.ai.didattica.competition.tablut.AI.Clients.Utils;

public class MetricsPartita_Genetic {

    private boolean draw;
    private boolean victory = false;
    private double opponentPawsEaten = 0;
    private double minePawsLosts = 0;
    private long time = 0;

    public MetricsPartita_Genetic(boolean victory, boolean draw, double opponentPawsEaten, double minePawsLosts, long time) {
        this.victory = victory;
        this.draw = draw;
        this.opponentPawsEaten = opponentPawsEaten;
        this.minePawsLosts = minePawsLosts;
        this.time = time;
    }

    public MetricsPartita_Genetic() {
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public boolean isVictory() {
        return victory;
    }

    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    public double getOpponentPawsEaten() {
        return opponentPawsEaten;
    }

    public void setOpponentPawsEaten(double opponentPawsEaten) {
        this.opponentPawsEaten = opponentPawsEaten;
    }

    public double getMinePawsLosts() {
        return minePawsLosts;
    }

    public void setMinePawsLosts(double minePawsLosts) {
        this.minePawsLosts = minePawsLosts;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "MetricsPartita_Genetic{" +
                "draw=" + draw +
                ", victory=" + victory +
                ", opponentPawsEaten=" + opponentPawsEaten +
                ", minePawsLosts=" + minePawsLosts +
                ", time=" + time +
                '}';
    }
}
