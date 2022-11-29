package test;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class FindAverage extends TickerBehaviour{
    private final DefaultAgent agent;
    FindAverage(DefaultAgent agent, long period){
        super(agent, period);
        this.setFixedPeriod(true);
        this.agent = agent;
    }

    @Override
    protected void onTick() {
        //System.out.println("Agent" + this.agent.getLocalName() + ": tick=" + getTickCount());

        if((!this.agent.current.empty()) && (this.agent.master != this.agent.id)){
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

            msg.addReceiver(new AID(this.agent.receiver, AID.ISLOCALNAME));

            ArrayList<Double> pckg = new ArrayList<>();

            while (!this.agent.current.empty()){
                double temp = this.agent.current.pop();
                pckg.add(temp);
                System.out.println("Agent" + this.agent.id + ": send =" + temp);
            }

            try {
                msg.setContentObject(pckg);
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.agent.messages++;
            this.agent.send(msg);
        }

        while (true) {
            ACLMessage msgRes = this.agent.receive();
            if (msgRes != null) {
                try {
                    if(msgRes.getContent().equals("terminate")){
                        this.agent.senders--;
                    }
                    else{
                        ArrayList<Double> receivedNumber = (ArrayList<Double>) msgRes.getContentObject();
                        if (receivedNumber != null) {
                            for(Double element: receivedNumber){
                                this.agent.current.push(element);
                                System.out.println("Agent" + this.agent.id + ": receive =" + element);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                break;
            }
        }

        if((this.agent.current.empty()) && (this.agent.senders == 0)){
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

            for (String receiver : this.agent.linkedAgents) {
                msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
            }

            msg.setContent("terminate");
            this.agent.messages += this.agent.linkedAgents.size();
            this.agent.send(msg);
            System.out.println("Agent" + this.agent.id + ": terminated with " + this.agent.messages + " messages");
            this.agent.doDelete();
            this.stop();
        }

        if((this.agent.master == this.agent.id) && (this.agent.senders == 0)){
            double sum = 0;
            int num = this.agent.current.size();
            while (!this.agent.current.empty()){
                sum += this.agent.current.pop();
            }
            sum /= num;
            System.out.println("Average " + sum);
            System.out.println("Agent" + this.agent.id + ": terminated with " + this.agent.messages + " messages");
            this.agent.doDelete();
            this.stop();
        }
    }
}
