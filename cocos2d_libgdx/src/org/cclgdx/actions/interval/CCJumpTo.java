package org.cclgdx.actions.interval;

import org.cclgdx.nodes.CCNode;
import org.cclgdx.types.CGPoint;

//
// JumpTo
//

/**
 * Moves a CCNode object to a parabolic position simulating a jump movement by
 * modifying it's position attribute.
 */
public class CCJumpTo extends CCJumpBy {

	public static CCJumpTo action(float time, CGPoint pos, float height, int jumps) {
		return new CCJumpTo(time, pos, height, jumps);
	}

	protected CCJumpTo(float time, CGPoint pos, float height, int jumps) {
		super(time, pos, height, jumps);
	}

	@Override
	public CCJumpTo copy() {
		return new CCJumpTo(duration, delta, height, jumps);
	}

	@Override
	public void start(CCNode aTarget) {
		super.start(aTarget);
		delta.x -= startPosition.x;
		delta.y -= startPosition.y;
	}
}
