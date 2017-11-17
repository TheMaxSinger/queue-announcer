package iot.pi.queue.run;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import iot.pi.queue.constants.Slot;
import iot.pi.queue.domain.Announcer;
import iot.pi.queue.domain.DigitAnnouncer;
import iot.pi.queue.util.AnnounceUtil;
import iot.pi.queue.util.DBUtil;
import iot.pi.queue.util.StringUtil;

public class QueueRunner implements NativeKeyListener { 

	private static boolean fixQueue = false;
	private static boolean somethingInProgress = false;
	private static int fixQueueNumber = 0;
	private static final int[] q000 = new int[] {0, 0, 0};
	private static final int[] q123 = new int[] {3, 2, 1};
	private static final int[] q213 = new int[] {3, 1, 2};
	private static final int[] q312 = new int[] {2, 1, 3};
	private static final Announcer digitAnnouncer = new DigitAnnouncer();
	static final int MINUS_KEY = 3658;
	static final int PLUS_KEY = 3662;
	static final List<Integer> allowKeys = Arrays.asList(NativeKeyEvent.VC_A, 
														 NativeKeyEvent.VC_B,
														 NativeKeyEvent.VC_C,
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
		return executeCommand(new String[] {"screen", "-S", "queue", "-X", "stuff", StringUtil.getInputString(slots, queues)});
	}

	private String renderQueue(String input) { 
		return executeCommand(new String[] {"screen", "-S", "queue", "-X", "stuff", input});
	}

	private void increaseQueue(Slot slot) { 
		somethingInProgress = true;
		if (fixQueue) { 
			if (fixQueueNumber == 999) { 
				fixQueueNumber = 0;
			}
			++fixQueueNumber;
			if (slot.equals(Slot.NEUNG)) { 
				renderQueue(q123, new int[] {fixQueueNumber,0,0});
			} else if (slot.equals(Slot.SONG)) { 
				renderQueue(q213, new int[] {fixQueueNumber,0,0});
			} else { 
				renderQueue(q312, new int[] {fixQueueNumber,0,0});
			}
			fixQueue = false;
			try {
				DBUtil.updateQueue(slot.value(), fixQueueNumber);
				renderQueue(DBUtil.loadQueue());
				digitAnnouncer.announce(AnnounceUtil.getNumberAnnounce(fixQueueNumber), slot);
			} catch (Exception ex) { 
				ex.printStackTrace();
			} 
			fixQueueNumber = 0;
		} else { 
			try {
				int newQueue = DBUtil.updateNextQueue(slot.value());
				renderQueue(DBUtil.loadQueue());
				digitAnnouncer.announce(AnnounceUtil.getNumberAnnounce(newQueue), slot);
			} catch (Exception ex) { 
				ex.printStackTrace();
			}
		}
		somethingInProgress = false;
	}

	private void repeatQueue(Slot slot) { 
		somethingInProgress = true;
		try {
			digitAnnouncer.announce(AnnounceUtil.getNumberAnnounce(DBUtil.repeatQueue(slot.value())), slot);
		} catch (Exception ex) { 
			ex.printStackTrace();
		}
		somethingInProgress = false;
	}

	public static void main(String[] args) throws Exception { 
		somethingInProgress = true;
		QueueRunner runner = new QueueRunner();
		runner.executeCommand(new String[] {"screen", "-d", "-m", "-S", "queue", "/dev/ttyUSB0", "9600"});
		runner.renderQueue(DBUtil.loadQueue());
		somethingInProgress = false;
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
					renderQueue(q123, q000);
					somethingInProgress = false;
					fixQueue = false;
					fixQueueNumber = 0;
				} catch (SQLException e) { 
					e.printStackTrace();
					somethingInProgress = false;
					fixQueue = false;
					fixQueueNumber = 0;
				} 
				break;
			case NativeKeyEvent.VC_A: 
				somethingInProgress = true;
				if (fixQueueNumber + 100 > 999) { 
					break;
				}
				if (!fixQueue) { 
					fixQueue = true;
					fixQueueNumber = 100;
					renderQueue(q000, new int[] {100,0,0});
				} else { 
					fixQueueNumber += 100;
					renderQueue(q000, new int[] {fixQueueNumber,0,0});
				}
				somethingInProgress = false;
				break;
			case NativeKeyEvent.VC_B: 
				somethingInProgress = true;
				if (fixQueueNumber + 10 > 999) { 
					break;
				}
				if (!fixQueue) { 
					fixQueue = true;
					fixQueueNumber = 10;
					renderQueue(q000, new int[] {10,0,0});
				} else { 
					fixQueueNumber += 10;
					renderQueue(q000, new int[] {fixQueueNumber,0,0});
				} 
				somethingInProgress = false;
				break;
			case NativeKeyEvent.VC_C: 
				somethingInProgress = true;
				if (fixQueueNumber + 1 > 999) { 
					break;
				}
				if (!fixQueue) { 
					fixQueue = true;
					fixQueueNumber = 1;
					renderQueue(q000, new int[] {1,0,0});
				} else { 
					fixQueueNumber += 1;
					renderQueue(q000, new int[] {fixQueueNumber,0,0});
				}
				somethingInProgress = false;
				break;
			case NativeKeyEvent.VC_7: 
				increaseQueue(Slot.NEUNG);
				break;
			case NativeKeyEvent.VC_4: 
				increaseQueue(Slot.SONG);
				break;
			case NativeKeyEvent.VC_1: 
				increaseQueue(Slot.SARM);
				break;
			case NativeKeyEvent.VC_8: 
				repeatQueue(Slot.NEUNG);
				break;
			case NativeKeyEvent.VC_5: 
				repeatQueue(Slot.SONG);
				break;
			case NativeKeyEvent.VC_2: 
				repeatQueue(Slot.SARM);
				break;
			default : break;
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
	}

}