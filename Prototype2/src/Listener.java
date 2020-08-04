import java.util.ArrayList;
import java.util.Date;

import com.garmin.fit.RecordMesg;
import com.garmin.fit.RecordMesgListener;
import com.garmin.fit.SessionMesg;
import com.garmin.fit.SessionMesgListener;

class Listener implements  SessionMesgListener, RecordMesgListener{

		private ArrayList<RecordData> recordData = new ArrayList<RecordData>();
		private ArrayList<SessionData> sessionData = new ArrayList<SessionData>();
		
		public Listener(){

		}
		
        @Override
        public void onMesg(RecordMesg mesg) {
        	RecordData currentRecord = new RecordData();
        	
			if(mesg.getSpeed()!= null )
				currentRecord.setSpeed(mesg.getSpeed());
			
			if(mesg.getTimestamp() != null){
				long temp = mesg.getTimestamp().getTimestamp().longValue() + 631065600L ; // Garmin offset + timezone offset
				Date currentTimestamp = new Date(temp*1000);
				
				currentRecord.setTimestamp(currentTimestamp);
				
			}
			if(mesg.getHeartRate() != null)
				currentRecord.setHeartRate(mesg.getHeartRate());
			
			this.recordData.add(currentRecord);
        }

		@Override
		public void onMesg(SessionMesg mesg) {
			SessionData currentSession = new SessionData();
			
			if(mesg.getStartTime()!= null ){
				long temp = mesg.getStartTime().getTimestamp().longValue() + 631065600L ; // Garmin offset + timezone offset
				Date currentTimestamp = new Date(temp * 1000);
				currentSession.setStartTime(currentTimestamp);
			}
				
			
			if(mesg.getStartTime()!= null )
				currentSession.setTotalElapsedTime(mesg.getTotalElapsedTime());
			
			this.sessionData.add(currentSession);
		}

		
		
		public ArrayList<RecordData> getRecordData() {
			return recordData;
		}

		public void setRecordData(ArrayList<RecordData> recordData) {
			this.recordData = recordData;
		}

		public ArrayList<SessionData> getSessionData() {
			return sessionData;
		}

		public void setSessionData(ArrayList<SessionData> sessionData) {
			this.sessionData = sessionData;
		}
		
		
		
}