import java.util.Date;



public class RecordData {
	private Float speed;
	private Date timestamp;
	private Short heartRate;
	
	public RecordData(){
		this.speed = 0.0f;
		this.timestamp = null;
		this.heartRate = 0;
	}
	

	public Float getSpeed() {
		return speed;
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Short getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(Short heartRate) {
		this.heartRate = heartRate;
	}
	
}
