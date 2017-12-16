package agents;

import behaviors.EncerrarTransacaoBehavior;
import behaviors.RequisitarOfertaBehavior;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.Servico;
import util.DescobrirReputacao;

public class Vendedor extends Agent {

    private static final long serialVersionUID = 7486689818349592167L;

    private Servico servico = new Servico();
    private AID reputacao = new AID();
    private float honestidade;

    @Override
    protected void setup() {

        // Registrando serviço
        Object[] args = getArguments();

        String[] argumentos = args[0].toString().split(" ");

        servico.setProfissao(argumentos[0]);
        servico.setServico(argumentos[1]);
        servico.setValor(Integer.parseInt(argumentos[2].toString()));
        servico.setDiasComecar(Integer.parseInt(argumentos[3].toString()));
        servico.setDiasFInalizar(Integer.parseInt(argumentos[4].toString()));
        this.honestidade = Float.parseFloat(argumentos[5].toString());

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

        this.reputacao = DescobrirReputacao.reputacao(this);

        addBehaviour(new RequisitarOfertaBehavior());
        addBehaviour(new EncerrarTransacaoBehavior());
        addBehaviour(new RecebeSancaoBehavior());

    }


    public float getHonestidade() {
        return honestidade;
    }


    public void setHonestidade(float honestidade) {
        this.honestidade = honestidade;
    }


    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public AID getReputacao() {
        return reputacao;
    }

    public void setReputacao(AID reputacao) {
        this.reputacao = reputacao;
    }

    private class RecebeSancaoBehavior extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate templatePunicao = MessageTemplate.MatchPerformative(ACLMessage.INFORM_IF);
            ACLMessage punicao = myAgent.receive(templatePunicao);

            MessageTemplate templateExpulsao = MessageTemplate.MatchPerformative(ACLMessage.INFORM_REF);
            ACLMessage expulsao = myAgent.receive(templateExpulsao);

            if (null != punicao) {
                honestidade += 0.2f;
            } else if (null != expulsao) {
                myAgent.doDelete();
            }

        }
    }

}
