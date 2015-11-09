package org.cclgdx.actions.instant;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.cclgdx.nodes.CCNode;

//
// CallFunc
//

/**
 * Calls a 'callback'
 */
public class CCCallFunc extends CCInstantAction {
	protected Object targetCallback;
	protected String selector;
	protected Class<?> partypes[];

	protected Method invocation;

	/** creates the action with the callback */
	public static CCCallFunc action(Object target, String selector) {
		return new CCCallFunc(target, selector, null);
	}

	/**
	 * creates an action with a callback
	 */
	protected CCCallFunc(Object t, String s, Class<?>[] p) {
		targetCallback = t;
		selector = s;
		partypes = p;

		if (partypes == null) {
			try {
				Class<?> cls = targetCallback.getClass();
				invocation = cls.getMethod(selector);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Class<?> cls = targetCallback.getClass();
				invocation = cls.getMethod(selector, partypes);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public CCCallFunc copy() {
		return new CCCallFunc(targetCallback, selector, partypes);
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
		try {
			invocation.invoke(targetCallback);
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
