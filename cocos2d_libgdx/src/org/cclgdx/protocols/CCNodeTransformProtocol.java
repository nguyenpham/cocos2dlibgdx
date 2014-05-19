package org.cclgdx.protocols;

import org.cclgdx.nodes.CCNode;
import org.cclgdx.nodes.CCNodeTransform;
import org.cclgdx.types.CGSize;

public interface CCNodeTransformProtocol extends CCTransformValueProtocol {

	CCNodeTransform getParent();

	void setParent(CCNode parent);

	void setContentSize(CGSize size);

	void setContentSize(float w, float h);

	CGSize getContentSize();

}
