package org.cclgdx.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.cclgdx.actions.base.CCAction;
import org.cclgdx.actions.base.CCActionManager;
import org.cclgdx.actions.base.CCScheduler;
import org.cclgdx.actions.base.CCUpdateCallback;
import org.cclgdx.types.CGSize;
import org.cclgdx.types.ccColor3B;
import org.cclgdx.types.ccColor4B;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * CCNode is the main element. Anything thats gets drawn or contains things that
 * get drawn is a CCNode. The most popular CCNodes are: CCScene, CCLayer,
 * CCSprite, CCMenu.
 * 
 * The main features of a CCNode are: - They can contain other CCNode nodes
 * (addChild, getChildByTag, removeChild, etc) - They can schedule periodic
 * callback (schedule, unschedule, etc) - They can execute actions (runAction,
 * stopAction, etc)
 * 
 * Some CCNode nodes provide extra functionality for them or their children.
 * 
 * Subclassing a CCNode usually means (one/all) of: - overriding init to
 * initialize resources and schedule callbacks - create callbacks to handle the
 * advancement of time - overriding draw to render the node
 * 
 * Features of CCNode: - position - scale (x, y) - rotation (in degrees,
 * clockwise) - CCCamera (an interface to gluLookAt ) - CCGridBase (to do mesh
 * transformations) - anchor point - size - visible - z-order - openGL z
 * position
 * 
 * Limitations: - A CCNode is a "void" object. It doesn't have a texture
 */
public class CCNode extends CCNodeTransform {
	protected static final String LOG_TAG = CCNode.class.getSimpleName();

	protected static SpriteBatch renderSpriteBatch = null;
	public static final int kCCNodeTagInvalid = -1;

	// is visible
	protected boolean visible_;

	/** Whether of not the node is visible. Default is YES */
	public boolean getVisible() {
		return visible_;
	}

	public void setVisible(boolean visible) {
		this.visible_ = visible;
	}

	// a tag. any number you want to assign to the node
	private int tag_;

	/** A tag used to identify the node easily */
	public int getTag() {
		return tag_;
	}

	public void setTag(int tag) {
		tag_ = tag;
	}

	protected ccColor3B color_ = ccColor3B.ccWHITE;

	public void setColor(ccColor3B color3) {
		color_ = color3;
	}

	public ccColor3B getColor() {
		return color_;
	}

	public void setColor(ccColor4B color) {
		opacity = color.a;
		setColor(ccColor3B.ccc3(color.r, color.g, color.b));
	}

	public ccColor4B getColor4B() {
		return ccColor4B.ccc4(color_.r, color_.g, color_.b, opacity);
	}

	/** Override synthesized setOpacity to recurse items */
	@Override
	public void setOpacity(int newOpacity) {
		opacity = newOpacity;
		if (children_ != null) {
			for (CCNode item : children_) {
				item.setOpacity(opacity);
			}
		}
	}

	// z-order value
	private int zOrder_;

	/**
	 * The z order of the node relative to it's "brothers": children of the same
	 * parent
	 */
	public int getZOrder() {
		return zOrder_;
	}

	// used internally to alter the zOrder variable. DON'T call this method
	// manually
	private void _setZOrder(int z) {
		zOrder_ = z;
	}

	// array of children
	protected List<CCNode> children_;

	public List<CCNode> getChildren() {
		return children_;
	}

	// user data field
	private Object userData;

	/** A custom user data pointer */
	public Object getUserData() {
		return userData;
	}

	public void setUserData(Object data) {
		userData = data;
	}

	// Is running
	private boolean isRunning_;

	/** whether or not the node is running */
	public boolean isRunning() {
		return isRunning_;
	}

	// initializators
	/**
	 * allocates and initializes a node. The node will be created as
	 * "autorelease".
	 */
	public static CCNode node() {
		return new CCNode();
	}

	/** initializes the node */
	protected CCNode() {
		isRunning_ = false;
		contentSize_ = CGSize.zero();
		zOrder_ = 0;
		visible_ = true;
		tag_ = kCCNodeTagInvalid;
		children_ = null;

		// userData is always inited as nil
		userData = null;
	}

	/**
	 * Adds a child to the container with z order and tag It returns self, so
	 * you can chain several addChilds.
	 */
	/*
	 * "add" logic MUST only be on this method If a class want's to extend the
	 * 'addChild' behaviour it only needs to override this method
	 */
	public CCNode addChild(CCNode child, int z, int tag) {
		assert child != null : "Argument must be non-nil";
		assert child.parent_ == null : "child already added. It can't be added again";

		if (children_ == null) {
			childrenAlloc();
		}

		insertChild(child, z);
		child.tag_ = tag;
		child.setParent(this);
		if (isRunning_) {
			child.onEnter();
		}
		return this;
	}

