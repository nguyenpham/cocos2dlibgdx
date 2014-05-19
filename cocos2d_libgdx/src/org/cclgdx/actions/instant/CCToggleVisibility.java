package org.cclgdx.actions.instant;

import org.cclgdx.nodes.CCNode;

/**
 * Toggles the visibility of a node
 */
public class CCToggleVisibility extends CCInstantAction {

	public static CCToggleVisibility action() {
		return new CCToggleVisibility();
	}

	@Override
	public CCToggleVisibility copy() {
		return new CCToggleVisibility();
	}

	@Override
	public void start(CCNode aTarget) {
		super.start(aTarget);
		target.setVisible(!target.getVisible());
	}
}
