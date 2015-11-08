package org.cclgdx.actions.interval;

import org.cclgdx.nodes.CCNode;

/**
 * Fades Out an object that implements the CCRGBAProtocol protocol. It modifies
 * the opacity from 255 to 0. The "reverse" of this action is FadeIn
 */
public class CCFadeOut extends CCIntervalAction {

	public static CCFadeOut action(float t) {
		return new CCFadeOut(t);
	}

	protected CCFadeOut(float t) {
		super(t);
	}

	@Override
	public CCFadeOut copy() {
		return new CCFadeOut(duration);
	}

	@Override
	public void update(float t) {
		((CCNode) target).setOpacity((int) (255.0f * (1 - t)));
	}

	@Override
	public CCFadeIn reverse() {
		return new CCFadeIn(duration);
	}
}
