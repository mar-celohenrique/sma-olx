package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import model.Reputacao;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class AgenteReputacaoCentralizada extends Agent {

    /**
     *
     */
    private static final long serialVersionUID = 4454736627411340343L;
    private Map<AID, Reputacao> reputacoes = new TreeMap<>();

    @Override
    protected void setup() {
        // Registrando o agente de reputacao nas paginas amarelas
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("reputacao");
        sd.setName("olx");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new TratamentoReputacao());
        addBehaviour(new EnviarReputacaoBehavior());
    }

    public Reputacao obterReputacaoAgente(AID identificador) {
        return this.reputacoes.get(identificador);
    }

    private class TratamentoReputacao extends CyclicBehaviour {

        /**
         *
         */
        private static final long serialVersionUID = 2558772007326173587L;

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM_REF);
            ACLMessage msg = myAgent.receive(mt);

            if (null != msg) {
                ACLMessage resposta = msg.createReply();
                Reputacao reputacao = null;
                try {
                    reputacao = (Reputacao) msg.getContentObject();

                    if (null != reputacao) {
                        if (!reputacoes.containsKey(reputacao.getAgente())) {
                            reputacoes.put(reputacao.getAgente(), reputacao);
                        }
                        switch (reputacao.getAvaliacao()) {
                            case NEGATIVA:
                                reputacoes.get(reputacao.getAgente()).adicionarFeedbackNegativo();
                                break;

                            case POSITIVA:
                                reputacoes.get(reputacao.getAgente()).adicionarFeedbackPositivo();
                                break;

                            default:
                                break;
                        }
                    }

                    for (Map.Entry<AID, Reputacao> entry : reputacoes.entrySet()) {
                        System.out.println(entry.getKey().getLocalName() + "|Positivas: " + entry.getValue().getPositiva()
                                + " |Negativas " + entry.getValue().getNegativa());
                    }

                } catch (UnreadableException e) {
                    resposta.setPerformative(ACLMessage.FAILURE);
                }

            }
        }
    }

    private class EnviarReputacaoBehavior extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = myAgent.receive(mt);

            if (null != msg) {
                ACLMessage resposta = new ACLMessage(ACLMessage.INFORM);
                resposta.addReceiver(msg.getSender());

                try {
                    resposta.setContentObject((Serializable) reputacoes);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                myAgent.send(resposta);

            }

        }
    }

}
