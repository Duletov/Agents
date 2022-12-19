package second;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

public class FindAverage extends TickerBehaviour {
    private final DefaultAgent agent;

    FindAverage(DefaultAgent agent, long period) {
        super(agent, period);
        this.setFixedPeriod(true);
        this.agent = agent;
    }

    @Override
    protected void onTick() {
        //System.out.println("Agent" + this.agent.getLocalName() + ": tick=" + getTickCount());

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

        for (String agent : this.agent.linkedAgents) {
            AID destination = new AID(agent, AID.ISLOCALNAME);
            msg.addReceiver(destination);
        }

        try {
            msg.setContentObject(this.agent.number + (20 * Math.random() - 10));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.agent.send(msg);

        for(int i = 0; i < this.agent.linkedAgents.size(); i++){
            ACLMessage msgRes = this.agent.receive();
            if (msgRes != null) {
                try {
                    Double element = (Double) msgRes.getContentObject();
                    String sender = msgRes.getSender().getLocalName();

                    if (Math.random() < 0.5) {
                        if (sender.equals("4") && this.agent.id == 3 || sender.equals("3") && this.agent.id == 4 || sender.equals("1") && this.agent.id == 5 || sender.equals("5") && this.agent.id == 1) {
                            this.agent.map.put(Integer.parseInt(sender), 0.0);
                        } else if (element != null) {
                            this.agent.map.put(Integer.parseInt(sender), (this.agent.number - element) * 0.06);
                        }
                    }
                    //System.out.println("Agent" + this.agent.id + ": receive =" + element);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        this.agent.map.forEach((k, v) -> {
            this.agent.number -= v;
        });

        if (getTickCount() == 10 || getTickCount() == 20 || getTickCount() == 50 || getTickCount() == 100) {
            System.out.println("Agent" + this.agent.getLocalName() + ": tick = " + getTickCount() + " , number = " + this.agent.number);
            if(getTickCount() == 100){
                this.agent.doDelete();
                this.stop();
            }
        }

    }
}