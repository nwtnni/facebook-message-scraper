
package parse;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.nodes.Element;

import util.Time;

public class Thread {
	
	// Eager
	private Element data;
	private String title; 
	private Set<String> people;
	private ZonedDateTime start;
	private int size;
	
	// Lazy
	private boolean loaded;
	private List<Message> messages;
	
	public Thread(Element e, String user) {

		// Eagerly loaded thread information
		this.data = e;
		
		this.title = e.select("h3")
			.first()
			.text()
			.replace("Conversation with ", "");
		
		this.people = new HashSet<String>(Arrays.asList(
			e.ownText()
				.replace("Participants: ", "")
				.toLowerCase()
				.split(",[ ]+")
		));
		
		this.start = Time.parse(
			e.select(".message")
				.last()
				.select(".meta")
				.first()
				.text()
		);
		
		this.size = e.select(".message").size();
		
		// Lazily loaded thread information
		this.loaded = false;
		this.messages = null;
	}
	
	public List<Message> getMessages() {
		
		if (!loaded) {
			messages = data.select(".message")
					.stream()
					.map(message -> new Message(message))
					.filter(message -> !message.empty())
					.collect(Collectors.toList());
			Collections.reverse(messages);
			
			size = messages.size();
			loaded = true;
		}
		
		return messages;
	}
	
	public boolean contains(String search) {
		return people.parallelStream().anyMatch(name -> name.contains(search));
	}
	
	public ZonedDateTime getStartTime() {
		return start;
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
