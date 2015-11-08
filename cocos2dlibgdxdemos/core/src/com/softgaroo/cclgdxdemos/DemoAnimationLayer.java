package com.softgaroo.cclgdxdemos;

import java.util.ArrayList;

import org.cclgdx.actions.base.CCRepeatForever;
import org.cclgdx.actions.instant.CCCallFunc;
import org.cclgdx.actions.instant.CCCallFuncN;
import org.cclgdx.actions.instant.CCFlipX;
import org.cclgdx.actions.interval.CCAnimate;
import org.cclgdx.actions.interval.CCAnimation;
import org.cclgdx.actions.interval.CCMoveTo;
import org.cclgdx.actions.interval.CCRotateTo;
import org.cclgdx.actions.interval.CCSequence;
import org.cclgdx.director.CCDirector;
import org.cclgdx.layers.CCLayer;
import org.cclgdx.layers.CCScene;
import org.cclgdx.sprites.CCSprite;
import org.cclgdx.sprites.CCSpriteFrame;
import org.cclgdx.sprites.CCSpriteFrameCache;
import org.cclgdx.types.CGPoint;
import org.cclgdx.types.CGSize;
import org.cclgdx.types.ccColor3B;

public class DemoAnimationLayer extends DemoLayer {

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new DemoAnimationLayer();
		scene.addChild(layer);
		return scene;
	}

	private final CCSprite kickingSprite, riderSprite;

	protected DemoAnimationLayer() {
		super();

		this.setIsTouchEnabled(true);

		setInfoString("This demo illustrates animations");

		CGSize sz = CCDirector.sharedDirector().winSize();

		/*
		 * Kicking girl
		 */
		float y = sz.height * 0.8f;
		kickingSprite = createAnimation("spritesheets/a-hd.plist", "a", 10, 0.25f);
		kickingSprite.setPosition(sz.width * 0.5f, y);

		/*
		 * Horse rider
		 */
		y = sz.height * 0.5f;
		riderSprite = createAnimation("spritesheets/rider.txt", "h", 12, 0.12f);
		riderSprite.setPosition(-riderSprite.getContentSize().width, y);

		CCMoveTo moveTo0 = CCMoveTo.action(6f, CGPoint.make(-riderSprite.getContentSize().width, y));
		CCMoveTo moveTo1 = CCMoveTo.action(6f, CGPoint.make(sz.width + riderSprite.getContentSize().width, y));
		CCCallFunc func = CCCallFunc.action(this, "changeRiderColorFunc");
		CCFlipX flipTrue = CCFlipX.action(true);
		CCFlipX flipFalse = CCFlipX.action(false);
		CCSequence sequence = CCSequence.actions(moveTo1, func, flipTrue, moveTo0, func, flipFalse);
		CCRepeatForever repeat = CCRepeatForever.action(sequence);
		riderSprite.runAction(repeat);

		/*
		 * Knight
		 */
		CCSprite knightSprite = createAnimation("spritesheets/knight-hd.plist", "knight_walk", 7, 0.1f);
		float h = knightSprite.getContentSize().height / 2;
		CGPoint pt0 = CGPoint.make(h, h);
		CGPoint pt1 = CGPoint.make(sz.width - h, h);
		CGPoint pt2 = CGPoint.make(sz.width - h, sz.height - h);
		CGPoint pt3 = CGPoint.make(h, sz.height - h);

		knightSprite.setPosition(pt0);

		CCMoveTo kMoveTo0 = CCMoveTo.action(8f, pt0);
		CCMoveTo kMoveTo1 = CCMoveTo.action(8f, pt1);
		CCMoveTo kMoveTo2 = CCMoveTo.action(8f, pt2);
		CCMoveTo kMoveTo3 = CCMoveTo.action(8f, pt3);
		CCSequence foxSequence = CCSequence.actions(kMoveTo1, CCRotateTo.action(0.1f, 90), kMoveTo2, CCRotateTo.action(0.1f, 180), kMoveTo3, CCRotateTo.action(0.1f, -90), kMoveTo0,
				CCRotateTo.action(0.1f, 0));
		CCRepeatForever kRepeat = CCRepeatForever.action(foxSequence);
		knightSprite.runAction(kRepeat);
	}

	/*
	 * load animation
	 */
	private CCSprite createAnimation(String spriteSheetName, String prefixName, int frameNumber, float intervalFrameDelay) {
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames(spriteSheetName);

		CCSprite sprite = CCSprite.spriteWithFrameName(prefixName + "1.png");
		addChild(sprite);

		ArrayList<CCSpriteFrame> animFrames = new ArrayList<CCSpriteFrame>();
		for (int i = 1; i <= frameNumber; ++i) {
			String frameName = prefixName + i + ".png";
			CCSpriteFrame frame = CCSpriteFrameCache.sharedSpriteFrameCache().getSpriteFrame(frameName);
			animFrames.add(frame);
		}

		// Create an animation from the set of frames you created earlier
		CCAnimation animation = CCAnimation.animation("ani_" + prefixName, intervalFrameDelay, animFrames);

		CCCallFuncN function = CCCallFuncN.action(this, "afterPlayingAllFramesFunc");
		CCSequence sequence = CCSequence.actions(CCAnimate.action(animation), function);

		// Create an action with the animation that can then be assigned to a
		// sprite
		CCRepeatForever repeat = CCRepeatForever.action(sequence);
		sprite.runAction(repeat);
		return sprite;
	}

	public void afterPlayingAllFramesFunc(Object sender) {
		if (sender == kickingSprite) {
			kickingSprite.setFlipX(!kickingSprite.isFlipX());
		}
	}

	public void changeRiderColorFunc() {
		int r = (int) (Math.random() * 255);
		int g = (int) (Math.random() * 255);
		int b = (int) (Math.random() * 255);
		riderSprite.setColor(ccColor3B.ccc3(r, g, b));

	}
}
