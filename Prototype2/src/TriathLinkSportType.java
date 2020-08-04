
public enum TriathLinkSportType {
	NOT_STARTED,
	SWIMMING,
	FIRST_TRANSITION,
	CYCLING,
	SECOND_TRANSITION,
	RUNNING,
	FINISHED;
	
	static TriathLinkSportType getNextSportType(TriathLinkSportType sportType){

		switch(sportType){
		case NOT_STARTED:
			return TriathLinkSportType.SWIMMING;
		case SWIMMING:
			return TriathLinkSportType.FIRST_TRANSITION;
		case FIRST_TRANSITION:
			return TriathLinkSportType.CYCLING;
		case CYCLING:
			return TriathLinkSportType.SECOND_TRANSITION;
		case SECOND_TRANSITION:
			return TriathLinkSportType.RUNNING;
		case RUNNING:
			return TriathLinkSportType.FINISHED;
		case FINISHED:
			return TriathLinkSportType.FINISHED;
		}
		return sportType;
	}
	
	
	
}
