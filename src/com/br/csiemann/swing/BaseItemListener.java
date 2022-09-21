package com.br.csiemann.swing;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFormattedTextField;

public class BaseItemListener implements ItemListener {

	private JFormattedTextField field;
	private int lastBase;

	public BaseItemListener(JFormattedTextField field) {
		this.field = field;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

		if (e.getItem() instanceof BasesRadioButton) {
			BasesRadioButton button = (BasesRadioButton) e.getItem();
			if (button.isSelected()) {
				int a = button.getBase();
				String textA = field.getText();
				String text = button.convert(textA, lastBase);
				if (a == 2) {
					field.setDocument(new FilterDocument("01+-*/"));
				} else if (a == 10) {
					field.setDocument(new FilterDocument("0123456789+-*/"));
				} else if (a == 16) {
					field.setDocument(new FilterDocument("0123456789abcdefABCDEF+-*/"));
				}
				field.setText(text);
			} else {
				lastBase = button.getBase();
			}

		}

	}

}