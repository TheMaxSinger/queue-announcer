package iot.pi.queue.ui;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import iot.pi.queue.domain.DigitAnnouncer;
import iot.pi.queue.util.NumberConverter;
import iot.pi.queue.util.Pause;

public class QueFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	int numQueue;
    int WriteNum1;
    int WriteNum2;
    int WriteNum3;
    int row[] = {WriteNum1, 1, WriteNum2, 2, WriteNum3, 3};
    DigitAnnouncer announcer = new DigitAnnouncer();

    private QueFrame() {
        initComponents();
    }

    private void initComponents() { 
        setSize(new Dimension(1049, 853));
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unused")
	private void inputKeyKeyPressed(KeyEvent evt) {
        try {
            int key;
            key = evt.getKeyCode();
            AudioClip cnumber = Applet.newAudioClip(new URL("file:///opt/audio/number.wav"));
            AudioClip c001 = Applet.newAudioClip(new URL("file:///opt/audio/001.wav"));
            AudioClip c002 = Applet.newAudioClip(new URL("file:///opt/audio/002.wav"));
            AudioClip c003 = Applet.newAudioClip(new URL("file:///opt/audio/003.wav"));
            AudioClip ckaa = Applet.newAudioClip(new URL("file:///opt/audio/kaa.wav"));
            AudioClip cdequeue = Applet.newAudioClip(new URL("file:///opt/audio/dequeue.wav"));
            if (key == KeyEvent.VK_NUMPAD7) {
                try {
                    numQueue = numQueue + 1;
                    WriteNum1 = numQueue;
                    row[0] = WriteNum1;
                    Pause.delay(1);
                    cnumber.play();
                    Pause.delay(1);
                    announcer.announce(NumberConverter.getNumberAnnounce(numQueue));
                    Pause.delay(1);
                    cdequeue.play();
                    Pause.delay(3);
                    c001.play();
                    Pause.delay(1);
                    ckaa.play();
                } catch (MalformedURLException ex) {
                    Logger.getLogger(QueFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Throwable ex) {
                    Logger.getLogger(QueFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (key == KeyEvent.VK_NUMPAD8) {
                try {
                    Pause.delay(1);
                    cnumber.play();
                    Pause.delay(1);
                    announcer.announce(NumberConverter.getNumberAnnounce(WriteNum1));
                    Pause.delay(1);
                    cdequeue.play();
                    Pause.delay(3);
                    c001.play();
                    Pause.delay(1);
                    ckaa.play();
                } catch (MalformedURLException ex) {
                    Logger.getLogger(QueFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Throwable ex) {
                    Logger.getLogger(QueFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (key == KeyEvent.VK_NUMPAD4) {
                try {
                    numQueue = numQueue + 1;
                    WriteNum2 = numQueue;
                    row[2] = WriteNum2;
                    Pause.delay(2);
                    cnumber.play();
                    Pause.delay(1);
                    announcer.announce(NumberConverter.getNumberAnnounce(numQueue));
                    Pause.delay(1);
                    cdequeue.play();
                    Pause.delay(3);
                    c002.play();
                    Pause.delay(1);
                    ckaa.play();
                } catch (MalformedURLException ex) {
                    Logger.getLogger(QueFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Throwable ex) {
                    Logger.getLogger(QueFrame.class.getName()).log(Level.SEVERE, null, ex);
                } 
            } else if (key == KeyEvent.VK_NUMPAD5) {
                try { 
                    Pause.delay(2);
                    cnumber.play();
                    Pause.delay(1);
                    announcer.announce(NumberConverter.getNumberAnnounce(WriteNum2));
                    Pause.delay(1);
                    cdequeue.play();
                    Pause.delay(3);
                    c002.play();
                    Pause.delay(1);
                    ckaa.play();
                } catch (MalformedURLException ex) {
                    Logger.getLogger(QueFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Throwable ex) {
                    Logger.getLogger(QueFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (key == KeyEvent.VK_NUMPAD1) {
                try { 
                    numQueue = numQueue + 1;
                    WriteNum3 = numQueue;
                    row[4] = WriteNum3;
                    Pause.delay(1);
                    cnumber.play();
                    Pause.delay(1);
                    announcer.announce(NumberConverter.getNumberAnnounce(numQueue));
                    Pause.delay(1);
                    cdequeue.play();
                    Pause.delay(3);
                    c003.play();
                    Pause.delay(1);
                    ckaa.play();
                } catch (MalformedURLException ex) {
                    Logger.getLogger(QueFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Throwable ex) {
                    Logger.getLogger(QueFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (key == KeyEvent.VK_NUMPAD2) {
                try {
                    Pause.delay(1);
                    cnumber.play();
                    Pause.delay(1);
                    announcer.announce(NumberConverter.getNumberAnnounce(WriteNum3));
                    Pause.delay(1);
                    cdequeue.play();
                    Pause.delay(3);
                    c003.play();
                    Pause.delay(1);
                    ckaa.play();
                } catch (MalformedURLException ex) {
                	Logger.getLogger(QueFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Throwable ex) {
                	Logger.getLogger(QueFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (MalformedURLException e) {
			e.printStackTrace();
		} 
    } 

    public static void main(String args[]) {
        Runtime r = Runtime.getRuntime();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QueFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                new QueFrame().setVisible(true);
            }
        });
        r.gc();
    }

}