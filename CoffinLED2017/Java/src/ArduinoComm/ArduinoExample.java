package ArduinoComm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.ArrayList;
import java.util.Enumeration;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ArduinoExample implements SerialPortEventListener {

	// make it a singleton
	private static ArduinoExample singleton;

	public static ArduinoExample getInstance() {
		if (singleton == null) {
			singleton = new ArduinoExample();
			singleton.initialize();
		}
		return singleton;
	}

	private static GUI display;

	SerialPort serialPort;
	/** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { "/dev/tty.usbserial-A9007UX1", // Mac
																				// OS X
			"/dev/ttyACM0", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM1", // Windows
			"COM2", // Windows
			"COM3", // Windows
			"COM4", // Windows
			"COM5", // Windows
			"COM6", // Windows
			"COM7", // Windows
	};
	/**
	 * A BufferedReader which will be fed by a InputStreamReader converting the
	 * bytes into characters making the displayed results codepage independent
	 */
	private BufferedReader input;
	/** The output stream to the port */
	private static OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	public void initialize() {
		

		// init GUI
		display = GUI.getInstance();
		
		CommPortIdentifier portId = null;
		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();
		// First, Find an instance of serial port as set in PORT_NAMES.
		// aka: fund the arduino
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {//arduino not found
			display.dissable();
			return;
		}else {//found arduino
			display.enablee();
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port. This will prevent
	 * port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	// function to send data to the arduino
	private static void sendStr(String data) {
		try {
			output.write(data.getBytes());
		} catch (Exception e) {
			display.appendOut(e.toString());
		}
	}

	// function to check if string ends in ';'
	private static boolean verify(String str) {
		if(str.length() < 2)//actually contains more than just a ';'
			return false;
		else if (str.charAt(str.length() - 1) == ';')
			return true;
		else
			return false;
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	// data happens
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				display.appendOut(input.readLine());

				// echo the input string to the console
			} catch (Exception e) {
				//System.err.println(e.toString());
				display.appendOut(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other
		// ones.
	}

	// the GUI class/obj
	private static class GUI extends Frame {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		// make it a singleton
		private static GUI singleton;

		public static GUI getInstance() {
			if (singleton == null) {
				singleton = new GUI();
				singleton.initalize();
			}
			return singleton;
		}

		// GUI elements
		int x = 325 /*1000 for 4k*/, y = 475 /*1500 for 4k*/;
		TextArea outText;
		TextArea inTextOut;
		TextArea status;
		TextField inText;
		String lastEntry = "";
		int outLine = 0, inLine = 0, index = 0;
		ArrayList<String> cmdBuffer = new ArrayList<String>();

		// make the GUI
		public void initalize() {

			setSize(x, y);// frame size
			setLayout(new GridLayout(4, 1));
			setVisible(true);// now frame will be visible, by default not visible
			setFont(new Font("TimesRoman", Font.PLAIN, 14 /*36 for 4k*/ ));
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent we) {
					dispose();
					System.exit(NORMAL);
				}
			});
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					inText.setText("Enter a command");
				}
			});

			status = new TextArea(2, 25);//arduino available/unavailable
			status.setText("Arduino unavailable");
			status.setForeground(Color.RED);
			add(status);
			
			outText = new TextArea();
			outText.setSize(x, y / 3);
			add(outText);

			inTextOut = new TextArea();
			inTextOut.setSize(x, y / 3);
			add(inTextOut);

			inText = new TextField("Enter a command here", 30);
			inText.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						// return key pressed
						String inStr = inText.getText();
						lastEntry = inStr;
						cmdBuffer.add(lastEntry);
						index = cmdBuffer.size();
						
						if (verify(inStr)) {
							sendStr(inStr);
							appendIn(inStr);
						}else {
							appendIn("***Please ekghvnd with a ';' : " + inStr + "***");
						}
						inText.setText("");
					}
					if(e.getKeyCode() == KeyEvent.VK_UP) {
						if(index - 1 >= 0) {
							index--;
							inText.setText((String) cmdBuffer.get(index));
						}
					}
					if(e.getKeyCode() == KeyEvent.VK_DOWN) {
						if(index + 1 < cmdBuffer.size()) {
							index++;
							inText.setText((String) cmdBuffer.get(index));
						}
					}
				}
			});
			inText.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(inText.getText().equals("Enter a command here")) {
						inText.setText("");
					}
				}
			});
			add(inText);

			setSize(x + 500, y);
			
			inTextOut.append("-------------Past Commands-------------\n");
			outText.append("-------------Arduino Ouput-------------\n");
		}

		public void appendIn(String str) {
			inTextOut.append(inLine + " | " + str + "\n");
			inLine++;
		}

		public void appendOut(String str) {
			outText.append(outLine + " | " + str + "\n");
			outLine++;
		}
		
		public void dissable() {
			status.setText("Arduino unavailable");
			status.setForeground(Color.RED);
		}
		public void enablee() {
			status.setText("Arduino available");
			status.setForeground(new Color(0, 155, 0));
		}
	}

	// MAIN

	public static void main(String[] args) throws Exception {

		ArduinoExample.getInstance();//start the ArduinoExample object

	}
}