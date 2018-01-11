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

// For test purposes : echo -e "0\n0\n330\n0" | nc -u -w1 localhost 8888

public class ReceiveServer {
	
	static int rId, rLocation, rSpeed, rRotation = 0;
	static int deltaTime = 3000;
	static double rTime;
	
	static ArrayList<RobotData> requestSequence = new ArrayList<RobotData>();
	static ArrayList<RobotData> passageSequence = new ArrayList<RobotData>();
	
	public void mainServer() throws IOException
	{
		while(true);
	}
	
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
					//System.out.println("Message : " + values[0] + "/" + values[1] + "/" + values[2]+ "/" + values[3]);
					try {
						rId = Integer.parseInt(values[0]);
					} catch (NumberFormatException e) {
						//System.out.println("Invalid data 0\n");
					}
					try {
						rLocation = Integer.parseInt(values[1]);
					} catch (NumberFormatException e) {
						//System.out.println("Invalid data 1\n");
					}
					try {
						rSpeed = Integer.parseInt(values[2]);
					} catch (NumberFormatException e) {
						//System.out.println("Invalid data 2\n");
					}
					try {
						rRotation = Integer.parseInt(values[3]);
					} catch (NumberFormatException e) {
						//System.out.println("Invalid data 3\n");
					}
					//int oldRotation = rRotation;
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
					System.out.println("New data : ID : " + rId + " Location : " + rLocation + " Speed : " + rSpeed + " Rotation : " + labelRotation/* + " Value : " + rRotation*/);
					
//					if(rLocation==0)
//					{
//						System.out.println("__________________________________");
//						System.out.println("Robot " + rId + " entered " + labelRotation);
//					}else if(rLocation ==3)
//					{
//						System.out.println("__________________________________");
//						System.out.println("Robot " + rId + " left");
//					}
					
					updateRequestSequence();
				}else {
					System.out.println("Passage sequence sent : " + Data);
				}
			} catch (NumberFormatException e) {
				//System.out.println("Invalid data : \n" + Data);
			}
		}
	}
	
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
	
	private static void updatePassageSequence() throws SocketException, IOException
	{
		int rSIndex = 0,pSIndex = 0, /*rSIndexToRemove = 0,*/pSIndexToRemove = 0, nbActualRS = 0;
		boolean found = false, remove = false;
		
		for (RobotData rS : requestSequence) {
			if(rS.getLocation() != Position.SORTIE.getID())
			{
				nbActualRS++;
			}
		}
		
		if(/*requestSequence.size()*/ nbActualRS == 1 && passageSequence.isEmpty())
		{
			for (RobotData rS : requestSequence) {
				if(rS.getLocation() != Position.SORTIE.getID())
				{
					//System.out.println("Passage Sequence is empty and there is only one robot on request sequence");
					System.out.println("Robot " + rS.getId() + " added to passage sequence\n");
					passageSequence.add(rS);
				}else {
					//System.out.println("Robot " + rS.getId() + " leaved the intersection\n");
				}
			}
		}else {
			/*ArrayList<RobotData> requestSequenceCopy = requestSequence;
			ArrayList<RobotData> passageSequenceCopy = passageSequence;*/
			
			for (RobotData rS : requestSequence/*Copy*/) {
				if(!passageSequence/*Copy*/.isEmpty()) {
					searchLoop:
						for (RobotData pS : passageSequence/*Copy*/) {
							//If a robot with the same ID is found on the passage sequence
							if(rS.equals(pS))
							{
								//System.out.println("Robot " + rS.getId()  +" found in passage list\n");
								//If the robot is leaving the intersection, remove from passage sequence and requestSequence
								if(rS.getLocation() == Position.SORTIE.getID())
		 						{
									System.out.println("Robot " + rS.getId()  + " leaved intersection : removed from passage and sequence list\n");
									//passageSequence.remove(pSIndex);
									//requestSequence.remove(rSIndex);
									//rSIndexToRemove =  rSIndex;
									pSIndexToRemove =  pSIndex;
									//rSIndex--;
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
					//System.out.println("Robot " + requestSequence.get(rSIndex).getId() + " not found in passage list\n");
					/*if(rS.getLocation() == Position.SORTIE.getID()) {
						//requestSequence.remove(rSIndex);
						//rSIndex--;
					}else {*/
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
