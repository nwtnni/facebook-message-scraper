package parse;
	
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Scraper {
	
	private ArrayList<Thread> threads;
	private String user;
	
	public void parse(File f) throws IOException {
		File indexFile = Paths.get(f.getPath(), "index.htm").toFile();
		File threadFiles = Paths.get(f.getPath(), "messages").toFile();
		Document index = Jsoup.parse(indexFile, null);
		
		this.user = index.select("title").first().text().replace(" - Profile", "");
		this.threads = new ArrayList<>();
		
		for (File threadFile : threadFiles.listFiles(file -> file.getPath().endsWith(".html"))) {
			Document thread = Jsoup.parse(threadFile, null);
			Element e = thread.select(".thread").first();
			System.out.println("Currently parsing: " + e.select("h3").text());
			threads.add(new Thread(e, this.user));
		}

		threads.removeIf(t -> t.size() < 10);
		threads.removeIf(t -> t.toString().equals("Conversation with Facebook User"));
	}

	public List<Thread> getThreads() {
		return threads;
	}
}
