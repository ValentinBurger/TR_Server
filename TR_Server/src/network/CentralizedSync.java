package network;

import java.io.IOException;
import java.net.SocketException;


/**
 * 
 * @author promet
 *
 */

public class CentralizedSync {	
	
	public static void addServerRcvListner(ReceiveServer myServer) throws IOException
	{
		Listener myListener = new Listener(myServer);
		BroadcastReceiver.getInstance().addListener(myListener);
	}

	/*
	 * Send robots information as a String
	 */
	public static void sendData(String DataToSend) throws SocketException, IOException
	{
		byte[] tmp = DataToSend.getBytes();
		
		BroadcastManager.getInstance().broadcast(tmp);
	}


	
}
