package org.cclgdx.actions.instant;

import org.cclgdx.actions.base.CCFiniteTimeAction;
import org.cclgdx.nodes.CCNode;

/**
 * Show the node
 */
public class CCShow extends CCInstantAction {

	public static CCShow action() {
		return new CCShow();
	}

	@Override
	public CCShow copy() {
		return new CCShow();
	}

	@Override
	public void start(CCNode aTarget) {
		super.start(aTarget);
		target.setVisible(true);
	}

	@Override
	public CCFiniteTimeAction reverse() {
		return new CCHide();
	}
}
