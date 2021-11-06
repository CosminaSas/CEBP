import java.util.concurrent.ThreadLocalRandom;

import broker.IBroker;
import broker.IBrokerImpl;
import stock.dtos.Transaction;

public class App {
    public static void main(String[] args) throws Exception {
        IBroker broker = new IBrokerImpl();
        Tester test = new Tester();
        test.broker = broker;


        Thread t = new Thread(broker,"Broker");
        t.start();
        
        Thread t2 = new Thread(test, "tester");
        t2.start();


        t2.join();
        t.join();
    }

    public static class Tester implements Runnable{

        IBroker broker;
        int c = 0;

        @Override
        public void run() {
            
            while (true) {
                broker.addTransaction(new Transaction(c++));
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1500));
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        }

    }
}
