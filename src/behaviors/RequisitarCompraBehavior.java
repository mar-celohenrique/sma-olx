package behaviors;

import java.io.IOException;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import model.Servico;

public class RequisitarCompraBehavior extends Behaviour {

	private static final long serialVersionUID = -4197249070066687719L;
	private AID[] vendedores;
	private AID melhorVendedor;
	private int melhorPreco;
	private MessageTemplate mt;
	private int passo = 0;
	private Servico servico;
	private int numeroRespostasAosVendedores = 0;

	public RequisitarCompraBehavior(AID[] vendedores, Servico servico) {
		super();
		this.vendedores = vendedores;
		this.servico = servico;
	}

	@Override
	public void action() {
		switch (passo) {
		case 0:
			ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
			for (int i = 0; i < vendedores.length; ++i) {
				cfp.addReceiver(vendedores[i]);
			}

			try {
				cfp.setContentObject(servico);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			cfp.setConversationId("olx");
			cfp.setReplyWith("cfp" + System.currentTimeMillis()); // para gerar
																	// valores
																	// únicos
			myAgent.send(cfp);

			// Preparação do template para capturar as propostas dos vendedores
			mt = MessageTemplate.and(MessageTemplate.MatchConversationId("olx"),
					MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
			passo = 1;
			break;
		case 1:

			// Recebendo todas as propostas aceitas e recusadas dos vendedores
			ACLMessage resposta = myAgent.receive(mt);
			if (resposta != null) {

				// analisando as respostas da minha proposta
				if (resposta.getPerformative() == ACLMessage.PROPOSE) {
					// oferta do vendedor
					Servico servico = new Servico();
					try {
						servico = (Servico) resposta.getContentObject();
					} catch (UnreadableException e) {
						e.printStackTrace();
					}

					if (melhorVendedor == null || servico.getValor() < melhorPreco) {
						melhorPreco = servico.getValor();
						melhorVendedor = resposta.getSender();
					}
				}
				numeroRespostasAosVendedores++;
				if (numeroRespostasAosVendedores >= vendedores.length) {
					// todas as propostas ja foram analisadas
					passo = 2;
				}
			}
			break;
		case 2:
			ACLMessage proposta = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
			proposta.addReceiver(melhorVendedor);
			try {
				proposta.setContentObject(servico);
			} catch (IOException e) {
				e.printStackTrace();
			}
			proposta.setConversationId("olx");
			proposta.setReplyWith("cfp" + System.currentTimeMillis());
			myAgent.send(proposta);

			mt = MessageTemplate.and(MessageTemplate.MatchConversationId("olx"),
					MessageTemplate.MatchInReplyTo(proposta.getReplyWith()));
			passo = 3;
			break;

		case 3:

			resposta = myAgent.receive(mt);
			if (resposta != null) {
				if (resposta.getPerformative() == ACLMessage.INFORM) {
					System.out.println(servico.getServico() + " comprado de  " + resposta.getSender().getName());
					System.out.println("Valor = " + melhorPreco);
					myAgent.doDelete();
				} else {
					System.out.println("Falha");
				}

				passo = 4;
			} else {
				block();
			}
			break;

		}

	}

	@Override
	public boolean done() {
		if (passo == 2 && melhorVendedor == null) {
			System.out.println("Falha: " + servico.getServico() + " indisponível.");
		}
		return ((passo == 2 && melhorVendedor == null) || passo == 4);
	}

}
