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

public class Arduino implements SerialPortEventListener {

	// make it a singleton
	private static Arduino singleton;

	public static Arduino getInstance() {
		if (singleton == null) {
			singleton = new Arduino();
			singleton.initialize();
		}
		return singleton;
	}
	
	public static GUI display;

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

	private String[] lcd = {"Shooter Speed", "Hood Angle", "Turret Angle"};//numbers to get for the lcd
	private String[] abrev = {"SP", "HA", "TA"};//abreviations to the lcd
	
	private String[] singleLEDs= {
			"Drive Low Gear", 
			"Can Gear See", 
			"Shooter Tracking",
			"Shooter Aligned", 
			"Hood Tracking", 
			"Hood Aligned", 
			"Turret Tracking", 
			"Turret Aligned",
			"Ready to Shoot"};
	
	/////////////VARIABLES////////////////
	//double
	//	lcd		| Shooter Speed, Hood Angle, Turret Angle
	//	ledStrip| Match Time
	//boolean	
	//	sigleLed| Can Gear See, Drive High Gear, Hood Aligned, Turret Aligned, Shooter Aligned, Hood Tracking, Turret Tracking, Shooter Tracking
	//////////////////////////////////////
	//lcdPrint(x, y, String)
	//setTimer(double)
	//setLED(LED#, state)
	//////////////////////////////////////
	public void initialize() {
		
		//connect to the robot
		Network.getInstance().connect();
		setRobotListener();//set the event listener for the robot vars
		//TODO: make this work with the GUI and make no connection handeling
		
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
		if (portId == null) {		//arduino not found
			display.dissable();
			return;
		}else {						//found arduino
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

	
	int runThrough = 0;
	
	//talk to the robot
	public void setRobotListener() {
		
		Network.getInstance().setOnMessageReceivedListener(new Network.OnMessageReceivedListener() {

			@Override
			public void onMessageReceived(String key, Object value) {
				runThrough++;
				if(runThrough % 12 == 0) {
				try {
					//read the values
					ArrayList<Double> values = new ArrayList<Double>();
					for(int i = 0; i < lcd.length; i++) {
						values.add(Network.getInstance().getNumber(lcd[i]));
					}
					
					ArrayList<String> sendStr = new ArrayList<String>();//string of commands to send
					
					sendStr.add("clearLcd();");

					//For the LCD screen
					int row = 0;
					for(int i = 0; i < values.size(); i++) {
						
						
						//Which row:
						if(i > 1) {
							row = 13;//new column after row 2
							if(values.get(i) < 0) {
								row--;
							}
							if(Math.abs(values.get(i)) >= 100) {
								row--;
							}
							if(Math.abs(values.get(i)) >= 10) {
								row--;
							}
						}
						
						
						sendStr.add("lcdPrint("
							+ Integer.toString(row)//row
							+ ","
							+ Integer.toString(i%2)//column
							+ ","
							//+ abrev[i]//print name
							//+ ":"
							+ (abrev[i].equals("SP")? Integer.toString(values.get(i).intValue()) : Math.round(values.get(i)*10)/10.0)//value
							+ ");"
						);//finished Ex:"lcdPrint(0, 0, SP:1.2123);
					}
					
					//read the bools
					ArrayList<Boolean> bools= new ArrayList<Boolean>();
					for(int i = 0; i < singleLEDs.length; i++) {
						bools.add(Network.getInstance().getBoolean(singleLEDs[i]));
					}
					//For the single led booleans
					char bool;
					for(int i = 0; i < bools.size(); i++) {
						if(bools.get(i)) bool = '1';
						else bool = '0';
						sendStr.add("setLed("
							+ Integer.toString(i)//led number
							+ ","
							+ bool//'1' || '0'
							+ ");"
						);//finished Ex:"setLED(0, 1);"
					}
					
					//match timer
					String totSec = "135";
					if(Network.getInstance().getBoolean("Is Auto")) {
						totSec = "15";
					}
					sendStr.add("setTimer("//cur time, tot time
						+ Network.getInstance().getNumber("Match Time")
						+ ","
						+ totSec
						+ ");"
					);
					
					//send the data
					for(int i = 0; i < sendStr.size(); i++) {
						if (verify(sendStr.get(i))) {
							sendStr(sendStr.get(i));
							System.out.println("Sending from robot: " + sendStr.get(i));
							GUI.getInstance().appendIn(sendStr.get(i));
						}else {
							GUI.getInstance().appendIn("***Please end with a ';' : " + sendStr.get(i) + "***");
						}
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				}
			}
			
		});
	}
	
	
	
	// the GUI class/obj
	static class GUI extends Frame {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 673042704298200714L;
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
		TextArea robotStatus;
		TextField inText;
		String lastEntry = "";
		int outLine = 0, inLine = 0, index = 0;
		ArrayList<String> cmdBuffer = new ArrayList<String>();

		// make the GUI
		public void initalize() {

			setSize(x, y);// frame size
			setLayout(new GridLayout(5, 1));
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
			
			robotStatus = new TextArea("No Connection to Robot", 1, 40);
			robotStatus.setForeground(Color.RED);
			add(robotStatus);
			
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
							appendIn("***Please end with a ';' : " + inStr + "***");
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

		public void setRobotStatus(String str, boolean redGreen) {
			robotStatus.setText(str);
			if(redGreen) robotStatus.setForeground(Color.GREEN);
			else robotStatus.setForeground(Color.RED);
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

		Arduino.getInstance();//start the Arduino object

	}
}