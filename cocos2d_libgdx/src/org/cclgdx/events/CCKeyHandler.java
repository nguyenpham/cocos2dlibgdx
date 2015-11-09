package org.cclgdx.events;

import org.cclgdx.protocols.CCKeyDelegateProtocol;

public class CCKeyHandler implements CCKeyDelegateProtocol {
	private final CCKeyDelegateProtocol delegate_;
	boolean enabledSelectors_;
	private int priority_;

	public CCKeyDelegateProtocol getDelegate() {
		return delegate_;
	}

	public void setSelectorFlag(boolean sf) {
		enabledSelectors_ = sf;
	}

	public boolean getSelectorFlag() {
		return enabledSelectors_;
	}

	public int getPriority() {
		return priority_;
	}

	public void setPriority(int prio) {
		priority_ = prio;
	}

	public static CCKeyHandler makeHandler(CCKeyDelegateProtocol delegate, int priority) {
		return new CCKeyHandler(delegate, priority);
	}

	public CCKeyHandler(CCKeyDelegateProtocol delegate, int priority) {
		assert delegate != null : "Key delegate may not be nil";
		delegate_ = delegate;
		enabledSelectors_ = false;
		priority_ = priority;
	}

	@Override
	public boolean ccKeyDown(CCKeyEvent event) {
		if (delegate_ != null) {
			return delegate_.ccKeyDown(event);
		} else {
			return CCKeyDispatcher.kEventIgnored;
		}
	}

	@Override
	public boolean ccKeyUp(CCKeyEvent event) {
		if (delegate_ != null) {
			return delegate_.ccKeyUp(event);
		} else {
			return CCKeyDispatcher.kEventIgnored;
		}
	}

	@Override
	public boolean ccKeyTyped(CCKeyEvent event) {
		if (delegate_ != null) {
			return delegate_.ccKeyTyped(event);
		} else {
			return CCKeyDispatcher.kEventIgnored;
		}
	}

}
