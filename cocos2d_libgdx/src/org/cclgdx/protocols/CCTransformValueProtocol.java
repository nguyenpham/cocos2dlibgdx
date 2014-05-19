package org.cclgdx.protocols;

import org.cclgdx.nodes.CCTransformValue;
import org.cclgdx.types.CGPoint;

public interface CCTransformValueProtocol {
	public CGPoint getPosition();

	float getX();

	float getY();

	void setPosition(CGPoint pt);

	void setPosition(float x, float y);

	// void updateTransform();

	float getScale();

	float getScaleX();

	void setScaleX(float scaleX);

	float getScaleY();

	void setScaleY(float scaleY);

	void setScale(float scale);

	float getRotation();

	void setRotation(float degrees);

	int getOpacity();

	void setOpacity(int opacity);

	CGPoint getAnchorPoint();

	float getAnchorPointX();

	float getAnchorPointY();

	void setAnchorPoint(float anchorX, float anchorY);

	void setAnchorPoint(CGPoint anchorPoint);

	boolean isFlipY();

	void setFlipY(boolean flipY);

	boolean isFlipX();

	void setFlipX(boolean flipX);

	void reset();

	void copyTransformValue(CCTransformValue value);

}
