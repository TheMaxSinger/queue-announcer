package iot.pi.queue.run;

import java.net.MalformedURLException;

import iot.pi.queue.constants.Slot;
import iot.pi.queue.domain.Announcer;
import iot.pi.queue.domain.DigitAnnouncer;
import iot.pi.queue.util.AnnounceUtil;

public class Tester {

	public static void main(String[] args) throws MalformedURLException {
		Announcer digitAnnouncer = new DigitAnnouncer();
		digitAnnouncer.announce(AnnounceUtil.getNumberAnnounce(222), Slot.SARM);
	}

}