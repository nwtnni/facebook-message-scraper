
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
	
	private static final int MAX_LENGTH = 60;
	
	private final Set<String> ID;
	private Set<String> people;
	private List<Message> messages;
	
	public Thread(Element e, String user) {
		this.messages = new ArrayList<>();
		e.select(".message").forEach(message -> messages.add(new Message(message)));
		Collections.reverse(messages);
		this.people = new HashSet<String>(e.select(".user").eachText());
		this.people.remove(user);
		this.ID = new HashSet<String>(Arrays.asList(e.ownText().split(",[ ]*")));
	}
	
	private Thread(Set<String> ID, Set<String> people, List<Message> messages) {
		this.ID = ID;
		this.people = people;
		this.messages = messages;
	}
	
	public Set<String> getID() {
		return Collections.unmodifiableSet(ID);
	}
	
	public ZonedDateTime getStartTime() {
		return messages.get(0).getTime();
	}
	
	public ZonedDateTime getEndTime() {
		return messages.get(messages.size() - 1).getTime();
	}
	
	public List<Message> getMessages() {
		return Collections.unmodifiableList(messages);
	}
	
	public String getPeople() {
		String names = people.stream().sorted().reduce("", (a, b) -> a + ", " + b);
		return (names.length() > 3) ? names.substring(2) : names;
	}
	
	public String toString() {
//		String temp = getPeople();
//		return (temp.length() > MAX_LENGTH) ? temp.substring(0, MAX_LENGTH) + "..." : temp;
		return getRandomName(5) + " " + getRandomName(3);
	}
	
	public static String getRandomName(int len) {
		String[] derp = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
		Random r = new Random();

		String name = derp[r.nextInt(26)].toUpperCase();
		
		for (int i = 0; i < r.nextInt(5) + len; i++) {
			name += derp[r.nextInt(26)];
		}
		return name;
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
		
		List<Message> combined = new ArrayList<>();
		
		if (timeA.isBefore(timeB)) {
			combined.addAll(messages);
			combined.addAll(other.getMessages());
		} else {
			combined.addAll(other.getMessages());
			combined.addAll(messages);
		}
		
		return new Thread(ID, people, combined);
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
