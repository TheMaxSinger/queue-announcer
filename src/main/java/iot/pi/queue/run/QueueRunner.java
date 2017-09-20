package iot.pi.queue.run;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class QueueRunner implements NativeKeyListener { 
	
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
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		System.out.println(arg0.getKeyCode());
		
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
