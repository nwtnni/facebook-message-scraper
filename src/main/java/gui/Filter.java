package gui;

import java.util.function.Predicate;
import parse.Thread;

public enum Filter {
	
	ALL("All", t -> {
		return true;
	}),
	
	GROUP("Group", t -> {
		return t.isGroup();
	}),
	
	PRIVATE("Private", t-> {
		return !t.isGroup();
	});
	
	private String str;
	private Predicate<Thread> condition;
	
	private Filter(String str, Predicate<Thread> condition) {
		this.str = str;
		this.condition = condition;
	}
	
	public Predicate<Thread> getPredicate() {
		return condition;
	}
	
	@Override
	public String toString() {
		return str;
	}
}
