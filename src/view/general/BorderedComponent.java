package view.general;

import view.Component;

import java.util.ArrayList;
import java.util.List;

public class BorderedComponent extends Component {
	public static final char HORIZONTAL_BORDER = '═';
	public static final char VERTICAL_BORDER = '║';
	public static final char LEFT_TOP_BORDER = '╔';
	public static final char RIGHT_TOP_BORDER = '╗';
	public static final char LEFT_BOTTOM_BORDER = '╚';
	public static final char RIGHT_BOTTOM_BORDER = '╝';

	private final Component component;
	private final Color borderColor;

	public BorderedComponent(Component component, Color borderColor) {
		this.component = component;
		this.borderColor = borderColor;
	}

	public BorderedComponent(Component component) {
		this(component, null);
	}

	@Override
	public List<String> format(int width) {
		if(width < 3) throw new IllegalArgumentException("Width must be at least 3");
		List<String> list = new ArrayList<>();
		String topBorder = LEFT_TOP_BORDER + Character.toString(HORIZONTAL_BORDER).repeat(width - 2) + RIGHT_TOP_BORDER;
		if(borderColor != null) {
			topBorder = borderColor.apply(topBorder);
		}
		list.add(topBorder);
		String border = Character.toString(VERTICAL_BORDER);
		if(borderColor != null) {
			border = borderColor.apply(border);
		}
		List<String> rows = component.format(width - 2);
		for(String row : rows) {
			list.add(border + row + border);
		}
		String bottomBorder = LEFT_BOTTOM_BORDER + Character.toString(HORIZONTAL_BORDER).repeat(width - 2) + RIGHT_BOTTOM_BORDER;
		if(borderColor != null) {
			bottomBorder = borderColor.apply(bottomBorder);
		}
		list.add(bottomBorder);
		return list;
	}
}