	/**
	 * Adds a child to the container with a z-order It returns self, so you can
	 * chain several addChilds.
	 */
	public CCNode addChild(CCNode child, int z) {
		assert child != null : "Argument must be non-nil";
		return addChild(child, z, child.tag_);
	}

	/**
	 * Adds a child to the container with z-order as 0. It returns self, so you
	 * can chain several addChilds.
	 */
	public CCNode addChild(CCNode child) {
		assert child != null : "Argument must be non-nil";
		return addChild(child, child.zOrder_, child.tag_);
	}

	/**
	 * Remove itself from its parent node. If cleanup is YES, then also remove
	 * all actions and callbacks. If the node orphan, then nothing happens.
	 */
	public void removeFromParentAndCleanup(boolean cleanup) {
		if (this.parent_ != null) {
			((CCNode) this.parent_).removeChild(this, cleanup);
		}
	}

	/**
	 * Remove myself from the parent, for action CCCallFunc
	 */
	public void removeSelf() {
		this.removeFromParentAndCleanup(true);
	}

	/**
	 * Removes a child from the container. It will also cleanup all running
	 * actions depending on the cleanup parameter.
	 */
	/*
	 * "remove" logic MUST only be on this method If a class want's to extend
	 * the 'removeChild' behavior it only needs to override this method
	 */
	public void removeChild(CCNode child, boolean cleanup) {
		// explicit nil handling
		if (child == null) {
			return;
		}

		if (children_.contains(child)) {
			detachChild(child, cleanup);
		}
	}

	/**
	 * Removes a child from the container by tag value. It will also cleanup all
	 * running actions depending on the cleanup parameter
	 */
	public void removeChildByTag(int tag, boolean cleanup) {
		assert tag != kCCNodeTagInvalid : "Invalid tag";

		CCNode child = getChildByTag(tag);
		if (child == null) {
			Gdx.app.log(LOG_TAG, "removeChild: child not found");
		} else {
			removeChild(child, cleanup);
		}
	}

	/**
	 * Removes all children from the container and do a cleanup all running
	 * actions depending on the cleanup parameter.
	 */
	public void removeAllChildren(boolean cleanup) {
		// not using detachChild improves speed here
		if (children_ == null) {
			return;
		}

		for (int i = 0; i < children_.size(); ++i) {
			CCNode child = children_.get(i);
			if (isRunning_) {
				child.onExit();
			}

			if (cleanup)
				child.cleanup();

			child.setParent(null);
		}
		children_.clear();

	}

	/**
	 * Gets a child from the container given its tag
	 * 
	 * @return returns a CCNode object
	 */
	public CCNode getChildByTag(int tag) {
		assert tag != kCCNodeTagInvalid : "Invalid tag_";

		if (children_ != null)
			for (int i = 0; i < children_.size(); ++i) {
				CCNode node = children_.get(i);
				if (node.tag_ == tag) {
					return node;
				}
			}

		return null;
	}

	private void detachChild(CCNode child, boolean doCleanup) {
		// IMPORTANT:
		// -1st do onExit
		// -2nd cleanup
		if (isRunning_) {
			child.onExit();
		}

		// If you don't do cleanup, the child's actions will not get removed and
		// the its scheduledSelectors_ dict will not get released!
		if (doCleanup) {
			child.cleanup();
		}

		// set parent nil at the end (issue #476)
		child.setParent(null);

		children_.remove(child);
	}

	/**
	 * Reorders a child according to a new z value. The child MUST be already
	 * added.
	 */
	public void reorderChild(CCNode child, int zOrder) {
		assert child != null : "Child must be non-null";
		children_.remove(child);
		this.insertChild(child, zOrder);
	}

	/**
	 * Override this method to draw your own node.
	 */
	public void draw(float dt) {
		// override me
		// Only use this function to draw your staff.
		// DON'T draw your stuff outside this method
	}

	/**
	 * recursive method that visit its children and draw them
	 */
	public void visit(float dt) {
		// quick return if not visible
		if (!visible_) {
			return;
		}

		updateTransform();

		if (children_ != null) {
			for (int i = 0; i < children_.size(); ++i) {
				CCNode child = children_.get(i);
				if (child.zOrder_ < 0) {
					child.visit(dt);
				} else {
					break;
				}
			}
		}

		draw(dt);

		if (children_ != null) {
			for (int i = 0; i < children_.size(); ++i) {
				CCNode child = children_.get(i);
				if (child.zOrder_ >= 0) {
					child.visit(dt);
				}
			}
		}
	}

