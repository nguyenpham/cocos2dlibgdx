package org.cclgdx.layers;

import org.cclgdx.director.CCDirector;
import org.cclgdx.events.CCKeyDispatcher;
import org.cclgdx.events.CCKeyEvent;
import org.cclgdx.events.CCMotionEvent;
import org.cclgdx.events.CCTouchDispatcher;
import org.cclgdx.nodes.CCNode;
import org.cclgdx.protocols.CCKeyDelegateProtocol;
import org.cclgdx.protocols.CCTouchDelegateProtocol;
import org.cclgdx.types.CGSize;

//
// CCLayer
//
/**
 * CCLayer is a subclass of CCNode that implements the TouchEventsDelegate and
 * CCKeyDelegateProtocol protocols.
 */

public class CCLayer extends CCNode implements CCTouchDelegateProtocol, CCKeyDelegateProtocol {

	/**
	 * whether or not it will receive Touch events. You can enable / disable
	 * touch events with this property. Only the touches of this node will be
	 * affected. This "method" is not propagated to it's children.
	 */
	protected boolean isTouchEnabled_;

	public static CCLayer node() {
		return new CCLayer();
	}

	protected CCLayer() {
		CGSize sz = CCDirector.sharedDirector().winSize();
		setContentSize(sz);
		setAnchorPoint(0, 0);
		setPosition(0, 0);

		isTouchEnabled_ = false;
		isKeyEnabled_ = false;
	}

	public boolean isTouchEnabled() {
		return isTouchEnabled_;
	}

	public void setIsTouchEnabled(boolean enabled) {
		if (isTouchEnabled_ != enabled) {
			isTouchEnabled_ = enabled;
			if (isRunning()) {
				if (enabled) {
					registerWithTouchDispatcher();
				} else {
					CCTouchDispatcher.sharedDispatcher().removeDelegate(this);
				}
			}
		}
	}

	protected boolean isKeyEnabled_;

	public boolean isKeyEnabled() {
		return isKeyEnabled_;
	}

	public void setIsKeyEnabled(boolean enabled) {
		if (isKeyEnabled_ != enabled) {
			isKeyEnabled_ = enabled;
			if (enabled) {
				CCKeyDispatcher.sharedDispatcher().addDelegate(this, 0);
			} else {
				CCKeyDispatcher.sharedDispatcher().removeDelegate(this);
			}
		}
	}

	/**
	 * If isTouchEnabled, this method is called onEnter. Override it to change
	 * the way CCLayer receives touch events. ( Default: [[TouchDispatcher
	 * sharedDispatcher] addStandardDelegate:self priority:0] )
	 */
	protected void registerWithTouchDispatcher() {
		CCTouchDispatcher.sharedDispatcher().addDelegate(this, 0);
	}

	/**
	 * If isKeyEnabled, this method is called onEnter. Override it to change the
	 * way CCLayer receives key events. ( Default: [[KeyDispatcher
	 * sharedDispatcher] addStandardDelegate:self priority:0] )
	 */
	protected void registerWithKeyDispatcher() {
		CCKeyDispatcher.sharedDispatcher().addDelegate(this, 0);
	}

	@Override
	public void onEnter() {
		super.onEnter();

		if (isTouchEnabled_) {
			registerWithTouchDispatcher();
		}

		if (isKeyEnabled_) {
			registerWithKeyDispatcher();
		}
	}

	@Override
	public void onExit() {

		if (isTouchEnabled_) {
			CCTouchDispatcher.sharedDispatcher().removeDelegate(this);
		}

		if (isKeyEnabled_) {
			CCKeyDispatcher.sharedDispatcher().removeDelegate(this);
		}
		super.onExit();
	}

	@Override
	public boolean ccTouchesBegan(CCMotionEvent event) {
		assert false : "Layer#ccTouchBegan override me";
		return CCTouchDispatcher.kEventHandled;
	}

	@Override
	public boolean ccTouchesMoved(CCMotionEvent event) {
		return CCTouchDispatcher.kEventIgnored;
	}

	@Override
	public boolean ccTouchesEnded(CCMotionEvent event) {
		return CCTouchDispatcher.kEventIgnored;
	}

	@Override
	public boolean ccTouchesCancelled(CCMotionEvent event) {
		return CCTouchDispatcher.kEventIgnored;
	}

	@Override
	public boolean ccKeyDown(CCKeyEvent event) {
		assert false : "Layer# ccKeyDown override me";
		return CCKeyDispatcher.kEventHandled;
	}

	@Override
	public boolean ccKeyUp(CCKeyEvent event) {
		assert false : "Layer# ccKeyUp override me";
		return CCKeyDispatcher.kEventHandled;
	}

	@Override
	public boolean ccKeyTyped(CCKeyEvent event) {
		assert false : "Layer# ccKeyTyped override me";
		return CCKeyDispatcher.kEventHandled;
	}
}
