package graphics;

import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import logic.MovementManager;
import logic.Quadtree;
import logic.Unit;

public class MainFrame extends JFrame implements ComponentListener {
	public static void main(String[] args) {
		new MainFrame();
	}

	private MovementManager movement;
	private Quadtree<Unit> qt;

	private DisplayPanel dp;

	private int targetFPS = 60;
	private volatile int framesSinceLast = 0;
	private volatile int currentFPS = 0;

	public MainFrame() {
		// Initialize
		movement = new MovementManager();
		qt = new Quadtree<Unit>();
		for (Unit u : movement.units) {
			qt.insert(u);
		}
		// Setup
		dp = new DisplayPanel(movement.units, qt);
		setContentPane(dp);

		Rectangle screenSize = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		setBounds(0, 0, screenSize.width, screenSize.height - 200);
		setExtendedState(MAXIMIZED_BOTH);

		addComponentListener(this);
		setVisible(true);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// FPS
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				repaint();
			}
		}, 0, 1000 / targetFPS);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				currentFPS = framesSinceLast;
				framesSinceLast = 0;
			}
		}, 0, 1000);

		// setSize(300, 89);
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		qt.setBounds(dp.getBounds());
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		framesSinceLast++;
	}

}
