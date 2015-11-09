package com.softgaroo.cclgdxdemos;

import org.cclgdx.director.CCDirector;
import org.cclgdx.layers.CCLayer;
import org.cclgdx.layers.CCScene;
import org.cclgdx.menus.CCMenu;
import org.cclgdx.menus.CCMenuItemLabel;
import org.cclgdx.menus.CCMenuItemSprite;
import org.cclgdx.nodes.CCNode;
import org.cclgdx.sprites.CCSprite;
import org.cclgdx.text.CCLabel;
import org.cclgdx.types.CGSize;
import org.cclgdx.types.ccColor3B;
import org.cclgdx.types.ccColor4B;

public class DemoMenuLayer extends DemoLayer {
	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new DemoMenuLayer();
		scene.addChild(layer);
		return scene;
	}

	private final CCMenu menu;
	private static final String fontPath = "fonts/timesnewroman26.fnt";

	protected DemoMenuLayer() {
		super();

		setInfoString("This demo illustrates how to create menus & buttons");

		menu = CCMenu.menu();
		addChild(menu);

		createMenuWithLabelItems();

		createMenuWithSprites();

		CCLabel.setDefaultFontPath(null);
	}

	private void createMenuWithLabelItems() {
		CCLabel.setDefaultFontPath(fontPath);

		CGSize sz = CCDirector.sharedDirector().winSize();
		float x = sz.width * 0.1f;
		float y = sz.height * 0.7f;
		float h = 60;

		String contents0[] = { "New", "Open", "Save", "Print", "Quit" };

		for (int i = 0; i < contents0.length; i++) {
			CCMenuItemLabel menuItem = CCMenuItemLabel.item(contents0[i], this, "menuItemClicked");
			menuItem.setTag(i);
			menuItem.setUserData(contents0[i]);
			menuItem.setPosition(x, y - h * i);
			menuItem.setColor(ccColor3B.ccc3((int) (255 * Math.random()), (int) (255 * Math.random()), (int) (255 * Math.random())));
			menu.addChild(menuItem);
		}

		x = sz.width * 0.25f;

		String contents1[] = { "Cut", "Copy", "Page", "Find/Replace" };
		CGSize dimensions = CGSize.make(150, h);

		for (int i = 0; i < contents1.length; i++) {
			CCLabel label = CCLabel.makeLabel(contents1[i], dimensions, fontPath);
			label.setBackgoundColor(ccColor4B.ccc4((int) (128 * Math.random()), (int) (128 * Math.random()), (int) (128 * Math.random()), 255));
			label.setColor(ccColor3B.ccWHITE);

			CCMenuItemLabel menuItem = CCMenuItemLabel.item(label, this, "menuItemClicked");
			menuItem.setTag(i);
			menuItem.setUserData(contents1[i]);
			menuItem.setPosition(x, y - h * i);
			menu.addChild(menuItem);
		}

		dimensions = CGSize.make(220, h);
		x = sz.width * 0.5f;
		h = 100;

		String contents2[] = { "Welcome", "Help", "Search", "Check for Updates" };

		for (int i = 0; i < contents2.length; i++) {
			CCLabel label = CCLabel.makeLabel(contents2[i], dimensions, fontPath);
			label.setBackgroundImage("images/buttonbg.png");
			label.setColor(ccColor3B.ccWHITE);

			CCMenuItemLabel menuItem = CCMenuItemLabel.item(label, this, "menuItemClicked");
			menuItem.setTag(i);
			menuItem.setUserData(contents2[i]);
			menuItem.setPosition(x, y - h * i);
			menu.addChild(menuItem);
		}
	}

	private void createMenuWithSprites() {
		String spriteNames[] = { "images/addbtn.png", "images/deletebtn.png", "images/downloadbtn.png", "images/okbtn.png", "images/playbtn.png", "images/nextbtn.png" };

		CGSize sz = CCDirector.sharedDirector().winSize();
		float x = sz.width * 0.75f;
		float y = sz.height * 0.7f;
		float h = 150;
		float w = 130;

		for (int i = 0; i < spriteNames.length; i++) {
			CCSprite normalSprite = CCSprite.sprite(spriteNames[i]);
			CCSprite selectedSprite = CCSprite.sprite(spriteNames[i].replace(".", "_d."));
			// selectedSprite.setColor(ccColor3B.ccYELLOW);

			CCMenuItemSprite menuItem = CCMenuItemSprite.item(normalSprite, selectedSprite, this, "menuItemClicked");
			String name = spriteNames[i].replace("images/", "").replace("btn.png", "");
			menuItem.setTag(i);
			menuItem.setUserData(name);

			float bx = x + (i & 1) * w;
			float by = y - h * (i / 2);
			menuItem.setPosition(bx, by);
			menu.addChild(menuItem);
		}
	}

	public void menuItemClicked(Object sender) {
		setInfoString("Tapped on button " + ((CCNode) sender).getUserData() + ", tag=" + ((CCNode) sender).getTag());
	}
}
