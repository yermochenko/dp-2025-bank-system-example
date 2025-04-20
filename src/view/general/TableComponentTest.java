package view.general;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableComponentTest {
	@Test
	void calcWidths01() {
		assertListEquals(
			List.of(0.5, 0.5),
			TableComponent.calcWidths(List.of(0, 0), 15)
		);
	}

	@Test
	void calcWidths02() {
		assertListEquals(
			List.of(0.4, 0.3, 0.3),
			TableComponent.calcWidths(List.of(0, 0, 0), 14)
		);
	}

	@Test
	void calcWidths03() {
		assertListEquals(
			List.of(0.1923, 0.2308, 0.2692, 0.3077),
			TableComponent.calcWidths(List.of(5, 6, 7, 8), 31)
		);
	}

	@Test
	void calcWidths04() {
		assertListEquals(
			List.of(0.2, 0.25, 0.25, 0.3),
			TableComponent.calcWidths(List.of(5, 6, 7, 8), 25)
		);
	}

	@Test
	void calcWidths05() {
		assertListEquals(
			List.of(0.2, 0.2222, 0.2667, 0.3111),
			TableComponent.calcWidths(List.of(5, 6, 7, 8), 50)
		);
	}

	@Test
	void calcWidths06() {
		assertListEquals(
			List.of(0.1111, 0.1333, 0.5556, 0.2),
			TableComponent.calcWidths(List.of(5, 6, 0, 9), 50)
		);
	}

	@Test
	void calcWidths07() {
		assertListEquals(
			List.of(0.1111, 0.3111, 0.2889, 0.2889),
			TableComponent.calcWidths(List.of(5, 0, 0, 0), 50)
		);
	}

	@Test
	void calcWidths08() {
		assertListEquals(
			List.of(0.1087, 0.3043, 0.3043, 0.2827),
			TableComponent.calcWidths(List.of(5, 0, 0, 0), 51)
		);
	}

	@Test
	void calcWidths09() {
		assertListEquals(
			List.of(0.25, 0.3, 0.4, 0.05),
			TableComponent.calcWidths(List.of(5, 6, 9, 0), 25)
		);
	}

	@Test
	void calcWidths10() {
		assertListEquals(
			List.of(0.4, 0.4, 0.1, 0.1),
			TableComponent.calcWidths(List.of(5, 6, 0, 0), 15)
		);
	}

	private static void assertListEquals(List<Double> expected, List<Double> actual) {
		assertNotNull(actual);
		assertEquals(expected.size(), actual.size());
		for(int i = 0; i < expected.size(); i++) {
			assertEquals(expected.get(i), actual.get(i), 0.0001);
		}
	}
}
