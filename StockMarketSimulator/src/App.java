import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simulation.SimulationEnvironment;

public class App {
    public static void main(String[] args) throws Exception {

        List<String> stocks = new ArrayList<>();
        List<String> clients = new ArrayList<>();
        List<Map<String,Integer>> os = new ArrayList<>();

        stocks.add("INTC");

        clients.add("c1");
        clients.add("c2");
        clients.add("c3");
        clients.add("c4");
        clients.add("c5");
        clients.add("c6");
        clients.add("c7");
        clients.add("c8");

        Map<String,Integer> c1,c2,c3,c4,c5,c6,c7,c8;

        c1 = new HashMap<>();
        c1.put("INTC",  5);
        c2 = new HashMap<>();
        c2.put("INTC", 10);
        c3 = new HashMap<>();
        c4 = new HashMap<>();
        c5 = new HashMap<>();
        c6 = new HashMap<>();
        c7 = new HashMap<>();
        c8 = new HashMap<>();

        os.add(c1);
        os.add(c2);
        os.add(c3);
        os.add(c4);
        os.add(c5);
        os.add(c6);
        os.add(c7);
        os.add(c8);

        SimulationEnvironment sim = new SimulationEnvironment(8, 1, 60L, stocks ,clients, os,true);

        Thread tsim = new Thread(sim);

        tsim.start();

        tsim.join();
    }

    
}
