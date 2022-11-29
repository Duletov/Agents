package test;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.concurrent.TimeUnit;

public class Rename extends OneShotBehaviour{
    private final DefaultAgent agent;
    Rename(DefaultAgent agent){
        super(agent);
        this.agent = agent;
    }

    @Override
    public void action(){
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

        for (String receiver : this.agent.linkedAgents) {
            msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
        }

        try {
            Integer[] pair = new Integer[2];
            pair[0] = this.agent.newNumber;
            pair[1] = this.agent.id;
            //System.out.println("S " + pair[0] + " " + pair[1]);
            msg.setContentObject(pair);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.agent.messages += this.agent.linkedAgents.size();
        this.agent.send(msg);

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch(InterruptedException e){

        }

        this.agent.linkedAgents.clear();

        while (true) {
            ACLMessage msgRes = this.agent.receive();
            if (msgRes != null) {
                try {
                    Integer[] pair = (Integer[]) msgRes.getContentObject();
                    //System.out.println("R "+ this.agent.newNumber + " " + this.agent.id + " " + pair[0] + " " + pair[1]);
                    if (pair[0] > this.agent.newNumber) {
                        this.agent.senders++;
                    }
                    else if(pair[0] < this.agent.newNumber){
                        this.agent.linkedAgents.add(Integer.toString(pair[1]));
                        this.agent.receiver = Integer.toString(pair[1]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                break;
            }
        }

        //System.out.println("Agent " + this.agent.id + " " + this.agent.senders);
        //for(String nei: this.agent.linkedAgents){
        //    System.out.println("Agent " + this.agent.id + " " + nei);
        //}
        //System.out.println("Agent " + this.agent.id + " " + this.agent.receiver);

        this.agent.addBehaviour(new FindAverage(this.agent, TimeUnit.SECONDS.toMillis(1)));
    }
}