	@Override
	protected void setDirty(boolean dirty) {
		super.setDirty(dirty);
		if (dirty && children_ != null) {
			for (CCNode child : children_) {
				child.setDirty(dirty);
			}
		}
	}

	/**
	 * Executes an action, and returns the action that is executed. The node
	 * becomes the action's target.
	 * 
	 * @warning Starting from v0.8 actions don't retain their target anymore.
	 * @return An Action pointer
	 */
	public CCAction runAction(CCAction action) {
		assert action != null : "Argument must be non-null";

		CCActionManager.sharedManager().addAction(action, this, !isRunning_);
		return action;
	}

	/** Removes all actions from the running action list */
	public void stopAllActions() {
		CCActionManager.sharedManager().removeAllActions(this);
	}

	/** Removes an action from the running action list */
	public void stopAction(CCAction action) {
		CCActionManager.sharedManager().removeAction(action);
	}

	/**
	 * Removes an action from the running action list given its tag
	 */
	public void stopAction(int tag) {
		assert tag != CCAction.kCCActionTagInvalid : "Invalid tag_";
		CCActionManager.sharedManager().removeAction(tag, this);
	}

	/**
	 * Gets an action from the running action list given its tag
	 * 
	 * @return the Action the with the given tag
	 */
	public CCAction getAction(int tag) {
		assert tag != CCAction.kCCActionTagInvalid : "Invalid tag_";

		return CCActionManager.sharedManager().getAction(tag, this);
	}

	/**
	 * Returns the numbers of actions that are running plus the ones that are
	 * schedule to run (actions in actionsToAdd and actions arrays). Composable
	 * actions are counted as 1 action. Example: If you are running 1 Sequence
	 * of 7 actions, it will return 1. If you are running 7 Sequences of 2
	 * actions, it will return 7.
	 */
	public int numberOfRunningActions() {
		return CCActionManager.sharedManager().numberOfRunningActions(this);
	}

	/**
	 * schedules the "update" method. It will use the order number 0. This
	 * method will be called every frame. Scheduled methods with a lower order
	 * value will be called before the ones that have a higher order value. Only
	 * one "update" method could be scheduled per node.
	 */
	public void scheduleUpdate() {
		this.scheduleUpdate(0);
	}

	/**
	 * schedules the "update" selector with a custom priority. This selector
	 * will be called every frame. Scheduled selectors with a lower priority
	 * will be called before the ones that have a higher value. Only one
	 * "update" selector could be scheduled per node (You can't have 2 'update'
	 * selectors).
	 */
	public void scheduleUpdate(int priority) {
		CCScheduler.sharedScheduler().scheduleUpdate(this, priority, !isRunning_);
	}

	/**
	 * unschedules the "update" method.
	 */
	public void unscheduleUpdate() {
		CCScheduler.sharedScheduler().unscheduleUpdate(this);
	}

	/**
	 * schedules a selector. The scheduled selector will be ticked every frame
	 */
	public void schedule(String selector) {
		schedule(selector, 0);
	}

	/**
	 * schedules a custom selector with an interval time in seconds. If time is
	 * 0 it will be ticked every frame. If time is 0, it is recommended to use
	 * 'scheduleUpdate' instead.
	 */
	public void schedule(String selector, float interval) {
		assert selector != null : "Argument selector must be non-null";
		assert interval >= 0 : "Argument interval must be positive";

		CCScheduler.sharedScheduler().schedule(selector, this, interval, !isRunning_);
	}

	/*
	 * schedules a selector. The scheduled callback will be ticked every frame.
	 * 
	 * This is java way version, uses interface based callbacks. UpdateCallback
	 * in this case. It would be preffered solution. It is more polite to Java,
	 * GC, and obfuscation.
	 */
	public void schedule(CCUpdateCallback callback) {
		schedule(callback, 0);
	}

	/*
	 * schedules a custom callback with an interval time in seconds. If time is
	 * 0 it will be ticked every frame. If time is 0, it is recommended to use
	 * 'scheduleUpdate' instead.
	 * 
	 * This is java way version, uses interface based callbacks. UpdateCallback
	 * in this case. It would be preffered solution. It is more polite to Java,
	 * GC, and obfuscation.
	 */
	public void schedule(CCUpdateCallback callback, float interval) {
		assert callback != null : "Argument callback must be non-null";
		assert interval >= 0 : "Argument interval must be positive";

		CCScheduler.sharedScheduler().schedule(callback, this, interval, !isRunning_);
	}

