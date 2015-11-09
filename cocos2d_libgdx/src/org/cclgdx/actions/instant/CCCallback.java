package org.cclgdx.actions.instant;

import org.cclgdx.actions.base.CCActionCallback;
import org.cclgdx.nodes.CCNode;

//
// CallFunc
//

/**
 * Calls a 'callback'
 */
public class CCCallback extends CCInstantAction {
	protected CCActionCallback callback;

	/** creates the action with the callback */
	public static CCCallback action(CCActionCallback callback) {
		return new CCCallback(callback);
	}

	/**
	 * creates an action with a callback
	 */
	protected CCCallback(CCActionCallback callback) {
		this.callback = callback;
	}

	@Override
	public CCCallback copy() {
		return new CCCallback(callback);
	}

	@Override
	public void start(CCNode aTarget) {
		super.start(aTarget);
		execute();
	}

	/**
	 * executes the callback
	 */
	public void execute() {
		callback.execute();
	}
}
