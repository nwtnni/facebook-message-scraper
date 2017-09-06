package gui;

import javafx.scene.layout.TilePane;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import parse.Thread;

public class ThreadButton extends Button {

	private static final int MAX_LENGTH = 60;
	private static final int HEIGHT = 50;
	private static final int WIDTH = 450;
	
	public ThreadButton(Thread thread, TextArea display, Text title) {

		String temp = thread.getPeople();
		final String people = (temp.length() > MAX_LENGTH) ? temp.substring(0, MAX_LENGTH) + "..." : temp;
		
		setText(people);
		setPrefHeight(HEIGHT);
		setPrefWidth(WIDTH);
		setAlignment(Pos.BASELINE_LEFT);
		
		this.setOnAction(click -> {
			StringBuilder sb = new StringBuilder();
			thread.getMessages().forEach(message -> sb.append(message + "\n"));
			display.setText(sb.toString());
			title.setText("Conversation With " + people + " (" + thread.getMessages().size() + " lines)");
		});
		
	}
}
