package behaviors;

import java.io.IOException;
import java.util.Random;

import agents.Vendedor;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import model.Reputacao;
import model.Servico;

public class EncerrarTransacaoBehavior extends CyclicBehaviour {

    /**
     *
     */
    private static final long serialVersionUID = 7655663174907029643L;

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        ACLMessage msg = myAgent.receive(mt);

        if (null != msg) {

            Servico servico = new Servico();
            try {
                servico = (Servico) msg.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
            ACLMessage resposta = msg.createReply();

            Vendedor vendedor = (Vendedor) myAgent;

            Servico servicoVendedor = vendedor.getServico();

            Reputacao reputacao = new Reputacao();
            reputacao.setAgente(msg.getSender());

            ACLMessage avaliacao = new ACLMessage(ACLMessage.INFORM_REF);
            avaliacao.addReceiver(vendedor.getReputacao());
            reputacao.setAgente(msg.getSender());

            Random r = new Random();
            float colaboracao = r.nextFloat();
            if (colaboracao <= vendedor.getHonestidade()) {
                servico.setConcluido(true);
                servico.setDiasDoncluido(servicoVendedor.getDiasFInalizar());
                reputacao.setAvaliacao(Reputacao.Avaliacao.POSITIVA);
            } else {
                servico.setConcluido(false);
                servico.setDiasFInalizar(0);
                reputacao.setAvaliacao(Reputacao.Avaliacao.NEGATIVA);
            }
            try {
                resposta.setContentObject(servico);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                avaliacao.setContentObject(reputacao);
                myAgent.send(avaliacao);
            } catch (IOException e) {
                e.printStackTrace();
            }

            resposta.setPerformative(ACLMessage.INFORM);
            myAgent.send(resposta);

        } else {
            block();
        }
    }

}
