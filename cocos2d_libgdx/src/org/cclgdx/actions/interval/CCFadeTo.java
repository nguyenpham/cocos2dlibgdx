package org.cclgdx.actions.interval;

import org.cclgdx.nodes.CCNode;

/**
 * Fades an object that implements the CCRGBAProtocol protocol. It modifies the
 * opacity from the current value to a custom one.
 * 
 * @warning This action doesn't support "reverse"
 */
public class CCFadeTo extends CCIntervalAction {
	int toOpacity;
	int fromOpacity;

	/** creates an action with duration and opactiy */
	public static CCFadeTo action(float t, int a) {
		return new CCFadeTo(t, a);
	}

	/** initializes the action with duration and opacity */
	protected CCFadeTo(float t, int a) {
		super(t);
		toOpacity = a;
	}

	@Override
	public CCFadeTo copy() {
		return new CCFadeTo(duration, toOpacity);
	}

	@Override
	public void start(CCNode aTarget) {
		super.start(aTarget);
		fromOpacity = target.getOpacity();
	}

	@Override
	public void update(float t) {
		target.setOpacity((int) (fromOpacity + (toOpacity - fromOpacity) * t));
	}
}
