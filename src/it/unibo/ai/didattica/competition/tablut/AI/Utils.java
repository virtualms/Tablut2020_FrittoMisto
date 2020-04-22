package it.unibo.ai.didattica.competition.tablut.AI;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

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
    public Map<State, Action> getSuccessorFirstLv(State state){
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

        check se vado fuori
        for(k=p-1; k>0; i--){
            vec[k] = NOT vec[k+1];
        }

        res_vec = (NOT array * vec)

        for(j=0; j<9; j++){
            if(res_vec[j] == 1)
                action = new Action(p, j, Turn)    //from - to
        }

        map.add(movePawn(state_father, action), action) oppure list.add(movePawn(state_father, action)

         */
        return res;
    }

    public List<State> getSuccessor(State state){
        List<State> res = null;

        return res;
    }
}
