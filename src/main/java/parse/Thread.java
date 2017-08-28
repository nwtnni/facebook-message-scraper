package parse;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Element;

public class Thread {
	
	private final Set<String> ID;
	private Set<String> people;
	private ArrayList<Message> messages;
	
	public Thread(Element e) {
		messages = new ArrayList<>();
		e.select(".message").forEach(message -> messages.add(new Message(message)));
		people = new HashSet<String>(e.select(".user").eachText());
		people.forEach(person -> System.out.println(person));
		ID = new HashSet<String>(Arrays.asList(e.ownText().split(",[ ]*")));
		ID.forEach(id -> System.out.println(id));
	}
	
	private Thread(Set<String> ID, Set<String> people, ArrayList<Message> messages) {
		this.ID = ID;
		this.people = people;
		this.messages = messages;
	}
	
	public Set<String> getID() {
		return Collections.unmodifiableSet(ID);
	}
	
	public boolean isGroup() {
		return ID.size() > 2;
	}
	
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
