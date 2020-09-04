import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import org.junit.Test;


public class TestTriathLinkAnalyzer {
	private TriathLinkFitDecoder decoder = null;
	private ArrayList<AnalyzedRecordData> analyzedRecords = null;

	private TriathLinkData obertrumTriathlonData = null;
	private TriathLinkData testTriathlonTriathlonData = null;
	private TriathLinkData ironmanData = null;
	private TriathLinkData trimotionData = null;
	
	private TriathLinkAnalyzer obertrumTriathlonAnalyzer = null;


	private String[] transitionNames = {
			TriathLinkSportType.NOT_STARTED.toString() + " to " + TriathLinkSportType.SWIMMING.toString(),
			TriathLinkSportType.SWIMMING.toString() + " to " + TriathLinkSportType.FIRST_TRANSITION.toString(),
			TriathLinkSportType.FIRST_TRANSITION.toString() + " to " + TriathLinkSportType.CYCLING.toString(),
			TriathLinkSportType.CYCLING.toString() + " to " + TriathLinkSportType.SECOND_TRANSITION.toString(),
			TriathLinkSportType.SECOND_TRANSITION.toString() + " to " + TriathLinkSportType.RUNNING.toString(),
			TriathLinkSportType.RUNNING.toString() + " to " + TriathLinkSportType.FINISHED.toString()};

	private String[] fitFileNames = {"Ironman", "ObertrumTriathlon", "Trimotion"};
	private ArrayList<Date> compareChangeTimestamps = new ArrayList<Date>();
	private ArrayList<Date> analyzedChangeTimestamps = null;

	
	@Test
	public void testAccuracyOfAnalyzer() {

		for (int i = 0; i < fitFileNames.length; i++) {
			analyzedRecords = null;
			analyzedChangeTimestamps = null;
			compareChangeTimestamps = new ArrayList<Date>();
			try {
				decoder = new TriathLinkFitDecoder(fitFileNames[i] + ".fit");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			TriathLinkData data = decoder.decode();
			TriathLinkAnalyzer analyzer = null;

			try {
				analyzer = new TriathLinkAnalyzer(data.getSessionData(), data.getRecordData());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			analyzedRecords = analyzer.analyze();

			analyzedChangeTimestamps = analyzer.getTransitionTimestamps();

			Scanner scanner = null;
			try {
				scanner = new Scanner(new File(fitFileNames[i] + "TestTimestamps.csv"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			scanner.useDelimiter(";");

			while (scanner.hasNext()){
				String timestamp = null;
				timestamp = scanner.next().trim();
				if(!timestamp.equals(""))
					compareChangeTimestamps.add(new Date(Long.parseLong(timestamp) * 1000));
			}
			scanner.close();

			if(analyzedChangeTimestamps.size() != compareChangeTimestamps.size())
				System.out.println("Size nicht gleich");

			System.out.println("analyzedTimestamp : compareTimestamp");
			for (int j = 0; j < compareChangeTimestamps.size(); j++) {
				System.out.println(analyzedChangeTimestamps.get(j) + " : " + compareChangeTimestamps.get(j));
			}

			long elapsedTime = 0;
			for (SessionData sessionData : data.getSessionData()) 
				elapsedTime += sessionData.getTotalElapsedTime().longValue() * 1000;


			for (int j = 0; j < compareChangeTimestamps.size(); j++) {
				long differenz = analyzedChangeTimestamps.get(j).getTime() - compareChangeTimestamps.get(j).getTime();
				if(differenz < 0 )
					differenz *= -1;

				double abweichung = ((double)differenz / (double)elapsedTime) * 100;
				System.out.println("Abweichung bei " + transitionNames[j] + " " + abweichung);
				System.out.println(differenz / 1000 + " sec");
			}
		}
	}

}
