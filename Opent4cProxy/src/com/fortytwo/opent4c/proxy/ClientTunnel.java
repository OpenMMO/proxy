package com.fortytwo.opent4c.proxy;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

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
	 * @param s 
	 * @param receivePacket 
	 * @param micros 
	 * @param stamp 
	 */
	public void getMessage(ServerTunnel s, long stamp, long micros, byte[] data){
		DatagramPacket sendPacket = null;
		if(!Proxy.sniffer){
			if (Proxy.clientVersion == 150){
				Pak150 received = new Pak150(data, Pak150.CLIENT_TO_SERVER, stamp, micros);
				ByteBuffer toSend = received.getSendData();
				Log.client.debug(HexString.from(toSend.array()));
				sendPacket = new DatagramPacket(toSend.array(), toSend.array().length, Proxy.serverAddress, Proxy.serverPort);
			}else if (Proxy.clientVersion == 125){
				//Pak125 decrypted = new Pak125(data, false, stamp, micros);
			}
			s.pile(sendPacket);
		}else{
			Log.proxy.info("C=>S : "+HexString.from(data));
			sendPacket = new DatagramPacket(data, data.length, Proxy.serverAddress, Proxy.serverPort);
			s.pile(sendPacket);
		}
		
	}
}
