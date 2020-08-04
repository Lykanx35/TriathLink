import java.util.Date;


public class SessionData {
	private Date startTime;
	private Float totalElapsedTime;
	
	public SessionData(){
		this.startTime = null;
		this.totalElapsedTime = 0.0f;
	}

	
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Float getTotalElapsedTime() {
		return totalElapsedTime;
	}

	public void setTotalElapsedTime(Float totalEllapsedTime) {
		this.totalElapsedTime = totalEllapsedTime;
	}
	
	
}
