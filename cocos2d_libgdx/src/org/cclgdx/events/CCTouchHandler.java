package org.cclgdx.events;

import org.cclgdx.protocols.CCTouchDelegateProtocol;

/*
 * This file contains the delegates of the touches
 * There are 2 possible delegates:
 *   - CCStandardTouchHandler: propagates all the events at once
 *   - CCTargetedTouchHandler: propagates 1 event at the time
 */

/**
 * CCTouchHandler Object than contains the delegate and priority of the event
 * handler.
 */
public class CCTouchHandler implements CCTouchDelegateProtocol {
	/** delegate */
	private final CCTouchDelegateProtocol delegate_;
	/** priority */
	private int priority_;
	/** enabled selectors */
	int enabledSelectors_;

	public CCTouchDelegateProtocol getDelegate() {
		return delegate_;
	}

	public int getPriority() {
		return priority_;
	}

	public void setPriority(int prio) {
		priority_ = prio;
	}

	public void setSelectorFlag(int sf) {
		enabledSelectors_ = sf;
	}

	public int getSelectorFlag() {
		return enabledSelectors_;
	}

	/** allocates a TouchHandler with a delegate and a priority */
	public static CCTouchHandler makeHandler(CCTouchDelegateProtocol delegate, int priority) {
		return new CCTouchHandler(delegate, priority);
	}

	/** initializes a TouchHandler with a delegate and a priority */
	public CCTouchHandler(CCTouchDelegateProtocol delegate, int priority) {
		assert delegate != null : "Touch delegate may not be nil";
		delegate_ = delegate;
		priority_ = priority;
		// enabledSelectors_ =
		// ccTouchSelectorFlag.ccTouchSelectorNoneBit.getFlag();
	}

	@Override
	public boolean ccTouchesBegan(CCMotionEvent event) {
		if (delegate_ != null) {
			return delegate_.ccTouchesBegan(event);
		}
		return CCTouchDispatcher.kEventIgnored;
	}

	@Override
	public boolean ccTouchesMoved(CCMotionEvent event) {
		if (delegate_ != null) {
			return delegate_.ccTouchesMoved(event);
		}
		return CCTouchDispatcher.kEventIgnored;
	}

	@Override
	public boolean ccTouchesEnded(CCMotionEvent event) {
		if (delegate_ != null) {
			return delegate_.ccTouchesEnded(event);
		}
		return CCTouchDispatcher.kEventIgnored;
	}

	@Override
	public boolean ccTouchesCancelled(CCMotionEvent event) {
		if (delegate_ != null)
			return delegate_.ccTouchesCancelled(event);
		return CCTouchDispatcher.kEventIgnored;
	}
}
