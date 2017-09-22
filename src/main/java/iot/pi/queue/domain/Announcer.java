package iot.pi.queue.domain;

import java.net.MalformedURLException;
import java.util.List;

import iot.pi.queue.constants.Slot;

public interface Announcer { 
	
	void announce(List<Announceable> list) throws MalformedURLException;
	void announce(List<Announceable> list, Slot slot) throws MalformedURLException;

}