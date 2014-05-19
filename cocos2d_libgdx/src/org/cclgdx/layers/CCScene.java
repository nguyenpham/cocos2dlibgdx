package org.cclgdx.layers;

import org.cclgdx.director.CCDirector;
import org.cclgdx.nodes.CCNode;
import org.cclgdx.types.CGSize;

/**
 * CCScene is a subclass of TNode that is used only as an abstract concept.
 * 
 * CCScene an TNode are almost identical with the difference that CCScene has
 * it's anchor point (by default) at the center of the screen.
 * 
 * For the moment CCScene has no other logic than that, but in future releases
 * it might have additional logic.
 * 
 * It is a good practice to use and CCScene as the parent of all your nodes.
 */
public class CCScene extends CCNode {

	public static CCScene node() {
		return new CCScene();
	}

	protected CCScene() {
		super();

		CGSize sz = CCDirector.sharedDirector().winSize();
		setContentSize(sz);
		setAnchorPoint(0, 0);
		setPosition(0, 0);
	}
}
