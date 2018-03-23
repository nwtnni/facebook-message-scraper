package gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.control.TextArea;
import javafx.scene.layout.TilePane;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import parse.Message;
import parse.Scraper;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.DirectoryChooser;
import parse.Thread;
import util.Paginate;

public class FacebookMessageScraper extends Application {

	private static final int MESSAGES_PER_PAGE = 1000;
	private static final int THREADS_PER_PAGE = 12;
	
	private Scraper scraper;
	private ChoiceBox<Filter> filter;
	private ChoiceBox<Sort> sort;
	private TextField search;
	private TextArea display;
	private TilePane people;
	private Text title;	
	private Button save;
	
	private String threadName;
	private Paginate<Thread> threads;
	private Paginate<Message> thread;
	
	private Button nextMessage;
	private Button backMessage;
	private TextField setMessage;
	private Text totalPages;
	
	private Button nextThread;
	private Button backThread;
	
	private Filter f;
	private Sort s;
	private String query;
	
	@Override
	public void start(Stage primary) throws Exception {
		DirectoryChooser dc = new DirectoryChooser();
		dc.setTitle("Select your Facebook folder to get started!");
		File f = dc.showDialog(primary);
		
		if (f == null) {
			Platform.exit();
		}
		
		try {
			scraper = new Scraper();
			scraper.parse(f);
		} catch (IOException e) {
			System.out.println(e);
			Platform.exit();
		}
		
		initializeGui(primary);
		primary.setTitle("Facebook Message Scraper");
		primary.show();
	}
	
	@SuppressWarnings("unchecked")
	private void initializeGui(Stage primary) throws IOException {
		FXMLLoader fxml = new FXMLLoader(getClass().getResource("/gui.fxml"));
		final Scene scene = new Scene(fxml.load());
		
		filter = (ChoiceBox<Filter>) fxml.getNamespace().get("filter");
		sort = (ChoiceBox<Sort>) fxml.getNamespace().get("sort");
		search = (TextField) fxml.getNamespace().get("search");
		
		display = (TextArea) fxml.getNamespace().get("display");
		people = (TilePane) fxml.getNamespace().get("people");
		
		title = (Text) fxml.getNamespace().get("title");
		save = (Button) fxml.getNamespace().get("save");
		
		nextMessage = (Button) fxml.getNamespace().get("nextMessage");
		backMessage = (Button) fxml.getNamespace().get("backMessage");
		setMessage = (TextField) fxml.getNamespace().get("setMessage");
		totalPages = (Text) fxml.getNamespace().get("totalPages");
		
		nextThread = (Button) fxml.getNamespace().get("nextThread");
		backThread = (Button) fxml.getNamespace().get("backThread");
		
		initializeSort();
		initializeFilter();
		initializeSearch();
		initializeSave(primary);
		
		refreshFilters();
		refreshThreads();
		primary.setScene(scene);
	}
	
	private void initializeSort() {
		sort.getItems().addAll(Sort.values());
		sort.setValue(Sort.LONG);
		s = Sort.LONG;
		sort.valueProperty().addListener((observable, oldV, newV) -> {
			s = newV;
			refreshFilters();
		});
	}
	
	private void initializeFilter() {
		filter.getItems().addAll(Filter.values());
		filter.setValue(Filter.ALL);
		f = Filter.ALL;
		filter.valueProperty().addListener((observable, oldV, newV) -> {
			f = newV;
			refreshFilters();
		});
	}
	
	private void initializeSearch() {
		search.textProperty().addListener((observable, oldV, newV) -> {
			query = (newV == null || newV.length() == 0) ? null : newV.toLowerCase();
			refreshFilters();
		});
	}
	
	private void initializeSave(Stage primary) {
		save.setOnAction(click -> {
			FileChooser fc = new FileChooser();
			fc.setTitle("Choose where to save this conversation.");
			fc.setSelectedExtensionFilter(new ExtensionFilter("Text file", "*.txt"));
			File f = fc.showSaveDialog(primary);
			
			try (FileWriter writer = new FileWriter(f)) {
				for (Message message : thread.allPages()) {
					writer.write(message.toString() + "\n");
				}
			} catch (IOException e) { //TODO 
			}
		});
	}
	
	private void refreshFilters() {
		threads = new Paginate<Thread>(scraper.getThreads()
			.stream()
			.filter(t -> {
				if (query == null || query.equals("")) {
					return true;
				} else {
					return t.contains(query.toLowerCase());
				}
			})
			.filter(f.getPredicate())
			.sorted(s.getComparator())
			.collect(Collectors.toList()), THREADS_PER_PAGE);
		refreshThreads();
	}
	
	private void refreshThreads() {
		people.getChildren().clear();
		
		nextThread.setOnAction(click -> {
			threads.next();
			refreshThreads();
		});
		
		backThread.setOnAction(click -> {
			threads.back();
			refreshThreads();
		});
		
		threads.page().forEach(t -> people.getChildren().add(new ThreadButton(t, this)));
		backThread.setDisable(threads.current() == 0);
		nextThread.setDisable(threads.current() == threads.pages() - 1);
	}
	
	private void refreshThread() {
		StringBuilder sb = new StringBuilder();
		thread.page().forEach(message -> sb.append(message));
		display.setText(sb.toString());
		
		totalPages.setText("out of " + thread.pages());
		setMessage.setText(Integer.toString(thread.current() + 1));

		title.setText(threadName + " (" + thread.size() + " lines)");
		
		backMessage.setDisable(thread.current() == 0);
		nextMessage.setDisable(thread.current() == thread.pages() - 1);
	}
	
	public void setThread(Thread t) {
		thread = new Paginate<>(t.getMessages(), MESSAGES_PER_PAGE);
		threadName = t.toString();
		
		nextMessage.setDisable(false);
		backMessage.setDisable(false);
		setMessage.setDisable(false);
		save.setDisable(false);
		
		nextMessage.setOnAction(click -> {
			thread.next();
			refreshThread();
		});
		
		backMessage.setOnAction(click -> {
			thread.back();
			refreshThread();
		});
		
		setMessage.setOnKeyPressed(key -> {
			if (key.getCode().equals(KeyCode.ENTER)) {
				try {
					thread.set(Integer.parseInt(setMessage.getText()) - 1);
					refreshThread();
				} catch (NumberFormatException e) {
					setMessage.setText(Integer.toString(thread.current() + 1));
				}
			}
		});
		refreshThread();
	}

	public static void main(String[] args) {
		try {
			Application.launch(args);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
