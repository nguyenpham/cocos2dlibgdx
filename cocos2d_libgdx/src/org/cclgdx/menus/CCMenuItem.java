package org.cclgdx.menus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.cclgdx.nodes.CCNode;
import org.cclgdx.types.CGPoint;

/**
 * CCMenuItem base class
 * 
 * Subclass CCMenuItem (or any subclass) to create your custom CCMenuItem
 * objects.
 */

public class CCMenuItem extends CCNode {
	public static final int kCurrentItem = 0xc0c05001;
	public static final int kZoomActionTag = 0xc0c05002;

	protected boolean isEnabled_;
	/** returns whether or not the item is selected @since v0.8.2 */
	protected boolean isSelected_;

	protected Object targetCallback;
	protected String selector;

	private Method invocation;

	/** Creates a CCMenuItem with a target/selector */
	public static CCMenuItem item(Object target, String selector) {
		return new CCMenuItem(target, selector);
	}

	/**
	 * Initializes a menu item with a target/selector
	 */
	public CCMenuItem(Object rec, String cb) {
		targetCallback = rec;
		selector = cb;

		setAnchorPoint(CGPoint.make(0.5f, 0.5f));

		invocation = null;
		if (rec != null && cb != null) {
			Class<?> cls = rec.getClass();
			try {
				invocation = cls.getMethod(cb, Object.class);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}

		isEnabled_ = true;
		isSelected_ = false;
	}

	/**
	 * Activate the item
	 */
	public void activate() {
		if (isEnabled_) {
			if (targetCallback != null & invocation != null) {
				try {
					invocation.invoke(targetCallback, this);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					if (e.getTargetException() instanceof RuntimeException)
						throw (RuntimeException) e.getTargetException();
					else
						e.printStackTrace();
				}
			}
		}
	}

	/**
	 * The item was selected (not activated), similar to "mouse-over"
	 */
	public void selected() {
		isSelected_ = true;
	}

	/**
	 * The item was unselected
	 */
	public void unselected() {
		isSelected_ = false;
	}

	/**
	 * Enable or disabled the MenuItem
	 */
	public void setIsEnabled(boolean enabled) {
		isEnabled_ = enabled;
	}

	/**
	 * Returns whether or not the MenuItem is enabled
	 */
	public boolean isEnabled() {
		return isEnabled_;
	}
}
