package org.cclgdx.menus;

import org.cclgdx.nodes.CCNode;
import org.cclgdx.types.CGSize;

/**
 * MenuItemSprite accepts CocosNode<CocosNodeRGBA> objects as items. The images
 * has 3 different states: - unselected image - selected image - disabled image
 */

public class CCMenuItemSprite extends CCMenuItem {
	/** the image used when the item is not selected */
	protected CCNode normalImage_;

	public void setNormalImage(CCNode image) {
		assert (image != null) : "Cann't set normalImage_ to be null!";
		if (image != normalImage_) {
			addImage(image);
			this.removeChild(normalImage_, true);
			normalImage_ = image;
		}
	}

	public CCNode getNormalImage() {
		return normalImage_;
	}

	/** the image used when the item is selected */
	protected CCNode selectedImage_;

	public void setSelectedImage(CCNode image) {
		if (image != selectedImage_) {
			if (selectedImage_ != null) {
				removeChild(selectedImage_, true);
			}

			if (image != null) {
				addImage(image);
			}

			selectedImage_ = image;
		}
	}

	private void addImage(CCNode image) {
		image.setAnchorPoint(0.5f, 0.5f);
		image.setPosition(image.getBoundingBox().width / 2, image.getBoundingBox().height / 2);
		image.setVisible(true);
		this.addChild(image);

	}

	public CCNode getSelectedImage() {
		return selectedImage_;
	}

	/** the image used when the item is disabled */
	protected CCNode disabledImage_;

	public void setDisabledImage(CCNode image) {
		if (image != disabledImage_) {
			if (disabledImage_ != null) {
				removeChild(disabledImage_, true);
			}
			if (image != null) {
				addImage(image);
			}
			disabledImage_ = image;
		}
	}

	public CCNode getDisabledImage() {
		return disabledImage_;
	}

	/**
	 * Creates a menu with normal image only, useful for menu toggles
	 */
	public static CCMenuItemSprite item(CCNode normalSprite) {
		return new CCMenuItemSprite(normalSprite, null, null, null, null);
	}

	/** creates a menu item with a normal and selected image */
	public static CCMenuItemSprite item(CCNode normalSprite, CCNode selectedSprite) {
		return new CCMenuItemSprite(normalSprite, selectedSprite, null, null, null);
	}

	/**
	 * creates a menu item with a normal and selected image with target/selector
	 */
	public static CCMenuItemSprite item(CCNode normalSprite, CCNode selectedSprite, CCNode target, String selector) {
		return new CCMenuItemSprite(normalSprite, selectedSprite, null, target, selector);
	}

	/**
	 * creates a menu item with a normal,selected and disabled image with
	 * target/selector
	 */
	public static CCMenuItemSprite item(CCNode normalSprite, CCNode selectedSprite, CCNode disabledSprite, CCNode target, String selector) {
		return new CCMenuItemSprite(normalSprite, selectedSprite, disabledSprite, target, selector);
	}

	/**
	 * initializes a menu item with a normal, selected and disabled image with
	 * target/selector
	 */
	protected CCMenuItemSprite(CCNode normalSprite, CCNode selectedSprite, CCNode disabledSprite, CCNode target, String selector) {
		super(target, selector);
		setNormalImage(normalSprite);
		setSelectedImage(selectedSprite);
		setDisabledImage(disabledSprite);

		if (selectedSprite != null) {
			selectedSprite.setVisible(false);
		}
		if (disabledSprite != null) {
			disabledSprite.setVisible(false);
		}
		CGSize size = normalImage_.getContentSize();
		setContentSize(size);
	}

	@Override
	public void selected() {
		super.selected();

		if (selectedImage_ != null) {
			normalImage_.setVisible(false);
			selectedImage_.setVisible(true);
		} else { // there is not selected image
			normalImage_.setVisible(true);
		}
		if (disabledImage_ != null) {
			disabledImage_.setVisible(false);
		}
	}

	@Override
	public void unselected() {
		super.unselected();
		normalImage_.setVisible(true);
		if (selectedImage_ != null) {
			selectedImage_.setVisible(false);
		}
		if (disabledImage_ != null) {
			disabledImage_.setVisible(false);
		}
	}

	@Override
	public void setIsEnabled(boolean enabled) {
		super.setIsEnabled(enabled);

		if (enabled) {
			normalImage_.setVisible(true);
			if (selectedImage_ != null) {
				selectedImage_.setVisible(false);
			}
			if (disabledImage_ != null) {
				disabledImage_.setVisible(false);
			}
		} else {
			if (disabledImage_ != null) {
				normalImage_.setVisible(false);
				if (selectedImage_ != null) {
					selectedImage_.setVisible(false);
				}
				disabledImage_.setVisible(true);
			} else {
				normalImage_.setVisible(true);
				if (selectedImage_ != null) {
					selectedImage_.setVisible(false);
				}
				// if (disabledImage_ != null)
				// disabledImage_.setVisible(false);
			}
		}
	}

}
