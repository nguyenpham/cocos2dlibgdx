package com.softgaroo.cclgdxdemos;

import org.cclgdx.director.CCDirector;
import org.cclgdx.events.CCMotionEvent;
import org.cclgdx.layers.CCColorLayer;
import org.cclgdx.menus.CCMenu;
import org.cclgdx.menus.CCMenuItem;
import org.cclgdx.menus.CCMenuItemLabel;
import org.cclgdx.menus.CCMenuItemSprite;
import org.cclgdx.menus.CCMenuItemToggle;
import org.cclgdx.nodes.CCNode;
import org.cclgdx.sound.CCSoundEngine;
import org.cclgdx.sprites.CCSprite;
import org.cclgdx.text.CCLabel;
import org.cclgdx.types.CGSize;
import org.cclgdx.types.ccColor3B;
import org.cclgdx.types.ccColor4B;

public class DemoLayer extends CCColorLayer {
	protected CCLabel infoLabel;
	private CCMenu menu;
	private CCMenuItem backItem, debugButton;
	private CCMenuItemToggle soundItem;

	protected static CCSoundEngine soundEngine;

	protected DemoLayer() {
		super(ccColor4B.ccWHITE);

		setIsTouchEnabled(true);

		setupSounds();

		createInfoLabel();

		createButtons();

		setupPositions();

	}

	private void setupSounds() {
		if (soundEngine == null) {
			soundEngine = new CCSoundEngine();
		}
		soundEngine.playSound("sounds/background.wav", true);
		soundEngine.setSoundVolume(0.2f);
	}

	private void createInfoLabel() {
		infoLabel = CCLabel.makeLabel("Information\n");
		infoLabel.setColor(ccColor3B.ccRED);
		addChild(infoLabel);
	}

	private void createButtons() {
		menu = CCMenu.menu();
		addChild(menu);

		CCSprite normalSprite = CCSprite.sprite("images/back.png");
		CCSprite selectedSprite = CCSprite.sprite("images/back.png");
		/*
		 * To save image, the selected sprite uses the same image with normal
		 * one but change color to make different
		 */
		selectedSprite.setColor(ccColor3B.ccGREEN);
		backItem = CCMenuItemSprite.item(normalSprite, selectedSprite, this, "backClicked");
		backItem.setAnchorPoint(0, 1);
		menu.addChild(backItem);

		CCSprite soundOnSprite = CCSprite.sprite("images/sound-on.png");
		CCSprite soundOffSprite = CCSprite.sprite("images/sound-off.png");
		CCMenuItemSprite soundOnItem = CCMenuItemSprite.item(soundOnSprite);
		CCMenuItemSprite soundOffItem = CCMenuItemSprite.item(soundOffSprite);
		soundItem = CCMenuItemToggle.item(this, "soundClicked", soundOnItem, soundOffItem);
		soundItem.setSelectedIndex(soundEngine.isMute() ? 1 : 0);
		soundItem.setAnchorPoint(1, 1);
		menu.addChild(soundItem);

		/*
		 * Create button to turn on / off debug bounding box
		 */
		String fontPath = "fonts/timesnewroman26.fnt";
		CGSize dimensions = CGSize.make(120, 40);
		CCLabel label = CCLabel.makeLabel("Bounding", dimensions, fontPath);
		label.setBackgroundImage("images/buttonbg.png");

		debugButton = CCMenuItemLabel.item(label, this, "debugButtonClicked");
		// debugButton.setColor(ccColor3B.ccWHITE);
		menu.addChild(debugButton);
	}

	private void setupPositions() {
		CGSize sz = CCDirector.sharedDirector().winSize();
		infoLabel.setPosition(sz.width / 2, 20);

		backItem.setPosition(2, sz.height - 2);

		soundItem.setPosition(sz.width - 2, sz.height - 2);

		debugButton.setAnchorPoint(1, 0);
		debugButton.setPosition(sz.width - 2, 2);
	}

	public void backClicked(Object sender) {
		CCDirector.sharedDirector().runWithScene(DemoMainMenuLayer.scene());
	}

	public void soundClicked(Object sender) {
		soundEngine.setMute(!soundEngine.isMute());
	}

	public void setInfoString(String string) {
		infoLabel.setString(string);
	}

	protected boolean debugMode = false;

	public void debugButtonClicked(Object sender) {
		debugMode = !debugMode;
		turnDebugModeForAllChildren(this);
	}

	private void turnDebugModeForAllChildren(CCNode parent) {
		if (parent.getChildren() != null) {
			for (CCNode node : parent.getChildren()) {
				if (node instanceof CCSprite) {
					/*
					 * Set random colors for bounding box
					 */
					((CCSprite) node).setDebugBoundingBoxColor(ccColor4B.ccc4((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), 128));
					((CCSprite) node).showDebugBoundingBox(debugMode);
					turnDebugModeForAllChildren(node);
				}
			}
		}
	}

	@Override
	public boolean ccTouchesBegan(CCMotionEvent event) {
		if (children_ != null) {
			for (CCNode node : children_) {
				if (!(node instanceof CCMenu) && node.containsScreenPoint(event.x, event.y)) {
					setInfoString("You tapped to object " + node.getClass().getSimpleName() + ", tag " + node.getTag());
					break;
				}
			}
		}
		return true;
	}

	@Override
	public void onResize(int width, int height) {
		super.onResize(width, height);
		setContentSize(width, height);
		setupPositions();
	}

	@Override
	public void onExit() {
		soundEngine.stopAllSounds();
		super.onExit();
	}
}
