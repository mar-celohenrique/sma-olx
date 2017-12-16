package agents;


import behaviors.RequisitarCompraBehavior;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import model.Servico;
import util.DescobrirReputacao;

public class Comprador extends Agent {

    private static final long serialVersionUID = 5933420525231961177L;
    private Servico servico = new Servico();
    private AID[] vendedores;

    @Override
    protected void setup() {
        System.out.println("Olá! O comprador " + getAID().getName() + " está pronto.");

        // Pegar os argumentos inseridos na inicializacao do agente
        Object[] args = getArguments();

        System.err.println(args[0].toString());

        String[] argumentos = args[0].toString().split(" ");

        if (args != null && args.length > 0) {
            servico.setProfissao(argumentos[0]);
            servico.setServico(argumentos[1]);
            servico.setValor(Integer.parseInt(argumentos[2].toString()));
            servico.setDiasComecar(Integer.parseInt(argumentos[3].toString()));
            servico.setDiasFInalizar(Integer.parseInt(argumentos[4].toString()));

            addBehaviour(new TickerBehaviour(this, 3000) {
                private static final long serialVersionUID = 6522463434708953978L;

                @Override
                protected void onTick() {
                    // Atualizando a lista de agentes vendedores
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("vendedor");
                    template.addServices(sd);

                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        vendedores = new AID[result.length];
                        for (int i = 0; i < result.length; ++i) {
                            vendedores[i] = result[i].getName();
                        }
                    } catch (FIPAException fe) {
                        fe.printStackTrace();
                    }



                    
                    // Adicionando behavior personalizado do comprador
                    myAgent.addBehaviour(new RequisitarCompraBehavior(vendedores, servico, DescobrirReputacao.reputacao(myAgent)));
                }
            });
        } else {
            // O nome do servico nao foi especificador e preciso terminar o
            // agent
            System.out.println("Serviço não especificado..");
            doDelete();
        }

    }

    protected void takeDown() {
        // Mensagem de encerramento do agent
        System.out.println("O agente comprador " + getAID().getName() + " está terminando..");
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public AID[] getVendedores() {
        return vendedores;
    }

    public void setVendedores(AID[] vendedores) {
        this.vendedores = vendedores;
    }

}
