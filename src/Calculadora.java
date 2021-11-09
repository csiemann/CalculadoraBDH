import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class Calculadora {
	static int value1 = 0,value2 = 0;

	public static void main(String[] args) {
		JFrame frame = new JFrame("Conversor");
		frame.setLayout(new BorderLayout(10,10));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		JPanel panel = new JPanel();
		JPanel panelButton = new JPanel();
		panel.setLayout(new GridLayout(5,1,10,10));
		panelButton.setLayout(new GridLayout(1,3,10,10));

		JFormattedTextField calculadora = new JFormattedTextField();
		BasesRadioButton binario = new BasesRadioButton("Binário",2);
		BasesRadioButton decimal = new BasesRadioButton("Decimal", true, 10);
		calculadora.setDocument(new CalculadoraDocument("0123456789+-*/"));
		BasesRadioButton hexa = new BasesRadioButton("Hexadecimal", 16);
		ButtonGroup grupo = new ButtonGroup();
		frame.setSize(500,500);
		frame.add(panel, BorderLayout.NORTH);
		panel.add(calculadora);
		panel.add(panelButton);
		panelButton.add(binario);
		panelButton.add(decimal);
		panelButton.add(hexa);
		grupo.add(binario);
		grupo.add(decimal);
		grupo.add(hexa);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel gate  = new JLabel("<html><p style=\"text-align:center;\">Gates (resultado em result.txt)<br>!=NOT |=OR &=AND 0=F 1=V</p></html>", SwingConstants.CENTER);
		JLabel message  = new JLabel("", SwingConstants.CENTER);
		JFormattedTextField gates = new JFormattedTextField();
		panel.add(gate);
		panel.add(gates);
		panel.add(message);

		frame.pack();
		frame.setVisible(true);
		calculadora.addKeyListener(new CalculadoraKeyListener(binario,decimal,hexa));
		BaseItemListener itemL = new BaseItemListener(calculadora);
		binario.addItemListener(itemL);
		decimal.addItemListener(itemL);
		hexa.addItemListener(itemL);
		gates.addKeyListener(new GateKeyListener(message));
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
		BasesRadioButton binario,decimal,hexa;

		public CalculadoraKeyListener(BasesRadioButton binario,BasesRadioButton decimal,BasesRadioButton hexa) {
			this.binario = binario;
			this.decimal = decimal;
			this.hexa = hexa;
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			char c = e.getKeyChar();
			JFormattedTextField field = (JFormattedTextField)e.getComponent();
			String textA = field.getText();
			if (textA.matches("[^+\\-*/]+[+\\-*/][^+\\-*/]+")) {
				String[] array = textA.split("[+\\-*/()]");
				if (binario.isSelected()) {
					value1 = convertBase(array[0], 2);
					value2 = convertBase(array[1], 2);
				}else if (decimal.isSelected()) {
					value1 = convertBase(array[0], 10);
					value2 = convertBase(array[1], 10);
				}else {
					value1 = convertBase(array[0], 16);
					value2 = convertBase(array[1], 16);
				}
			}
			if (c == KeyEvent.VK_ENTER) {
				char[] arrayOper = field.getText().replaceAll("[^+\\-*/()]+", "").toCharArray();
				if (arrayOper.length == 1) {
					int result = 0;
					if (arrayOper[0] == '+') {
						result = value1 + value2;
					}else if (arrayOper[0] == '-') {
						if (value1 >= value2) {
							result = value1 - value2;
						} else {
							result = value2 - value1;
						}
					}else if (arrayOper[0] == '*') {
						result = value1 * value2;
					}else if (arrayOper[0] == '/') {
						result = value1 / value2;
					}

					String re = "";

					if (binario.isSelected()) {
						re = toBinary(result);
					}else if (decimal.isSelected()) {
						re = ""+result;
					}else {
						re = toHexa(result);
					}

					field.setText(re);
				}
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}
	}

	public static class BasesRadioButton extends JRadioButton {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		private int base;

		public BasesRadioButton(String text, int base) {
			super(text);
			this.base = base;
		}

		public BasesRadioButton(String text, boolean selected, int base) {
			super(text, selected);
			this.base = base;
		}

		public String convert(String textA, int lastBase) {
			StringBuilder builder = new StringBuilder();
			if (textA.matches("[^+\\-*/]+")) {
				if (base == 10) {
					builder.append(""+convertBase(textA, lastBase));
				}else if (base == 2) {
					builder.append(toBinary(convertBase(textA, lastBase)));
				}else if (base == 16) {
					builder.append(toHexa(convertBase(textA, lastBase)));
				}
				return builder.toString();
			}else if (textA.isEmpty()) {
				return "";
			}
			char[] arrayOper = textA.replaceAll("[^+\\-*/()]+", "").toCharArray();
			if (base == 10) {
				builder.append(""+value1+arrayOper[0]+value2);
			}else if (base == 2) {
				builder.append(toBinary(value1)+arrayOper[0]+toBinary(value2));
			}else if (base == 16) {
				builder.append(toHexa(value1)+arrayOper[0]+toHexa(value2));
			}

			return builder.toString();
		}

		public int getBase() {
			return base;
		}
	}

	static private int convertBase(String text, int lastBase) {
		int value = 0;
		if (lastBase == 2) {
			int pow = 1;
			for (int i = text.length(); i > 0 ; i--) {
				int b = Character.getNumericValue(text.charAt(i-1));
				value += b * pow;
				pow *= 2;
			}
		}else if (lastBase == 10) {
			try {
				value = Integer.parseInt(text);
			}catch (Exception e) {
			}
		}else if (lastBase == 16) {
			int base = 1;
			for (int i = text.length(); i > 0 ; i--) {
				char chara = text.charAt(i-1);
				int b = 0;
				if (chara >= 'A' && chara <= 'F') {
					b = chara - 55;
				}else {
					b = Character.getNumericValue(chara);
				}
				value += b * base;
				base *= 16;
			}
		}
		return value;
	}

	static private String toBinary(int value) {
		StringBuilder builder = new StringBuilder();
		if (value == 0) {
			return "0";
		}
		while(value != 0) {
			if ((value % 2 != 0)) {
				builder.append("1");
				value--;
			}else {
				builder.append("0");
			}
			value /= 2;
		}
		return builder.reverse().toString();
	}

	static private String toHexa(int value) {
		StringBuilder builder = new StringBuilder();
		if (value == 0) {
			return "0";
		}
		while(value != 0) {
			if ((value % 16 != 0)) {
				if(value % 16 > 9) {
					builder.append((char)(55+(value % 16)));
				}else {
					builder.append(value % 16);
				}
				value -= value % 16;
			}else {
				builder.append("0");
			}
			value /= 16;
		}
		return builder.reverse().toString();
	}

	public static class BaseItemListener implements ItemListener{

		private JFormattedTextField field;
		private int lastBase;

		public BaseItemListener(JFormattedTextField field) {
			this.field = field;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {

			if (e.getItem() instanceof BasesRadioButton) {
				BasesRadioButton button = (BasesRadioButton)e.getItem();
				if (button.isSelected()) {
					int a = button.getBase();
					String textA = field.getText();
					String text = button.convert(textA, lastBase);
					if (a == 2) {
						field.setDocument(new CalculadoraDocument("01+-*/"));
					}else if (a == 10) {
						field.setDocument(new CalculadoraDocument("0123456789+-*/"));
					}else if (a == 16) {
						field.setDocument(new CalculadoraDocument("0123456789abcdefABCDEF+-*/"));
					}
					field.setText(text);
				}else {
					lastBase = button.getBase();
				}

			}

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
		}

		@Override
		public void keyReleased(KeyEvent e) {
			char c = e.getKeyChar();
			if (c == KeyEvent.VK_ENTER) {
				String message = "";
				try {
					message = writeTable(((JFormattedTextField)e.getComponent()).getText());
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
			int mask = 1 << size-1;

			for (int variavel = 0 ; variavel < size; variavel ++) {
				Character v = variaveis.get(size-variavel-1);
				buffer.append(v+"|");
			}
			// escreve a fórmula que será resolvida com chaves (ex: input = A|X output = [A|X])
			buffer.append("["+text+"]\n");
			for (int possibilidade = 0; possibilidade < possibilidades; possibilidade++) {
				String textP = text;
				int p = possibilidade;
				for (int variavel = 0 ; variavel < size; variavel ++) {
					Character v = variaveis.get(size-variavel-1);
					char r = (p & mask)==0?'0':'1';
					textP = textP.replace(v, r);
					p <<= 1;
					// aqui é para cada variável em sequencia para cada possibilidade
					buffer.append(r+"|");
				}
				StringBuilder builder = new StringBuilder(textP);
				while (builder.length() > 1) {
					int f = builder.indexOf(")");
					int i = -1;
					if (f > 0) {
						i = builder.substring(0, f).lastIndexOf("(");
					}
					if (i > -1) {
						String re = resolve(builder.substring(i+1, f));
						if (re == null) {
							message = "<html><p style=\"text-align:center;color:red;\">Erro ao resolver!</p></html>";
							break;
						}else {
							builder.replace(i, f+1, re);
						}
					}else if (builder.length() > 1) {
						String re = resolve(builder.toString());
						if (re == null) {
							message = "<html><p style=\"text-align:center;color:red;\">Erro ao resolver!</p></html>";
							break;
						}else {
							builder = new StringBuilder(re);
						}
					}
				}
				// aqui é a solução de cada possibilidade
				buffer.append(builder.toString()+"\n");
			}
			buffer.close();
			writer.close();
			if (message.isEmpty()) {
				message = "<html><p style=\"text-align:center;color:green;\">Feito a tabela</p></html>";
			}else {
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
				if ((a <= o || o < 0) && a >= 0 ) {
					int a1 = textP.indexOf("1&1");
					if (a1 <= a && a1 >= 0) {
						textP = textP.replaceFirst("1&1", "1");
					}else {
						textP = textP.replaceFirst("0&1|1&0|0&0", "0");
					}
				}else if((o <= a || a < 0) && o >= 0 ) {
					int o1 = textP.indexOf("0|0");
					if (o1 <= o && o1 >= 0) {
						textP = textP.replaceFirst("0\\|0", "0");
					}else {
						textP = textP.replaceFirst("0\\|1|1\\|0|1\\|1", "1");
					}
				}else if (a < 0 && o < 0) {
					return null;
				}
			}
			return textP;
		}
	}
}
