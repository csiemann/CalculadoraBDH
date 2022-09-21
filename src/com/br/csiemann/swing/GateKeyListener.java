package com.br.csiemann.swing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

public class GateKeyListener implements KeyListener {

	JLabel message;

	public GateKeyListener(JLabel message) {
		super();
		this.message = message;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		char c = e.getKeyChar();
		if (c == KeyEvent.VK_ENTER) {
			String message = "";
			try {
				message = writeTable(((JFormattedTextField) e.getComponent()).getText());
			} catch (IOException e1) {
			}
			this.message.setText(message);
		}
	}

	private String writeTable(String text) throws IOException {
		String message = "";
		File result = new File("results.txt");
		FileWriter writer = new FileWriter(result);
		BufferedWriter buffer = new BufferedWriter(writer);
		ArrayList<Character> variaveis = new ArrayList<>();
		text = text.toUpperCase();
		String textoV = text.replaceAll("(\\d|\\W)", "");

		for (char character : textoV.toCharArray()) {
			if (!variaveis.contains(character)) {
				variaveis.add(character);
			}
		}
		variaveis.sort(new Comparator<Character>() {
			@Override
			public int compare(Character c1, Character c2) {
				return c2.compareTo(c1);
			}
		});
		int size = variaveis.size();
		int possibilidades = 1 << size;
		int mask = 1 << size - 1;

		for (int variavel = 0; variavel < size; variavel++) {
			Character v = variaveis.get(size - variavel - 1);
			buffer.append(v + "|");
		}
		// escreve a fórmula que será resolvida com chaves (ex: input = A|X output =
		// [A|X])
		buffer.append("[" + text + "]\n");
		for (int possibilidade = 0; possibilidade < possibilidades; possibilidade++) {
			String textP = text;
			int p = possibilidade;
			for (int variavel = 0; variavel < size; variavel++) {
				Character v = variaveis.get(size - variavel - 1);
				char r = (p & mask) == 0 ? '0' : '1';
				textP = textP.replace(v, r);
				p <<= 1;
				// aqui é para cada variável em sequencia para cada possibilidade
				buffer.append(r + "|");
			}
			StringBuilder builder = new StringBuilder(textP);
			while (builder.length() > 1) {
				int f = builder.indexOf(")");
				int i = -1;
				if (f > 0) {
					i = builder.substring(0, f).lastIndexOf("(");
				}
				if (i > -1) {
					String re = resolve(builder.substring(i + 1, f));
					if (re == null) {
						message = "<html><p style=\"text-align:center;color:red;\">Erro ao resolver!</p></html>";
						break;
					} else {
						builder.replace(i, f + 1, re);
					}
				} else if (builder.length() > 1) {
					String re = resolve(builder.toString());
					if (re == null) {
						message = "<html><p style=\"text-align:center;color:red;\">Erro ao resolver!</p></html>";
						break;
					} else {
						builder = new StringBuilder(re);
					}
				}
			}
			// aqui é a solução de cada possibilidade
			buffer.append(builder.toString() + "\n");
		}
		buffer.close();
		writer.close();
		if (message.isEmpty()) {
			message = "<html><p style=\"text-align:center;color:green;\">Feito a tabela</p></html>";
		} else {
			result.delete();
		}
		return message;
	}

	private String resolve(String textP) {
		textP = textP.replace("!0", "1");
		textP = textP.replace("!1", "0");
		while (textP.length() > 1) {

			int o = textP.indexOf("|");
			int a = textP.indexOf("&");
			if ((a <= o || o < 0) && a >= 0) {
				int a1 = textP.indexOf("1&1");
				if (a1 <= a && a1 >= 0) {
					textP = textP.replaceFirst("1&1", "1");
				} else {
					textP = textP.replaceFirst("0&1|1&0|0&0", "0");
				}
			} else if ((o <= a || a < 0) && o >= 0) {
				int o1 = textP.indexOf("0|0");
				if (o1 <= o && o1 >= 0) {
					textP = textP.replaceFirst("0\\|0", "0");
				} else {
					textP = textP.replaceFirst("0\\|1|1\\|0|1\\|1", "1");
				}
			} else if (a < 0 && o < 0) {
				return null;
			}
		}
		return textP;
	}
}