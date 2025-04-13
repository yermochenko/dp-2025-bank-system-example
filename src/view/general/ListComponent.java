package view.general;

import view.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class ListComponent extends Component {
	private final List<Component> components;
	private final Color markerColor;
	private final Type type;

	public ListComponent(List<Component> components, Type type, Color markerColor) {
		this.components = components;
		this.markerColor = markerColor;
		this.type = type;
	}

	public ListComponent(List<Component> components, Color markerColor) {
		this(components, Type.NUMBERED, markerColor);
	}

	public ListComponent(List<Component> components, Type type) {
		this(components, type, null);
	}

	public ListComponent(List<Component> components) {
		this(components, Type.NUMBERED, null);
	}

	@Override
	public List<String> format(int width) {
		List<String> markers = switch(type) {
			case NUMBERED -> Stream.iterate(1, n -> n + 1).limit(components.size()).map(n -> n + ") ").toList();
			case DOTTED -> Collections.nCopies(components.size(), "● ");
			case DASHED -> Collections.nCopies(components.size(), "– ");
		};
		int leftPaddingSize = markers.stream().map(String::length).max(Integer::compare).orElse(0);
		if(width - leftPaddingSize <= 0) throw new IllegalArgumentException("Width must be at least " + (leftPaddingSize + 1));
		String leftPadding = " ".repeat(leftPaddingSize);
		if(type == Type.NUMBERED) {
			markers = markers.stream().map(marker -> " ".repeat(leftPaddingSize - marker.length()) + marker).toList();
		}
		if(markerColor != null) {
			markers = markers.stream().map(markerColor::apply).toList();
		}
		List<String> result = new ArrayList<>();
		for(int i = 0; i < components.size(); i++) {
			List<String> rows = components.get(i).format(width - leftPaddingSize);
			String marker = markers.get(i);
			if(!rows.isEmpty()) {
				result.add(marker + rows.getFirst());
				for(int j = 1; j < rows.size(); j++) {
					result.add(leftPadding + rows.get(j));
				}
			}
		}
		return result;
	}

	public enum Type {
		NUMBERED, DOTTED, DASHED
	}
}
