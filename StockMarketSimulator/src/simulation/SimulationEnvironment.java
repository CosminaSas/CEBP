package simulation;

import client.Client;
import clientbroker.ICBrokerImpl;
import common.Logger;
import common.OfferType;
import stock.Stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import broker.IBroker;
import broker.IBrokerImpl;

public class SimulationEnvironment implements Runnable{

    private int client;
    private int stock;
    private long timer;
    private List<String> clientIDs;
    private List<String> stockIDs;
    private boolean output;
    private List<Map<String,Integer>> ownedStocks;

    public SimulationEnvironment(int client, int stock, long timer, List<String> stockIDs, List<String> clientIDs,List<Map<String,Integer>> ownedStocks,boolean output) {
        this.client = client;
        this.stock = stock;
        this.timer = timer;
        this.clientIDs = clientIDs;
        this.stockIDs = stockIDs;
        this.ownedStocks = ownedStocks;
        this.output = output;
    }

    @Override
    public void run() {

        Client[] client_array = new Client[client];
        Stock[] stock_array = new Stock[stock];
        Thread[] client_threads = new Thread[client];
        Thread[] stock_threads = new Thread[stock];

        IBroker ibroker = new IBrokerImpl();

        for (int i = 0; i < client; i++) {
            client_array[i] = new Client(clientIDs.get(i), new ICBrokerImpl(ibroker, clientIDs.get(i)),ownedStocks.get(i));
            client_threads[i] = new Thread(client_array[i]);
        }

        for (int i = 0; i < stock; i++) {

            stock_array[i] = new Stock(stockIDs.get(i));
            stock_threads[i] = new Thread(stock_array[i]);
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

        Logger.log("SIM","simulation started");

        while (endTime - startTime < timer * 1000) {
            endTime = System.currentTimeMillis();
        }

        //map client
        //initializare cu id 

        Logger.log("SIM","is stopped...");

        for (Client c : client_array) {
            c.setRunning(false);
        }

        for (Stock s : stock_array) {
            s.setRunning(false);
        }

        for (Thread t : client_threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Thread t : stock_threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        
        for (Client client : client_array) {
            client.printInfo();
        }



    }

}
