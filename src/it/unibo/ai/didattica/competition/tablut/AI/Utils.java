package it.unibo.ai.didattica.competition.tablut.AI;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Utils {

    //Qualche classe sui bit set, le metto qui per non perderle
    //1- https://docs.oracle.com/javase/7/docs/api/java/util/BitSet.html
    //2- http://simul.iro.umontreal.ca/ssj-2/doc/html/umontreal/iro/lecuyer/util/BitVector.html
    //Per ora meglio la 1. Possibile anche byte[], ma non ha utility così come boolean[]

    BitSet bitSet = new BitSet(9);

    public void doStuff(){
        bitSet.and(new BitSet(9));
        bitSet.stream(); //could be useful
        List<BitSet> matr = new ArrayList<>(9);
        //matr.stream().forEach(p -> p || 1);
    }

    //provando cose a caso
    public static void main(String [] args){
        List<BitSet> matr = new ArrayList<>(9);

        for (int i=0; i<9; i++){
            BitSet bs = new BitSet(9);
            bs.set(0, 8);
            matr.add(bs);
        }
        matr.stream().forEach(p -> p.stream().forEach(System.out::println));

        System.out.println(matr.get(0).get(0));

    }
}
