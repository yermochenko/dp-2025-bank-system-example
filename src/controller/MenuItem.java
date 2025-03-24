package controller;

import exception.ApplicationException;

abstract public class MenuItem {
	private final String name;

	public MenuItem(String name) {
		this.name = name;
	}

	abstract public void execute() throws ApplicationException;

	final public String getName() {
		return name;
	}
}
