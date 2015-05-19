package logic;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Unit implements Shape {
	public final int id;
	public Shape shape;

	public Unit(int iid, Shape ibounds) {
		id = iid;
		shape = ibounds;
	}

	@Override
	public boolean contains(Point2D arg0) {
		return shape.contains(arg0);
	}

	@Override
	public boolean contains(Rectangle2D arg0) {
		return shape.contains(arg0);
	}

	@Override
	public boolean contains(double arg0, double arg1) {
		return shape.contains(arg0, arg1);
	}

	@Override
	public boolean contains(double arg0, double arg1, double arg2, double arg3) {
		return shape.contains(arg0, arg1, arg2, arg3);
	}

	@Override
	public Rectangle getBounds() {
		return shape.getBounds();
	}

	@Override
	public Rectangle2D getBounds2D() {
		return shape.getBounds2D();
	}

	@Override
	public PathIterator getPathIterator(AffineTransform arg0) {
		return shape.getPathIterator(arg0);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform arg0, double arg1) {
		return shape.getPathIterator(arg0, arg1);
	}

	@Override
	public boolean intersects(Rectangle2D arg0) {
		return shape.intersects(arg0);
	}

	@Override
	public boolean intersects(double arg0, double arg1, double arg2, double arg3) {
		return shape.intersects(arg0, arg1, arg2, arg3);
	}

	public String toString() {
		return String.valueOf(id);
	}
}
