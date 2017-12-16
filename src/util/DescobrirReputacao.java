package util;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class DescobrirReputacao {


    public static AID reputacao(Agent agent){
        AID reputacao = null;

        // Atualizando a lista de agentes reputacao
        DFAgentDescription templateReputacao = new DFAgentDescription();
        ServiceDescription sdReputacao = new ServiceDescription();
        sdReputacao.setType("reputacao");
        templateReputacao.addServices(sdReputacao);
        try {
            DFAgentDescription[] result = DFService.search(agent, templateReputacao);
            for (int i = 0; i < result.length; ++i) {
                reputacao = result[i].getName();
            }
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        return reputacao;

    }

}
