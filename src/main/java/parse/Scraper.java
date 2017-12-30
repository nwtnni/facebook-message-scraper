package parse;
	
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Scraper {
	
	private ArrayList<Thread> threads;
	private String user;
	
	public void parse(File f) throws IOException {
		System.out.println("Starting to parse...");
		Document doc = Jsoup.parse(f, null);
		System.out.println("Done parsing HTML file.");
		this.threads = new ArrayList<>();
		this.user = doc.select("h1").first().text();
		doc.select(".thread").forEach(thread -> threads.add(new Thread(thread, user)));
		System.out.println("Filtering threads...");
		filterThreads();
		System.out.println("Collapsing threads...");
		collapseThreads();
	}

	public List<Thread> getThreads() {
		return threads;
	}
	
	// Facebook caps single threads at 10,000 lines
	private void collapseThreads() {
		ArrayList<Thread> collapsed = new ArrayList<>();
		Set<Set<String>> unique = new HashSet<>();
		System.out.println("Before collapsing: " + threads.size() + " threads");
		threads.forEach(thread -> unique.add(thread.getID()));
		unique.forEach(id -> {
			collapsed.add(
				threads.stream()
				.filter(t -> t.getID().equals(id))
				.sorted((a, b) -> a.getStartTime().compareTo(b.getStartTime()))
				.reduce((a, b) -> a.add(b))
				.get()
			);
		});
		threads = collapsed;
		System.out.println("After collapsing: " + threads.size() + " threads");
	}
	
	// Remove empty conversations
	private void filterThreads() {
		System.out.println("\tBefore filtering: " + threads.size() + " threads");
		threads.removeIf(thread -> thread.getPeople().equals(""));
		threads.removeIf(thread -> thread.size() < 10);
		System.out.println("\tAfter filtering: " + threads.size() + " threads");
	}
}
