package logic;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

///http://gamedev.stackexchange.com/questions/31021/quadtree-store-only-points-or-regions
public class QuadtreeNode implements Iterable<Shape> {
	private int MAX_OBJECTS = 5;
	private int MAX_DEPTH = 5;

	/** The number of levels above this node to the root. */
	private final int depth;
	private List<Shape> objects;
	public Rectangle bounds;
	private QuadtreeNode parent;
	public List<QuadtreeNode> nodes;

	/**
	 * Create a root QuadtreeNode. It has no parent by definition.
	 * 
	 * @param pBounds
	 *            The bounding box of this level of quadtree. Any objects
	 *            outside this area will be ignored.
	 */
	public QuadtreeNode(Rectangle pBounds) {
		this(null, pBounds, 0);
	}

	private QuadtreeNode(QuadtreeNode pparent, Rectangle pBounds, int idepth) {
		parent = pparent;
		objects = new ArrayList<>();
		nodes = new ArrayList<>(4);
		bounds = pBounds;
		depth = idepth;
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

	/**
	 * Insert a new object into the quadtree.
	 * 
	 * @param s
	 *            The bounding shape of the object
	 * @return <b>true</b> if the insertion was successful.<br>
	 *         <b>false</b> if the insertion failed because the object does not
	 *         belong in this level of the quad tree.
	 */
	public boolean insert(Shape s) {
		// Does the shape fit into this quadtree?
		if (!s.getBounds().intersects(bounds)) {
			return false;
		}

		// No split, add to existing children
		if (!nodes.isEmpty()) {

			for (QuadtreeNode st : nodes) {
				st.insert(s);
			}
			return true;
		}

		// Add to current node (leaf)
		objects.add(s);
		// Split
		if (objects.size() > MAX_OBJECTS && depth < MAX_DEPTH) {
			split();
		}

		return true;
	}

	public boolean update(Shape s) {
		// TODO
		return false;
	}

	/**
	 * This method involves constructing a data set from a tree structure and
	 * its use should be minimized.
	 * 
	 * @return A set containing all the objects at this level or below in the
	 *         quadtree.
	 */
	public Set<Shape> getObjects() {
		Set<Shape> retobj = new HashSet<>(objects);
		for (QuadtreeNode st : nodes) {
			retobj.addAll(st.getObjects());
		}
		return retobj;
	}

	/**
	 * Retrieve all the objects using {@link #getObjects()} and return its
	 * iterator. This method is suitable for use in <i>foreach</i> loops.<br>
	 * This method involves constructing a data set from a tree structure and
	 * its use should be minimized.
	 */
	@Override
	public Iterator<Shape> iterator() {
		return getObjects().iterator();
	}

	/**
	 * Gets the depth of this node in the quadtree structure. The depth is
	 * defined as the number of levels of nodes above this node to the root.<br>
	 * This function returns a stored value, so there are no performance
	 * implications.
	 * 
	 * @return the depth of this node
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * Gets the greatest number of levels in the quadtree below this node.
	 * 
	 * @return the number of levels to the deepest part of the tree
	 */
	public int getDepthBelow() {
		int ret = 0;
		for (QuadtreeNode st : nodes) {
			ret = Math.max(ret, st.getDepthBelow() + 1);
		}
		return ret;
	}

	/**
	 * Applies a consumer to the tree in an pre-order traversal.
	 * 
	 * @param consumer
	 *            the consumer to apply
	 */
	public void processNodes(Consumer<QuadtreeNode> consumer) {
		consumer.accept(this);
		for (QuadtreeNode node : nodes) {
			node.processNodes(consumer);
		}
	}

	public Rectangle getBounds() {
		return (Rectangle) bounds.clone();
	}
}
