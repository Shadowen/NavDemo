package logic;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

public class Quadtree {
	private QuadtreeNode root;
	/**
	 * Shapes that were inserted into the quadtree that are out of bounds of the
	 * root level.
	 */
	private Set<Shape> outOfBounds;

	public Quadtree() {
		root = new QuadtreeNode(new Rectangle(0, 0, 0, 0));
		outOfBounds = new HashSet<>();
	}

	public void setBounds(Rectangle newBounds) {
		// Use outOfBounds as a temp to store all the objects we are
		// transferring
		outOfBounds.addAll(root.getObjects());
		root = new QuadtreeNode(newBounds);

		// Add all the shapes to the new root
		Iterator<Shape> it = outOfBounds.iterator();
		while (it.hasNext()) {
			Shape s = it.next();
			if (root.insert(s)) {
				it.remove();
			}
		}
	}

	/**
	 * Add a shape to the quadtree. Shapes that do not lie within the quadtree
	 * are stored.
	 * 
	 * @param s
	 *            the shape to be added
	 */
	public void insert(Shape s) {
		if (!root.insert(s)) {
			outOfBounds.add(s);
		}
	}

	/**
	 * Process the tree nodes in pre-order.
	 * 
	 * @param consumer
	 *            the consumer that processes the nodes
	 */
	public void processNodes(Consumer<QuadtreeNode> consumer) {
		root.processNodes(consumer);
	}

	/**
	 * @return the depth of the deepest part of the tree
	 */
	public int getDepth() {
		return root.getDepthBelow() + 1;
	}
}
