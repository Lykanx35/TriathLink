import java.util.ArrayList;


public class TriathLinkAnalyzerNullpunkte {
	private ArrayList<SessionData> sessionData;
	private ArrayList<RecordData> recordData;



	public TriathLinkAnalyzerNullpunkte(ArrayList<SessionData> sessionData, ArrayList<RecordData> recordData) {
		this.sessionData = sessionData;
		this.recordData = recordData;
	}


	//Cycle through Data and define Starttimes
	public ArrayList<AnalyzedRecordData> analyze(){
		ArrayList<AnalyzedRecordData> returnData = new ArrayList<AnalyzedRecordData>();
		
		
		TriathLinkSportType currentSport = TriathLinkSportType.NOT_STARTED;
		TriathLinkSportType lastSport = TriathLinkSportType.NOT_STARTED;
		RecordData lastRecord = null; //no last record yet
		RecordData lastLastRecord = null; //no last record yet

		
		int counterSinceChange = 0;
		boolean lockChange = false;

		
		for (RecordData currentRecord : recordData) {

			
			if(lastRecord == null)
				lastRecord = currentRecord;
			if(lastLastRecord == null)
				lastLastRecord = currentRecord;

			
			currentSport = getCurrentSportType(lastSport, currentRecord, lastRecord, lastLastRecord);
			
			returnData.add(new AnalyzedRecordData(currentRecord, currentSport));
			
			if(currentSport != lastSport){ //TODO Change Sport function
				if(lockChange == true && counterSinceChange < 25){
					counterSinceChange++;
					continue;
				}
				else{
					lockChange = false;
					counterSinceChange = 0;
				}
				
				lastSport = currentSport;
				lockChange = true; 
			}
			
			if(lockChange == true && counterSinceChange < 25)
				counterSinceChange++;
			
			lastLastRecord = lastRecord;
			lastRecord = currentRecord;
		}
		return returnData;
	}


	private TriathLinkSportType getCurrentSportType(TriathLinkSportType lastSport, RecordData currentRecord, RecordData lastRecord, RecordData lastlastRecord) {
		if(lastSport == TriathLinkSportType.NOT_STARTED && currentRecord.getSpeed() >= 0.1f)
			return getNextSportType(lastSport);
		if(currentRecord.getSpeed() <= 0.1f && lastRecord.getSpeed() <= 0.1f && lastlastRecord.getSpeed() <= 0.1f)
			return getNextSportType(lastSport);

		return lastSport;
	}

	private TriathLinkSportType getNextSportType(TriathLinkSportType sportType){

		switch(sportType){
		case NOT_STARTED:
			return TriathLinkSportType.SWIMMING;
		case SWIMMING:
			return TriathLinkSportType.CYCLING;
		case CYCLING:
			return TriathLinkSportType.RUNNING;
		case RUNNING:
			return TriathLinkSportType.FINISHED;
		case FINISHED:
			return TriathLinkSportType.FINISHED;
		}

		return sportType;
	}

}
