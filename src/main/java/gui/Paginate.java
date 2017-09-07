package gui;

import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Paginate<T> {

	private final int PAGE_SIZE;
	
	private List<T> all;
	private int page;
	
	public Paginate(List<T> all, int size) {
		this.all = all;
		this.page = 0;
		this.PAGE_SIZE = size;
	}
	
	public int size() {
		return all.size() / PAGE_SIZE;
	}

	public List<T> page() {
		return all.subList(PAGE_SIZE * page, PAGE_SIZE * (page + 1));
	}

	public Button getNextButton() {
		Button next = new Button(">");
		next.setOnAction(click -> page = (page == size()) ? page : page + 1);
		return next;
	}
	
	public Button getBackButton() {
		Button back = new Button("<");
		back.setOnAction(click -> page = (page == 0) ? page : page - 1);
		return back;
	}
	
	public TextField getPageField() {
		TextField field = new TextField(Integer.toString(page));
		field.textProperty().addListener((obs, oldV, newV) -> {
			try {
				set(Integer.parseInt(newV));
			} catch (NumberFormatException e) {
				field.setText(Integer.toString(page));
			}
		});
		return field;
	}
	
	public void set(int newPage) {
		page = (newPage > size() || newPage < 0) ? page : newPage;
	}
}
