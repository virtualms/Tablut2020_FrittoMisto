package it.unibo.ai.didattica.competition.tablut.AI;


//classe abbandonata in favore dell'interfaccia callable per minmax (guardare AI.test.TestTimer)
public class Timer implements Runnable {

    private Thread threadToStop;
    private int timer;

    public Timer(int timer) {
        this.timer = timer;
    }

    public Timer() {
        timer = 60;
    }


    public void startCountDown(Thread threadToStop, int timerOut){
        timer = timerOut;
        this.threadToStop = threadToStop;
        new Thread(this).start();
    }

    public void startCountDown(Thread threadToStop){
        this.threadToStop = threadToStop;
        new Thread(this).start();
    }


    @Override
    public void run() {
        try {
            Thread.sleep(timer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(threadToStop.isAlive())
            threadToStop.interrupt();

    }
}
