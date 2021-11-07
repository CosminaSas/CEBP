import java.util.concurrent.ThreadLocalRandom;

import broker.IBroker;
import broker.IBrokerImpl;
import stock.dtos.Transaction;

public class App {
    public static void main(String[] args) throws Exception {
        IBroker broker = new IBrokerImpl();
        Thread t = new Thread(broker,"Broker");
        t.start();
        Thread.sleep(10000);
        ((IBrokerImpl)broker).setRunning(false);
        t.join();
    }

    
}
