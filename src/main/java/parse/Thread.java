
package parse;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Random;

import org.jsoup.nodes.Element;

public class Thread {
	
	private static final int MAX_LENGTH = 55;
	
	private final Set<String> ID;
	private Set<String> people;
	private StringBuilder messages;
	private ZonedDateTime start;
	private ZonedDateTime end;
	
	public Thread(Element e, String user) {
		this.messages = new StringBuilder();
		ArrayList<Element> data = e.select(".message");
		Collections.reverse(data);
		if (data.size() > 0) {
			start = Message.getTime(data.get(0));
			end = Message.getTime(data.get(data.size() - 1));
		}
		data.forEach(message -> Message.append(message, messages));
		this.people = new HashSet<String>(e.select(".user").eachText());
		this.people.remove(user);
		this.ID = new HashSet<String>(Arrays.asList(e.ownText().split(",[ ]*")));
	}
	
	private Thread(Set<String> ID, Set<String> people, StringBuilder messages) {
		this.ID = ID;
		this.people = people;
		this.messages = messages;
	}
	
	public Set<String> getID() {
		return Collections.unmodifiableSet(ID);
	}
	
	public ZonedDateTime getStartTime() {
		return start;
	}
	
	public ZonedDateTime getEndTime() {
		return end;
	}
	
	public StringBuilder getMessages() {
		return messages;
	}
	
	public String getPeople() {
		String names = people.stream().sorted().reduce("", (a, b) -> a + ", " + b);
		return (names.length() > 3) ? names.substring(2) : names;
	}
	
	public String toString() {
		String temp = getPeople();
		return (temp.length() > MAX_LENGTH) ? temp.substring(0, MAX_LENGTH) + "..." : temp;
	}
	
	public boolean isGroup() {
		return ID.size() > 2;
	}
	
	/*
	 * Combines two threads with the same person or group.
	 */
	public Thread add(Thread other) {
		
		if (!equals(other)) {
			return null;
		}
		
		ZonedDateTime timeA = getStartTime();
		ZonedDateTime timeB = other.getStartTime();
		
		if (timeA.isBefore(timeB)) {
			messages.append(other.getMessages());
			return new Thread(ID, people, messages);
		} else {
			other.getMessages().append(messages);
			return new Thread(ID, people, other.getMessages());
		}
	}
	
	/*
	 * Two threads are identical if their participants are
	 * identical.
	 * 
	 * Note: this ignores edge case of different group threads
	 * with the same people.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Thread) {
			Thread t = (Thread) o;
			return t.getID().equals(ID);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return ID.hashCode();
	}
}
