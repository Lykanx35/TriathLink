import java.util.ArrayList;

import com.garmin.fit.DateTime;


public class AnalyzedRecordData {
	
	private RecordData recordData = null;
	private TriathLinkSportType sportType = null;
	
	
	
	public AnalyzedRecordData(RecordData recordData, TriathLinkSportType sportType){
		this.recordData = recordData;
		this.sportType = sportType;
	}
	
	public RecordData getRecordData() {
		return recordData;
	}
	public void setRecordData(RecordData recordData) {
		this.recordData = recordData;
	}
	public TriathLinkSportType getSportType() {
		return sportType;
	}
	public void setSportType(TriathLinkSportType sportType) {
		this.sportType = sportType;
	}
	
	
}
