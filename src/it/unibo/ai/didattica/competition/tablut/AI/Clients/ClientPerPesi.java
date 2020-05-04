package it.unibo.ai.didattica.competition.tablut.AI.Clients;

import it.unibo.ai.didattica.competition.tablut.AI.Clients.Utils.MetricsPartita_Genetic;
import it.unibo.ai.didattica.competition.tablut.AI.HeuristicFrittoMisto;

import java.io.IOException;

public class ClientPerPesi {

    private String player;

    public MetricsPartita_Genetic getMetrics() throws IOException, ClassNotFoundException {

        String[] array = new String[]{player};
//        String[] array = new String[]{"BLACK"};

        return TablutFrittoMistoClient.main(array);
    }



    public ClientPerPesi(String player, // "BLACK" or "WHITE"
                         double manhattanKing,
                         double kingSidesThreatened,
                         double pawnsDifference,
                         double whitePaws,
                         double victoryPath,
                         double victory,
                         double blackPaws
                         ) throws IOException, ClassNotFoundException {

        double[] weights = new double[7];

        weights[HeuristicFrittoMisto.KING_MANHATTAN] = manhattanKing;
        weights[HeuristicFrittoMisto.KING_CAPTURED_SIDES] = kingSidesThreatened;
        weights[HeuristicFrittoMisto.PAWS_DIFFERENCE] = pawnsDifference;
        weights[HeuristicFrittoMisto.PAWS_WHITE] = whitePaws;
        weights[HeuristicFrittoMisto.VICTORY_PATH] = victoryPath;
        weights[HeuristicFrittoMisto.VICTORY] = victory;
        weights[HeuristicFrittoMisto.PAWS_BLACK] = blackPaws;
        HeuristicFrittoMisto.setWeight(weights);

        this.player = player;
    }

}
