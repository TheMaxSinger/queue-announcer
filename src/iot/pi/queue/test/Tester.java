package iot.pi.queue.test;

import java.net.MalformedURLException;

import iot.pi.queue.domain.Announcer;
import iot.pi.queue.domain.DigitAnnouncer;
import iot.pi.queue.util.NumberConverter;

public class Tester {

	public static void main(String[] args) throws MalformedURLException {
		Announcer digitAnnouncer = new DigitAnnouncer();
		digitAnnouncer.announce(NumberConverter.getNumberAnnounce(222));
	}

}