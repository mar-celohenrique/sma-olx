package model;

import jade.core.AID;
import jade.util.leap.Serializable;

public class Reputacao implements Serializable {

    private Integer positiva = 0;
    private Integer negativa = 0;
    private AID agente;

    private Avaliacao avaliacao;

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public AID getAgente() {
        return agente;
    }

    public void setAgente(AID agente) {
        this.agente = agente;
    }

    public void adicionarFeedbackPositivo() {
        this.positiva++;
    }

    public void adicionarFeedbackNegativo() {
        this.negativa++;
    }

    public Integer getPositiva() {
        return positiva;
    }

    public Integer getNegativa() {
        return negativa;
    }

    public enum Avaliacao {
        POSITIVA("1"), NEGATIVA("2");

        private String descricao;

        private Avaliacao(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;

        }
    }
}
