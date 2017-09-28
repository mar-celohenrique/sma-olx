package model;

import jade.util.leap.Serializable;

public class Servico implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7542903203168669139L;
	
	String profissao;
	String servico;
	Integer valor;
	Integer diasComecar;
	Integer diasFInalizar;

	Integer[] valorEDias;

	public String getProfissao() {
		return profissao;
	}

	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}

	public String getServico() {
		return servico;
	}

	public void setServico(String servico) {
		this.servico = servico;
	}

	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}

	public Integer getDiasComecar() {
		return diasComecar;
	}

	public void setDiasComecar(Integer diasComecar) {
		this.diasComecar = diasComecar;
	}

	public Integer getDiasFInalizar() {
		return diasFInalizar;
	}

	public void setDiasFInalizar(Integer diasFInalizar) {
		this.diasFInalizar = diasFInalizar;
	}

	public Integer[] getValorEDias() {
		this.valorEDias[0] = this.valor;
		this.valorEDias[1] = this.diasComecar;
		this.valorEDias[2] = this.diasFInalizar;
		return valorEDias;
	}

	public void setValorEDias(Integer[] valorEDias) {
		this.valorEDias = valorEDias;
	}

}
