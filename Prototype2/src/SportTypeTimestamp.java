import com.garmin.fit.DateTime;


public class SportTypeTimestamp {
	
	private TriathLinkSportType sportType = null;
	private DateTime startTime = null;
	private DateTime endTime = null;
	
	public SportTypeTimestamp(TriathLinkSportType sportType, DateTime startTime, DateTime endTime){
		this.sportType = sportType;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public TriathLinkSportType getSportType() {
		return sportType;
	}
	public void setSportType(TriathLinkSportType sportType) {
		this.sportType = sportType;
	}
	public DateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}
	public DateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}
	
	
}
