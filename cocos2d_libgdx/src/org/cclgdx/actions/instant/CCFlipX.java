package org.cclgdx.actions.instant;

import org.cclgdx.nodes.CCNode;
import org.cclgdx.sprites.CCSprite;

/**
 * Flips the sprite horizontally
 */
public class CCFlipX extends CCInstantAction {
	boolean flipX;

	public static CCFlipX action(boolean fx) {
		return new CCFlipX(fx);
	}

	public CCFlipX(boolean fx) {
		super();
		flipX = fx;
	}

	@Override
	public CCFlipX copy() {
		CCFlipX copy = new CCFlipX(flipX);
		return copy;
	}

	@Override
	public void start(CCNode aTarget) {
		super.start(aTarget);
		CCSprite sprite = (CCSprite) aTarget;
		sprite.setFlipX(flipX);
	}

	@Override
	public CCFlipX reverse() {
		return CCFlipX.action(!flipX);
	}
}
