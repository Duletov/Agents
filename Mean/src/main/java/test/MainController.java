package test;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class MainController {
    private static final int numberOfAgents = 5;

    void initAgents() {
        Runtime rt = Runtime.instance();

        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.MAIN_PORT, "10098");
        p.setParameter(Profile.GUI, "true");
        ContainerController cc = rt.createMainContainer(p);


        try{
            for(int i=1; i <= MainController.numberOfAgents;i++){
                AgentController agent = cc.createNewAgent(Integer.toString(i), "test.DefaultAgent", null);
                agent.start();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
