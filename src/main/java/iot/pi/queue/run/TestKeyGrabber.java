package iot.pi.queue.run;

import de.ksquared.system.keyboard.GlobalKeyListener;
import de.ksquared.system.keyboard.KeyAdapter;
import de.ksquared.system.keyboard.KeyEvent;

public class TestKeyGrabber {
	public static void main(String[] args) {
		
		new GlobalKeyListener().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) { 
				System.out.println(event.getVirtualKeyCode() + " has been pressed");
			}

			@Override
			public void keyReleased(KeyEvent event) { 
				if (event.getVirtualKeyCode() == KeyEvent.VK_J && event.isCtrlPressed()) { 
					System.out.println("CTRL - J");
				}
			}
		});
		while (true)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
}
