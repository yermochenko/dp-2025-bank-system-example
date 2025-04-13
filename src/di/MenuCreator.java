package di;

import controller.ExitMenuItem;
import controller.MenuItem;
import exception.ApplicationException;

import java.util.List;

public class MenuCreator {
	public static List<MenuItem> createMenu() throws ApplicationException {
		ExitMenuItem exitMenuItem = new ExitMenuItem();

		return List.of(
			exitMenuItem
		);
	}
}
