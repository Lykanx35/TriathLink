import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;



public class TriathLinkAnalyzer {
	private ArrayList<SessionData> sessionData;
	private ArrayList<RecordData> recordData;
	
	private ArrayList<Date> transitionTimestamps = null;
	private HashMap<String, Float> classificationParameters;
	
	private int triathletDoesNotStartSwimmingAgainCounter;
	private int triathletDoesNotStartCyclingAgainCounter;
	private int triathletDoesNotStartRunningAgainCounter;
	private int triathletIsInTransitionCounter;
	
	
	public TriathLinkAnalyzer(ArrayList<SessionData> sessionData, ArrayList<RecordData> recordData) throws FileNotFoundException {
		this.sessionData = sessionData;
		this.recordData = recordData;
		transitionTimestamps = new ArrayList<Date>();
		
		TriathLinkAnalyzerInit();
	}

	private void TriathLinkAnalyzerInit() throws FileNotFoundException {

		classificationParameters = new HashMap<>();
        Scanner scanner = new Scanner(new File("TriathlinkAnalyzerInit.csv"));
        scanner.useDelimiter(";");
        
        while (scanner.hasNext())
        	classificationParameters.put(scanner.next().trim(), scanner.nextFloat());
        
        scanner.close();
        
        setFutureRecordsCounter();
		
	}

	public ArrayList<AnalyzedRecordData> analyze(){
		ArrayList<AnalyzedRecordData> returnData = new ArrayList<AnalyzedRecordData>();
		int positionOfCurrentRecord = 0;
		TriathLinkSportType currentSport = TriathLinkSportType.NOT_STARTED;
		TriathLinkSportType lastSport = TriathLinkSportType.NOT_STARTED;
		
		for (RecordData currentRecord : recordData) {
			currentSport = getCurrentSportType(lastSport, currentRecord, positionOfCurrentRecord);
			
			returnData.add(new AnalyzedRecordData(currentRecord, currentSport));
			
			if(currentSport != lastSport){
				transitionTimestamps.add(new Date(currentRecord.getTimestamp().getTime()));
				lastSport = currentSport;
			}

			if(positionOfCurrentRecord == recordData.size()-1 && currentSport != TriathLinkSportType.FINISHED)
				transitionTimestamps.add(currentRecord.getTimestamp());
			
			positionOfCurrentRecord++;
		}
		
		return returnData;
	}
	
	
	public ArrayList<Date> getTransitionTimestamps() {
		return transitionTimestamps;
	}

	private TriathLinkSportType getCurrentSportType(TriathLinkSportType lastSport, RecordData currentRecord, int positionOfCurrentRecord){
		TriathLinkSportType currentSport = lastSport;
		
		switch(lastSport){
		case NOT_STARTED:
			if(triathletStartsSwimming(currentRecord) || triathletStartsRunning(currentRecord) && triathletDoesStartSwimmingAgain(currentRecord, positionOfCurrentRecord))
				currentSport = TriathLinkSportType.getNextSportType(lastSport);
			break;
			
		case SWIMMING:
			if(triathletStopsSwimmming(currentRecord) && !triathletDoesStartSwimmingAgain(currentRecord, positionOfCurrentRecord ) || triathletStartsRunning(currentRecord))
				currentSport = TriathLinkSportType.getNextSportType(lastSport);
			break;
			
		case FIRST_TRANSITION:
			if(triathletStartsCycling(currentRecord) && !triathletIsInTransition(currentRecord, positionOfCurrentRecord))
				currentSport = TriathLinkSportType.getNextSportType(lastSport);
			break;
			
		case CYCLING:
			if(triathletStopsCycling(currentRecord) && !triathletDoesStartCyclingAgain(currentRecord, positionOfCurrentRecord ))
				currentSport = TriathLinkSportType.getNextSportType(lastSport);
			break;
			
		case SECOND_TRANSITION:
			if(triathletStartsRunning(currentRecord) && !triathletIsInTransition(currentRecord, positionOfCurrentRecord))
				currentSport = TriathLinkSportType.getNextSportType(lastSport);
			break;
			
		case RUNNING:
			if(triathletStopsRunning(currentRecord) && !triathletDoesStartRunningAgain(currentRecord, positionOfCurrentRecord ))
				currentSport = TriathLinkSportType.getNextSportType(lastSport);
			break;
			
		case FINISHED:
			currentSport = TriathLinkSportType.FINISHED;
			break;
		}
		return currentSport;
	}

