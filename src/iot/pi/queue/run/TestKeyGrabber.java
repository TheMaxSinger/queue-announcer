package iot.pi.queue.run;

import de.ksquared.system.keyboard.GlobalKeyListener;
import de.ksquared.system.keyboard.KeyAdapter;
import de.ksquared.system.keyboard.KeyEvent;

public class TestKeyGrabber {
	public static void main(String[] args) {
		new GlobalKeyListener().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) { 
				System.out.println(event.getSource());
				System.out.println(event.getVirtualKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent event) {
				System.out.println(event);
				if (event.getVirtualKeyCode() == KeyEvent.VK_ADD && event.isCtrlPressed())
					System.out.println("CTRL+ADD was just released (CTRL is still pressed)");
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
