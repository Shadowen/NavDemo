package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

import javax.swing.JPanel;

import logic.Quadtree;
import logic.QTNode;
import logic.Unit;

public class DisplayPanel extends JPanel implements MouseListener,
		MouseMotionListener {
	private List<Unit> units;
	private Set<Unit> selectedUnits = new HashSet<>();
	private Quadtree<Unit> qt;

	private Point mouseDownPosition;
	private Point mouseDragPosition;
	private Point mousePosition = new Point(0, 0);
	private Optional<Unit> unitAtMousePoint = Optional.empty();

	private Graphics g;

	public DisplayPanel(List<Unit> u, Quadtree<Unit> iqt) {
		units = u;
		qt = iqt;
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		qt.stream().filter(u -> u.contains(e.getPoint())).findFirst()
				.ifPresent(u -> {
					selectedUnits.clear();
					selectedUnits.add(u);
				});
		;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseDownPosition = e.getPoint();
		mouseDragPosition = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TOOD make all mouse functions left click only
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			int left = Math.min(mouseDragPosition.x, mouseDownPosition.x);
			int right = Math.max(mouseDragPosition.x, mouseDownPosition.x);
			int top = Math.min(mouseDragPosition.y, mouseDownPosition.y);
			int bottom = Math.max(mouseDragPosition.y, mouseDownPosition.y);
			Rectangle selectRect = new Rectangle(left, top, right - left,
					bottom - top);
			selectedUnits.clear();
			selectedUnits = qt.stream().filter(u -> u.intersects(selectRect))
					.reduce(selectedUnits, (r, u) -> {
						r.add(u);
						return r;
					}, (x, y) -> {
						x.addAll(y);
						return x;
					});

			mouseDownPosition = null;
			mouseDragPosition = null;
		default:
			;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDragPosition = e.getPoint();
		mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		mousePosition = arg0.getPoint();
		unitAtMousePoint = qt.getAt(mousePosition);
	}

	@Override
	public void paintComponent(Graphics ig) {
		super.paintComponent(ig);
		g = ig;

		int totalDepth = qt.getTotalDepth();
		qt.processNodes(tree -> {
			if (tree.nodes.isEmpty()) {
				Rectangle bounds = tree.getBounds();
				g.setColor(new Color(0, 0, 0, (int) (255 - (double) tree
						.getDepth() / totalDepth * 200)));
				g.drawString("Depth: " + String.valueOf(tree.getDepth()),
						bounds.x + 5, bounds.y + 15);
				g.drawString(
						"Objects: " + String.valueOf(tree.getObjects().size()),
						bounds.x + 5, bounds.y + 30);
				g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
			}
		});

		if (mouseDownPosition != null) {
			g.setColor(Color.GREEN);
			g.fillOval(mouseDownPosition.x - 5, mouseDownPosition.y - 5, 10, 10);

			int left = Math.min(mouseDragPosition.x, mouseDownPosition.x);
			int right = Math.max(mouseDragPosition.x, mouseDownPosition.x);
			int top = Math.min(mouseDragPosition.y, mouseDownPosition.y);
			int bottom = Math.max(mouseDragPosition.y, mouseDownPosition.y);
			g.drawRect(left, top, right - left, bottom - top);
		}
		if (mouseDragPosition != null) {
			g.setColor(Color.RED);
			g.fillOval(mouseDragPosition.x - 5, mouseDragPosition.y - 5, 10, 10);
		}

		units.forEach(u -> {
			g.setColor(Color.BLACK);
			drawUnit(u);
			g.setColor(Color.WHITE);
			g.drawString(u.toString(), u.shape.getBounds().x,
					u.shape.getBounds().y);
		});
		selectedUnits.forEach(u -> {
			g.setColor(Color.GREEN);
			drawUnit(u);
		});
		qt.getNearest(mousePosition).ifPresent(u -> {
			g.setColor(Color.RED);
			drawUnit(u);
		});
		unitAtMousePoint.ifPresent(u -> {
			g.setColor(Color.BLUE);
			drawUnit(u);
		});
	}

	/**
	 * Draw a single unit.
	 * 
	 * @param u
	 *            The unit to be drawn
	 */
	private void drawUnit(Unit u) {
		g.fillRect(u.shape.getBounds().x, u.shape.getBounds().y,
				u.shape.getBounds().width, u.shape.getBounds().height);
	}
}
