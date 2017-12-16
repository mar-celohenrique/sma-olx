package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import model.Reputacao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Sancao extends Agent {

    private Map<AID, Reputacao> punicoes = new TreeMap<>();
    private List<AID> expulsoes = new ArrayList<>();
    private List<AID> agentesAvisados = new ArrayList<>();

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new RecebeJulgamentoBehavior());
        addBehaviour(new EnviaSancaoBehavior());
    }


    private class RecebeJulgamentoBehavior extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = myAgent.receive(mt);

            if (null != msg) {

                try {
                    Map<AID, Reputacao> julgamento = (Map<AID, Reputacao>) msg.getContentObject();
                    for (Map.Entry<AID, Reputacao> entry : julgamento.entrySet()) {
                        if (!punicoes.containsKey(entry.getKey())) {
                            punicoes.put(entry.getKey(), entry.getValue());
                        } else {
                            expulsoes.add(entry.getKey());
                        }
                    }
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private class EnviaSancaoBehavior extends CyclicBehaviour {

        @Override
        public void action() {
            if (!punicoes.isEmpty()) {

                ACLMessage aviso = new ACLMessage(ACLMessage.INFORM_IF);

                for (Map.Entry<AID, Reputacao> entry : punicoes.entrySet()) {
                    if (!agentesAvisados.contains(entry.getKey())) {
                        aviso.addReceiver(entry.getKey());
                        agentesAvisados.add(entry.getKey());
                    }
                }

                myAgent.send(aviso);

            }

            if (!expulsoes.isEmpty()) {

                ACLMessage expulsao = new ACLMessage(ACLMessage.INFORM_REF);

                for (AID agente : expulsoes) {
                    if (!agentesAvisados.contains(agente)) {
                        expulsao.addReceiver(agente);
                        agentesAvisados.add(agente);
                    }
                }

                myAgent.send(expulsao);

            }
        }
    }


}
