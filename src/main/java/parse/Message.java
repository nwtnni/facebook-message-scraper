package parse;

import java.time.ZonedDateTime;
import org.jsoup.nodes.Element;

import util.Time;

public class Message {

	public ZonedDateTime time;
	public String text;
	public String user;
	
	public Message(Element e) {
		time = Time.parse(e.select(".meta").first().text());
		text = e.nextElementSibling().text();
		user = e.select(".user").first().text();
	}
	
	public boolean empty() {
		return text.length() == 0;
	}
	
	@Override
	public String toString() {
		return Time.toString(time) + " " + user + ": " + text + "\n";
	}
}
