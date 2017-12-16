package ui;

import jade.lang.acl.ACLMessage;
import model.Servico;

import javax.swing.*;
import java.awt.*;


public class TelaConfirmacaoCompra extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1596364936565258489L;
	
	
	public TelaConfirmacaoCompra(Servico servico, ACLMessage confirmacao) {
		super("CONFIRMAÇÃO DE COMPRA");
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 1));
		p.add(new JLabel(servico.getServico().toUpperCase() + " comprado de  " + confirmacao.getSender().getName()));
		getContentPane().add(p, BorderLayout.CENTER);
		getContentPane().add(p, BorderLayout.SOUTH);
		
		setResizable(false);
	}
	
	@SuppressWarnings("deprecation")
	public void show() {
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth() / 2;
		int centerY = (int)screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		super.show();
	}	
	
}
