package org.cclgdx.actions.instant;

import java.lang.reflect.InvocationTargetException;

/**
 * Calls a 'callback' with the node as the first argument
 * N means Node
 */
public class CCCallFuncN extends CCCallFunc {

    public static CCCallFuncN action(Object t, String s) {
        return new CCCallFuncN(t, s, new Class[] { Object.class });
    }

    /**
     * creates the action with the callback
     * @param classes 
     */
    protected CCCallFuncN(Object t, String s, Class<?>[] p) {
        super(t, s, p);
    }

    /**
     * executes the callback
     */
    public void execute() {
        try {
			invocation.invoke(targetCallback, new Object[]{target});
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
