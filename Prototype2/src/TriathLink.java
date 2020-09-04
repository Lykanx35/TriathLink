
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;


public class TriathLink {
	public static void main(String[] args) {
		TriathLinkFitDecoder decoder = null;
		ArrayList<AnalyzedRecordData> analyzedRecords = null;
		ArrayList<Date> analyzedChangeTimestamps = new ArrayList<Date>();

		//TODO Prüfen auf File ID ob fit File auch wirklich Activity
		
		try {
			//decoder = new TriathLinkFitDecoder("TestTriathlon.fit");
			decoder = new TriathLinkFitDecoder("ObertrumTriathlon.fit");
			//decoder = new TriathLinkFitDecoder("Ironman.fit");
			//decoder = new TriathLinkFitDecoder("Trimotion.fit");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		TriathLinkData data = decoder.decode();
		
		//analyze data via classification
		TriathLinkAnalyzer analyzer = null;
		
		try {
			analyzer = new TriathLinkAnalyzer(data.getSessionData(), data.getRecordData());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		analyzedRecords = analyzer.analyze();
		
		//Visualize
		TriathLinkXYLineChart chart = new TriathLinkXYLineChart(analyzedRecords);
		
		
		analyzedChangeTimestamps = analyzer.getTransitionTimestamps();
		
        
	}
}



