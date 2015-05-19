package logic;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 
 * @author wesley
 *
 * @param <T>
 *            The type of element the Quadtree is to store.
 */
public class Quadtree<T extends Shape> {
	/** Stores the {@link QTElement} corresponding to each item stored. */
	private Map<T, QTElement<T>> elements;

	/** Root level of the Quadtree. */
	private QTNode<T> root;
	/**
	 * Shapes that were inserted into the quadtree that are out of bounds of the
	 * root level.
	 */
	private Set<QTElement<T>> outOfBounds;

	public Quadtree() {
		elements = new HashMap<>();
		root = new QTNode<>(new Rectangle(0, 0, 0, 0));
		outOfBounds = new HashSet<>();
	}

	public void setBounds(Rectangle newBounds) {
		// Use outOfBounds as a temp to store all the objects we are
		// transferring
		outOfBounds.addAll(root.getObjects());
		root = new QTNode<T>(newBounds);

		// Add all the shapes to the new root
		Iterator<QTElement<T>> it = outOfBounds.iterator();
		while (it.hasNext()) {
			QTElement<T> s = it.next();
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
	public void insert(T s) {
		QTElement<T> e = new QTElement<T>(s);
		elements.put(s, e);
		if (!root.insert(e)) {
			outOfBounds.add(e);
		}
	}

	public boolean remove(T s) {
		QTElement<T> e = elements.get(s);
		if (outOfBounds.remove(e)) {
			return true;
		}
		return root.remove(e);
	}

	/**
	 * Process the tree nodes in pre-order.
	 * 
	 * @param consumer
	 *            the consumer that processes the nodes
	 */
	public void processNodes(Consumer<QTNode<T>> consumer) {
		root.processNodes(consumer);
	}

	/**
	 * @return the depth of the deepest part of the tree
	 */
	public int getDepth() {
		return root.getDepthBelow() + 1;
	}

	/**
	 * Gets the {@link QTElement} at the point given.
	 * 
	 * @param point
	 *            the point of interest
	 * @return a QTElement
	 */
	public T getAt(Point point) {
		stream().forEach(t -> System.out.println(t.toString()));
		return stream().filter(e -> e.contains(point)).findFirst().orElse(null);
		// QTElement<T> e = root.getAt(point);
		// return e == null ? null : e.payload;
	}

	public Stream<T> stream() {
		return root.stream().map(e -> e.payload);
	}
}
