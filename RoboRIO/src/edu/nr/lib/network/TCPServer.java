package edu.nr.lib.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * A TCP server that listens for data on a new thread.
 * 
 * It takes data in the following format: byte 0: identifier (character)
 * byte 1-4: data (int)
 * 
 * It stores data in a NetworkingDataType according to the identifier.
 * 
 * All values that could be sent to the server need to match the data types that are given it, or they will be ignored
 *
 */
public class TCPServer implements Runnable {

	private boolean m_hasData = false;

	private boolean m_isConnected = false;
	
	private static TCPServer singleton;
	
	/**
	 * Initialize the singleton
	 */
	public static void init() {
		if(singleton == null) {
			singleton = new TCPServer(null);
		}
	}
	
	/**
	 * Initialize the singleton with the given data types.
	 * 
	 * If the singleton already initialized, add the given data types to the singleton.
	 * 
	 * @param dataTypes A Collection of TCPServer.NetworkingDataType. All values that
	 *            		could be sent to the server need to match these data types (or others that are added later).
	 *            
	 *            		If this is null, no data types will be added initially.
	 */
	public static void init(Collection<? extends NetworkingDataType> dataTypes) {
		if(dataTypes != null) {
			if(singleton == null) {
				singleton = new TCPServer(dataTypes);
			} else {
				singleton.addDataTypes(dataTypes);
			}
		} else {
			init();
		}
	}
	
	public static TCPServer getInstance() {
		init();
		return singleton;
	}
	
	/**
	 * Has the robot received data yet?
	 * 
	 * Once this becomes true, it never becomes false
	 */
	public boolean hasData() {
		return m_hasData;
	}

	/**
	 * Is the robot connected to a client?
	 */
	public boolean isConnected() {
		return m_isConnected;
	}

	private ArrayList<NetworkingDataType> data;

	/**
	 * Create a TCP server on a new thread.
	 * 
	 * @param dataTypes
	 *            A Collection of TCPServer.NetworkingDataType. All values that
	 *            could be sent to the server need to match these data types (or others that are added later).
	 *            
	 *            If this is null, no data types will be added initially.
	 */
	private TCPServer(Collection<? extends NetworkingDataType> dataTypes) {
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
	 * Add the given data type to the list to listen for.
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
	 * Add the given data types to the list to listen for.
	 * 
	 * If a data type overlaps with one already in the list, then it is not added.
	 * 
	 * Overlap happens if either the name or identifier matches any already existing name or identifier.
	 * 
	 * @param dataTypes The collection of NetworkingDataTypes to add
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
	 * @param identifier a character that matches a NetworkingDataType
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
	 * @param identifier a character that matches a NetworkingDataType
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
	 * @param identifier a character that matches a NetworkingDataType
	 * @return the data that matches the identifier, or 0 if none do
	 */
	public int getValue(char identifier) {
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
	 * @param name a name that matches a NetworkingDataType
	 * @return the type that matches the name, or 0 if none do
	 */
	public int getValue(String name) {
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
				ServerSocket socket = new ServerSocket(5800); 
				// Allowed for "Team Use" in 2017 game manual
				
				while (true) {
					m_isConnected = false;
					System.out.println("Trying to connect");
					Socket connectionSocket = socket.accept();
					m_isConnected = true;
					System.out.println("Connected!");
					BufferedReader inFromClient = new BufferedReader(
							new InputStreamReader(connectionSocket.getInputStream()));
					while (!connectionSocket.isClosed()) {
						char firstCharacter = (char) inFromClient.read();
						// inFromClient.read() should really return a character, not an int...
						// Blame Oracle
						NetworkingDataType type = getType(firstCharacter);
						if (type != null) {
							char[] data = new char[4];
							inFromClient.read(data, 0, 4);
							type.data = ((data[0] & 0xFF) << 24) + ((data[1] & 0xFF) << 16) + ((data[2] & 0xFF) << 8)
									+ (data[3] & 0xFF);
							m_hasData = true;
						} else {
							inFromClient.read(new char[4], 0, 4);
						}
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static class NetworkingDataType {

		/**
		 * Create a networking data type.
		 * 
		 * @param identifier
		 *            A single character that is a short name for the data. Eg
		 *            'a'
		 * @param name
		 *            A string describing the data. Eg "angle"
		 */
		public NetworkingDataType(char identifier, String name) {
			this.identifier = identifier;
			this.name = name;
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
		private int data = 0;

	}

}
