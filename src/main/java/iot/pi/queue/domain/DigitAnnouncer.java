package iot.pi.queue.domain;

import java.net.MalformedURLException;
import java.util.List;

import iot.pi.queue.constants.QueueVoices;
import iot.pi.queue.constants.Slot;
import iot.pi.queue.util.SoundUtil;
import iot.pi.queue.util.SystemUtil;

public class DigitAnnouncer implements Announcer { 

	@Override
	public void announce(List<Announceable> announceables) throws MalformedURLException { 
		if (announceables.size() == 1 && announceables.get(0) == QueueVoices.NEUNG) { 
			SystemUtil.delay(0.1);
		}
		for (Announceable digit : announceables) { 
			SoundUtil.play(((QueueVoices)digit).getAudioFile());
			SystemUtil.delay(0.1);
		}
	}
	
	@Override
	public void announce(List<Announceable> announceables, Slot slot) throws MalformedURLException { 
		if (announceables.isEmpty()) { 
			return;
		}
		SoundUtil.play(QueueVoices.NUMBER.getAudioFile());
		announce(announceables);
		SoundUtil.play(QueueVoices.DEQUEUE.getAudioFile());
		SystemUtil.delay(0.1);
		switch (slot.value()) { 
			case 1: SoundUtil.play(QueueVoices.NEUNG.getAudioFile()); break;
			case 2: SoundUtil.play(QueueVoices.SONG.getAudioFile()); break;
			case 3: SoundUtil.play(QueueVoices.SARM.getAudioFile()); break;
			default: break;
		}
		SystemUtil.delay(0.1);
		SoundUtil.play(QueueVoices.KAA.getAudioFile());
	}

}