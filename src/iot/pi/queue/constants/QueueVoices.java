package iot.pi.queue.constants;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

import iot.pi.queue.domain.Announceable;

public enum QueueVoices implements Announceable { 
	
	NEUNG, SONG, SARM, SEE, HA, HOK, JED, PAD, KAOH, SIB, ED, YEE, ROI, NUMBER, DEQUEUE, KAA;
	
	private static Properties props = new Properties();
	
	static {
		try {
			props.load(new FileReader("/opt/text/sound.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public File getAudioFile() throws MalformedURLException { 
		return new File(props.getProperty(this.name()).trim());
	}
	
}
