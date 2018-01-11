package robotmodel;

/**
 * 
 * @author promet
 * and modify by vburger
 *
 */

public class RobotData {
	private int id;
	private int location;
	private int speed;
	private double time;
	private int rotation;
	
	public RobotData(int id, int location, int speed, double time, int rotation)
	{
		this.id = id;
		this.location = location;
		this.speed = speed;
		this.time = time;
		this.rotation = rotation;
	}
	
	/**
	 * Return robot ID
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Return robot Location
	 * @return
	 */
	public int getLocation() {
		return location;
	}

	/**
	 * Set location for the robot
	 * @param location
	 */
	public void setLocation(int location) {
		this.location = location;
	}

	/**
	 * Return the speed of the robot
	 * @return
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Set robot speed
	 * @param speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	/**
	 * Return time of the request sent by the server
	 * @return
	 */
	public double getTime() {
		return time;
	}

	/**
	 * Set current time of the server to the robot
	 * @param time
	 */
	public void setTime(int time) {
		this.time = time;
	}
	
	/**
	 * Return last curve followed by the robot
	 * @return
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * Set the rotation of the robot
	 * @param rotation
	 */
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RobotData)
		{
			if(this.id==((RobotData)obj).id) {
				return true;
			}
		}
		return false;
	}

}