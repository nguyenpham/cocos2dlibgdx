package org.cclgdx.events;

public class CCKeyEvent {

	public static final int ACTION_DOWN = 0;
	public static final int ACTION_UP = 1;
	public static final int ACTION_TYPED = 2;

	private final int action;

	private final int keyCode;

	public CCKeyEvent(int action, int keyCode) {
		this.action = action;
		this.keyCode = keyCode;
	}

	public int getAction() {
		return action;
	}

	public int getKeyCode() {
		return keyCode;
	}

	@Override
	public String toString() {
		return "<action=" + action + ", keycode=" + keyCode + ">";
	}
}
