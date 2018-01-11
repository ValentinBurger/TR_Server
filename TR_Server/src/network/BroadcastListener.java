package network;

import java.io.IOException;
import java.net.SocketException;

/**
 * Broadcast listener interface
 * @author Alexandre Lombard
 */
public interface BroadcastListener {
	/**
	 * Triggered on broadcast received
	 * @param message the raw message
	 * @throws IOException 
	 * @throws SocketException 
	 */
	public void onBroadcastReceived(byte[] message) throws SocketException, IOException;
}
