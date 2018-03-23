
package parse;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Element;

public class Thread {
	
	private String title; 
	private Set<String> people;
	private StringBuilder messages;
	private ZonedDateTime start;
	private ZonedDateTime end;
	private int size;
	
	public Thread(Element e, String user) {
		this.messages = new StringBuilder();
		ArrayList<Element> data = e.select(".message");
		Collections.reverse(data);
		if (data.size() > 0) {
			start = Message.getTime(data.get(0));
			end = Message.getTime(data.get(data.size() - 1));
		}
		
		// Remove empty sticker messages
		data.removeIf(message -> Message.empty(message));
		data.forEach(message -> Message.append(message, messages));
		
		this.size = data.size();
		this.title = e.select("h3").first().text().replace("Conversation with ", "");
		this.people = new HashSet<String>(Arrays.asList(
			e.ownText().replace("Participants: ", "").toLowerCase().split(",[ ]+")
		));
	}
	
	public StringBuilder getMessages() {
		return messages;
	}
	
	public boolean contains(String search) {
		return people.parallelStream().anyMatch(name -> name.contains(search));
	}
	
	public ZonedDateTime getStartTime() {
		return start;
	}
	
	public ZonedDateTime getEndTime() {
		return end;
	}
	
	public int size() {
		return size;
	}
	
	public String toString() {
		return title;
	}
	
	public boolean isGroup() {
		return people.size() > 1;
	}
}
