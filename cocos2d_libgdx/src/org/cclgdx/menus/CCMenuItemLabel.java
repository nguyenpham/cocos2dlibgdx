package org.cclgdx.menus;

import org.cclgdx.actions.base.CCAction;
import org.cclgdx.actions.interval.CCScaleTo;
import org.cclgdx.nodes.CCNode;
import org.cclgdx.text.CCLabel;
import org.cclgdx.types.CGRect;
import org.cclgdx.types.CGSize;
import org.cclgdx.types.ccColor3B;
import org.cclgdx.types.ccColor4B;

/**
 * An abstract class for "label" CCMenuItemLabel items
 */

public class CCMenuItemLabel extends CCMenuItem {
	private CCLabel label_;
	private ccColor3B colorBackup;

	/** the color that will be used to disable the item */
	private final ccColor3B disabledColor_;
	private float originalScale_;

	/** creates a CCMenuItemLabel with a Label, target and selector */
	public static CCMenuItemLabel item(CCLabel label, CCNode target, String selector) {
		return new CCMenuItemLabel(label, target, selector);
	}

	public static CCMenuItemLabel item(String text, CCNode target, String selector) {
		return item(text, null, target, selector);
	}

	public static CCMenuItemLabel item(String text, String fontPath, CCNode target, String selector) {
		CCLabel label = CCLabel.makeLabel(text, fontPath);
		return new CCMenuItemLabel(label, target, selector);
	}

	/** initializes a CCMenuItemLabel with a Label, target and selector */
	protected CCMenuItemLabel(CCLabel label, CCNode target, String selector) {
		super(target, selector);
		originalScale_ = 1.0f;
		setLabel(label);
		disabledColor_ = new ccColor3B(126, 126, 126);
	}

	@Override
	public void setOpacity(int opacity) {
		label_.setOpacity(opacity);
	}

	@Override
	public int getOpacity() {
		return label_.getOpacity();
	}

	@Override
	public void setColor(ccColor3B color) {
		label_.setColor(color);
	}

	@Override
	public ccColor3B getColor() {
		return label_.getColor();
	}

	public void setBackgoundColor(ccColor4B backgroundColor) {
		label_.setBackgoundColor(backgroundColor);
	}

	public ccColor3B getDisabledColor() {
		return new ccColor3B(disabledColor_.r, disabledColor_.g, disabledColor_.b);
	}

	public void setDisabledColor(ccColor3B color) {
		disabledColor_.r = color.r;
		disabledColor_.g = color.g;
		disabledColor_.b = color.b;
	}

	public CCLabel getLabel() {
		return label_;
	}

	@Override
	public CGSize getContentSize() {
		return label_.getContentSize();
	}

	@Override
	public CGRect getBoundingBox() {
		return label_.getBoundingBox();
	}

	public void setLabel(CCLabel label) {
		label_ = label;
		CGSize sz = label_.getContentSize();
		label.setAnchorPoint(0.5f, 0.5f);
		label.setPosition(sz.width / 2, sz.height / 2);
		label.setParent(this);
		setContentSize(sz);
		addChild(label);
	}

	/** sets a new string to the inner label */
	public void setString(String string) {
		label_.setString(string);
		CGSize sz = label_.getContentSize();
		label_.setPosition(sz.width / 2, sz.height / 2);
		setContentSize(sz);
	}

	@Override
	public void activate() {
		if (isEnabled_) {
			stopAllActions();

			setScale(originalScale_);

			super.activate();
		}
	}

	@Override
	public void selected() {
		if (isEnabled_) {
			super.selected();

			stopAction(kZoomActionTag);
			originalScale_ = getScale();
			CCAction zoomAction = CCScaleTo.action(0.1f, 1.2f * originalScale_);
			zoomAction.setTag(kZoomActionTag);
			runAction(zoomAction);
		}
	}

	@Override
	public void unselected() {
		if (isEnabled_) {
			super.unselected();

			stopAction(kZoomActionTag);
			CCAction zoomAction = CCScaleTo.action(0.1f, originalScale_);
			zoomAction.setTag(kZoomActionTag);
			runAction(zoomAction);
		}
	}

	/**
	 * Enable or disabled the CCMenuItemFont
	 */
	@Override
	public void setIsEnabled(boolean enabled) {
		if (isEnabled_ != enabled) {
			if (!enabled) {
				colorBackup = label_.getColor();
				label_.setColor(disabledColor_);
			} else {
				label_.setColor(colorBackup);
			}
		}

		super.setIsEnabled(enabled);
	}

}
