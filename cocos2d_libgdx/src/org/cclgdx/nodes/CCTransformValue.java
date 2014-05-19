package org.cclgdx.nodes;

import org.cclgdx.config.ccMacros;
import org.cclgdx.protocols.CCTransformValueProtocol;
import org.cclgdx.types.CGPoint;

public class CCTransformValue implements CCTransformValueProtocol {
	private static final String LOG_TAG = "CCTransformValue";

	// position of the node
	protected float x, y;

	// scaling factors
	protected float scaleX, scaleY;

	// rotation angle in degree
	protected float rotation;

	// opacity value from 0 to 255
	protected int opacity;

	protected boolean dirty_;

	protected boolean isDirty() {
		return dirty_;
	}

	protected void setDirty(boolean dirty) {
		this.dirty_ = dirty;
	}

	/**
	 * anchorPoint is the point around which all transformations and positioning
	 * manipulations take place. It's like a pin in the node where it is
	 * "attached" to its parent. The anchorPoint is normalized, like a
	 * percentage. (0,0) means the bottom-left corner and (1,1) means the
	 * top-right corner. But you can use values higher than (1,1) and lower than
	 * (0,0) too. The default anchorPoint is (0.5,0.5), so it starts in the
	 * center of the node.
	 */

	protected float anchorX, anchorY;

	/**
	 * whether or not the sprite is flipped horizontally or vertically. It only
	 * flips the texture of the sprite, and not the texture of the sprite's
	 * children. Also, flipping the texture doesn't alter the anchorPoint. If
	 * you want to flip the anchorPoint too, and/or to flip the children too
	 * use: sprite.scaleY *= -1;
	 */
	protected boolean flipX, flipY;

	public CCTransformValue() {
		reset();
	}

	/**
	 * Position (x,y) of the node. (0,0) is the left-bottom corner.
	 */
	@Override
	public CGPoint getPosition() {
		return CGPoint.make(x, y);
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public void setPosition(CGPoint pt) {
		setPosition(pt.x, pt.y);
	}

	@Override
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		setDirty(true);
	}

	@Override
	public float getScale() {
		if (scaleX == scaleY) {
			return scaleX;
		}

		ccMacros.CCLOGERROR(LOG_TAG, "CCTransformValue#scale. ScaleX != ScaleY. Don't know which one to return");
		return 0;
	}

	/**
	 * The scale factor of the node. 1.0 is the default scale factor. It only
	 * modifies the X scale factor.
	 */
	@Override
	public float getScaleX() {
		return scaleX;
	}

	@Override
	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
		setDirty(true);
	}

	@Override
	public float getScaleY() {
		return scaleY;
	}

	@Override
	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
		setDirty(true);
	}

	/**
	 * The scale factor of the node. 1.0 is the default scale factor. It
	 * modifies the X and Y scale at the same time.
	 */
	@Override
	public void setScale(float scale) {
		this.scaleX = scale;
		this.scaleY = scale;
		setDirty(true);
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	/**
	 * The rotation (angle) of the node in degrees. 0 is the default rotation
	 * angle. Positive values rotate node CW.
	 */

	@Override
	public void setRotation(float degrees) {
		this.rotation = degrees;
		setDirty(true);
	}

	@Override
	public int getOpacity() {
		return opacity;
	}

	@Override
	public void setOpacity(int opacity) {
		this.opacity = opacity;
	}

	@Override
	public CGPoint getAnchorPoint() {
		return CGPoint.make(anchorX, anchorY);
	}

	@Override
	public float getAnchorPointX() {
		return anchorX;
	}

	@Override
	public float getAnchorPointY() {
		return anchorY;
	}

	@Override
	public void setAnchorPoint(float anchorX, float anchorY) {
		this.anchorX = anchorX;
		this.anchorY = anchorY;
		setDirty(true);
	}

	@Override
	public void setAnchorPoint(CGPoint anchorPoint) {
		setAnchorPoint(anchorPoint.x, anchorPoint.y);
	}

	@Override
	public boolean isFlipY() {
		return flipY;
	}

	@Override
	public void setFlipY(boolean flipY) {
		this.flipY = flipY;
		setDirty(true);
	}

	@Override
	public boolean isFlipX() {
		return flipX;
	}

	@Override
	public void setFlipX(boolean flipX) {
		this.flipX = flipX;
		setDirty(true);
	}

	@Override
	public void reset() {
		x = y = rotation = 0.0f;
		scaleX = scaleY = 1.0f;
		anchorX = anchorY = 0.5f;
		opacity = 255;
		flipX = flipY = false;
		this.dirty_ = false;
	}

	@Override
	public void copyTransformValue(CCTransformValue value) {
		value.x = x;
		value.y = y;
		value.scaleX = scaleX;
		value.scaleY = scaleY;
		value.anchorX = anchorX;
		value.anchorY = anchorY;
		value.rotation = rotation;
		value.opacity = opacity;
		value.flipX = flipX;
		value.flipY = flipY;
	}

	@Override
	public String toString() {
		return transformValuetoString();
	}

	public String transformValuetoString() {
		String str = "<xy=" + x + "," + y + ",scale=" + scaleX + "," + scaleY + ",anchor=" + anchorX + "," + anchorY + ",rotation=" + rotation + ">";
		return str;
	}
}
