package iot.pi.queue.domain;

import java.net.MalformedURLException;
import java.util.List;

import iot.pi.queue.constants.QueueVoices;
import iot.pi.queue.util.Pause;
import iot.pi.queue.util.SoundPlayer;

public class DigitAnnouncer implements Announcer { 

	@Override
	public void announce(List<Announceable> announceables) throws MalformedURLException { 
		if (announceables.size() == 1 && announceables.get(0) == QueueVoices.NEUNG) { 
			Pause.delay(0.1);
		}
		for (Announceable digit : announceables) { 
			SoundPlayer.play(((QueueVoices)digit).getAudioFile());
			Pause.delay(0.1);
		}
	}
	
	@Override
	public void announce(List<Announceable> announceables, int slot) throws MalformedURLException { 
		SoundPlayer.play(QueueVoices.NUMBER.getAudioFile());
		announce(announceables);
		SoundPlayer.play(QueueVoices.DEQUEUE.getAudioFile());
		Pause.delay(0.1);
		switch (slot) { 
			case 1: SoundPlayer.play(QueueVoices.NEUNG.getAudioFile()); break;
			case 2: SoundPlayer.play(QueueVoices.SONG.getAudioFile()); break;
			case 3: SoundPlayer.play(QueueVoices.SARM.getAudioFile()); break;
			default: break;
		}
		Pause.delay(0.1);
		SoundPlayer.play(QueueVoices.KAA.getAudioFile());
	}

}
