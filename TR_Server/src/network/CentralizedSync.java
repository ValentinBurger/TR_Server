package network;

import java.io.IOException;
import java.net.SocketException;


/**
 * 
 * @author promet
 * modified by vburger
 *
 */

public class CentralizedSync {	
	
	public static void addServerRcvListner(ReceiveServer myServer) throws IOException
	{
		Listener myListener = new Listener(myServer);
		BroadcastReceiver.getInstance().addListener(myListener);
	}

	public static void sendData(String DataToSend) throws SocketException, IOException
	{
		byte[] tmp = DataToSend.getBytes();
		
		BroadcastManager.getInstance().broadcast(tmp);
	}
	
}
