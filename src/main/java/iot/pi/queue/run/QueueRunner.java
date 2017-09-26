package iot.pi.queue.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.net.MalformedURLException;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import iot.pi.queue.util.DBUtil;
import iot.pi.queue.util.StringUtil;
import iot.pi.queue.domain.DigitAnnouncer;
import iot.pi.queue.util.AnnounceUtil;
import iot.pi.queue.domain.Announcer;

public class QueueRunner implements NativeKeyListener { 
	
	private static boolean locked = false;
	private static boolean jump = false;
	private static boolean fixQueue = false;
	private static boolean somethingInProgress = false;
	private static int fixQueueNumber = 0;

	private static final Announcer digitAnnouncer = new DigitAnnouncer();
	
	static final int MINUS_KEY = 3658;
	static final int PLUS_KEY = 3662;
	static final List<Integer> allowKeys = Arrays.asList(NativeKeyEvent.VC_A, 
														 NativeKeyEvent.VC_B,
														 NativeKeyEvent.VC_C,
														 NativeKeyEvent.VC_R,
														 NativeKeyEvent.VC_F,
														 NativeKeyEvent.VC_1, 
														 NativeKeyEvent.VC_2,
														 NativeKeyEvent.VC_3, 
														 NativeKeyEvent.VC_4, 
														 NativeKeyEvent.VC_5, 
														 NativeKeyEvent.VC_6, 
														 NativeKeyEvent.VC_7, 
														 NativeKeyEvent.VC_8, 
														 NativeKeyEvent.VC_9, 
														 MINUS_KEY, 
														 PLUS_KEY);
	
	private final String hardResetString = StringUtil.getInputString(new String[] {"0", "0", "0"},  new String[] {StringUtil.headZeroFill(0), StringUtil.headZeroFill(0), StringUtil.headZeroFill(0)});
	private final String resetString = StringUtil.getInputString(new String[] {"3", "2", "1"},  new String[] {StringUtil.headZeroFill(0), StringUtil.headZeroFill(0), StringUtil.headZeroFill(0)});
	
	public QueueRunner() { 
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			e.printStackTrace();
		}
		GlobalScreen.addNativeKeyListener(this);
		LogManager.getLogManager().reset();
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
	}
	
	public void lock() {
        synchronized (QueueRunner.class) {
            locked = true;
        }
    }
	
	private String executeCommand(String[] command) {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toString();
	}

	private String renderQueue(int[] slots, int[] queues) { 
		String sequence = StringUtil.getInputString(new String[] {"" + slots[0], "" + slots[1], "" + slots[2]},  new String[] {StringUtil.headZeroFill(queues[0]), StringUtil.headZeroFill(queues[1]), StringUtil.headZeroFill(queues[2])});
		return executeCommand(new String[] {"screen", "-S", "queue", "-X", "stuff", sequence});
	}
	
	private void resetFixQueue() { 
		if (fixQueue) { 
			fixQueue = false;
			fixQueueNumber = 0;
		} 
	}

	public static void main(String[] args) throws IOException { 
		QueueRunner test = new QueueRunner();
		test.executeCommand(new String[] {"screen", "-d", "-m", "-S", "queue", "/dev/ttyUSB0", "9600"});
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent event) { 
		int keyCode = event.getKeyCode();
		if (!allowKeys.contains(keyCode) || somethingInProgress || 
			(fixQueue && 
				keyCode != NativeKeyEvent.VC_1 && 
				keyCode != NativeKeyEvent.VC_4 && 
				keyCode != NativeKeyEvent.VC_7 && 
				keyCode != NativeKeyEvent.VC_A && 
				keyCode != NativeKeyEvent.VC_B && 
				keyCode != NativeKeyEvent.VC_C)) { 
			return;
		}
		switch (event.getKeyCode()) { 
			case MINUS_KEY: 
				try {
					somethingInProgress = true;
					DBUtil.resetQueue();
					somethingInProgress = false;
				} catch (SQLException e) { 
					e.printStackTrace();
					somethingInProgress = false;
				} 
				break;
			case NativeKeyEvent.VC_R: 
				break;
			case NativeKeyEvent.VC_A: 
				if (!fixQueue) { 
					fixQueue = true;
					fixQueueNumber = 100;
					renderQueue(new int[] {0,0,0}, new int[] {100,0,0});
				} else { 
					fixQueueNumber += 100;
					renderQueue(new int[] {0,0,0}, new int[] {fixQueueNumber,0,0});
				}
				break;
			case NativeKeyEvent.VC_B: 
				break;
			case NativeKeyEvent.VC_C: 
				break;
			case NativeKeyEvent.VC_F: 
	
				break;
			case NativeKeyEvent.VC_1: 
				if (fixQueue) { 
					fixQueue = false;
					renderQueue(new int[] {3,2,1}, new int[] {++fixQueueNumber,0,0});
				} 
				try {
					digitAnnouncer.announce(AnnounceUtil.getNumberAnnounce(fixQueueNumber), Slot.NEUNG);
				} catch (MalformedURLException ex) { 
					ex.printStackTrace();
				}
				fixQueueNumber = 0;
			case NativeKeyEvent.VC_4: 
				break;
			case NativeKeyEvent.VC_7: 
				break;
			default : break;
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
	}

}