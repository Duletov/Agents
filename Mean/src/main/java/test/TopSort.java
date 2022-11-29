package test;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.concurrent.TimeUnit;

public class TopSort extends TickerBehaviour{
    private final DefaultAgent agent;
    TopSort(DefaultAgent agent, long period){
        super(agent, period);
        this.setFixedPeriod(true);
        this.agent = agent;
    }

    @Override
    protected void onTick() {
        //System.out.println("Agent" + this.agent.getLocalName() + ": tick=" + getTickCount());
        if (this.agent.oldNumber != this.agent.newNumber) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

            for (String receiver : this.agent.linkedAgents) {
                msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
            }

            Integer[] oMsg = new Integer[2];
            oMsg[0] = this.agent.master;
            oMsg[1] = this.agent.newNumber + 1;

            try {
                msg.setContentObject(oMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.agent.messages += this.agent.linkedAgents.size();
            this.agent.send(msg);

            this.agent.oldNumber = this.agent.newNumber;
        }

        while (true) {
            ACLMessage msgRes = this.agent.receive();
            if (msgRes != null) {
                try {
                    Integer[] receivedNumber = (Integer[]) msgRes.getContentObject();
                    if (receivedNumber != null) {
                        if (this.agent.master > receivedNumber[0]) {
                            this.agent.master = receivedNumber[0];
                            this.agent.newNumber = receivedNumber[1];
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
        if(getTickCount() > 7){
            //System.out.println("Agent " + this.agent.id + " new number " + this.agent.newNumber);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch(InterruptedException e){

            }
            this.agent.addBehaviour(new Rename(this.agent));
            this.stop();
        }
    }
}