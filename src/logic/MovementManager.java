package logic;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MovementManager {
	public List<Unit> units;

	public MovementManager() {
		units = new LinkedList<Unit>();

		final int SPACING = 30;
		final int SIZE = 20;
		final int COLUMNS = 10;
		final int COUNT = 10;
		for (int i = 0; i < COUNT; i++) {
			units.add(new Unit(i, new Rectangle(50 + i % COLUMNS * SPACING, 50
					+ (i / COLUMNS) * SPACING, SIZE, SIZE)));
		}
	}
}
