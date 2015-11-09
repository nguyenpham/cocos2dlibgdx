package org.cclgdx.actions.interval;

import org.cclgdx.nodes.CCNode;
import org.cclgdx.types.CGPoint;

//
// MoveTo
//

/**
 * Moves a CCNode object to the position x,y. x and y are absolute coordinates
 * by modifying it's position attribute.
 */
public class CCMoveTo extends CCIntervalAction {
	private final CGPoint endPosition;
	private final CGPoint startPosition;
	protected CGPoint delta;

	/** creates the action */
	public static CCMoveTo action(float t, CGPoint pos) {
		return new CCMoveTo(t, pos);
	}

	/** initializes the action */
	protected CCMoveTo(float t, CGPoint pos) {
		super(t);
		startPosition = CGPoint.zero();
		endPosition = CGPoint.make(pos.x, pos.y);
		delta = CGPoint.zero();
	}

	/**
	 * Lets extend basic functionality for reuse action.
	 */
	public void setEndPosition(CGPoint pos) {
		endPosition.set(pos);
	}

	@Override
	public CCIntervalAction copy() {
		return new CCMoveTo(duration, endPosition);
	}

	@Override
	public void start(CCNode aTarget) {
		super.start(aTarget);

		startPosition.set(target.getX(), target.getY());
		delta.set(endPosition.x - startPosition.x, endPosition.y - startPosition.y);
	}

	@Override
	public void update(float t) {
		target.setPosition(startPosition.x + delta.x * t, startPosition.y + delta.y * t);
	}
}
