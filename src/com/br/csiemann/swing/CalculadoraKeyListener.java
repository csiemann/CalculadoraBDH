package com.br.csiemann.swing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFormattedTextField;

import com.br.csiemann.Calculadora;

public class CalculadoraKeyListener implements KeyListener {
	private BasesRadioButton binario, decimal, hexa;
	private Calculadora calculadora;
	public CalculadoraKeyListener(Calculadora calculadora, BasesRadioButton binario, BasesRadioButton decimal, BasesRadioButton hexa) {
		this.binario = binario;
		this.decimal = decimal;
		this.hexa = hexa;
		this.calculadora = calculadora;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Calculadora calculadora = getCalculadora();
		char c = e.getKeyChar();
		JFormattedTextField field = (JFormattedTextField) e.getComponent();
		String textA = field.getText();
		if (textA.matches("[^+\\-*/]+[+\\-*/][^+\\-*/]+")) {
			String[] array = textA.split("[+\\-*/()]");
			if (binario.isSelected()) {
				calculadora.setValue1(calculadora.convertBase(array[0], 2));
				calculadora.setValue2(calculadora.convertBase(array[1], 2));
			} else if (decimal.isSelected()) {
				calculadora.setValue1(calculadora.convertBase(array[0], 10));
				calculadora.setValue2(calculadora.convertBase(array[1], 10));
			} else if (hexa.isSelected()){
				calculadora.setValue1(calculadora.convertBase(array[0], 16));
				calculadora.setValue2(calculadora.convertBase(array[1], 16));
			}
		}
		int value1 = calculadora.getValue1();
		int value2 = calculadora.getValue2();
		if (c == KeyEvent.VK_ENTER) {
			char[] arrayOper = field.getText().replaceAll("[^+\\-*/()]+", "").toCharArray();
			if (arrayOper.length == 1) {
				int result = 0;
				if (arrayOper[0] == '+') {
					result = value1 + value2;
				} else if (arrayOper[0] == '-') {
					if (value1 >= value2) {
						result = value1 - value2;
					} else {
						result = value2 - value1;
					}
				} else if (arrayOper[0] == '*') {
					result = value1 * value2;
				} else if (arrayOper[0] == '/') {
					result = value1 / value2;
				}

				String re = "";

				if (binario.isSelected()) {
					re = calculadora.toBinary(result);
				} else if (decimal.isSelected()) {
					re = "" + result;
				} else if (hexa.isSelected()){
					re = calculadora.toHexa(result);
				}

				field.setText(re);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	public Calculadora getCalculadora() {
		return calculadora;
	}
}