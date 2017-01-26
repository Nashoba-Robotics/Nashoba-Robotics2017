package edu.nr.lib.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements Runnable {

	private boolean m_hasData = false;

	private boolean m_isConnected = false;

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

	private NetworkingDataType[] data;

	/**
	 * Create a TCP server on a new thread.
	 * 
	 * It takes data in the following format: byte 0: identifier (character)
	 * byte 1-4: data (int)
	 * 
	 * It stores data in a NetworkingDataType according to the identifier.
	 * 
	 * @param dataTypes
	 *            An array of TCPServer.NetworkingDataType. All values that
	 *            could be sent to the server need to match these data types.
	 */
	public TCPServer(NetworkingDataType[] dataTypes) {
		this.data = dataTypes;
		new Thread(this).start();
	}

	/**
	 * Get the data type that matches the identifier
	 * 
	 * @param identifier a character that matches a NetworkingDataType
	 * @return the data that matches the identifier, or null if none do
	 */
	NetworkingDataType getTypeForIdentifier(char identifier) {
		for (NetworkingDataType type : data) {
			if (type.identifier == identifier) {
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
	public int getValueForIdentifier(char identifier) {
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
	public int getValueForName(String name) {
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
						NetworkingDataType type = getTypeForIdentifier(firstCharacter);
						if (type != null) {
							char[] data = new char[4];
							inFromClient.read(data, 0, 4);
							type.data = ((data[0] & 0xFF) << 24) + ((data[1] & 0xFF) << 16) + ((data[2] & 0xFF) << 8)
									+ (data[3] & 0xFF);
							m_hasData = true;
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
