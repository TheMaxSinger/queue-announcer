package iot.pi.queue.constants;

public enum Slot {
	
	SOON(0), 
	NEUNG(1), 
	SONG(2), 
	SARM(3);
	
	int value;
	
	private Slot(int value) { 
		this.value = value;
	}
	
	public int value() { 
		return this.value;
	}
	
}