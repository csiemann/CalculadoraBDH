import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.JTextComponent.KeyBinding;

public class Calculadora {
	static int value = 0;
	public static void main(String[] args) {
		int valor;
		JFrame f = new JFrame("Conversor");

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(320, 90);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		JTextField binary = new JTextField();
		JTextField decimal = new JTextField();
		JTextField hexa = new JTextField();
		f.add(binary);
		f.add(decimal);
		f.add(hexa);
		binary.setBounds(0,15,100, 30);
		decimal.setBounds(110,15,100, 30);
		hexa.setBounds(220,15,100, 30);
		binary.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '1') ||
						(c == KeyEvent.VK_BACK_SPACE) ||
						(c == KeyEvent.VK_DELETE))) {
					e.consume();
					return;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '1') ||
						(c == KeyEvent.VK_BACK_SPACE) ||
						(c == KeyEvent.VK_DELETE))) {
					return;
				}
				convert(binary.getText(), 1);
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
