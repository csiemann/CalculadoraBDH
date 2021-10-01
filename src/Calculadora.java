import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.JTextComponent.KeyBinding;

public class Calculadora {
	static int value = 0;

	public static void main(String[] args) {

		JFrame f = new JFrame("Conversor");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(320, 90);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		JLabel b = new JLabel("Binário");
		JLabel d = new JLabel("Decimal");
		JLabel h  = new JLabel("Hexadecimal");
		JTextField binary = new JTextField();
		JTextField decimal = new JTextField();
		JTextField hexa = new JTextField();
		f.add(b);
		f.add(d);
		f.add(h);
		f.add(binary);
		f.add(decimal);
		f.add(hexa);
		b.setBounds(25, 7, 100, 10);
		d.setBounds(135, 7, 100, 10);
		h.setBounds(225, 7, 100, 10);
		binary.setBounds(0,20,100, 30);
		decimal.setBounds(110,20,100, 30);
		hexa.setBounds(220,20,100, 30);
		binary.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c=='+')||(c=='-')||(c=='*')||(c >= '0') && (c <= '1') ||
						(c == KeyEvent.VK_BACK_SPACE) ||
						(c == KeyEvent.VK_DELETE))) {
					e.consume();
					return;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c=='+')||(c=='-')||(c=='*')||(c >= '0') && (c <= '1') ||
						(c == KeyEvent.VK_BACK_SPACE) ||
						(c == KeyEvent.VK_DELETE))) {
					return;
				}
				String texto = binary.getText();
				// método para cálculo
				if (texto.matches("[01]+[*\\-+][01]+")) {
					calculate(texto);
				} else if(texto.matches("[01]+")) {
					convert(texto, 1);
				}
			}
		});
		decimal.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '9') ||
						(c == KeyEvent.VK_BACK_SPACE) ||
						(c == KeyEvent.VK_DELETE))) {
					e.consume();
					return;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '9') ||
						(c == KeyEvent.VK_BACK_SPACE) ||
						(c == KeyEvent.VK_DELETE))) {
					return;
				}
				convert(decimal.getText(), 2);
			}
		});
		hexa.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c >= 'A' && c <= 'F')||(c >= 'a' && c <= 'f')||(c >= '0') && (c <= '9') ||
						(c == KeyEvent.VK_BACK_SPACE) ||
						(c == KeyEvent.VK_DELETE))) {
					e.consume();
					return;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c >= 'A' && c <= 'F')||(c >= 'a' && c <= 'f')||(c >= '0') && (c <= '9') ||
						(c == KeyEvent.VK_BACK_SPACE) ||
						(c == KeyEvent.VK_DELETE))) {
					return;
				}
				convert(hexa.getText(), 3);
			}
		});
		KeyListener lo = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				char c = e.getKeyChar();
				if (c == KeyEvent.VK_ENTER) {
					binary.setText(toBinary());
					decimal.setText(""+value);
					hexa.setText(toHexa());
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		};
		binary.addKeyListener(lo);
		decimal.addKeyListener(lo);
		hexa.addKeyListener(lo);

		KeyBinding[] bind  = {new JTextComponent.KeyBinding(
				KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK),
				DefaultEditorKit.beepAction)};
		JTextComponent.loadKeymap(binary.getKeymap(), bind, binary.getActions());
	}
	protected static void calculate(String texto) {
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
		}else {
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
				System.out.println("1 "+Arrays.toString(result));
				System.out.println("2 "+Arrays.toString(results[i]));
				System.out.println("3 "+Arrays.toString(aux));
				result = aux;
			}
			for (int i = 0, pow = 1; i < result.length; i++, pow *= 2) {
				value += result[i] * pow;
			}

		}
	}
	protected static String toBinary() {
		int a = value;
		StringBuilder builder = new StringBuilder();
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
	protected static String toHexa() {
		int a = value;
		StringBuilder builder = new StringBuilder();
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
	protected static void convert(String text, int context) {
		try {
			switch (context) {
			case 1:
				value = 0;
				int pow = 1;
				for (int i = text.length(); i > 0 ; i--) {
					int b = Character.getNumericValue(text.charAt(i-1));
					value += b * pow;
					pow *= 2;
				}
				return;
			case 2:
				value = Integer.parseInt(text);
				return;
			case 3:
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
				return;
			default:
				break;
			}
		}catch (Exception e) {
		}
	}
}
