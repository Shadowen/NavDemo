package logic;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

///http://gamedev.stackexchange.com/questions/31021/quadtree-store-only-points-or-regions
public class QuadtreeNode implements Iterable<Shape> {
	int MAX_OBJECTS = 5;
	private int MAX_DEPTH = 5;

	private final int depth;
	private List<Shape> objects;
	public Rectangle bounds;
	private QuadtreeNode parent;
	public List<QuadtreeNode> nodes;

	public QuadtreeNode(Rectangle pBounds) {
		this(null, pBounds, 0);
	}

	private QuadtreeNode(QuadtreeNode pparent, Rectangle pBounds, int idepth) {
		parent = pparent;
		objects = new ArrayList<>();
		nodes = new ArrayList<>();
		bounds = pBounds;
		depth = idepth;
	}

	public void clear() {
		objects.clear();
		for (int i = 0; i < nodes.size(); i++) {
			nodes.get(i).clear();
		}
		nodes.clear();
	}

	private void split() {
		int subWidth = (int) (bounds.getWidth() / 2);
		int subHeight = (int) (bounds.getHeight() / 2);
		int x = (int) bounds.getX();
		int y = (int) bounds.getY();

		nodes.add(new QuadtreeNode(this, new Rectangle(x, y, subWidth,
				subHeight), depth + 1));
		nodes.add(new QuadtreeNode(this, new Rectangle(x + subWidth, y,
				subWidth, subHeight), depth + 1));
		nodes.add(new QuadtreeNode(this, new Rectangle(x + subWidth, y
				+ subHeight, subWidth, subHeight), depth + 1));
		nodes.add(new QuadtreeNode(this, new Rectangle(x, y + subHeight,
				subWidth, subHeight), depth + 1));

		// Reinsert all shapes into children
		for (Shape o : objects) {
			insert(o);
		}
		objects.clear();
	}

	public boolean insert(Shape s) {
		// Does the shape fit into this quadtree?
		if (!s.getBounds().intersects(bounds)) {
			return false;
		}

		if (nodes.isEmpty()) {
			// Add to current node (leaf)
			objects.add(s);
			// Split
			if (objects.size() > MAX_OBJECTS && depth < MAX_DEPTH) {
				split();
			}

			return true;
		}

		// No split, add to existing children
		for (QuadtreeNode st : nodes) {
			st.insert(s);
		}
		return true;
	}

	public Set<Shape> getObjects() {
		Set<Shape> retobj = new HashSet<>(objects);
		for (QuadtreeNode st : nodes) {
			retobj.addAll(st.getObjects());
		}
		return retobj;
	}

	@Override
	public Iterator<Shape> iterator() {
		// Only retrieve unique objects from the tree
		Set<Shape> allObjects = new HashSet<Shape>();
		allObjects.addAll(getObjects());
		return allObjects.iterator();
	}

	public int getDepth() {
		return depth;
	}

	public int getDepthBelow() {
		int ret = 0;
		for (QuadtreeNode st : nodes) {
			ret = Math.max(ret, st.getDepthBelow() + 1);
		}
		return ret;
	}

	// private class Iter implements Iterator<Shape> {
	// private Deque<QuadtreeNode> stack;
	// private Deque<Shape> currentObjects;
	// private Shape lastReturned;
	//
	// public Iter() {
	// stack = new ArrayDeque<>();
	// stack.push(QuadtreeNode.this);
	//
	// currentObjects = new ArrayDeque<Shape>();
	// }
	//
	// @Override
	// public boolean hasNext() {
	// return !stack.isEmpty() || !currentObjects.isEmpty();
	// }
	//
	// @Override
	// public Shape next() {
	// // Populate the current objects array
	// while (currentObjects.isEmpty()) {
	// // Find a leaf node
	// QuadtreeNode top = stack.poll();
	// if (top == null) {
	// throw new NoSuchElementException();
	// }
	//
	// while (top.nodes != null) {
	// // Pop off the top and add its children to the stack
	// for (QuadtreeNode st : top.nodes) {
	// stack.push(st);
	// }
	// }
	//
	// currentObjects.addAll(top.objects);
	// }
	// // Actually return something
	// lastReturned = currentObjects.pop();
	// return lastReturned;
	// }
	//
	// @Override
	// public void remove() {
	// if (lastReturned == null) {
	// throw new IllegalStateException();
	// }
	// QuadtreeNode.this.remove(lastReturned);
	// lastReturned = null;
	// }
	//
	// }
}
