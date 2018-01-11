package network;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

import robotmodel.Position;
import robotmodel.RobotData;

/**
 * 
 * @author vburger
 *
 */

public class ReceiveServer {
	
	static int rId, rLocation, rSpeed, rRotation = 0;
	static int deltaTime = 3000;
	static double rTime;
	
	static ArrayList<RobotData> requestSequence = new ArrayList<RobotData>();
	static ArrayList<RobotData> passageSequence = new ArrayList<RobotData>();
	
	/**
	 * Loop for server
	 * @throws IOException
	 */
	public void mainServer() throws IOException
	{
		while(true);
	}
	
	/**
	 * Data processing to retrieve data received from robots
	 * @param Data
	 * @throws SocketException
	 * @throws IOException
	 */
	public void processData(String Data) throws SocketException, IOException
	{
		String delim = "\n";
		String labelRotation = "";
		
		if(Data != null)
		{
			//Get the values from the data received
			String[] values = Data.split(delim);
			
			
			try {
				if(Integer.parseInt(values[0]) != -1)
				{
					try {
						rId = Integer.parseInt(values[0]);
					} catch (NumberFormatException e) {}
					try {
						rLocation = Integer.parseInt(values[1]);
					} catch (NumberFormatException e) {}
					try {
						rSpeed = Integer.parseInt(values[2]);
					} catch (NumberFormatException e) {}
					try {
						rRotation = Integer.parseInt(values[3]);
					} catch (NumberFormatException e) {}

					if(rRotation>0)
					{
						rRotation = 0;
						labelRotation = "Left";
					}else {
						rRotation = 1;
						labelRotation = "Right";
					}
					
					//Register system time when data are processed
					if(rLocation == Position.ENTREE.getID())
						rTime = System.nanoTime()*Math.pow(10,-9);
					
					System.out.println("__________________________________");
					System.out.println("New data : ID : " + rId + " Location : " + rLocation + " Speed : " + rSpeed + " Rotation : " + labelRotation);
					
					updateRequestSequence();
				}else {
					System.out.println("Passage sequence sent : " + Data);
				}
			} catch (NumberFormatException e) {}
		}
	}
	
	/**
	 * Creation and update of the request sequence depending on the data received previously
	 * @throws SocketException
	 * @throws IOException
	 */
	private static void updateRequestSequence() throws SocketException, IOException
	{			
		boolean found = false;
		// Go through the sequence list
		for (RobotData r : requestSequence) 
		{
			// If the Robot is on the sequence list
			if (r.getId() == rId) {
				System.out.println("Update robot attributes in sequence list\n");
				// Refresh it's location and speed
				r.setLocation(rLocation);
				r.setSpeed(rSpeed);
				r.setRotation(rRotation);
				
				//Set the found flag
				found = true;
			}			
		}
		
		// If the Robot is not on the sequence list
		if(!found && rLocation != Position.SORTIE.getID())
		{
			// Add the Robot to the sequence
			System.out.println("New robot added to sequence list\n");
			requestSequence.add(new RobotData(rId, rLocation, rSpeed, rTime, rRotation));
		}
				
		updatePassageSequence();
	}
	
	/**
	 * Creation and update of the passage sequence, based on the request sequence and the Batch politic
	 * @throws SocketException
	 * @throws IOException
	 */
	private static void updatePassageSequence() throws SocketException, IOException
	{
		int rSIndex = 0,pSIndex = 0, pSIndexToRemove = 0, nbActualRS = 0;
		boolean found = false, remove = false;
		
		for (RobotData rS : requestSequence) {
			if(rS.getLocation() != Position.SORTIE.getID())
			{
				nbActualRS++;
			}
		}
		
		if(nbActualRS == 1 && passageSequence.isEmpty())
		{
			for (RobotData rS : requestSequence) {
				if(rS.getLocation() != Position.SORTIE.getID())
				{
					System.out.println("Robot " + rS.getId() + " added to passage sequence\n");
					passageSequence.add(rS);
				}
			}
		}else {	
			for (RobotData rS : requestSequence) {
				if(!passageSequence.isEmpty()) {
					searchLoop:
						for (RobotData pS : passageSequence) {
							//If a robot with the same ID is found on the passage sequence
							if(rS.equals(pS))
							{
								//If the robot is leaving the intersection, remove from passage sequence and requestSequence
								if(rS.getLocation() == Position.SORTIE.getID())
		 						{
									System.out.println("Robot " + rS.getId()  + " leaved intersection : removed from passage list\n");
									pSIndexToRemove =  pSIndex;
									remove = true;
		 						}else {
		 							// If new data from robot already in passageSequence -> Update
		 							System.out.println("Robot " + rS.getId()  + " data updated from passage list\n");
		 							passageSequence.get(pSIndex).setLocation(rS.getLocation());
									passageSequence.get(pSIndex).setSpeed(rS.getSpeed());
									passageSequence.get(pSIndex).setRotation(rS.getRotation());
		 						}
								found = true;
								break searchLoop;
							}
							pSIndex++;
						}
				}
				if(remove)
				{
					passageSequence.remove(pSIndexToRemove);
					remove = false;
				}
				pSIndex = 0;
				
				if(!found)
				{
					if(!passageSequence.isEmpty())
					{
						if(rS.getLocation() != Position.SORTIE.getID()) {
							// If the Robot from the request list is from the same path as the last one from sequence list, add the Robot
							if(passageSequence.get(passageSequence.size()-1).getRotation() == rS.getRotation())
							{
								System.out.println("Robot " + requestSequence.get(rSIndex).getId()  + " added cause same rotation\n");
								passageSequence.add(rS);
							}else {
								if(rS.getTime()-passageSequence.get(passageSequence.size()-1).getTime() >= deltaTime)
								{
									System.out.println("Robot " + requestSequence.get(rSIndex).getId()  + " added cause delta time\n");
									passageSequence.add(rS);
								}
							}
						}
					}else {
						passageSequence.add(rS);
					}
				}
				found = false;
				rSIndex++;
			}
		}
		
		sendPassageSequence();
	}
	
	/**
	 * Send the passage sequence previously created to all the robots
	 * @throws SocketException
	 * @throws IOException
	 */
	private static void sendPassageSequence() throws SocketException, IOException
	{		
		if(passageSequence.isEmpty()) {
			System.out.println("There is nothing to send\n");
		}else {
			String data = "-1\n";
			
			for (RobotData r : passageSequence) {
				data += Integer.toString(r.getId()) + "\n" 
						+ Integer.toString(r.getLocation()) + "\n" 
						+ Integer.toString(r.getSpeed()) + "\n";
			}
			
			data += "-1";
			
			System.out.println("Server is sending passage sequence\n");
			
			CentralizedSync.sendData(data);
		}
	}
}
