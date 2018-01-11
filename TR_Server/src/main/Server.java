package main;

import java.io.IOException;
import network.ReceiveServer;

/**
 * @author vburger
 *
 */

public class Server {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ReceiveServer myServer = new ReceiveServer();
		
		System.out.println("Server running ...");
		System.out.println("Waiting for data ...\n");
		
		network.CentralizedSync.addServerRcvListner(myServer);
		myServer.mainServer();

	}

}
