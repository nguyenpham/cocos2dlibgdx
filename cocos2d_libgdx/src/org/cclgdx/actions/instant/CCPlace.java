package org.cclgdx.actions.instant;

import org.cclgdx.nodes.CCNode;
import org.cclgdx.types.CGPoint;

/**
 * Places the node in a certain position
 */
public class CCPlace extends CCInstantAction {
	private final CGPoint position;

	public static CCPlace action(CGPoint pnt) {
		return new CCPlace(pnt);
	}

	/**
	 * creates a Place action with a position
	 */
	protected CCPlace(CGPoint pnt) {
		super();
		position = CGPoint.make(pnt.x, pnt.y);
	}

	@Override
	public CCPlace copy() {
		return new CCPlace(position);
	}

	@Override
	public void start(CCNode aTarget) {
		super.start(aTarget);
		target.setPosition(position);
	}
}
