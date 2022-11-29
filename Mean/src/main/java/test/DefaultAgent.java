package test;

import jade.core.Agent;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.Stack;

public class DefaultAgent extends Agent{
    public ArrayList<String> linkedAgents = new ArrayList<>();
    public String receiver;
    private double number;
    public Stack<Double> current = new Stack<Double>();
    public int senders;
    public int oldNumber;
    public int newNumber;
    public int master;
    public int id;
    public int messages;

    @Override
    protected void setup(){
        id = Integer.parseInt(getAID().getLocalName());
        number = Math.random() * 1000;
        System.out.println("Agent " + id + " number " + number);
        senders = 0;
        oldNumber = -1;
        newNumber = id;
        master = id;
        current.push(number);

        for (String neighbour : Graph.getLinks(id)) {
            linkedAgents.add(neighbour);
        }

        addBehaviour(new TopSort(this, TimeUnit.SECONDS.toMillis(1)));
        //this.doDelete();
    }
}
