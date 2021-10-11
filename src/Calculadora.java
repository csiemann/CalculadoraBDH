import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class Calculadora {
	static int value = 0;

	public static void main(String[] args) {
		JFrame frame = new JFrame("Conversor");
		frame.setLayout(new BorderLayout(10,10));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,3,10,10));

		JLabel b = new JLabel("Binário", SwingConstants.CENTER);
		JLabel d = new JLabel("Decimal", SwingConstants.CENTER);
		JLabel h  = new JLabel("Hexadecimal", SwingConstants.CENTER);
		JFormattedTextField binary = new JFormattedTextField();
		JFormattedTextField decimal = new JFormattedTextField();
		JFormattedTextField hexa = new JFormattedTextField();
		binary.setDocument(new CalculadoraDocument("01+-*"));
		decimal.setDocument(new CalculadoraDocument("0123456789"));
		hexa.setDocument(new CalculadoraDocument("0123456789abcdefABCDEF"));
		frame.setSize(500,500);
		frame.add(panel, BorderLayout.NORTH);
		panel.add(b);
		panel.add(d);
		panel.add(h);
		panel.add(binary);
		panel.add(decimal);
		panel.add(hexa);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel panelGate = new JPanel();
		panelGate.setLayout(new GridLayout(3,1,10,10));
		JLabel gate  = new JLabel("<html><p style=\"text-align:center;\">Gates (resultado em result.txt)<br>!=NOT |=OR &=AND 0=F 1=V</p></html>", SwingConstants.CENTER);
		JLabel message  = new JLabel("", SwingConstants.CENTER);
		JTextField gates = new JTextField();
		panelGate.add(gate);
		panelGate.add(gates);
		panelGate.add(message);
		frame.add(panelGate,BorderLayout.SOUTH);
		panelGate.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		frame.pack();
		frame.setVisible(true);
		binary.addKeyListener(new CalculadoraKeyListener(binary, decimal, hexa));
		decimal.addKeyListener(new CalculadoraKeyListener(binary, decimal, hexa));
		hexa.addKeyListener(new CalculadoraKeyListener(binary, decimal, hexa));
		gates.addKeyListener(null);
	}

	public static class CalculadoraDocument extends PlainDocument {

		/**
		 *
		 */
		private static final long serialVersionUID = 3962617005621446085L;

		String comp;

		public CalculadoraDocument(String comp) {
			super();
			this.comp = comp;
		}

		@Override
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

			for (int i = str.length(); i > 0; i--) {
				String s = ""+str.charAt(i-1);
				if (comp.contains(s)) {
					super.insertString(offs, s, a);
				}
			}
		}

	}
	public static class CalculadoraKeyListener implements KeyListener {

		private JFormattedTextField binary;
		private JFormattedTextField decimal;
		private JFormattedTextField hexa;

		public CalculadoraKeyListener(JFormattedTextField binary, JFormattedTextField decimal, JFormattedTextField hexa) {
			super();
			this.binary = binary;
			this.decimal = decimal;
			this.hexa = hexa;
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			char c = e.getKeyChar();
			if (c == KeyEvent.VK_ENTER) {
				if (validateText(((JFormattedTextField)e.getComponent()).getText(), "+-*")) {
					calculate((JFormattedTextField)e.getComponent());
				}else {
					convert((JFormattedTextField)e.getComponent());
				}
				binary.setText(toBinary());
				decimal.setText(""+value);
				hexa.setText(toHexa());
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

		private boolean validateText(String texto, String comp) {
			for (int i = 0; i < texto.length(); i++) {
				String s = ""+texto.charAt(i);
				if (comp.contains(s)) {
					return true;
				}
			}
			return false;
		}

		private void convert(JFormattedTextField prin) {
			String text = prin.getText();
			if (prin.equals(binary)) {
				value = 0;
				int pow = 1;
				for (int i = text.length(); i > 0 ; i--) {
					int b = Character.getNumericValue(text.charAt(i-1));
					value += b * pow;
					pow *= 2;
				}
			}else if (prin.equals(decimal)) {
				try {
					value = Integer.parseInt(text);
				}catch (Exception e) {
				}
			}else if (prin.equals(hexa)) {
				value = 0;
				int base = 1;
				for (int i = text.length(); i > 0 ; i--) {
					char chara = text.charAt(i-1);
					int b = 0;
					if (chara >= 'a' && chara <= 'f') {
						b = chara - 87;
					}else if (chara >= 'A' && chara <= 'F') {
						b = chara - 55;
					}else {
						b = Character.getNumericValue(chara);
					}
					value += b * base;
					base *= 16;
				}
			}
		}

		private void calculate(JFormattedTextField prin) {
			String texto = prin.getText();
			StringBuilder builder = new StringBuilder();
			value = 0;
			if (texto.contains("+")) {
				// soma
				byte[] result;
				String[] array = texto.split("\\+");
				builder.append(array[0]);
				array[0] = builder.reverse().toString();
				builder.delete(0, builder.length());
				builder.append(array[1]);
				array[1] = builder.reverse().toString();
				builder.delete(0, builder.length());

				if (array[0].length() > array[1].length()) {
					result = new byte[array[0].length()+1];
				}else {
					result = new byte[array[1].length()+1];
				}
				for (int i = 0; i < result.length-1; i++) {
					char b1;
					char b2;
					if (array[0].length() > i) {
						b1 = array[0].charAt(i);
					} else {
						b1 = '0';
					}
					if (array[1].length() > i) {
						b2 = array[1].charAt(i);
					} else {
						b2 = '0';
					}
					// comparação (soma)
					if (b1 == '0'&& b2 == '0') {
						if (result[i] != 1) {
							result[i] = 0;
						}
					} else if ((b1 == '1'&& b2 == '0')||(b1 == '0'&& b2 == '1')) {
						if (result[i] != 1) {
							result[i] = 1;
						} else {
							result[i] = 0;
							result[i+1] = 1;
						}
					} else if (b1 == '1'&& b2 == '1') {
						// vai um (+1)
						if (result[i] != 1) {
							result[i] = 0;
							result[i+1] = 1;
						} else {
							result[i] = 1;
							result[i+1] = 1;
						}
					}
				}
				for (int i = 0, pow = 1; i < result.length; i++, pow *= 2) {
					value += result[i] * pow;
				}
			}else if (texto.contains("-")) {
				// subtração
				String[] array = texto.split("-");
				builder.append(array[0]);
				array[0] = builder.reverse().toString();
				builder.delete(0, builder.length());
				builder.append(array[1]);
				array[1] = builder.reverse().toString();
				builder.delete(0, builder.length());

				byte[] result = new byte[array[0].length()];

				for (int i = 0; i < result.length; i++) {
					char b1;
					char b2;
					if (array[0].length() > i) {
						b1 = array[0].charAt(i);
					} else {
						b1 = '0';
					}
					if (array[1].length() > i) {
						b2 = array[1].charAt(i);
					} else {
						b2 = '0';
					}
					// comparação
					if (b1 == '0'&& b2 == '0') {
						result[i] = 0;
					} else if (b1 == '1'&& b2 == '0') {
						result[i] = 1;
					} else if (b1 == '0'&& b2 == '1') {
						// vem um (-1)
						for (int j = 0; j < array[0].length(); j++) {
							if (j < i) {
								builder.append(array[0].charAt(j));
							} else {
								if (array[0].charAt(j) == '0') {
									builder.append(array[0].charAt(j));
									continue;
								} else if (array[0].charAt(j) == '1') {
									builder.append('0');
									array[0] = builder.replace(j+1, array[0].length(), array[0].substring(j+1)).toString();
									break;
								}
							}

						}
						result[i] = 1;
					} else if (b1 == '1'&& b2 == '1') {
						result[i] = 0;
					}
				}
				for (int i = 0, pow = 1; i < result.length; i++, pow *= 2) {
					value += result[i] * pow;
				}
			}else if (texto.contains("*")){
				// multiplicação
				String[] array = texto.split("\\*");
				builder.append(array[0]);
				array[0] = builder.reverse().toString();
				builder.delete(0, builder.length());
				builder.append(array[1]);
				array[1] = builder.reverse().toString();
				builder.delete(0, builder.length());

				byte[][] results;

				if (array[0].length() > array[1].length()) {
					results = new byte[array[1].length()][array[0].length() * 2];
				}else {
					results = new byte[array[1].length()][array[1].length() * 2];
				}

				for (int i = 0; i < results.length; i++) {
					char b1;
					char b2;
					if (array[1].length() > i) {
						b2 = array[1].charAt(i);
					} else {
						b2 = '0';
					}
					for (int j = 0; j < results[i].length-i; j++) {
						if (array[0].length() > j) {
							b1 = array[0].charAt(j);
						} else {
							b1 = '0';
						}

						if (b1 == '0'&& b2 == '0') {
							results[i][j+i] = 0;
						} else if ((b1 == '1'&& b2 == '0')||(b1 == '0'&& b2 == '1')) {
							results[i][j+i] = 0;
						} else if (b1 == '1'&& b2 == '1') {
							results[i][j+i] = 1;
						}
					}
				}
				byte[] result = new byte[results[0].length];

				for (int i = 0; i < results.length; i++) {
					byte[] aux = new byte[results[0].length];
					for (int j = 0; j < results[i].length; j++) {
						byte b1 = result[j];
						byte b2 = results[i][j];
						byte bAux = aux[j];
						if (b1 == 1 && b2 == 1) {
							if (bAux == 1) {
								aux[j+1] = 1;
								aux[j] = 1;
							}else {
								aux[j+1] = 1;
								aux[j] = 0;
							}
						} else if (b1 == 1 || b2 == 1) {
							if (bAux == 1) {
								aux[j+1] = 1;
								aux[j] = 0;
							}else {
								aux[j] = 1;
							}
						}else {
							if (bAux == 1) {
								aux[j] = 1;
							}else {
								aux[j] = 0;
							}
						}
					}
					result = aux;
				}
				for (int i = 0, pow = 1; i < result.length; i++, pow *= 2) {
					value += result[i] * pow;
				}

			}
		}
		private String toBinary() {
			int a = value;
			StringBuilder builder = new StringBuilder();
			if (a == 0) {
				return "0";
			}
			while(a != 0) {
				if ((a % 2 != 0)) {
					builder.append("1");
					a--;
				}else {
					builder.append("0");
				}
				a /= 2;
			}
			return builder.reverse().toString();
		}
		private String toHexa() {
			int a = value;
			StringBuilder builder = new StringBuilder();
			if (a == 0) {
				return "0";
			}
			while(a != 0) {
				if ((a % 16 != 0)) {
					if(a % 16 > 9) {
						builder.append((char)(55+(a % 16)));
					}else {
						builder.append(a % 16);
					}
					a -= a % 16;
				}else {
					builder.append("0");
				}
				a /= 16;
			}
			return builder.reverse().toString();
		}
	}
	public static class GateKeyListener implements KeyListener {

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
			char c = e.getKeyChar();
			if (c == KeyEvent.VK_ENTER) {
				String message = writeTable(((JFormattedTextField)e.getComponent()).getText());

			}
		}

		private String writeTable(String text) {
			String message = "";
			ArrayList<Character> variaveis = new ArrayList<>();
			String textoV = text.replaceAll("(\\d|\\W)", "").toUpperCase();
			for (char character : textoV.toCharArray()) {
				if (!variaveis.contains(character)) {
					variaveis.add(character);
				}
			}
			variaveis.sort(new Comparator<Character>() {
				@Override
				public int compare(Character o1, Character o2) {
					return o1.compareTo(o2);
				}
			});
			int size = variaveis.size();
			int possibilidades = 1 << size;
			System.out.println(size);
			System.out.println(possibilidades);
			System.out.println(Arrays.toString(variaveis.toArray()));
			int mask = 1<<size-1;
			for (int possibilidade = 0; possibilidade < possibilidades; possibilidade++) {
				String textP = text;
				int p = possibilidade;
				for (int variavel = 0 ; variavel < size; variavel ++) {
					Character v = variaveis.get(size-variavel-1);
					textP = textP.replace(v, (p & mask)==0?'0':'1');
					p <<= 1;
				}
				System.out.println(textP+" "+(char)resolve(textP));
			}



			return null;
		}
		private char resolve(String textP) {

			return '0';
		}
		@Override
		public void keyReleased(KeyEvent e) {

		}

	}
}
