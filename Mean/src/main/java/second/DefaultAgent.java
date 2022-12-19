package second;

import jade.core.Agent;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;

public class DefaultAgent extends Agent{
    public ArrayList<String> linkedAgents = new ArrayList<>();
    public double number;
    public int id;
    public HashMap<Integer, Double> map = new HashMap<>();

    @Override
    protected void setup(){
        id = Integer.parseInt(getAID().getLocalName());
        number = Math.random() * 1000;
        System.out.println("Agent " + id + " number " + number);

        for (String neighbour : Graph.getLinks(id)) {
            linkedAgents.add(neighbour);
        }

        addBehaviour(new FindAverage(this, TimeUnit.SECONDS.toMillis(1)));
    }
}
