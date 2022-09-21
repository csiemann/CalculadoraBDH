package com.br.csiemann;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.br.csiemann.swing.BaseItemListener;
import com.br.csiemann.swing.BasesRadioButton;
import com.br.csiemann.swing.CalculadoraKeyListener;
import com.br.csiemann.swing.FilterDocument;
import com.br.csiemann.swing.GateKeyListener;

/**
 * Calculadora e conversora de base sem usar métodos do Java
 * @author Caetano Siemann
 */
public class Calculadora {
	private int value1;
	private int value2;

	public Calculadora() {
		this.value1 = 0;
		this.value2 = 0;
	}

	public int convertBase(String text, int lastBase) {
		int value = 0;
		if (lastBase == 2) {
			int pow = 1;
			for (int i = text.length(); i > 0; i--) {
				int b = Character.getNumericValue(text.charAt(i - 1));
				value += b * pow;
				pow *= 2;
			}
		} else if (lastBase == 10) {
			try {
				value = Integer.parseInt(text);
			} catch (Exception e) {
			}
		} else if (lastBase == 16) {
			int base = 1;
			for (int i = text.length(); i > 0; i--) {
				char chara = text.charAt(i - 1);
				int b = 0;
				if (chara >= 'A' && chara <= 'F') {
					b = chara - 55;
				} else {
					b = Character.getNumericValue(chara);
				}
				value += b * base;
				base *= 16;
			}
		}
		return value;
	}

	public String toBinary(int value) {
		StringBuilder builder = new StringBuilder();
		if (value == 0) {
			return "0";
		}
		while (value != 0) {
			if ((value % 2 != 0)) {
				builder.append("1");
				value--;
			} else {
				builder.append("0");
			}
			value /= 2;
		}
		return builder.reverse().toString();
	}

	public String toHexa(int value) {
		StringBuilder builder = new StringBuilder();
		if (value == 0) {
			return "0";
		}
		while (value != 0) {
			if ((value % 16 != 0)) {
				if (value % 16 > 9) {
					builder.append((char) (55 + (value % 16)));
				} else {
					builder.append(value % 16);
				}
				value -= value % 16;
			} else {
				builder.append("0");
			}
			value /= 16;
		}
		return builder.reverse().toString();
	}

	public int getValue1() {
		return value1;
	}

	public void setValue1(int value1) {
		this.value1 = value1;
	}

	public int getValue2() {
		return value2;
	}

	public void setValue2(int value2) {
		this.value2 = value2;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Conversor");
		frame.setLayout(new BorderLayout(10, 10));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		JPanel panel = new JPanel();
		JPanel panelButton = new JPanel();
		panel.setLayout(new GridLayout(5, 1, 10, 10));
		panelButton.setLayout(new GridLayout(1, 3, 10, 10));
		Calculadora calculadora = new Calculadora();
		JFormattedTextField campoCalculadora = new JFormattedTextField();
		BasesRadioButton binario = new BasesRadioButton(calculadora, "Binário", 2);
		BasesRadioButton decimal = new BasesRadioButton(calculadora, "Decimal", true, 10);
		campoCalculadora.setDocument(new FilterDocument("0123456789+-*/"));
		BasesRadioButton hexa = new BasesRadioButton(calculadora, "Hexadecimal", 16);
		ButtonGroup grupo = new ButtonGroup();
		frame.setSize(500, 500);
		frame.add(panel, BorderLayout.NORTH);
		panel.add(campoCalculadora);
		panel.add(panelButton);
		panelButton.add(binario);
		panelButton.add(decimal);
		panelButton.add(hexa);
		grupo.add(binario);
		grupo.add(decimal);
		grupo.add(hexa);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel gate = new JLabel(
				"<html><p style=\"text-align:center;\">Gates (resultado em result.txt)<br>!=NOT |=OR &=AND 0=F 1=V</p></html>",
				SwingConstants.CENTER);
		JLabel message = new JLabel("", SwingConstants.CENTER);
		JFormattedTextField gates = new JFormattedTextField();
		panel.add(gate);
		panel.add(gates);
		panel.add(message);

		frame.pack();
		frame.setVisible(true);
		campoCalculadora.addKeyListener(new CalculadoraKeyListener(calculadora, binario, decimal, hexa));
		BaseItemListener itemL = new BaseItemListener(campoCalculadora);
		binario.addItemListener(itemL);
		decimal.addItemListener(itemL);
		hexa.addItemListener(itemL);
		gates.addKeyListener(new GateKeyListener(message));
	}
}
