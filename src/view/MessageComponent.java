package view;

import view.general.BorderedComponent;
import view.general.Color;
import view.general.TextComponent;

import java.util.List;

public class MessageComponent extends Component {
	private final String message;
	private final Type type;

	public MessageComponent(String message, Type type) {
		this.message = message;
		this.type = type;
	}

	public MessageComponent(String message) {
		this(message, Type.INFO);
	}

	@Override
	public List<String> format(int width) {
		TextComponent text = new TextComponent(message, TextComponent.Alignment.CENTER, type.color);
		BorderedComponent border = new BorderedComponent(text, type.color);
		return border.format(width);
	}

	private static void showMessage(String message, Type type) {
		int width = message.length() < 98 ? (message.length() > 18 ? message.length() + 2 : 20) : 100;
		new MessageComponent(message, type).print(width);
	}

	public static void showError(String message) {
		showMessage(message, Type.ERROR);
	}

	public static void showWarning(String message) {
		showMessage(message, Type.WARNING);
	}

	public static void showSuccess(String message) {
		showMessage(message, Type.SUCCESS);
	}

	public static void showInfo(String message) {
		showMessage(message, Type.INFO);
	}

	public enum Type {
		ERROR(Color.RED), WARNING(Color.YELLOW), SUCCESS(Color.GREEN), INFO(Color.BLUE);

		private final Color color;

		Type(Color color) {
			this.color = color;
		}
	}
}
