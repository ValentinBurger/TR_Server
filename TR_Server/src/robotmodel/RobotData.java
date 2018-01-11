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
	private int rotation;	//Right : 0 or Left : 1
	
	public RobotData(int id, int location, int speed, double time, int rotation)
	{
		this.id = id;
		this.location = location;
		this.speed = speed;
		this.time = time;
		this.rotation = rotation;
	}
	
	public int getId() {
		return id;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public double getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	public int getRotation() {
		return rotation;
	}

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