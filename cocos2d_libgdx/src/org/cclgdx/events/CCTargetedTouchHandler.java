package org.cclgdx.events;

import java.util.ArrayList;

import org.cclgdx.protocols.CCTouchDelegateProtocol;

public class CCTargetedTouchHandler extends CCTouchHandler {

	boolean swallowsTouches;

	private final ArrayList<Integer> claimedSet;

	public CCTargetedTouchHandler(CCTouchDelegateProtocol delegate, int priority, boolean swallow) {
		super(delegate, priority);
		swallowsTouches = swallow;
		claimedSet = new ArrayList<Integer>();
	}

	void addClaimed(int pid) {
		if (!claimedSet.contains(pid)) {
			claimedSet.add(pid);
		}
	}

	void removeClaimed(int pid) {
		int ind = claimedSet.indexOf(pid);
		claimedSet.remove(ind);
	}

	boolean hasClaimed(int pid) {
		return claimedSet.contains(pid);
	}
}
