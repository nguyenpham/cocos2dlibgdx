package org.cclgdx.types;

import com.badlogic.gdx.math.Rectangle;

public class CGRect extends Rectangle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2357553227448625243L;

	public CGRect() {
		this(0, 0, 0, 0);
	}

	public CGRect(final CGPoint origin, final CGSize size) {
		this(origin.x, origin.y, size.width, size.height);
	}

	public CGRect(final Rectangle r) {
		this(r.x, r.y, r.width, r.height);
	}

	private static final CGRect ZERO_RECT = new CGRect(0, 0, 0, 0);

	public static CGRect getZero() {
		return ZERO_RECT;
	}

	public static CGRect zero() {
		return new CGRect(0, 0, 0, 0);
	}

	public static CGRect make(final CGPoint origin, final CGSize size) {
		return new CGRect(origin.x, origin.y, size.width, size.height);
	}

	public static CGRect make(float x, float y, float w, float h) {
		return new CGRect(x, y, w, h);
	}

	public static CGRect make(CGRect r) {
		return new CGRect(r.x, r.y, r.width, r.height);
	}

	public static CGRect make(Rectangle r) {
		return new CGRect(r.x, r.y, r.width, r.height);
	}

	private CGRect(float x, float y, float w, float h) {
		super(x, y, w, h);
	}

	public void set(CGRect r) {
		super.set(r.x, r.y, r.width, r.height);
	}

	public CGPoint getCenter() {
		return CGPoint.make(x + width / 2, y + height / 2);
	}

	@Override
	public String toString() {
		return "((" + x + ", " + y + "),(" + width + ", " + height + "))";
	}

	//
	public static boolean equalToRect(final CGRect r1, final CGRect r2) {
		return Float.compare(r1.x, r2.x) == 0 && Float.compare(r1.y, r2.y) == 0 && Float.compare(r1.width, r2.width) == 0 && Float.compare(r1.height, r2.height) == 0;
	}

	/**
	 * Returns true if aPoint is inside aRect.
	 */
	public static boolean containsPoint(final CGRect aRect, final CGPoint aPoint) {
		return aRect.contains(aPoint.x, aPoint.y);
	}

	public static boolean containsRect(final CGRect aRect, final CGRect bRect) {
		return aRect.contains(bRect);
	}

	// public static boolean intersects(CGRect a, CGRect b) {
	// return a.overlaps(b);
	// }
	public static boolean intersects(CGRect a, CGRect b) {
		return (a.x >= (b.x - a.width) && a.x <= (b.x - a.width) + (b.width + a.width) && a.y >= (b.y - a.height) && a.y <= (b.y - a.height) + (b.height + a.height));
	}

	public static boolean isEmptyRect(CGRect aRect) {
		return (!(aRect.width > 0 && aRect.height > 0));
	}

}
