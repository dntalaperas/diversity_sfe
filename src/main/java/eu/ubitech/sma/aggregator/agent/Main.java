/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ubitech.sma.aggregator.agent;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Chris Paraskeva
 */
public class Main {

    static String[] agentNamesList = {"Facebook", "Twitter"};

//    public static void main(String[] args) {
//        args = new String[]{"facebook"};
//        //Create an agent list to hold all social media agents
//        List<AgentService> agentsList = new ArrayList<AgentService>();
//
//        //Add a Facebook Agent to the list
//        if (args.length == 0) {
//            int iter = 0;
//            System.out.println("No arguments gave... Loading all Social Media Agents\n\n");
//            for (String agentName : agentNamesList) {
//
//                System.out.println("[i] Creating Agent : " + agentName.toLowerCase());
//                agentsList.add(AgentFactory.getAgentByName(agentName));
//                System.out.println("[i] " + agentName + "Agent started!");
//                agentsList.get(iter).getPageFeed("",0);
//                iter++;
//
//                //System.out.println("[i] Agent created with AppID :" + agentsList.get(agentsList.size()). );
//            }
//
//        } else {
//            for (int i = 0; i < args.length; i++) {
//                System.out.println("[i] Argument : " + args[i]);
//                if (args[i].equalsIgnoreCase("facebook") || args[i].equalsIgnoreCase("twitter")) {
//                    System.out.println("[i] Creating Agent : " + args[i].toLowerCase());
//                    System.out.println("[i] Facebook Agent started!");
//                    agentsList.add(AgentFactory.getAgentByName(args[i]));
//                    System.out.println("[i] " + args[i] + "Agent started!");
//                    agentsList.get(i).getPageFeed("",0);
//                    //agentsList.get(i).isValidPage("myfcmb");
//
//                } else {
//                    System.err.println("No such agent exist.. \nAvailable Agents : ");
//                    printAllAgents();
//                }
//            }
//        }
//
//    }

    public static void printAllAgents() {
        for (String agentName : agentNamesList) {
            System.out.print(agentName + " ");
        }
        System.out.print("\n");
    }

}
