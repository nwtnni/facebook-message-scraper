package gui;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import parse.Scraper;
import javafx.stage.FileChooser;

public class FacebookMessageScraper extends Application {

	private Scraper scraper;
	private ChoiceBox<Filter> filter;
	private ChoiceBox<Sort> sort;
	private TextField search;
	private TextArea display;
	private TilePane people;
	private Text title;
	
	private Filter f;
	private Sort s;
	private String query;
	
	@Override
	public void start(Stage primary) throws Exception {

		FileChooser fc = new FileChooser();
		fc.setTitle("Select your message file to get started!");
		File f = fc.showOpenDialog(primary);

		if (f == null) {
			Platform.exit();
		}
		
		try {
			scraper = new Scraper();
			scraper.parse(f);
		} catch (IOException e) {
			Platform.exit();
		}
		
		initializeGui(primary);
		
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
		title = (Text) fxml.getNamespace().get("text");
	
		initializeSort();
		initializeFilter();
		initializeSearch();
		
		refreshChats();
		
		primary.setScene(scene);
		
	}
	
	private void initializeSort() {
		sort.getItems().addAll(Sort.values());
		sort.setValue(Sort.LONG);
		s = Sort.LONG;
		sort.valueProperty().addListener((observable, oldV, newV) -> {
			s = newV;
			refreshChats();
		});
	}
	
	private void initializeFilter() {
		filter.getItems().addAll(Filter.values());
		filter.setValue(Filter.ALL);
		f = Filter.ALL;
		filter.valueProperty().addListener((observable, oldV, newV) -> {
			f = newV;
			refreshChats();
		});
	}
	
	private void initializeSearch() {
		search.textProperty().addListener((observable, oldV, newV) -> {
			query = (newV == null || newV.length() == 0) ? null : newV;
			refreshChats();
		});
	}
	
	private void refreshChats() {
		people.getChildren().clear();
		scraper.getThreads().stream()
			.filter(t -> {
				if (query == null) {
					return true;
				} else {
					return t.getPeople().contains(query);
				}
			})
			.filter(f.getPredicate())
			.sorted(s.getComparator())
			.forEachOrdered(t -> {
				people.getChildren().add(new ThreadButton(t, display));
			});
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
