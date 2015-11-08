package com.softgaroo.cclgdxdemos;

import org.cclgdx.director.CCDirector;
import org.cclgdx.layers.CCLayer;
import org.cclgdx.layers.CCScene;
import org.cclgdx.menus.CCMenu;
import org.cclgdx.menus.CCMenuItemLabel;
import org.cclgdx.types.CGSize;

public class DemoMainMenuLayer extends CCLayer {
	private static final float MENU_ITEM_HEIGHT = 80;

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new DemoMainMenuLayer();
		scene.addChild(layer);
		return scene;
	}

	private final String fontPath = "fonts/comicsans48.fnt";
	private final String menuStrings[] = { "Menus, buttons", "Sprites", "Text", "Scale, rotate, move", "Animations", "Simple game" };

	protected DemoMainMenuLayer() {
		super();

		CCMenu menu = CCMenu.menu();
		addChild(menu);

		CGSize sz = CCDirector.sharedDirector().winSize();

		float x = sz.width / 2;
		float y = (sz.height + menuStrings.length * MENU_ITEM_HEIGHT) / 2;

		for (int i = 0; i < menuStrings.length; i++, y -= MENU_ITEM_HEIGHT) {
			String str = menuStrings[i];
			CCMenuItemLabel menuItem = CCMenuItemLabel.item(str, fontPath, this, "menuItemClicked");
			menuItem.setPosition(x, y);
			menuItem.setTag(i);
			menuItem.setUserData(str);
			menu.addChild(menuItem);
		}
	}

	public void menuItemClicked(Object sender) {
		CCScene newScene = null;
		switch (((CCMenuItemLabel) sender).getTag()) {
		case 0:
			newScene = DemoMenuLayer.scene();
			break;
		case 1:
			newScene = DemoSpriteLayer.scene();
			break;
		case 2:
			newScene = DemoTextLayer.scene();
			break;
		case 3:
			newScene = DemoParentChildrenLayer.scene();
			break;
		case 4:
			newScene = DemoAnimationLayer.scene();
			break;

		case 5:
		default:
			newScene = SimpleGameLayer.scene();
			break;
		}

		if (newScene != null) {
			CCDirector.sharedDirector().runWithScene(newScene);
		}

	}
}
