package parse;

import java.time.ZonedDateTime;

import org.jsoup.nodes.Element;

public class Message {

	private final String author;
	private final String text;
//	private final ZonedDateTime time;
	
	public Message(Element e) {
		author = e.select(".user").first().text();
		text = e.nextElementSibling().text();
//		time = ZonedDateTime.parse(e.select(".meta").first().text());
	}
	
	@Override
	public String toString() {
		return "";
//		return time.toString() + " " + author + ": " + text;
	}
}
