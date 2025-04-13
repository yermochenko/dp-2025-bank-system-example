package controller;

import exception.ApplicationException;
import view.general.Color;

abstract public class MenuItem {
	private final String name;
	private final Color color;

	public MenuItem(String name, Color color) {
		this.name = name;
		this.color = color;
	}

	abstract public boolean execute() throws ApplicationException;

	final public String getName() {
		return name;
	}

	final public Color getColor() {
		return color;
	}
}
