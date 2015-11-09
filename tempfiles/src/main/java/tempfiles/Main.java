package tempfiles;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileCleaningTracker;

public class Main {
	
	

	public static void main(String[] args) throws IOException, InterruptedException {
		
		FileCleaningTracker fileCleaningTracker = new FileCleaningTracker();
		
		for(int i=0; i< 100; i++){
			String tracker = String.valueOf(i);
			File file = File.createTempFile("tempfile", "bin");
			fileCleaningTracker.track(file, file);
		}
//		Thread.sleep(10000);
		System.out.println(fileCleaningTracker.getTrackCount());
		System.gc();
		Thread.sleep(1000);
		System.out.println(fileCleaningTracker.getTrackCount());
	}

}
