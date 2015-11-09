package org.cclgdx.actions.instant;

import java.lang.reflect.InvocationTargetException;


/**
 * Calls a 'callback' with the node as the first argument and the 2nd argument is data
 * ND means: Node Data
 */
public class CCCallFuncND extends CCCallFuncN {
	protected Object data;

	/** creates the action with the callback and the data to pass as an argument */
	public static CCCallFuncND action(Object t, String s, Object d) {
		return new CCCallFuncND(t, s, d, new Class<?>[] {
				Object.class, Object.class,
		});
	}

	/**
	 * creates the action with the callback and the data to pass as an argument
	 */
	protected CCCallFuncND(Object t, String s, Object d, Class<?>[] p) {
		super(t, s, p);
		data = d;
	}

	/**
	 * executes the callback
	 */
	public void execute() {
		try {
			invocation.invoke(targetCallback, new Object[]{target, data});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			if(e.getTargetException() instanceof RuntimeException)
				throw (RuntimeException)e.getTargetException();
			else
				e.printStackTrace();
		}
	}
}
