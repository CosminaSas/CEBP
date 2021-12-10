package simulation;

import client.Client;
import clientbroker.ICBrokerImpl;
import common.OfferType;
import stock.Stock;

import broker.IBroker;
import broker.IBrokerImpl;

public class SimulationEnvironment {

    private int client;
    private int stock;
    private long timer;
    private boolean exit = false;

    public SimulationEnvironment(int client, int stock, long timer) {
        this.client = client;
        this.stock = stock;
        this.timer = timer;
    }

    public void run() {

        Client[] client_array = new Client[client];
        Stock[] stock_array = new Stock[stock];
        Thread[] client_threads = new Thread[client];
        Thread[] stock_threads = new Thread[stock];

        IBroker ibroker = new IBrokerImpl();

        for (int i = 0; i < client; i++) {
            client_array[i] = new Client(i + "", new ICBrokerImpl(ibroker, i + ""));
            client_threads[i] = new Thread(client_array[i]);
        }

        for (int i = 0; i < stock; i++) {

            stock_array[i] = new Stock(i + "");
            stock_threads[i] = new Thread();
            ibroker.subscribe(stock_array[i]);
        }

        for (Thread t : stock_threads) {
            t.start();
        }

        for (Thread t : client_threads) {
            t.start();
        }

        long startTime = System.currentTimeMillis();
        long endTime = startTime;

        while (endTime - startTime < timer * 1000) {
            System.out.println("is running...");
            endTime = System.currentTimeMillis();
        }

        System.out.println("is stopped...");

        for (Client c : client_array) {
            c.setRunning(false);
        }

        for (Stock s : stock_array) {
            s.setRunning(false);
        }

        for (Thread t : stock_threads) {
            try {
                t.join();
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }

        for (Thread t : client_threads) {
            try {
                t.join();
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        Stock s = new Stock("INTC");

        IBroker broker = new IBrokerImpl();

        Client c1 = new Client("1", new ICBrokerImpl(broker, "1"));
        Client c2 = new Client("2", new ICBrokerImpl(broker, "2"));

        broker.subscribe(s);

        c1.addOffer("INTC", 25, 100, OfferType.SELL);
        c2.addOffer("INTC", 25, 100, OfferType.BUY);

        s.cyclic();


    }




}
