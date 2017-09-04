package parse;
	
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Scraper {
	
	private ArrayList<Thread> threads;
	private String user;
	
	public void parse(File f) throws IOException {
	
		Document doc = Jsoup.parse(f, null);

		threads = new ArrayList<>();
		doc.select(".thread").forEach(thread -> threads.add(new Thread(thread)));
		user = doc.select("h1").first().text();
	}

	public List<Thread> getThreads() {
		return Collections.unmodifiableList(threads);
	}
}
