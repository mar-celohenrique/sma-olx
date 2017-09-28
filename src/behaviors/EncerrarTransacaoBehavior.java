package behaviors;

import agents.Vendedor;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
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

			resposta.setPerformative(ACLMessage.INFORM);
			resposta.setContent(String.valueOf(servico.getDiasComecar() + servicoVendedor.getDiasFInalizar()));

			myAgent.send(resposta);

		} else {
			block();
		}
	}

}
