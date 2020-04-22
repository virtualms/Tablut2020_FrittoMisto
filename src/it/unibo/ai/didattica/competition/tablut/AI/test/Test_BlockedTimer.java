package it.unibo.ai.didattica.competition.tablut.AI.test;

import java.util.concurrent.*;


public final class Test_BlockedTimer implements Callable<String> {

    //here to testing
    //TODO: Se faccio questo campo statico, ogni istanza di Test_BlockedTimer, lavora sugli stessi dati, giusto?
    // Va bene così, non statico. Right?
    private String test_bestResult_finOra = "iniziato";

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public Test_BlockedTimer() {
    }


    public String makeDecision(int timeOut) {

        //metto in esecuzione il task (viene chiamata "call()")
        Future<String> risultato = executorService.submit(this);

        String result = "iniziato";
        try {
            result = risultato.get(timeOut, TimeUnit.SECONDS);
        } catch (TimeoutException e) {

            //importante altrimenti il thread che si occupa della call() continua ad eseguire
            risultato.cancel(true);

            //Questo è il metodo che fa terminare il thread che lavora sui callable.
            //Decommentato altrimenti non potrebbero esserci chiamate successive di questo metodo.
            //In alternativa si potrebbe instanziare un ExecutorService ad ogni chiamata, ma per me è inutile,
            //inoltre in questo modo si ha sempre un pool di thread pronti ad eseguire. Se venisse instanziato ogni volta,
            //i thread verrebbero inizializzati ogni volta con perdita di prestazioni.
            //executorService.shutdownNow();

            return test_bestResult_finOra;

        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String call() throws Exception {
        long i = 0;
        while (i != 20) {
            i++;
            Thread.sleep(1000);
            System.out.println("ho dormito " + i + " secondi");
            if (i == 5) {
                test_bestResult_finOra = " ho superato 5 ";
            }
            if (i == 10) {
                test_bestResult_finOra = " ho superato 10 ";
            }
            if (i == 15) {
                test_bestResult_finOra = " ho superato 15 ";
            }
        }
        return test_bestResult_finOra;

    }
}