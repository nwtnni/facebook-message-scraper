package gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import parse.Thread;

public class ThreadButton extends Button {

	private static final int HEIGHT = 53;
	private static final int WIDTH = 375;
	
	public ThreadButton(Thread thread, FacebookMessageScraper gui) {
		setText(thread.toString());
		setPrefHeight(HEIGHT);
		setPrefWidth(WIDTH);
		setAlignment(Pos.BASELINE_LEFT);
		
		this.setOnAction(click -> {
			gui.setThread(thread);
		});
	}
}
