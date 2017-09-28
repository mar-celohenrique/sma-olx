package agents;

import behaviors.EncerrarTransacaoBehavior;
import behaviors.RequisitarOfertaBehavior;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import model.Servico;

public class Vendedor extends Agent {

	private static final long serialVersionUID = 7486689818349592167L;

	private Servico servico = new Servico();

	@Override
	protected void setup() {

		//Registrando serviço
		Object[] args = getArguments();
		
		String[] argumentos = args[0].toString().split(" ");
		
		servico.setProfissao(argumentos[0]);
		servico.setServico(argumentos[1]);
		servico.setValor(Integer.parseInt(argumentos[2].toString()));
		servico.setDiasComecar(Integer.parseInt(argumentos[3].toString()));
		servico.setDiasFInalizar(Integer.parseInt(argumentos[4].toString()));

		// Registrando o vendedor nas paginas amarelas
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("vendedor");
		sd.setName("olx");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		addBehaviour(new RequisitarOfertaBehavior());
		addBehaviour(new EncerrarTransacaoBehavior());
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

}
