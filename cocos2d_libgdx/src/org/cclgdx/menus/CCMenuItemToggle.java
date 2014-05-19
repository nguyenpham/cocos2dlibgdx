package org.cclgdx.menus;

import java.util.ArrayList;
import java.util.Arrays;

import org.cclgdx.nodes.CCNode;
import org.cclgdx.types.CGPoint;
import org.cclgdx.types.CGSize;

/**
 * A CCMenuItemToggle A simple container class that "toggles" it's inner items
 * The inner items can be any MenuItem
 */

public class CCMenuItemToggle extends CCMenuItem {
	/** returns the selected item */

	private int selectedIndex_;
	/**
	 * NSMutableArray that contains the sub items. You can add/remove items in
	 * runtime, and you can replace the array with a new one.
	 */
	private ArrayList<CCMenuItem> subItems_;

	public ArrayList<CCMenuItem> getSubItemsRef() {
		if (subItems_ == null) {
			subItems_ = new ArrayList<CCMenuItem>();
		}
		return subItems_;
	}

	// /** conforms with CCRGBAProtocol protocol */
	// private byte opacity_;
	// /** conforms with CCRGBAProtocol protocol */
	// ccColor3B color_;

	/** creates a menu item from a list of items with a target/selector */
	public static CCMenuItemToggle item(CCNode target, String selector, CCMenuItem... items) {
		return new CCMenuItemToggle(target, selector, items);
	}

	/** initializes a menu item from a list of items with a target selector */
	protected CCMenuItemToggle(CCNode target, String selector, CCMenuItem... items) {
		super(target, selector);

		subItems_ = new ArrayList<CCMenuItem>(items.length);

		subItems_.addAll(Arrays.asList(items));

		selectedIndex_ = Integer.MAX_VALUE;
		setSelectedIndex(0);
	}

	public void setSelectedIndex(int index) {
		if (index != selectedIndex_) {
			selectedIndex_ = index;
			removeChildByTag(kCurrentItem, false);

			CCMenuItem item = subItems_.get(selectedIndex_);
			addChild(item, 0, kCurrentItem);

			CGSize s = item.getContentSize();
			setContentSize(s);
			item.setPosition(CGPoint.make(s.width / 2, s.height / 2));
		}
	}

	public int selectedIndex() {
		return selectedIndex_;
	}

	@Override
	public void selected() {
		super.selected();
		subItems_.get(selectedIndex_).selected();
	}

	@Override
	public void unselected() {
		super.unselected();
		subItems_.get(selectedIndex_).unselected();
	}

	@Override
	public void activate() {
		// update index

		if (isEnabled_) {
			int newIndex = (selectedIndex_ + 1) % subItems_.size();
			setSelectedIndex(newIndex);

		}
		super.activate();
	}

	@Override
	public void setIsEnabled(boolean enabled) {
		super.setIsEnabled(enabled);
		for (CCMenuItem item : subItems_)
			item.setIsEnabled(enabled);
	}

	/** return the selected item */
	public CCMenuItem selectedItem() {
		return subItems_.get(selectedIndex_);
	}

	// public void setOpacity(byte opacity) {
	// opacity_ = opacity;
	// for (CCMenuItem item : subItems_)
	// ((CCRGBAProtocol) item).setOpacity(opacity);
	// }
	//
	// public void setColor(ccColor3B color) {
	// color_ = color;
	// for (CCMenuItem item : subItems_)
	// ((CCRGBAProtocol) item).setColor(color);
	// }
}
