package org.cclgdx.nodes;

import org.cclgdx.protocols.CCNodeTransformProtocol;
import org.cclgdx.types.CGPoint;
import org.cclgdx.types.CGRect;
import org.cclgdx.types.CGSize;

public class CCNodeTransform extends CCTransformValue implements CCNodeTransformProtocol {

	protected CCNodeTransform parent_;

	/*
	 * originX, originY used only for scaling and rotating
	 */
	public float originX_, originY_;

	@Override
	public CCNodeTransform getParent() {
		return parent_;
	}

	@Override
	public void setParent(CCNode parent) {
		parent_ = parent;
	}

	protected CCTransformValue screenTransformValue_ = new CCTransformValue();

	protected CCNodeTransform() {
		super();
		dirty_ = true;
	}

	public CGRect getScreenBoundingBox() {
		updateTransform();
		return screenBoundingBox_;
	}

	public boolean containsScreenPoint(float x, float y) {
		return containsScreenPoint(CGPoint.make(x, y));
	}

	public boolean containsScreenPoint(CGPoint pt) {
		return CGRect.containsPoint(getScreenBoundingBox(), pt);
	}

	public CCTransformValue getScreenTransformValue() {
		updateTransform();
		return screenTransformValue_;
	}

	protected void updateScreenTransformValue() {
		copyTransformValue(screenTransformValue_);

		/*
		 * Note: originX, orginY, x, y are not scaled nor rotated
		 */
		originX_ = contentSize_.width * anchorX;
		originY_ = contentSize_.height * anchorY;

		if (parent_ != null) {
			CCTransformValue parentSTValue = parent_.getScreenTransformValue();

			float parentOriginX = parent_.contentSize_.width * parent_.anchorX * parentSTValue.scaleX;
			float parentOriginY = parent_.contentSize_.height * parent_.anchorY * parentSTValue.scaleY;

			if (parentSTValue.rotation == 0) {
				screenTransformValue_.x = screenTransformValue_.x * parentSTValue.scaleX + parentSTValue.x - parentOriginX;
				screenTransformValue_.y = screenTransformValue_.y * parentSTValue.scaleY + parentSTValue.y - parentOriginY;
			} else {
				double radian = Math.toRadians(parentSTValue.rotation);
				double sin = Math.sin(radian);
				double cos = Math.cos(radian);

				float dx = x * parentSTValue.scaleX - parentOriginX;
				float dy = y * parentSTValue.scaleY - parentOriginY;
				float dx2 = (float) (cos * dx - sin * dy);
				float dy2 = (float) (sin * dx + cos * dy);
				screenTransformValue_.x = parentSTValue.x + dx2;
				screenTransformValue_.y = parentSTValue.y + dy2;
			}

			screenTransformValue_.scaleX *= parentSTValue.scaleX;
			screenTransformValue_.scaleY *= parentSTValue.scaleY;
			screenTransformValue_.rotation += parentSTValue.rotation;
		}
	}

	/*
	 * return bounding rectangle which origin as the anchor point
	 */
	private CGRect calBoundingRect(float x, float y, float scaleX, float scaleY, float rotation) {
		// Bottom left corner
		float x0 = -originX_ * scaleX;
		float y0 = -originY_ * scaleY;

		// bottom right corner
		float x1 = (getContentSize().width - originX_) * scaleX;
		float y1 = y0;

		// up right corner
		float x2 = x1;
		float y2 = (getContentSize().height - originY_) * scaleY;

		// bottom left corner
		float x3 = x0;
		float y3 = y2;

		float xx0, yy0, xx1, yy1, xx2, yy2, xx3, yy3;

		if (rotation != 0) {
			double radian = Math.toRadians(rotation);
			double sin = Math.sin(radian);
			double cos = Math.cos(radian);

			xx0 = (float) (cos * x0 - sin * y0);
			yy0 = (float) (sin * x0 + cos * y0);

			xx1 = (float) (cos * x1 - sin * y1);
			yy1 = (float) (sin * x1 + cos * y1);

			xx2 = (float) (cos * x2 - sin * y2);
			yy2 = (float) (sin * x2 + cos * y2);

			xx3 = (float) (cos * x3 - sin * y3);
			yy3 = (float) (sin * x3 + cos * y3);

		} else {
			xx0 = x0;
			yy0 = y0;
			xx1 = x1;
			yy1 = y1;
			xx2 = x2;
			yy2 = y2;
			xx3 = x3;
			yy3 = y3;
		}

		x0 = Math.min(Math.min(xx0, xx1), Math.min(xx2, xx3));
		y0 = Math.min(Math.min(yy0, yy1), Math.min(yy2, yy3));

		x1 = Math.max(Math.max(xx0, xx1), Math.max(xx2, xx3));
		y1 = Math.max(Math.max(yy0, yy1), Math.max(yy2, yy3));

		float w = x1 - x0;
		float h = y1 - y0;

		return CGRect.make(x + x0, y + y0, w, h);
	}

	// untransformed size of the node
	protected CGSize contentSize_;

	/**
	 * The untransformed size of the node. The contentSize remains the same no
	 * matter the node is scaled or rotated. All nodes has a size. Layer and
	 * Scene has the same size of the screen.
	 */
	@Override
	public void setContentSize(CGSize size) {
		setContentSize(size.width, size.height);
	}

	@Override
	public void setContentSize(float w, float h) {
		if (!(contentSize_.width == w && contentSize_.height == h)) {
			contentSize_.set(w, h);
			setDirty(true);
		}
	}

	@Override
	public CGSize getContentSize() {
		return CGSize.make(contentSize_.width, contentSize_.height);
	}

	public CGSize getContentSizeRef() {
		return contentSize_;
	}

	protected CGRect boundingBox_ = CGRect.zero();
	protected CGRect screenBoundingBox_ = CGRect.zero();

	protected void updateBoundingBox() {
		boundingBox_ = calBoundingRect(x, y, scaleX, scaleY, rotation);
		screenBoundingBox_ = calBoundingRect(screenTransformValue_.x, screenTransformValue_.y, screenTransformValue_.scaleX, screenTransformValue_.scaleY, screenTransformValue_.rotation);

	}

	protected void updateTransform() {
		if (dirty_) {
			updateScreenTransformValue();
			updateBoundingBox();
			dirty_ = false;
		}
	}

	/**
	 * returns a "local" axis aligned bounding box of the node. The returned box
	 * is relative only to its parent. if its parent is transformed, the box is
	 * still unchanged
	 */
	public CGRect getBoundingBox() {
		updateTransform();
		return boundingBox_;
	}

	public String toTransformString() {
		return "<" + super.toString() + ",content sz:<" + contentSize_ + ",bounding:" + boundingBox_ + ">";
	}

	@Override
	public String toString() {
		return toTransformString();
	}
}
