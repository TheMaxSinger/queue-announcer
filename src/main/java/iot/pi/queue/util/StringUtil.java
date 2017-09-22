package iot.pi.queue.util;

public class StringUtil { 
	
	public static String headZeroFill(int num) { 
		if (num < 0 || num > 999) { 
			throw new IllegalArgumentException(num + " is not a proper queue number.");
		}
		String toReturn = String.valueOf(num);
		if (toReturn.length() == 1) { 
			toReturn = "00" + toReturn;
		} else if (toReturn.length() == 2) { 
			toReturn = "0" + toReturn;
		}
		return toReturn;
	}
	
	public static String getInputString(String[] slots, String[] queues) { 
		StringBuilder str = new StringBuilder(":1");
		for (String slot : slots) { 
			str.append(slot);
		}
		for (String queue : queues) { 
			str.append(queue);
		}
		str.append("\r");
		return str.toString();
	}

}