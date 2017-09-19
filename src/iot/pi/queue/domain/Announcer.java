package iot.pi.queue.domain;

import java.net.MalformedURLException;
import java.util.List;

public interface Announcer { 
	
	void announce(List<Announceable> list) throws MalformedURLException;
	void announce(List<Announceable> list, int slot) throws MalformedURLException;

}