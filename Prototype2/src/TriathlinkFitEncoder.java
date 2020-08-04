import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import com.garmin.fit.*;


public class TriathlinkFitEncoder {
	private File outputFile = null;


	public TriathlinkFitEncoder(String fileName){
		this.outputFile = new File(fileName);
	}

	public File encodeToMultisport(ArrayList<AnalyzedRecordData> analyzedRecords) throws FitRuntimeException{

		FileEncoder encode;

		encode = new FileEncoder(outputFile, Fit.ProtocolVersion.V2_0);

		FileIdMesg fileIdMesg = new FileIdMesg(); // Every FIT file MUST contain a 'File ID' message as the first message
		fileIdMesg.setManufacturer(Manufacturer.DYNASTREAM);
		fileIdMesg.setType(com.garmin.fit.File.ACTIVITY);
		fileIdMesg.setProduct(9001);
		fileIdMesg.setSerialNumber(1701L);
		encode.write(fileIdMesg); 

		//TODO Triathllink eerwähnen
		byte[] appId = new byte[]{
				0x1, 0x1, 0x2, 0x3,
				0x5, 0x8, 0xD, 0x15,
				0x22, 0x37, 0x59, (byte) 0x90,
				(byte) 0xE9, 0x79, 0x62, (byte) 0xDB
		};

		DeveloperDataIdMesg developerIdMesg = new DeveloperDataIdMesg();
		for (int i = 0; i < appId.length; i++) {
			developerIdMesg.setApplicationId(i, appId[i]);
		}
		developerIdMesg.setDeveloperDataIndex((short)0);
		encode.write(developerIdMesg);


		// write Records
		RecordMesg record = new RecordMesg();
		
		for (AnalyzedRecordData temp : analyzedRecords) {
			Date currentTimestamp = new Date(temp.getRecordData().getTimestamp().getTime() + 631065600);
					
			record.setHeartRate(temp.getRecordData().getHeartRate());
	        record.setSpeed(temp.getRecordData().getSpeed());
	        encode.write(record);
		}
		


		return null;

	}

}
