package edu.nr.lib.network;

import edu.nr.lib.network.TCPServer.NetworkingDataType;

/**
 * A listener that listens for updates to {@link NetworkingDataType}
 * 
 * To begin listening for updates, call {@link NetworkingDataType#addListener}
 * 
 * A single listener can listen to multiple instances of NetworkingDataType by calling {@link NetworkingDataType#addListener} multiple times.
 *
 */
public interface NetworkingDataTypeListener {
	
	public void updateDataType(TCPServer.NetworkingDataType type, double data);

}