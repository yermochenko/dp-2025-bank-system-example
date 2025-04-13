package controller;

import view.general.Color;

import java.util.Scanner;

public class ExitMenuItem extends MenuItem {
	public ExitMenuItem() {
		super("Exit", Color.PURPLE);
	}

	@Override
	public boolean execute() {
		System.out.print("Are you sure you want to exit the system? (y/n) ");
		return !"y".equalsIgnoreCase(new Scanner(System.in).nextLine().trim());
	}
}
