package com.br.csiemann.swing;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class FilterDocument extends PlainDocument {

	/**
	 *
	 */
	private static final long serialVersionUID = 3962617005621446085L;

	String comp;

	public FilterDocument(String comp) {
		super();
		this.comp = comp;
	}

	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

		for (int i = str.length(); i > 0; i--) {
			String s = "" + str.charAt(i - 1);
			if (comp.contains(s)) {
				super.insertString(offs, s, a);
			}
		}
	}

}