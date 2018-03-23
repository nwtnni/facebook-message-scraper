package util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Time {

	private static final DateTimeFormatter IN_FORMAT = 
			DateTimeFormatter.ofPattern("EEEE',' MMMM d',' uuuu 'at' h:mma z");
	private static final DateTimeFormatter OUT_FORMAT =
			DateTimeFormatter.ofPattern("h:mma M/d/yy").withZone(ZoneId.of("EST", ZoneId.SHORT_IDS));

	public static ZonedDateTime parse(String input) {
		return ZonedDateTime.parse(input.replace("am", "AM").replace("pm", "PM"), IN_FORMAT);
	}

	public static String toString(ZonedDateTime output) {
		return output.format(OUT_FORMAT);
	}
	
}
