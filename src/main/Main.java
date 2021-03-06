package main;

import agents.AgenteReputacaoCentralizada;
import agents.Comprador;
import agents.Vendedor;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

	private static String hostname = "127.0.0.1";
	private static HashMap<String, ContainerController> containerList = new HashMap<String, ContainerController>();// container's
																													// name
																													// -
																													// container's
																													// ref
	private static List<AgentController> agentList;// agents's ref
	private static Runtime rt;

	public static void main(String[] args) throws StaleProxyException {

		// 1), create the platform (Main container (DF+AMS) + containers +
		// monitoring agents : RMA and SNIFFER)
		rt = emptyPlatform(containerList);

		// 2) create agents and add them to the platform.
		agentList = createAgents(containerList);

		try {
			System.out.println("Press a key to start the agents");
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 3) launch agents
		startAgents(agentList);

	}

	private static Runtime emptyPlatform(HashMap<String, ContainerController> containerList) {

		Runtime rt = Runtime.getRuntime();

		// 1) create a platform (main container+DF+AMS)
		Profile pMain = new ProfileImpl(hostname, 8888, null);
		System.out.println("Launching a main-container..." + pMain);
		AgentContainer mainContainerRef = rt.createMainContainer(pMain); // DF
																			// and
																			// AMS
																			// are
																			// include

		// 2) create the containers
		containerList.putAll(createContainers(rt));

		// 3) create monitoring agents : rma agent, used to debug and monitor
		// the platform; sniffer agent, to monitor communications;
		createMonitoringAgents(mainContainerRef);

		System.out.println("Plaform ok");
		return rt;

	}

	private static HashMap<String, ContainerController> createContainers(Runtime rt) {
		String containerName;
		ProfileImpl pContainer;
		ContainerController containerRef;
		HashMap<String, ContainerController> containerList = new HashMap<String, ContainerController>();// bad
																										// to
																										// do
																										// it
																										// here.

		System.out.println("Launching containers ...");

		// create the container0
		containerName = "container0";
		pContainer = new ProfileImpl(null, 8888, null);
		System.out.println("Launching container " + pContainer);
		containerRef = rt.createAgentContainer(pContainer); // ContainerController
															// replace
															// AgentContainer in
															// the new versions
															// of Jade.
		containerList.put(containerName, containerRef);

		// create the container1
		containerName = "container1";
		pContainer = new ProfileImpl(null, 8888, null);
		System.out.println("Launching container " + pContainer);
		containerRef = rt.createAgentContainer(pContainer); // ContainerController
															// replace
															// AgentContainer in
															// the new versions
															// of Jade.
		containerList.put(containerName, containerRef);

		// create the container2
		containerName = "container2";
		pContainer = new ProfileImpl(null, 8888, null);
		System.out.println("Launching container " + pContainer);
		containerRef = rt.createAgentContainer(pContainer); // ContainerController
															// replace
															// AgentContainer in
															// the new versions
															// of Jade.
		containerList.put(containerName, containerRef);

		System.out.println("Launching containers done");
		return containerList;
	}

	/**
	 * create the monitoring agents (rma+sniffer) on the main-container given in
	 * parameter and launch them. - RMA agent's is used to debug and monitor the
	 * platform; - Sniffer agent is used to monitor communications
	 * 
	 * @param mc
	 *            the main-container's reference
	 * @return a ref to the sniffeur agent
	 */
	private static void createMonitoringAgents(ContainerController mc) {

		System.out.println("Launching the rma agent on the main container ...");
		AgentController rma;

		try {
			rma = mc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
			rma.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
			System.out.println("Launching of rma agent failed");
		}

		System.out.println("Launching  Sniffer agent on the main container...");
		AgentController snif = null;

		try {
			snif = mc.createNewAgent("sniffeur", "jade.tools.sniffer.Sniffer", new Object[0]);
			snif.start();

		} catch (StaleProxyException e) {
			e.printStackTrace();
			System.out.println("launching of sniffer agent failed");

		}

	}

	/**********************************************
	 * 
	 * Methods used to create the agents and to start them
	 * 
	 **********************************************/

	/**
	 * Creates the agents and add them to the agentList. agents are NOT started.
	 * 
	 * @param containerList
	 *            :Name and container's ref
	 * @param sniff
	 *            : a ref to the sniffeur agent
	 * @return the agentList
	 */
	private static List<AgentController> createAgents(HashMap<String, ContainerController> containerList) {
		System.out.println("Launching agents...");
		ContainerController c;
		String agentName;
		List<AgentController> agentList = new ArrayList<AgentController>();

		// Agent0 on container0
		c = containerList.get("container0");

		agentName = "REPUTAÇÃO";
		try {
			Object[] objtab = new Object[1];// used to give informations
			AgentController ag = c.createNewAgent(agentName, AgenteReputacaoCentralizada.class.getName(), objtab);
			agentList.add(ag);
			System.out.println(agentName + " launched");
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		agentName = "MARCELO";
		try {
			Object[] objtab = new Object[1];// used to give informations
			objtab[0] = "pintor muro 50 2 4 0.25";
			AgentController ag = c.createNewAgent(agentName, Vendedor.class.getName(), objtab);
			agentList.add(ag);
			System.out.println(agentName + " launched");
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		agentName = "MARCOS";
		try {
			Object[] objtab = new Object[1];// used to give informations
			objtab[0] = "pintor muro 150 2 4 0.75";
			AgentController ag = c.createNewAgent(agentName, Vendedor.class.getName(), objtab);
			agentList.add(ag);
			System.out.println(agentName + " launched");
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < 1000; i++) {
			agentName = "Comprador" + i;
			try {
				Object[] objtab = new Object[1];// used to give informations
				objtab[0] = "pintor muro 50 2 4"; // to the agent
				AgentController ag = c.createNewAgent(agentName, Comprador.class.getName(), objtab);
				agentList.add(ag);
				System.out.println(agentName + " launched");
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Agents launched...");
		return agentList;
	}

	/**
	 * Start the agents
	 * 
	 * @param agentList
	 */
	private static void startAgents(List<AgentController> agentList) {

		System.out.println("Starting agents...");

		for (final AgentController ac : agentList) {
			try {
				ac.start();
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		System.out.println("Agents started...");
	}

}
