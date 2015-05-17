package logic;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Quadtree {
	public QuadtreeNode root;
	/**
	 * Shapes that were inserted into the quadtree that are out of bounds of the
	 * root level.
	 */
	public Set<Shape> outOfBounds;

	public Quadtree() {
		root = new QuadtreeNode(new Rectangle(0, 0, 0, 0));
		outOfBounds = new HashSet<>();
	}

	public void setBounds(Rectangle newBounds) {
		outOfBounds.addAll(root.getObjects());
		root = new QuadtreeNode(newBounds);

		Iterator<Shape> it = outOfBounds.iterator();
		while (it.hasNext()) {
			Shape s = it.next();
			if (root.insert(s)) {
				it.remove();
			}
		}
	}

	public boolean insert(Shape s) {
		return root.insert(s) || outOfBounds.add(s);
	}
}
