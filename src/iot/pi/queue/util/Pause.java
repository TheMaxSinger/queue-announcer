/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iot.pi.queue.util;

import java.util.Date;

/**
 *
 * @author master
 */
public class Pause { 
	
	public static void delay(double seconds) {
		Date start = new Date();
		Date end = new Date();
		while (end.getTime() - start.getTime() < (int)(seconds * 900)) {
			end = new Date();
		}
	}
	
	Runtime r = Runtime.getRuntime();
}