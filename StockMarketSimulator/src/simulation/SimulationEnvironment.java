package simulation;
import client.Client;
import clientbroker.ICBrokerImpl;
import stock.Stock;

import broker.IBroker;
import broker.IBrokerImpl;

public class SimulationEnvironment {

    private int client;
    private int stock;
    private long time;
    

    public SimulationEnvironment (int client, int stock, long time) {
        this.client = client;
        this.stock = stock;
        this.time = time;
    }

    //cat timp

    public void run() {

		Client[] clientarray = new Client [client];
        Stock[] stockarray = new Stock[stock];
        Thread[] client_threads = new Thread[client];

        IBroker ibroker = new IBrokerImpl();

        for (int i = 0; i< client; i++) {
            clientarray[i] = new Client(i + "", new ICBrokerImpl(ibroker, i + ""));
            client_threads[i] = new Thread(clientarray[i]);
        }
	}
}
