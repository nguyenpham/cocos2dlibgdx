package org.cclgdx.events;

import java.util.ArrayList;

import org.cclgdx.protocols.CCKeyDelegateProtocol;
import org.cclgdx.utils.collections.ConcNodeCachingLinkedQueue;

public class CCKeyDispatcher {
	public static final boolean kEventHandled = true;
	public static final boolean kEventIgnored = false;

	private final ArrayList<CCKeyHandler> keyHandlers;
	private boolean dispatchEvents;

	public boolean getDispatchEvents() {
		return dispatchEvents;
	}

	public void setDispatchEvents(boolean b) {
		dispatchEvents = b;
	}

	private static CCKeyDispatcher _sharedDispatcher = new CCKeyDispatcher();

	public static CCKeyDispatcher sharedDispatcher() {
		return _sharedDispatcher;
	}

	public CCKeyDispatcher() {
		dispatchEvents = true;
		keyHandlers = new ArrayList<CCKeyHandler>();
	}

	private void addHandler(CCKeyHandler handler) {
		int i = 0;

		synchronized (keyHandlers) {
			for (int ind = 0; ind < keyHandlers.size(); ind++) {
				CCKeyHandler h = keyHandlers.get(ind);
				if (h.getPriority() < handler.getPriority())
					i++;
				if (h.getDelegate() == handler.getDelegate())
					return;
			}
			keyHandlers.add(i, handler);
		}
	}

	public void addDelegate(CCKeyDelegateProtocol delegate, int prio) {
		addHandler(new CCKeyHandler(delegate, prio));
	}

	public void removeDelegate(CCKeyDelegateProtocol delegate) {
		if (delegate == null)
			return;
		synchronized (keyHandlers) {
			for (int ind = 0; ind < keyHandlers.size(); ind++) {
				CCKeyHandler handler = keyHandlers.get(ind);
				if (handler.getDelegate() == delegate) {
					keyHandlers.remove(handler);
					break;
				}
			}
		}
	}

	public void removeAllDelegates() {
		keyHandlers.clear();
	}

	private final ConcNodeCachingLinkedQueue<CCKeyEvent> eventQueue = new ConcNodeCachingLinkedQueue<CCKeyEvent>();

	public void queueKeyEvent(CCKeyEvent event) {
		eventQueue.push(event);
	}

	public void update() {
		CCKeyEvent event;
		while ((event = eventQueue.poll()) != null) {
			switch (event.getAction()) {
			case CCKeyEvent.ACTION_DOWN:
				onKeyDown(event);
				break;
			case CCKeyEvent.ACTION_UP:
				onKeyUp(event);
				break;
			case CCKeyEvent.ACTION_TYPED:
				onKeyTyped(event);
				break;
			}
		}
	}

	public void onKeyDown(CCKeyEvent event) {
		if (dispatchEvents) {

			synchronized (keyHandlers) {
				for (int ind = 0; ind < keyHandlers.size(); ind++) {
					CCKeyHandler handler = keyHandlers.get(ind);
					if (handler.ccKeyDown(event) == kEventHandled)
						break;
				}
			}
		}
	}

	public void onKeyUp(CCKeyEvent event) {
		if (dispatchEvents) {
			synchronized (keyHandlers) {
				for (int ind = 0; ind < keyHandlers.size(); ind++) {
					CCKeyHandler handler = keyHandlers.get(ind);
					if (handler.ccKeyUp(event) == kEventHandled)
						break;
				}
			}
		}
	}

	public void onKeyTyped(CCKeyEvent event) {
		if (dispatchEvents) {
			synchronized (keyHandlers) {
				for (int ind = 0; ind < keyHandlers.size(); ind++) {
					CCKeyHandler handler = keyHandlers.get(ind);
					if (handler.ccKeyTyped(event) == kEventHandled)
						break;
				}
			}
		}
	}
}
