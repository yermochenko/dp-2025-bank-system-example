package view.general;

import view.Component;

import java.util.ArrayList;
import java.util.List;

public class TextComponent extends Component {
	private final String text;
	private final Alignment alignment;
	private final Color color;

	public TextComponent(String text, Alignment alignment, Color color) {
		this.text = text;
		this.alignment = alignment;
		this.color = color;
	}

	public TextComponent(String text, Alignment alignment) {
		this(text, alignment, null);
	}

	public TextComponent(String text, Color color) {
		this(text, Alignment.LEFT, color);
	}

	public TextComponent(String text) {
		this(text, Alignment.LEFT, null);
	}

	@Override
	public List<String> format(int width) {
		String text = this.text;
		List<String> list = new ArrayList<>();
		while(text.length() > width) {
			list.add(text.substring(0, width));
			text = text.substring(width);
		}
		list.add(switch(alignment) {
			case LEFT -> text + space(width - text.length());
			case RIGHT -> space(width - text.length()) + text;
			case CENTER -> {
				int leftGap = (width - text.length()) / 2;
				int rightGap = width - text.length() - leftGap;
				yield space(leftGap) + text + space(rightGap);
			}
		});
		return color != null ? list.stream().map(color::apply).toList() : list;
	}

	private static String space(int n) {
		return " ".repeat(n);
	}

	public enum Alignment {
		LEFT, CENTER, RIGHT
	}
}
