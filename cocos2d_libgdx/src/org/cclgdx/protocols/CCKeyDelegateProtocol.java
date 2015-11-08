package org.cclgdx.protocols;

import org.cclgdx.events.CCKeyEvent;

public interface CCKeyDelegateProtocol {
	boolean ccKeyDown(CCKeyEvent event);

	boolean ccKeyUp(CCKeyEvent event);

	boolean ccKeyTyped(CCKeyEvent event);
}
