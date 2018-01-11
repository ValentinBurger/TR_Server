package network;

import java.io.IOException;
import java.net.SocketException;

/**
 * @author valentin
 *
 */

public class Listener implements BroadcastListener {

	private ReceiveServer myServerListener;
	
	public Listener(ReceiveServer myServer)	{
		this.myServerListener = myServer;
	}
	
	@Override	
	public void onBroadcastReceived(byte[] message) throws SocketException, IOException {
		String data = new String(message);
		
		myServerListener.processData(data);
		
	}
}
