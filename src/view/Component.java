package view;

import java.util.List;

abstract public class Component {
	public final void print() {
		print(100);
	}

	public final void print(int width) {
		List<String> rows = format(width);
		for(String row : rows) {
			System.out.println(row);
		}
	}

	abstract public List<String> format(int width);
}
