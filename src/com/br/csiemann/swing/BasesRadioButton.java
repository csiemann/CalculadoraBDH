package com.br.csiemann.swing;

import javax.swing.JRadioButton;

import com.br.csiemann.Calculadora;

public class BasesRadioButton extends JRadioButton {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private int base;
	private Calculadora calculadora;

	public BasesRadioButton(Calculadora calculadora, String text, int base) {
		super(text);
		this.base = base;
		this.calculadora = calculadora;
	}

	public BasesRadioButton(Calculadora calculadora, String text, boolean selected, int base) {
		super(text, selected);
		this.base = base;
		this.calculadora = calculadora;
	}

	public String convert(String textA, int lastBase) {
		Calculadora calculadora = getCalculadora();
		StringBuilder builder = new StringBuilder();
		if (textA.matches("[^+\\-*/]+")) {
			if (base == 10) {
				builder.append("" + calculadora.convertBase(textA, lastBase));
			} else if (base == 2) {
				builder.append(calculadora.toBinary(calculadora.convertBase(textA, lastBase)));
			} else if (base == 16) {
				builder.append(calculadora.toHexa(calculadora.convertBase(textA, lastBase)));
			}
			return builder.toString();
		} else if (textA.isEmpty()) {
			return "";
		}
		char[] arrayOper = textA.replaceAll("[^+\\-*/()]+", "").toCharArray();
		if (base == 10) {
			builder.append("" + calculadora.getValue1() + arrayOper[0] + calculadora.getValue2());
		} else if (base == 2) {
			builder.append(calculadora.toBinary(calculadora.getValue1()) + arrayOper[0] + calculadora.toBinary(calculadora.getValue2()));
		} else if (base == 16) {
			builder.append(calculadora.toHexa(calculadora.getValue1()) + arrayOper[0] + calculadora.toHexa(calculadora.getValue2()));
		}

		return builder.toString();
	}

	public int getBase() {
		return base;
	}
	public Calculadora getCalculadora() {
		return calculadora;
	}
}