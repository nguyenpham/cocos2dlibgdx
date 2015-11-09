package org.cclgdx.actions.instant;

import org.cclgdx.nodes.CCNode;
import org.cclgdx.sprites.CCSprite;

/**
 * Flips the sprite vertically
 */
public class CCFlipY extends CCInstantAction {
	boolean flipY;

	public static CCFlipY action(boolean fy) {
		return new CCFlipY(fy);
	}

	public CCFlipY(boolean fy) {
		super();
		flipY = fy;
	}

	@Override
	public CCFlipY copy() {
		CCFlipY copy = new CCFlipY(flipY);
		return copy;
	}

	@Override
	public void start(CCNode aTarget) {
		super.start(aTarget);
		CCSprite sprite = (CCSprite) aTarget;
		sprite.setFlipY(flipY);
	}

	@Override
	public CCFlipY reverse() {
		return CCFlipY.action(!flipY);
	}
}
