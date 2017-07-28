package iot.pi.queue.util;

import java.util.ArrayList;
import java.util.List;

import iot.pi.queue.constants.QueueVoices;
import iot.pi.queue.domain.Announceable;

public class NumberConverter { 
	
	public static List<Announceable> getNumberAnnounce(int number) { 
		List<Announceable> digits = new ArrayList<Announceable>();
		if (number == 0) { 
			return digits;
		}
		int roi = number / 100;
		if (roi > 0) {
			digits.add(QueueVoices.values()[roi - 1]);
			digits.add(QueueVoices.ROI);
		}
		int sib = number / 100 > 0 ? ((number - (number / 100) * 100) / 10) : number / 10 > 0 ? number / 10 : 0;
		if (sib != 0) {
			if (sib == 2) { 
				digits.add(QueueVoices.YEE);
			} else if (sib > 2) {
				digits.add(QueueVoices.values()[sib - 1]);
			}
			digits.add(QueueVoices.SIB);
		}
		int nuai = number % 10;
		if (number / 10 == 0) {
			digits.add(QueueVoices.values()[number - 1]);
		} else if (nuai == 1 && sib > 0) { 
			digits.add(QueueVoices.ED);
		} else if (nuai == 1 && sib == 0) { 
			digits.add(QueueVoices.NEUNG);
		} else if (nuai > 1) { 
			digits.add(QueueVoices.values()[nuai - 1]);
		}
		for (Announceable digit : digits) { 
			System.out.println(((QueueVoices)digit).name());
		}
		return digits;
	}

}