	/* unschedules a custom selector. */
	public void unschedule(String selector) {
		// explicit null handling
		if (selector == null) {
			return;
		}

		CCScheduler.sharedScheduler().unschedule(selector, this);
	}

	/*
	 * unschedules a custom callback.
	 * 
	 * This is java way version, uses interface based callbacks. UpdateCallback
	 * in this case. It would be preffered solution. It is more polite to Java,
	 * GC, and obfuscation.
	 */
	public void unschedule(CCUpdateCallback callback) {
		// explicit null handling
		if (callback == null) {
			return;
		}

		CCScheduler.sharedScheduler().unschedule(callback, this);
	}

	/**
	 * unschedule all scheduled selectors: custom selectors, and the 'update'
	 * selector. Actions are not affected by this method.
	 */
	public void unscheduleAllSelectors() {
		CCScheduler.sharedScheduler().unscheduleAllSelectors(this);
	}

	/**
	 * resumes all scheduled selectors and actions. Called internally by onEnter
	 */
	public void resumeSchedulerAndActions() {
		CCScheduler.sharedScheduler().resume(this);
		CCActionManager.sharedManager().resume(this);
	}

	/**
	 * pauses all scheduled selectors and actions. Called internally by onExit
	 */
	public void pauseSchedulerAndActions() {
		CCScheduler.sharedScheduler().pause(this);
		CCActionManager.sharedManager().pause(this);
	}

	// lazy allocs
	private void childrenAlloc() {
		children_ = Collections.synchronizedList(new ArrayList<CCNode>(4));
	}

	private static Comparator<CCNode> zOrderComparator = new Comparator<CCNode>() {

		@Override
		public int compare(CCNode o1, CCNode o2) {
			return o1.zOrder_ - o2.zOrder_;
		}
	};

	// helper that reorder a child
	private void insertChild(CCNode node, int z) {
		node._setZOrder(z);
		int ind = Collections.binarySearch(children_, node, zOrderComparator);
		// let's find new index
		if (ind >= 0) { // go to last if index is found
			int size = children_.size();
			CCNode prev;

			do {
				prev = children_.get(ind);
				ind++;
			} while (ind < size && children_.get(ind).zOrder_ == prev.zOrder_);
		} else { // index not found
			ind = -(ind + 1);
		}
		children_.add(ind, node);
	}

	/**
	 * Stops all running actions and schedulers
	 */
	public void cleanup() {
		// actions
		stopAllActions();
		// timers
		unscheduleAllSelectors();

		if (children_ != null) {
			for (CCNode node : children_) {
				node.cleanup();
			}
		}
	}

	@Override
	public String toString() {
		// return "<instance of " + this.getClass() + "| Tag = " + tag_ + ">";
		return "<" + this.getClass().getSimpleName() + ",tag=" + tag_ + "," + super.toString();
	}

	/**
	 * callback that is called every time the CCNode enters the 'stage'. If the
	 * CCNode enters the 'stage' with a transition, this callback is called when
	 * the transition starts. During onEnter you can't a "sister/brother" node.
	 */
	public void onEnter() {
		if (children_ != null) {
			for (CCNode child : children_) {
				child.onEnter();
			}
		}
		resumeSchedulerAndActions();
		// activateTimers();
		isRunning_ = true;
	}

	/**
	 * callback that is called when the CCNode enters in the 'stage'. If the
	 * CCNode enters the 'stage' with a transition, this callback is called when
	 * the transition finishes.
	 */
	public void onEnterTransitionDidFinish() {

		if (children_ != null)
			for (CCNode child : children_) {
				child.onEnterTransitionDidFinish();
			}
	}

	/**
	 * callback that is called every time the CCNode leaves the 'stage'. If the
	 * CCNode leaves the 'stage' with a transition, this callback is called when
	 * the transition finishes. During onExit you can't a "sister/brother" node.
	 */
	public void onExit() {
		// deactivateTimers();
		pauseSchedulerAndActions();
		isRunning_ = false;

		if (children_ != null) {
			for (CCNode child : children_) {
				child.onExit();
			}
		}
	}

	public void onResize(int width, int height) {
		if (children_ != null) {
			for (CCNode child : children_) {
				child.onResize(width, height);
			}
		}
	}

}
