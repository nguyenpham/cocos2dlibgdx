package org.cclgdx.types;

public class CGPoint {
	private static final float kCGPointEpsilon = 0.00000012f;
	public float x, y;

	private static final CGPoint ZERO_POINT = new CGPoint(0, 0);

	public static CGPoint getZero() {
		return ZERO_POINT;
	}

	public static CGPoint zero() {
		return new CGPoint(0, 0);
	}

	public static CGPoint make(float x, float y) {
		return new CGPoint(x, y);
	}

	public CGPoint() {
		this(0, 0);
	}

	private CGPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Set value
	 */
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void set(CGPoint p) {
		this.x = p.x;
		this.y = p.y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public static boolean equalToPoint(CGPoint p1, CGPoint p2) {
		return p1.x == p2.x && p1.y == p2.y;
	}

	/**
	 * Helper macro that creates a CCPoint
	 * 
	 * @return CCPoint
	 */
	public static CGPoint ccp(float x, float y) {
		return new CGPoint(x, y);
	}

	/**
	 * Returns opposite of point.
	 * 
	 * @return CCPoint
	 */
	public static CGPoint ccpNeg(final CGPoint v) {
		return ccp(-v.x, -v.y);
	}

	/**
	 * Calculates sum of two points.
	 * 
	 * @return CCPoint
	 */
	public static CGPoint ccpAdd(final CGPoint v1, final CGPoint v2) {
		return ccp(v1.x + v2.x, v1.y + v2.y);
	}

	/**
	 * Calculates difference of two points.
	 * 
	 * @return CCPoint
	 */
	public static CGPoint ccpSub(final CGPoint v1, final CGPoint v2) {
		return ccp(v1.x - v2.x, v1.y - v2.y);
	}

	/**
	 * Returns point multiplied by given factor.
	 * 
	 * @return CCPoint
	 */
	public static CGPoint ccpMult(final CGPoint v, final float s) {
		return ccp(v.x * s, v.y * s);
	}

	/**
	 * Calculates midpoint between two points.
	 * 
	 * @return CCPoint
	 */
	public static CGPoint ccpMidpoint(final CGPoint v1, final CGPoint v2) {
		return ccpMult(ccpAdd(v1, v2), 0.5f);
	}

	/**
	 * Calculates dot product of two points.
	 * 
	 * @return float
	 */
	public static float ccpDot(final CGPoint v1, final CGPoint v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}

	/**
	 * Calculates cross product of two points.
	 * 
	 * @return float
	 */
	public static float ccpCross(final CGPoint v1, final CGPoint v2) {
		return v1.x * v2.y - v1.y * v2.x;
	}

	/**
	 * Calculates perpendicular of v, rotated 90 degrees counter-clockwise --
	 * cross(v, perp(v)) >= 0
	 * 
	 * @return CCPoint
	 */
	public static CGPoint ccpPerp(final CGPoint v) {
		return ccp(-v.y, v.x);
	}

	/**
	 * Calculates perpendicular of v, rotated 90 degrees clockwise -- cross(v,
	 * rperp(v)) <= 0
	 * 
	 * @return CCPoint
	 */
	public static CGPoint ccpRPerp(final CGPoint v) {
		return ccp(v.y, -v.x);
	}

	/**
	 * Calculates the projection of v1 over v2.
	 * 
	 * @return CCPoint
	 */
	public static CGPoint ccpProject(final CGPoint v1, final CGPoint v2) {
		return ccpMult(v2, ccpDot(v1, v2) / ccpDot(v2, v2));
	}

	/**
	 * Rotates two points.
	 * 
	 * @return CCPoint
	 */
	public static CGPoint ccpRotate(final CGPoint v1, final CGPoint v2) {
		return ccp(v1.x * v2.x - v1.y * v2.y, v1.x * v2.y + v1.y * v2.x);
	}

	/**
	 * Unrotates two points.
	 * 
	 * @return CCPoint
	 */
	public static CGPoint ccpUnrotate(final CGPoint v1, final CGPoint v2) {
		return ccp(v1.x * v2.x + v1.y * v2.y, v1.y * v2.x - v1.x * v2.y);
	}

	/**
	 * Calculates the square length of a CCPoint (not calling sqrt() )
	 * 
	 * @return float
	 */
	public static float ccpLengthSQ(final CGPoint v) {
		return ccpDot(v, v);
	}

	/**
	 * Calculates distance between point and origin
	 * 
	 * @return CGFloat
	 */
	public static float ccpLength(final CGPoint v) {
		return (float) Math.sqrt(ccpLengthSQ(v));
	}

	/**
	 * Calculates the distance between two points
	 * 
	 * @return float
	 */
	public static float ccpDistance(final CGPoint v1, final CGPoint v2) {
		return ccpLength(ccpSub(v1, v2));
	}

	/**
	 * Returns point multiplied to a length of 1.
	 * 
	 * @return CCPoint
	 */
	public static CGPoint ccpNormalize(final CGPoint v) {
		return ccpMult(v, 1.0f / ccpLength(v));
	}

	/**
	 * Converts radians to a normalized vector.
	 * 
	 * @return CCPoint
	 */
	public static CGPoint ccpForAngle(final float a) {
		return ccp((float) Math.cos(a), (float) Math.sin(a));
	}

	/**
	 * Converts a vector to radians.
	 * 
	 * @return float
	 */
	public static float ccpToAngle(final CGPoint v) {
		return (float) Math.atan2(v.y, v.x);
	}
}
