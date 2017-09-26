package iot.pi.queue.run;

import java.io.IOException;
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

import iot.pi.queue.util.DBUtil;
import iot.pi.queue.util.StringUtil;

public class QueueRunner implements NativeKeyListener { 
	
	private static boolean locked = false;
	private static boolean jump = false;
	private static boolean somethingInProgress = false;
	
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

	public static void main(String[] args) throws IOException { 
		QueueRunner test = new QueueRunner();
		Process p = Runtime.getRuntime().exec(new String[] {"sceen", "-d", "/dev/ttyUSB0", "9600"});
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent event) { 
		if (!allowKeys.contains(event.getKeyCode()) || somethingInProgress) { 
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
				System.out.println(resetString);
				break;
			case NativeKeyEvent.VC_R: 
				break;
			case NativeKeyEvent.VC_A: 
				break;
			case NativeKeyEvent.VC_B: 
				break;
			case NativeKeyEvent.VC_C: 
				break;
			case NativeKeyEvent.VC_F: 
	
				break;
			case NativeKeyEvent.VC_1: 
				break;
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