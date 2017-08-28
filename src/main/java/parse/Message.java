package parse;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.jsoup.nodes.Element;

public class Message {

	private static final DateTimeFormatter IN_FORMAT = 
			DateTimeFormatter.ofPattern("EEEE',' MMMM d',' uuuu 'at' h:mma z");
	
	private final String author;
	private final String text;
	private final ZonedDateTime time;
	
	public Message(Element e) {
		author = e.select(".user").first().text();
		text = e.nextElementSibling().text();
		String temp = e.select(".meta").first().text();
		time = ZonedDateTime.parse(temp.replace("am", "AM").replace("pm", "PM"), IN_FORMAT);
	}
	
	@Override
	public String toString() {
		return time.toString() + " " + author + ": " + text;
	}
}
