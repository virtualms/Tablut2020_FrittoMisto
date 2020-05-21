package it.unibo.ai.didattica.competition.tablut.AI;

import it.unibo.ai.didattica.competition.tablut.domain.State;

/**
 *
 * @author E.Cerulo, V.M.Stanzione
 *
 */

public interface Heuristic {
    double eval(State state, int depth);
}