	private boolean triathletIsInTransition(RecordData currentRecord, int positionOfCurrentRecord) {
		int cnt_zeros = 0;
		
		for (int i = positionOfCurrentRecord; i < positionOfCurrentRecord + triathletIsInTransitionCounter; i++) {
			if(i > recordData.size()-1)
				break;
			if(recordData.get(i).getSpeed() <= classificationParameters.get("notMoving"))
				cnt_zeros++;
		}
		if(cnt_zeros >= 5)
			return true;
		
		return false;
	}

	private boolean triathletStartsSwimming(RecordData currentRecord) {
		
		if(currentRecord.getSpeed() >= classificationParameters.get("swimmingSpeedMin") && currentRecord.getSpeed() <= classificationParameters.get("swimmingSpeedMax"))
			return true;
		
		return false;
	}

	private boolean triathletStopsSwimmming(RecordData currentRecord) {
		if(currentRecord.getSpeed() <= classificationParameters.get("notMoving"))
				return true;
		return false;
	}

	private boolean triathletDoesStartSwimmingAgain(RecordData currentRecord, int positionOfCurrentRecord) {
		
		for (int i = positionOfCurrentRecord; i < positionOfCurrentRecord + triathletDoesNotStartSwimmingAgainCounter; i++) {
			if(i > recordData.size()-1)
				break;
			if(triathletStartsSwimming(recordData.get(i)))
				return true;
		}
		
		return false;
	}

	private boolean triathletStartsCycling(RecordData currentRecord) {
		if(currentRecord.getSpeed() >= classificationParameters.get("cyclingSpeedMin"))
			return true;
		
		return false;
	}
	
	private boolean triathletStopsCycling(RecordData currentRecord) {
		
		if(currentRecord.getSpeed() <= classificationParameters.get("cyclingSpeedMin"))
			return true;
		
		return false;
	}
	
	private boolean triathletDoesStartCyclingAgain(RecordData currentRecord, int positionOfCurrentRecord) {
		
		for (int i = positionOfCurrentRecord; i < positionOfCurrentRecord + triathletDoesNotStartCyclingAgainCounter; i++) {
			if(i > recordData.size()-1)
				break;
			if(triathletStartsCycling(recordData.get(i)))
				return true;
		}
		return false;
	}

	private boolean triathletStartsRunning(RecordData currentRecord) {
		if(currentRecord.getSpeed() >= classificationParameters.get("runningSpeedMin") && currentRecord.getSpeed() <= classificationParameters.get("runningSpeedMax"))
			return true;
		
		return false;
	}

	private boolean triathletStopsRunning(RecordData currentRecord) {
		if(currentRecord.getSpeed() <= classificationParameters.get("notMoving"))
				return true;
		
		return false;
	}

	private boolean triathletDoesStartRunningAgain(RecordData currentRecord, int positionOfCurrentRecord) {
		
		for (int i = positionOfCurrentRecord; i < positionOfCurrentRecord + triathletDoesNotStartRunningAgainCounter; i++) {
			if(i > recordData.size()-1)
				break;
			if(triathletStartsRunning(recordData.get(i)))
				return true;
		}
		return false;
	}
	
	private void setFutureRecordsCounter(){
		triathletDoesNotStartSwimmingAgainCounter = (int) (recordData.size() * classificationParameters.get("futureSwimmingRecordsPercentage"));
		triathletDoesNotStartCyclingAgainCounter = (int) (recordData.size() * classificationParameters.get("futureCyclingRecordsPercentage"));
		triathletDoesNotStartRunningAgainCounter = (int) (recordData.size() * classificationParameters.get("futureRunningRecordsPercentage"));
		triathletIsInTransitionCounter = (int) (recordData.size() * classificationParameters.get("futureTransitioningRecordsPercentage"));
	}
}
