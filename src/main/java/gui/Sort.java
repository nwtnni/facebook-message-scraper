package gui;

import java.util.Comparator;
import parse.Thread;

public enum Sort {
	
	EARLY("Earliest First", (a, b) -> {
		return a.getStartTime().compareTo(b.getStartTime());
	}), 
	
	LONG("Longest First", (a, b) -> {
		return b.getMessages().size() - a.getMessages().size();
	});
	
	private String str;
	private Comparator<Thread> cmp;
	
	private Sort(String str, Comparator<Thread> cmp) {
		this.str = str;
		this.cmp = cmp;
	}
	
	public Comparator<Thread> getComparator() {
		return cmp;
	}
	
	@Override
	public String toString() {
		return str;
	}
}
