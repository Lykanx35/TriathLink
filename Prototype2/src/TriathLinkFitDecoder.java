import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.garmin.fit.Decode;
import com.garmin.fit.FitRuntimeException;
import com.garmin.fit.MesgBroadcaster;
import com.garmin.fit.RecordMesgListener;
import com.garmin.fit.SessionMesgListener;


public class TriathLinkFitDecoder {

	private FileInputStream inputFile = null;
	
	public TriathLinkFitDecoder(String fileName) throws FileNotFoundException{
		this.inputFile = new FileInputStream(fileName);
	}
	
	public TriathLinkData decode(){
		TriathLinkData data = new TriathLinkData();
		
		//init
		Decode decode = new Decode();

		MesgBroadcaster mesgBroadcaster = new MesgBroadcaster(decode);
		Listener listener = new Listener();


		//stream data from Fit File into Listener
		mesgBroadcaster.addListener((RecordMesgListener)listener);
		mesgBroadcaster.addListener((SessionMesgListener)listener);

		try {
			decode.read(inputFile, mesgBroadcaster, mesgBroadcaster);
		} catch (FitRuntimeException e) {
			try {
				inputFile.close();
			} catch (java.io.IOException f) {
				throw new RuntimeException(f);
			}
		}
		
		data.setRecordData(listener.getRecordData());
		data.setSessionData(listener.getSessionData());
		
		return data;

	}

}
