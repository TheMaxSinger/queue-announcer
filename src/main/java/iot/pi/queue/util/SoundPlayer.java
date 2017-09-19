package iot.pi.queue.util;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {

	public static void play(File audioFile) {
		class AudioListener implements LineListener {
			private boolean done = false;
			@Override
			public synchronized void update(LineEvent event) {
				Type eventType = event.getType();
				if (eventType == Type.STOP || eventType == Type.CLOSE) {
					done = true;
					notifyAll();
				}
			}
			public synchronized void waitUntilDone() throws InterruptedException {
				while (!done) {
					wait();
				}
			}
		}
		AudioListener listener = new AudioListener();
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
			clip.addLineListener(listener);
			clip.open(AudioSystem.getAudioInputStream(audioFile));
			clip.start();
			listener.waitUntilDone();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			clip.close();
		}
	}

}