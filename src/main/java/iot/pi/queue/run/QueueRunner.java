package iot.pi.queue.run;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class QueueRunner implements NativeKeyListener { 
	
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

	public static void main(String[] args) { 
		QueueRunner test = new QueueRunner();
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent event) { 
		if (!allowKeys.contains(event.getKeyCode())) { 
			return;
		}
		switch (event.getKeyCode()) { 
			case MINUS_KEY: 
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