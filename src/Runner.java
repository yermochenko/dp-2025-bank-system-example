import controller.MenuItem;
import di.MenuCreator;
import exception.ApplicationException;
import view.MenuComponent;
import view.MessageComponent;

import java.util.Scanner;

public class Runner {
	public static void main(String[] args) {
		try {
			MenuComponent menu = new MenuComponent(MenuCreator.createMenu());
			Scanner console = new Scanner(System.in);
			boolean work = true;
			while(work) {
				menu.print(26);
				System.out.print("Choose an option: ");
				try {
					int option = Integer.parseInt(console.nextLine());
					MenuItem item = menu.choose(option);
					work = item.execute();
				} catch(NumberFormatException e) {
					MessageComponent.showError("Option should be an integer");
				} catch(ApplicationException e) {
					MessageComponent.showError(e.getMessage());
				}
			}
			MessageComponent.showInfo("Good bye");
		} catch(ApplicationException e) {
			MessageComponent.showError(e.getMessage());
		}
	}
}
