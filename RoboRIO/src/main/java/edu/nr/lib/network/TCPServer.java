package edu.nr.lib.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

import edu.nr.lib.units.GenericUnit;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 * A TCP server that listens for data on a new thread on a given port.
 * Not all ports are allowed to be used by the radio, so this should be accounted for.
 * The 2017 game manual (as of team update 10) allows ports 5800-5810 for non-preallocated team use. 
 * 
 * It takes data in the following format: 
 * byte 0: identifier (character)
 * byte 1-4: data (integer)
 * 
 * It stores data in a {@link NetworkingDataType} according to the identifier.
 * 
 * All values that could be sent to the server need to match the data types that are given it, or they will be ignored.
 * 
 * An object can listen to a {@link NetworkingDataType} by implementing {@link NetworkingDataTypeListener} and calling {@link NetworkingDataType#addListener}
 *
 */
public class TCPServer implements Runnable {

	private boolean m_hasData = false;

	private boolean m_isConnected = false;
		
	private int port;
	
	private Num num;
	
	/**
	 * Allowed for "Team Use" in 2017 game manual
	 */
	public static final int defaultPort = 5800;
	
	/**
	 * Which server to connect to.
	 */
	public enum Num {
		turret, gear;
		
		private TCPServer singleton;
		
		/**
		 * Initialize the singleton without any default {@link NetworkingDataType}s or port number.
		 */
		public synchronized void init() {
			init(defaultPort);
		}
		
		/**
		 * Initialize the singleton without any default {@link NetworkingDataType}s and with the given port.
		 */
		public synchronized void init(int port) {
			if(singleton == null) {
				singleton = new TCPServer(null, port, this);
			}
		}
		

		/**
		 * Initialize the singleton with the given {@link NetworkingDataType}s and default port.
		 * 
		 * If the singleton already initialized, add the given data types to the singleton.
		 * 
		 * @param dataTypes A Collection of {@link NetworkingDataType}. All values that
		 *            		could be sent to the server need to match these data types (or others that are added later).
		 *            
		 *            		If this is null, no data types will be added initially.
		 */
		public synchronized void init(Collection<? extends NetworkingDataType> dataTypes) {
			init(dataTypes, defaultPort);
		}
		
		
		/**
		 * Initialize the singleton with the given {@link NetworkingDataType}s and given port.
		 * 
		 * If the singleton already initialized, add the given data types to the singleton.
		 * 
		 * @param dataTypes A Collection of {@link NetworkingDataType}. All values that
		 *            		could be sent to the server need to match these data types (or others that are added later).
		 *            
		 *            		If this is null, no data types will be added initially.
		 */
		public synchronized void init(Collection<? extends NetworkingDataType> dataTypes, int port) {
			if(dataTypes != null) {
				if(singleton == null) {
					singleton = new TCPServer(dataTypes, port, this);
				} else {
					singleton.addDataTypes(dataTypes);
				}
			} else {
				init();
			}

		}
		
		public synchronized TCPServer getInstance() {
			init();
			
			return singleton;
		}

	}
	
		
	/**
	 * Has the server received data yet?
	 * 
	 * Once this becomes true, it never becomes false
	 */
	public boolean hasData() {
		return m_hasData;
	}

	/**
	 * Is the server connected to a client?
	 */
	public boolean isConnected() {
		return m_isConnected;
	}

	private ArrayList<NetworkingDataType> data;

	/**
	 * Create a TCP server on a new thread.
	 * 
	 * It listens on the given port
	 * 
	 * @param dataTypes
	 *            A Collection of {@link NetworkingDataType}. All values that
	 *            could be sent to the server need to match these data types (or others that are added later).
	 *            
	 *            If this is null, no data types will be added initially.
	 * @param port
	 * 			  The port to listen to. Not all ports are allowed to be used by the radio, so this should be accounted for.
	 * 
	 * 			  The 2017 game manual (as of team update 10) allows ports 5800-5810 for non-preallocated team use.
	 */
	private TCPServer(Collection<? extends NetworkingDataType> dataTypes, int port, Num num) {
		this.port = port;
		
		this.num = num;
		
		data = new ArrayList<>();
		if(dataTypes != null) {
			data.addAll(dataTypes);
		}
		
		new Thread(this).start();
	}
	
	private boolean doesDataHaveSimilarDataTypeAlready(NetworkingDataType dataType) {
		for(NetworkingDataType dataTypeGood : data) {
			if(dataTypeGood.name ==  dataType.name || dataTypeGood.identifier == dataType.identifier) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Add the given {@link NetworkingDataType} to the list to listen for.
	 * 
	 * If the data type overlaps with one already in the list, then it is not added.
	 * 
	 * Overlap happens if either the name or identifier matches any already existing name or identifier.
	 * 
	 * @param dataType
	 * @return false if there is no overlap, true if there is overlap or dataType is null.
	 */
	public boolean addDataType(NetworkingDataType dataType) {
		if(dataType != null) {
			boolean similar = doesDataHaveSimilarDataTypeAlready(dataType);
			
			if(!similar) {
				data.add(dataType);
			}
			
			return similar;
		} else {
			return true;
		}
	}
	
	/**
	 * Add the given {@link NetworkingDataType}s to the list to listen for.
	 * 
	 * If a data type overlaps with one already in the list, then it is not added.
	 * 
	 * Overlap happens if either the name or identifier matches any already existing name or identifier.
	 * 
	 * @param dataTypes The collection of {@link NetworkingDataType} to add
	 * @return false if there is no overlap, true if there is overlap for at least one data type (or dataTypes is null).
	 */
	public boolean addDataTypes(Collection<? extends NetworkingDataType> dataTypes) {
		if(dataTypes != null) {
			boolean similar = false;
			for(NetworkingDataType dataType : dataTypes) {
				if(addDataType(dataType)) {
					similar = true;
				}
			} 
			return similar;
		} else {
			return true;
		}
	}

	/**
	 * Get the data type that matches the identifier
	 * 
	 * @param identifier a character that matches a {@link NetworkingDataType}
	 * @return the data that matches the identifier, or null if none do
	 */
	public NetworkingDataType getType(char identifier) {
		for (NetworkingDataType type : data) {
			if (type.identifier == identifier) {
				return type;
			}
		}
		return null;
	}
	
	/**
	 * Get the data type that matches the name
	 * 
	 * @param identifier a character that matches a {@link NetworkingDataType}
	 * @return the data that matches the identifier, or null if none do
	 */
	public NetworkingDataType getType(String name) {
		for (NetworkingDataType type : data) {
			if (type.name == name) {
				return type;
			}
		}
		return null;
	}

	/**
	 * Get the data type that matches the identifier
	 * 
	 * @param identifier a character that matches a {@link NetworkingDataType}
	 * @return the data that matches the identifier, or 0 if none do
	 */
	public double getValue(char identifier) {
		for (NetworkingDataType type : data) {
			if (type.identifier == identifier) {
				return type.data;
			}
		}
		return 0;
	}

	/**
	 * Get the data type that matches the name
	 * 
	 * @param name a name that matches a {@link NetworkingDataType}
	 * @return the type that matches the name, or 0 if none do
	 */
	public double getValue(String name) {
		for (NetworkingDataType type : data) {
			if (type.name.equals(name)) {
				return type.data;
			}
		}
		return 0;
	}

	@Override
	public void run() {
		while (true) {
			try {
				@SuppressWarnings("resource")
				ServerSocket socket = new ServerSocket(port); 
				
				while (true) {
					m_isConnected = false;
					SmartDashboard.putBoolean("Connected to " + num, false);
					System.out.println("Trying to connect to " + num);
					Socket connectionSocket = socket.accept();
					m_isConnected = true;
					System.out.println("Connected to " + num + "!" );
					SmartDashboard.putBoolean("Connected to " + num, true);
					BufferedReader inFromClient = new BufferedReader(
							new InputStreamReader(connectionSocket.getInputStream()));
					while (!connectionSocket.isClosed()) {
						char firstCharacter = (char) inFromClient.read();
						// inFromClient.read() should really return a character, not an int...
						// Blame Oracle
						
						if(firstCharacter == 65535) { //This is the error code, showing that the camera disconnected.
							break;
						}
						
						NetworkingDataType type = getType(firstCharacter);
						if (type != null) {
							char[] data = new char[4];
							inFromClient.read(data, 0, 4);
							if(data[0] == 0 && data[1] == 0 && data[2] == 0 && data[3] == 0) {
							} else {
								type.setData(((data[0] & 0xFF) << 24) + ((data[1] & 0xFF) << 16) + ((data[2] & 0xFF) << 8)
										+ (data[3] & 0xFF));
							}
							//System.out.println("Data read: 0:" + (int) data[0] + " 1:" + (int) data[1] + " 2:" + (int) data[2] + " 3:" + (int) data[3]);
							m_hasData = true;
							type.updateListeners();
						}
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * A type of data that will be sent to the server. An object can listen to the data type by implementing {@link NetworkingDataTypeListener}.
	 *
	 */
	public static class NetworkingDataType {
		
		ArrayList<NetworkingDataTypeListener> listeners;
		public GenericUnit unit;
		
		/**
		 * Create a networking data type.
		 * 
		 * @param identifier
		 *            A single character that is a short name for the data. Eg
		 *            'a'
		 * @param name
		 *            A string describing the data. Eg "angle"
		 */
		public NetworkingDataType(char identifier, String name, GenericUnit unit) {
			this.identifier = identifier;
			this.name = name;
			this.unit = unit;
			
			this.listeners = new ArrayList<>();
		}

		/**
		 * This is the character that identifies the data type when it is sent.
		 * Ex: for an "angle," the identifier could be 'a'
		 */
		public final char identifier;

		/**
		 * This is the long name of the string. Ex: "angle"
		 */
		public final String name;

		/**
		 * This is the actual data that is sent.
		 */
		private double data = 0;
		
		private void setData(int in) {
			data = convert(in);
		}
		
		public void addListener(NetworkingDataTypeListener listener) {
			listeners.add(listener);
		}
		
		public void removeListener(NetworkingDataTypeListener listener) {
			listeners.remove(listener);
		}
		
		private void updateListeners() {
			for(NetworkingDataTypeListener listener : listeners) {
				listener.updateDataType(this, this.data);
			}
		}
		
		public double convert(int in) {
			return in;
		}

	}

}
