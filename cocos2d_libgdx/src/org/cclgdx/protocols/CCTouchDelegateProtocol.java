package org.cclgdx.protocols;

import org.cclgdx.events.CCMotionEvent;

/**
 * CCTargetedTouchDelegate.
 * 
 * Using this type of delegate results in two benefits: 1. You don't need to
 * deal with NSSets, the dispatcher does the job of splitting them. You get
 * exactly one UITouch per call. 2. You can *claim* a UITouch by returning YES
 * in ccTouchBegan. Updates of claimed touches are sent only to the delegate(s)
 * that claimed them. So if you get a move/ ended/cancelled update you're sure
 * it's your touch. This frees you from doing a lot of checks when doing
 * multi-touch.
 * 
 * (The name TargetedTouchDelegate relates to updates "targeting" their specific
 * handler, without bothering the other handlers.)
 */
public interface CCTouchDelegateProtocol {
	/**
	 * Return YES to claim the touch.
	 */
	public boolean ccTouchesBegan(CCMotionEvent event);

	public boolean ccTouchesMoved(CCMotionEvent event);

	public boolean ccTouchesEnded(CCMotionEvent event);

	public boolean ccTouchesCancelled(CCMotionEvent event);

}
