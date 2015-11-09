package org.cclgdx.actions.instant;

import org.cclgdx.actions.base.CCFiniteTimeAction;
import org.cclgdx.nodes.CCNode;

/**
 * Hide the node
 */
public class CCHide extends CCInstantAction {

	public static CCHide action() {
		return new CCHide();
	}

	@Override
	public CCHide copy() {
		return new CCHide();
	}

	@Override
	public void start(CCNode aTarget) {
		super.start(aTarget);
		target.setVisible(false);
	}

	@Override
	public CCFiniteTimeAction reverse() {
		return new CCShow();
	}
}
