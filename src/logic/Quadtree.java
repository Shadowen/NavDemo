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
		// assert ((root.getObjects().size() + outOfBounds.size()) == 10);

		root = new QuadtreeNode(newBounds);
		outOfBounds.addAll(root.getObjects());

		// System.out.println(root.getObjects().size());
		// assert (outOfBounds.size() == 10);

		// System.out.println("total"
		// + (root.getObjects().size() + outOfBounds.size()));
		Iterator<Shape> it = outOfBounds.iterator();
		while (it.hasNext()) {
			Shape s = it.next();
			if (root.insert(s)) {
				it.remove();
			}
		}

		// assert ((root.getObjects().size() + outOfBounds.size()) == 10);
	}

	public boolean insert(Shape s) {
		return root.insert(s) || outOfBounds.add(s);
	}
}
