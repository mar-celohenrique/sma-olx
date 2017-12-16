package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import model.Reputacao;
import util.DescobrirReputacao;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;


public class Julgamento extends Agent {

    private Map<AID, Reputacao> reputacoes = new TreeMap<>();
    private AID reputacao;

    @Override
    protected void setup() {
        super.setup();
        this.reputacao = DescobrirReputacao.reputacao(this);

        addBehaviour(new RequisitaReputacoesBehavior());
        addBehaviour(new RecebeReputacoesBehavior());
        addBehaviour(new EnviaReputacaoSancaoBehavior());

    }

    private class RequisitaReputacoesBehavior extends CyclicBehaviour {

        @Override
        public void action() {
            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
            message.addReceiver(reputacao);
            myAgent.send(message);
        }
    }

    private class RecebeReputacoesBehavior extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = myAgent.receive(mt);

            if (null != msg) {
                try {
                    reputacoes = (Map<AID, Reputacao>) msg.getContentObject();
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private class EnviaReputacaoSancaoBehavior extends CyclicBehaviour {

        @Override
        public void action() {
            Map<AID, Reputacao> punicao = new TreeMap<>();
            if (!reputacoes.isEmpty()) {
                for (Map.Entry<AID, Reputacao> entry : reputacoes.entrySet()) {
                    if (((entry.getValue().getNegativa() / 100) * (entry.getValue().getNegativa() + entry.getValue().getPositiva())) > 0.3) {
                        punicao.put(entry.getKey(), entry.getValue());
                    }
                }

                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                try {
                    msg.setContentObject((Serializable) punicao);
                    myAgent.send(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

}
