package util;

import java.util.List;

public class Paginate<T> {

	private final int PAGE_SIZE;
	
	private List<T> all;
	private int page;
	
	public Paginate(List<T> all, int size) {
		this.all = all;
		this.page = 0;
		this.PAGE_SIZE = size;
	}
	
	public int pages() {
		return all.size() / PAGE_SIZE;
	}
	
	public int size() {
		return all.size();
	}
	
	public int current() {
		return page;
	}

	public List<T> page() {
		return all.subList(PAGE_SIZE * page, PAGE_SIZE * (page + 1));
	}
	
	public List<T> allPages() {
		return all;
	}
	
	public void next() {
		set(page + 1);
	}
	
	public void back() {
		set(page - 1);
	}
	
	public void set(int newPage) {
		page = (newPage > size() || newPage < 0) ? page : newPage;
	}
}
