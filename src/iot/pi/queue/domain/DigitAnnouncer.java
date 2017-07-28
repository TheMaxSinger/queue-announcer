package iot.pi.queue.domain;

import java.net.MalformedURLException;
import java.util.List;

import iot.pi.queue.constants.QueueVoices;
import iot.pi.queue.util.Pause;
import iot.pi.queue.util.SoundPlayer;

public class DigitAnnouncer implements Announcer { 

	public void announce(List<Announceable> announceables) throws MalformedURLException { 
		if (announceables.size() == 1 && announceables.get(0) == QueueVoices.NEUNG) { 
			Pause.delay(0.1);
		}
		for (Announceable digit : announceables) { 
			SoundPlayer.play(((QueueVoices)digit).getAudioFile());
			Pause.delay(0.1);
		}
	}

}