package parse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
	
	public static void main(String[] args) {
		File f = new File(args[0]);
		Scraper s = new Scraper();
		try {
			s.parse(f);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
