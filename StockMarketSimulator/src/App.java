import java.util.concurrent.ThreadLocalRandom;

import broker.IBroker;
import broker.IBrokerImpl;
import client.Client;
import clientbroker.ICBrokerImpl;
import common.OfferType;
import stock.Stock;
import stock.dtos.Transaction;

public class App {
    public static void main(String[] args) throws Exception {


        IBroker broker = new IBrokerImpl();
        Client c1 = new Client("id 1", new ICBrokerImpl(broker,"id 1"));
        Client c2 = new Client("id 1", new ICBrokerImpl(broker,"id 2"));

        Stock s1 = new Stock("s1");
        Stock s2 = new Stock("s2");
        Stock s3 = new Stock("s3");
        Stock s4 = new Stock("s4");
        Stock s5 = new Stock("s5");

        c1.getStocks();
        broker.subscribe(s1);
        c1.getStocks();
        broker.subscribe(s2);
        c1.getStocks();
        broker.subscribe(s3);
        c1.getStocks();
        broker.subscribe(s4);
        c1.getStocks();
        broker.subscribe(s5);
        c1.getStocks();
        
        c1.getBuyOffers(s1.getID());
        c1.addOffer(s1.getID(), 2, 3.5, OfferType.BUY);
        c1.getBuyOffers(s1.getID());

        c1.addOffer(s1.getID(), 3, 3.5, OfferType.BUY);
        c1.addOffer(s1.getID(), 4, 3.5, OfferType.BUY);
        c1.addOffer(s1.getID(), 5, 3.5, OfferType.BUY);
        c1.addOffer(s1.getID(), 6, 3.5, OfferType.BUY);
        c1.getBuyOffers(s1.getID());

        c2.getBuyOffers(s1.getID());

        c1.addOffer(s1.getID(), 6, 3.5, OfferType.SELL);
        c1.getSellOffers(s1.getID());
    }

    
}
