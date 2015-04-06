package com.fortytwo.opent4c.proxy;

import java.net.DatagramPacket;
import java.net.InetAddress;

import com.fortytwo.opent4c.tools.HexString;
import com.fortytwo.opent4c.tools.Log;

/**
 * Manages client<->proxy connection.
 * We have a thread looking periodically to a thread-safe list's size.
 * If it's not empty, we send the first element and delete it from the list.
 * If it's empty we wait for a while.
 * 
 * We have another thread waiting for messages from client, modifying them, and adding them to server send pile.
 * 
 * @author syno
 *
 */
public class ClientTunnel {
	public int port = -1;
	public InetAddress ip;

	public ClientTunnel(InetAddress ip, int port) {
		this.port = port;
		this.ip = ip;
		Log.client.info("New client : "+ip.toString()+":"+port);
	}

	/**
	 * Waits for a message to be received from a client, modifies it and sends it to the server.
	 * Timestamping will be useful later, for logging with micro precision, compute latencies and such.
	 * As we don't know packet size before receiving it, we always prepare a maxed size buffer (1024 bytes).
	 * @param receivePacket 
	 * @param micros 
	 * @param stamp 
	 */
	public void getMessage(long stamp, long micros, DatagramPacket receivePacket){
		if (Proxy.clientVersion == 150){
			Pak150 decrypted = new Pak150(receivePacket, Pak150.CLIENT_TO_SERVER, stamp, micros);
			Pak150 encrypted = new Pak150(decrypted.getType(), false, decrypted.getData());
			Log.client.trace(HexString.from(encrypted.getData()));
			DatagramPacket sendPacket = new DatagramPacket(encrypted.getData(), encrypted.getData().length, Proxy.serverAddress, Proxy.serverPort);
			new Pak150(sendPacket, false, stamp, micros);
			//ServerTunnel.pile(sendPacket);
			//sendData = decrypted.getSendData();
		}else if (Proxy.clientVersion == 125){
			Pak125 decrypted = new Pak125(receivePacket, false, stamp, micros);
			//sendData = decrypted.getSendData();
		}
		//DatagramPacket sendPacket = new DatagramPacket(receiveData, receiveData.length, Proxy.serverAddress, Proxy.serverPort);
		//DatagramPacket sendPacket = new DatagramPacket(sendData.array(), sendData.array().length, Proxy.serverAddress, Proxy.serverPort);
		//ServerTunnel.pile(sendPacket);
	}
}
