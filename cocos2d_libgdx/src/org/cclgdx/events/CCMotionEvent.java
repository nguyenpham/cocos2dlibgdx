package org.cclgdx.events;

import javax.swing.text.View;

public class CCMotionEvent {

	/**
	 * Constant for {@link #getActionMasked}: A pressed gesture has started, the
	 * motion contains the initial starting location.
	 * <p>
	 * This is also a good time to check the button state to distinguish
	 * secondary and tertiary button clicks and handle them appropriately. Use
	 * {@link #getButtonState} to retrieve the button state.
	 * </p>
	 */
	public static final int ACTION_DOWN = 0;

	/**
	 * Constant for {@link #getActionMasked}: A pressed gesture has finished,
	 * the motion contains the final release location as well as any
	 * intermediate points since the last down or move event.
	 */
	public static final int ACTION_UP = 1;

	/**
	 * Constant for {@link #getActionMasked}: A change has happened during a
	 * press gesture (between {@link #ACTION_DOWN} and {@link #ACTION_UP}). The
	 * motion contains the most recent point, as well as any intermediate points
	 * since the last down or move event.
	 */
	public static final int ACTION_MOVE = 2;

	/**
	 * Constant for {@link #getActionMasked}: The current gesture has been
	 * aborted. You will not receive any more points in it. You should treat
	 * this as an up event, but not perform any action that you normally would.
	 */
	public static final int ACTION_CANCEL = 3;

	/**
	 * Constant for {@link #getActionMasked}: A movement has happened outside of
	 * the normal bounds of the UI element. This does not provide a full
	 * gesture, but only the initial location of the movement/touch.
	 */
	public static final int ACTION_OUTSIDE = 4;

	/**
	 * Constant for {@link #getActionMasked}: A non-primary pointer has gone
	 * down.
	 * <p>
	 * Use {@link #getActionIndex} to retrieve the index of the pointer that
	 * changed.
	 * </p>
	 * <p>
	 * The index is encoded in the {@link #ACTION_POINTER_INDEX_MASK} bits of
	 * the unmasked action returned by {@link #getAction}.
	 * </p>
	 */
	public static final int ACTION_POINTER_DOWN = 5;

	/**
	 * Constant for {@link #getActionMasked}: A non-primary pointer has gone up.
	 * <p>
	 * Use {@link #getActionIndex} to retrieve the index of the pointer that
	 * changed.
	 * </p>
	 * <p>
	 * The index is encoded in the {@link #ACTION_POINTER_INDEX_MASK} bits of
	 * the unmasked action returned by {@link #getAction}.
	 * </p>
	 */
	public static final int ACTION_POINTER_UP = 6;

	/**
	 * Constant for {@link #getActionMasked}: A change happened but the pointer
	 * is not down (unlike {@link #ACTION_MOVE}). The motion contains the most
	 * recent point, as well as any intermediate points since the last hover
	 * move event.
	 * <p>
	 * This action is always delivered to the window or view under the pointer.
	 * </p>
	 * <p>
	 * This action is not a touch event so it is delivered to
	 * {@link View#onGenericMotionEvent(MotionEvent)} rather than
	 * {@link View#onTouchEvent(MotionEvent)}.
	 * </p>
	 */
	public static final int ACTION_HOVER_MOVE = 7;

	/**
	 * Constant for {@link #getActionMasked}: The motion event contains relative
	 * vertical and/or horizontal scroll offsets. Use {@link #getAxisValue(int)}
	 * to retrieve the information from {@link #AXIS_VSCROLL} and
	 * {@link #AXIS_HSCROLL}. The pointer may or may not be down when this event
	 * is dispatched.
	 * <p>
	 * This action is always delivered to the window or view under the pointer,
	 * which may not be the window or view currently touched.
	 * </p>
	 * <p>
	 * This action is not a touch event so it is delivered to
	 * {@link View#onGenericMotionEvent(MotionEvent)} rather than
	 * {@link View#onTouchEvent(MotionEvent)}.
	 * </p>
	 */
	public static final int ACTION_SCROLL = 8;

	/**
	 * Constant for {@link #getActionMasked}: The pointer is not down but has
	 * entered the boundaries of a window or view.
	 * <p>
	 * This action is always delivered to the window or view under the pointer.
	 * </p>
	 * <p>
	 * This action is not a touch event so it is delivered to
	 * {@link View#onGenericMotionEvent(MotionEvent)} rather than
	 * {@link View#onTouchEvent(MotionEvent)}.
	 * </p>
	 */
	public static final int ACTION_HOVER_ENTER = 9;

	/**
	 * Constant for {@link #getActionMasked}: The pointer is not down but has
	 * exited the boundaries of a window or view.
	 * <p>
	 * This action is always delivered to the window or view that was previously
	 * under the pointer.
	 * </p>
	 * <p>
	 * This action is not a touch event so it is delivered to
	 * {@link View#onGenericMotionEvent(MotionEvent)} rather than
	 * {@link View#onTouchEvent(MotionEvent)}.
	 * </p>
	 */
	public static final int ACTION_HOVER_EXIT = 10;

	/**
	 * Bits in the action code that represent a pointer index, used with
	 * {@link #ACTION_POINTER_DOWN} and {@link #ACTION_POINTER_UP}. Shifting
	 * down by {@link #ACTION_POINTER_INDEX_SHIFT} provides the actual pointer
	 * index where the data for the pointer going up or down can be found; you
	 * can get its identifier with {@link #getPointerId(int)} and the actual
	 * data with {@link #getX(int)} etc.
	 * 
	 * @see #getActionIndex
	 */

	public int x, y, pointer, button, action;
	private final int pid;
	private static int pidcount = 0;

	public int getPid() {
		return pid;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public CCMotionEvent(int x, int y, int pointer, int button, int action) {
		this.x = x;
		this.y = y;
		this.pointer = pointer;
		this.button = button;
		this.action = action;

		if (action == ACTION_DOWN) {
			pidcount++;
		}
		this.pid = pidcount;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getPointer() {
		return pointer;
	}

	public void setPointer(int pointer) {
		this.pointer = pointer;
	}

	public int getButton() {
		return button;
	}

	public void setButton(int button) {
		this.button = button;
	}

	private static final String actionNames[] = { "ACTION_DOWN", "ACTION_UP", "ACTION_MOVE", "ACTION_CANCEL", "ACTION_OUTSIDE", "ACTION_POINTER_DOWN", "ACTION_POINTER_UP", "ACTION_HOVER_MOVE",
			"ACTION_SCROLL", "ACTION_HOVER_ENTER", "ACTION_HOVER_EXIT" };

	@Override
	public String toString() {
		String str = "x=" + x + ", y=" + y + ", pointer=" + pointer + ", button=" + button + ", action=";

		if (action >= 0 && action < actionNames.length) {
			str += actionNames[action];
		} else {
			str += "UNKNOWN";
		}
		return str;
	}
}
