package behaviors;

import java.io.IOException;

import agents.Vendedor;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import model.Servico;

public class RequisitarOfertaBehavior extends CyclicBehaviour {

	private static final long serialVersionUID = -1721570621496124052L;

	@Override
	public void action() {

		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
		ACLMessage msg = myAgent.receive(mt);

		if (msg != null) {

			Servico servico = new Servico();
			try {
				servico = (Servico) msg.getContentObject();
			} catch (UnreadableException e1) {
				e1.printStackTrace();
			}
			ACLMessage resposta = msg.createReply();

			Vendedor vendedor = (Vendedor) myAgent;

			Servico servicoVendedor = vendedor.getServico();
			if (servico.getProfissao().equalsIgnoreCase(servicoVendedor.getProfissao())
					&& servico.getServico().equalsIgnoreCase(servicoVendedor.getServico())) {
				resposta.setPerformative(ACLMessage.PROPOSE);
				try {
					resposta.setContentObject(servico);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				resposta.setPerformative(ACLMessage.REFUSE);
				resposta.setContent("nao-sou-capaz-de-fazer");
			}

			myAgent.send(resposta);
		} else {
			block();
		}
	}

}
