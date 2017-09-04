package gui;

import javafx.scene.layout.TilePane;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import parse.Thread;

public class ThreadButton extends Button {

	private static final int MAX_LENGTH = 40;
	
	private Thread thread;
	
	public ThreadButton(Thread thread, TextArea display) {
		super(thread.getPeople().length() > MAX_LENGTH ? thread.getPeople().substring(0, MAX_LENGTH) : thread.getPeople());
		this.thread = thread;
		
		this.setOnAction(click -> {
			StringBuilder sb = new StringBuilder();
			thread.getMessages().forEach(message -> sb.append(message + "\n"));
			display.setText(sb.toString());
		});
		
	}
}
