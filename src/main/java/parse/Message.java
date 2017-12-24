package parse;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.jsoup.nodes.Element;

public class Message {

	private static final DateTimeFormatter IN_FORMAT = 
			DateTimeFormatter.ofPattern("EEEE',' MMMM d',' uuuu 'at' h:mma z");
	private static final DateTimeFormatter OUT_FORMAT =
			DateTimeFormatter.ofPattern("h:mma M/d/yy").withZone(ZoneId.of("EST", ZoneId.SHORT_IDS));
	
	public static ZonedDateTime getTime(Element e) {
		String temp = e.select(".meta").first().text();
		return ZonedDateTime.parse(temp.replace("am", "AM").replace("pm", "PM"), IN_FORMAT);
	}
	
	public static void append(Element e, StringBuilder sb) {
		String temp = e.select(".meta").first().text();
		ZonedDateTime time = ZonedDateTime.parse(temp.replace("am", "AM").replace("pm", "PM"), IN_FORMAT);
		sb.append(time.format(OUT_FORMAT) + " " + e.select(".user").first().text() + ": " + e.nextElementSibling().text() + "\n");
	}
	
	public static boolean empty(Element e) {
		return e.nextElementSibling().text().length() == 0;
	}
}
