package view.general;

public enum Color {
	BLACK(30),
	RED(31),
	GREEN(32),
	YELLOW(33),
	BLUE(34),
	PURPLE(35),
	CYAN(36),
	WHITE(37);

	private final int code;

	Color(int code) {
		this.code = code;
	}

	public String apply(String s) {
		return "\u001B[" + code + 'm' + s + "\u001B[0m";
	}
}
