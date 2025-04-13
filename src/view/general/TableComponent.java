package view.general;

import view.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableComponent extends Component {
	public static final char HORIZONTAL_BORDER = '─';
	public static final char VERTICAL_BORDER = '│';
	public static final char LEFT_TOP_BORDER = '┌';
	public static final char RIGHT_TOP_BORDER = '┐';
	public static final char LEFT_BOTTOM_BORDER = '└';
	public static final char RIGHT_BOTTOM_BORDER = '┘';
	public static final char LEFT_MIDDLE_BORDER = '├';
	public static final char RIGHT_MIDDLE_BORDER = '┤';
	public static final char TOP_MIDDLE_BORDER = '┬';
	public static final char BOTTOM_MIDDLE_BORDER = '┴';
	public static final char CROSS_BORDER = '┼';

	private final List<List<Component>> table;
	private final List<Double> columnWidths;
	private final Color borderColor;

	public TableComponent(List<List<Component>> table, List<Double> columnWidths, Color borderColor) {
		if(Math.abs(columnWidths.stream().reduce(0.0, Double::sum) - 1) > 0.0001) throw new IllegalArgumentException("Sum of column widths must be equal to 1");
		int columnCount = columnWidths.size();
		int rowIndex = 0;
		for(List<Component> row : table) {
			if(columnCount != row.size()) {
				throw new IllegalArgumentException(String.format(
					"Column count mismatch for row %d: columnWidths size is %d, but row contains %d cells",
					rowIndex,
					columnCount,
					row.size()
				));
			}
		}
		this.table = table;
		this.columnWidths = columnWidths;
		this.borderColor = borderColor;
	}

	public TableComponent(List<List<Component>> table, List<Double> columnWidths) {
		this(table, columnWidths, null);
	}

	@Override
	public List<String> format(int width) {
		int columnCount = columnWidths.size();
		int totalContentWidth = width - columnCount - 1;
		if(totalContentWidth < columnCount) throw new IllegalArgumentException("Width should be larger than " + (2 * columnCount + 1));
		List<Integer> widths = new ArrayList<>(columnWidths.stream().map(coefficient -> (int) Math.round(coefficient * totalContentWidth)).limit(columnCount - 1).toList());
		widths.add(totalContentWidth - widths.stream().reduce(0, Integer::sum));
		String topBorder = LEFT_TOP_BORDER
				+ widths.stream()
						.map(w -> Character.toString(HORIZONTAL_BORDER).repeat(w))
						.collect(Collectors.joining(Character.toString(TOP_MIDDLE_BORDER)))
				+ RIGHT_TOP_BORDER;
		String middleBorder = LEFT_MIDDLE_BORDER
				+ widths.stream()
						.map(w -> Character.toString(HORIZONTAL_BORDER).repeat(w))
						.collect(Collectors.joining(Character.toString(CROSS_BORDER)))
				+ RIGHT_MIDDLE_BORDER;
		String bottomBorder = LEFT_BOTTOM_BORDER
				+ widths.stream()
						.map(w -> Character.toString(HORIZONTAL_BORDER).repeat(w))
						.collect(Collectors.joining(Character.toString(BOTTOM_MIDDLE_BORDER)))
				+ RIGHT_BOTTOM_BORDER;
		String verticalBorder = Character.toString(VERTICAL_BORDER);
		if(borderColor != null) {
			topBorder = borderColor.apply(topBorder);
			middleBorder = borderColor.apply(middleBorder);
			bottomBorder = borderColor.apply(bottomBorder);
			verticalBorder = borderColor.apply(verticalBorder);
		}
		List<String> result = new ArrayList<>();
		result.add(topBorder);
		boolean notFirst = false;
		for(List<Component> row : table) {
			if(notFirst) {
				result.add(middleBorder);
			}
			List<List<String>> rowData = new ArrayList<>(row.size());
			for(int i = 0; i < row.size(); i++) {
				rowData.add(row.get(i).format(widths.get(i)));
			}
			int linesCount = rowData.stream().map(List::size).max(Integer::compare).orElse(0);
			for(int i = 0; i < linesCount; i++) {
				List<String> line = new ArrayList<>(rowData.size());
				for(int j = 0; j < rowData.size(); j++) {
					List<String> cell = rowData.get(j);
					if(i < cell.size()) {
						line.add(cell.get(i));
					} else {
						line.add(" ".repeat(widths.get(j)));
					}
				}
				result.add(verticalBorder + String.join(verticalBorder, line) + verticalBorder);
			}
			notFirst = true;
		}
		result.add(bottomBorder);
		return result;
	}
}
