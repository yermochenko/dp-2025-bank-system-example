package view;

import controller.MenuItem;
import exception.ApplicationException;
import view.general.Color;
import view.general.ListComponent;
import view.general.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class MenuComponent extends Component {
	private final List<MenuItem> menu;

	public MenuComponent(List<MenuItem> menu) {
		this.menu = menu;
	}

	@Override
	public List<String> format(int width) {
		TextComponent caption = new TextComponent("MENU", TextComponent.Alignment.CENTER, Color.BLUE);
		List<String> result = new ArrayList<>(caption.format(width));
		ListComponent list = new ListComponent(
			menu.stream()
					.map(item -> (Component) new TextComponent(item.getName(), item.getColor()))
					.toList(),
			Color.BLUE
		);
		result.addAll(list.format(width));
		return result;
	}

	public MenuItem choose(int n) throws ApplicationException {
		try {
			return menu.get(n - 1);
		} catch (IndexOutOfBoundsException e) {
			throw new ApplicationException("Incorrect menu number");
		}
	}
}
