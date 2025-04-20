package view.general;

import view.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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

	/**
	 * Calculate relative widths of table columns for given fixed width of whole table and preferred max width of each
	 * column.
	 * Method calculates:
	 * 1. Columns count as size of list <code>maxWidths</code>
	 * 2. Total widths of all columns as difference between width of whole table and width of all vertical borders
	 *    (width of all vertical borders is calculated as count of columns plus 1)
	 * 3. Total widths is divided between all columns using absolute values
	 * 4. Absolute values is converted into relative values considering that total width is equal 1
	 * @param maxWidths contains preferred max width for each column or 0 if there is no preferred max width
	 *                  (if this column can have any width)
	 * @param width width of whole table including borders
	 * @return list of double which contains relative widths of table columns (sum of all these widths will equal 1)
	 */
	public static List<Double> calcWidths(List<Integer> maxWidths, int width) {
		final int n = maxWidths.size();
		final int totalWidth = width - n - 1;
		AtomicInteger maxSum = new AtomicInteger();
		AtomicInteger flexCount = new AtomicInteger();
		maxWidths.forEach(w -> {
			if(w == 0) {
				flexCount.getAndIncrement();
			} else {
				maxSum.addAndGet(w);
			}
		});
		int minWidth = maxSum.get() + flexCount.get();
		List<Integer> widths = new ArrayList<>(n);
		if(minWidth < totalWidth) {
			if(flexCount.get() > 0) {
				int[] rests = div(totalWidth - maxSum.get(), flexCount.get());
				for(int i = 0, j = 0; i < n; i++) {
					int w = maxWidths.get(i);
					widths.add(w == 0 ? rests[j++] : w);
				}
			} else {
				for(int i = 0; i < n; i++) {
					widths.add((int)Math.round((double)maxWidths.get(i) * totalWidth / minWidth));
				}
				checkWidth(widths, totalWidth);
			}
		} else if(minWidth > totalWidth) {
			for(int i = 0; i < n; i++) {
				int w = maxWidths.get(i);
				if(w != 0) {
					widths.add((int) Math.round((double) w * (totalWidth - flexCount.get()) / maxSum.get()));
				} else {
					widths.add(1);
				}
			}
			checkWidth(widths, totalWidth);
		} else {
			for(int i = 0; i < n; i++) {
				int w = maxWidths.get(i);
				widths.add(w == 0 ? 1 : w);
			}
		}
		List<Double> result = new ArrayList<>(widths.stream().map(w -> Math.round(10000.0 * w / totalWidth) / 10000.0).toList());
		double sum = result.stream().mapToDouble(w -> w).sum();
		if(sum != 1) {
			int imax = 0;
			for(int i = 1; i < n; i++) {
				if(result.get(i) > result.get(imax)) {
					imax = i;
				}
			}
			result.set(imax, 1 - sum + result.get(imax));
		}
		return result;
	}

	private static void checkWidth(List<Integer> widths, int totalWidth) {
		int n = widths.size();
		int sum = widths.stream().mapToInt(w -> w).sum();
		if(sum != totalWidth) {
			int imax = 0;
			for(int i = 1; i < n; i++) {
				if(widths.get(i) > widths.get(imax)) {
					imax = i;
				}
			}
			widths.set(imax, totalWidth - sum + widths.get(imax));
		}
	}

	private static int[] div(int dividend, int divider) {
		int[] result = new int[divider];
		Arrays.fill(result, dividend / divider);
		for(int i = 0, n = dividend % divider; i < n; i++) {
			result[i]++;
		}
		return result;
	}
}
