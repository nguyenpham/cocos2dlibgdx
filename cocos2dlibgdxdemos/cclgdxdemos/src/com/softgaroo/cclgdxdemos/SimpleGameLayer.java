package com.softgaroo.cclgdxdemos;

import java.util.ArrayList;

import org.cclgdx.actions.base.CCRepeatForever;
import org.cclgdx.actions.instant.CCCallFunc;
import org.cclgdx.actions.instant.CCCallFuncN;
import org.cclgdx.actions.interval.CCAnimate;
import org.cclgdx.actions.interval.CCAnimation;
import org.cclgdx.actions.interval.CCMoveTo;
import org.cclgdx.actions.interval.CCSequence;
import org.cclgdx.director.CCDirector;
import org.cclgdx.events.CCMotionEvent;
import org.cclgdx.layers.CCLayer;
import org.cclgdx.layers.CCScene;
import org.cclgdx.sprites.CCSprite;
import org.cclgdx.sprites.CCSpriteFrame;
import org.cclgdx.sprites.CCSpriteFrameCache;
import org.cclgdx.text.CCLabel;
import org.cclgdx.types.CGPoint;
import org.cclgdx.types.CGRect;
import org.cclgdx.types.CGSize;
import org.cclgdx.types.ccColor3B;

public class SimpleGameLayer extends DemoLayer {

	private final ArrayList<CCSprite> ballList = new ArrayList<CCSprite>();
	private final ArrayList<CCSprite> zombieList = new ArrayList<CCSprite>();
	private final CCSprite plantSprite;
	private float timeTotal;
	private final CCRepeatForever plantStandingRepeat;
	private final CCSequence plantAttackingSequence;
	CCAnimation zombieWalking;

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new SimpleGameLayer();
		scene.addChild(layer);
		return scene;
	}

	protected SimpleGameLayer() {
		super();

		setInfoString("This demo illustrates a simple game. Tap to fire");

		this.setIsTouchEnabled(true);

		CGSize sz = CCDirector.sharedDirector().winSize();

		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames("spritesheets/plants_zombies.plist");

		float y = sz.height * 0.5f;
		plantSprite = CCSprite.spriteWithFrameName("plant1.png");
		plantSprite.setPosition(plantSprite.getContentSize().width * 0.6f, y);
		addChild(plantSprite);

		String plantStandingFrames[] = { "plant1.png", "plant2.png", "plant3.png", "plant2.png" };
		CCAnimation plantStanding = createAnimation(plantStandingFrames, 0.4f);
		plantStandingRepeat = CCRepeatForever.action(CCAnimate.action(plantStanding));

		String plantAtttackingFrames[] = { "plant5.png", "plant2.png" };
		CCAnimation plantAttacking = createAnimation(plantAtttackingFrames, 0.4f);
		CCCallFunc function = CCCallFunc.action(this, "plantStanding");
		plantAttackingSequence = CCSequence.actions(CCAnimate.action(plantAttacking), function);

		plantSprite.runAction(plantStandingRepeat);

		String zombieWalkingFrames[] = { "zomb1.png", "zomb2.png", "zomb3.png", "zomb4.png", "zomb5.png", "zomb6.png", "zomb7.png", "zomb8.png" };
		zombieWalking = createAnimation(zombieWalkingFrames, 0.3f);

		soundEngine.preloadEffect("sounds/laser.wav");
		soundEngine.preloadEffect("sounds/beat.wav");

		timeTotal = 0;
		schedule("updateGameLogic", 0.1f);
	}

	/*
	 * load animation
	 */
	private CCAnimation createAnimation(String frameNames[], float intervalFrameDelay) {

		ArrayList<CCSpriteFrame> animFrames = new ArrayList<CCSpriteFrame>();
		for (String frameName : frameNames) {
			CCSpriteFrame frame = CCSpriteFrameCache.sharedSpriteFrameCache().getSpriteFrame(frameName);
			animFrames.add(frame);
		}

		// Create an animation from the set of frames you created earlier
		CCAnimation animation = CCAnimation.animation("ani", intervalFrameDelay, animFrames);
		return animation;
	}

	public void plantStanding() {
		plantSprite.runAction(plantStandingRepeat);
	}

	@Override
	public boolean ccTouchesBegan(CCMotionEvent event) {
		/*
		 * Starting position of balls
		 */
		float x1 = plantSprite.getX() + plantSprite.getBoundingBox().width * 0.3f;
		float y1 = plantSprite.getY() + plantSprite.getBoundingBox().height * 0.1f;
		CGPoint startPt = CGPoint.make(x1, y1);

		/*
		 * Not allow to fire with too wide angles
		 */
		if (event.x < x1 + 20) {
			return true;
		}

		float x2 = event.x, y2 = event.y;
		float a = (y2 - y1) / (x2 - x1);
		float b = y2 - a * x2;

		/*
		 * End position of a ball
		 */
		float x3 = CCDirector.sharedDirector().winSize().width + 100;
		float y3 = a * x3 + b;
		CGPoint endPt = CGPoint.make(x3, y3);

		CCMoveTo moveTo = CCMoveTo.action(1.5f, endPt);
		CCCallFuncN function = CCCallFuncN.action(this, "removeBallFunc");
		CCSequence sequence = CCSequence.actions(moveTo, function);

		CCSprite ballSprite = CCSprite.spriteWithFrameName("ball.png");
		ballList.add(ballSprite);
		ballSprite.setPosition(startPt);
		addChild(ballSprite);

		ballSprite.runAction(sequence);
		ballSprite.showDebugBoundingBox(debugMode);

		plantSprite.stopAllActions();
		plantSprite.runAction(plantAttackingSequence);

		soundEngine.playEffect("sounds/laser.wav");

		return true;
	}

	public void removeBallFunc(Object sender) {
		ballList.remove(sender);
		((CCSprite) sender).removeFromParentAndCleanup(true);
	}

	public void removeZombieFunc(Object sender) {
		zombieList.remove(sender);
		((CCSprite) sender).removeFromParentAndCleanup(true);
	}

	public void updateGameLogic(float dt) {
		timeTotal += dt;
		if (timeTotal >= 3.0f) {
			timeTotal = 0;
			addZombie();
		}

		for (CCSprite zombie : zombieList) {
			CGPoint zombPt = zombie.getScreenBoundingBox().getCenter();
			/*
			 * If a zombie reaches the left border or catches the plant, game
			 * over
			 */
			if (zombPt.x < 0 || CGRect.containsPoint(plantSprite.getScreenBoundingBox(), zombPt)) {
				CCLabel gameOverLabel = CCLabel.makeLabel("Game Over", "fonts/comicsans48.fnt");
				gameOverLabel.setColor(ccColor3B.ccRED);
				addChild(gameOverLabel);

				CGSize sz = CCDirector.sharedDirector().winSize();
				gameOverLabel.setPosition(sz.width / 2, sz.height / 2);

				plantSprite.stopAllActions();
				for (CCSprite zombie2 : zombieList) {
					zombie2.stopAllActions();
				}
				unschedule("updateGameLogic");
				return;
			}

			for (CCSprite ball : ballList) {
				if (CGRect.intersects(zombie.getScreenBoundingBox(), ball.getScreenBoundingBox())) {
					removeBallFunc(ball);
					removeZombieFunc(zombie);
					soundEngine.playEffect("sounds/beat.wav");
					return;
				}
			}
		}
	}

	private void addZombie() {
		CCSprite zombieSprite = CCSprite.spriteWithFrameName("zomb1.png");
		addChild(zombieSprite);
		zombieList.add(zombieSprite);

		CCRepeatForever zombieWalkingRepeat = CCRepeatForever.action(CCAnimate.action(zombieWalking));
		zombieSprite.runAction(zombieWalkingRepeat);

		float zh = zombieSprite.getContentSize().height;
		CGSize sz = CCDirector.sharedDirector().winSize();
		float y = (float) (zh * 0.5 + (sz.height - zh) * Math.random());

		zombieSprite.setPosition(sz.width + zombieSprite.getContentSize().width / 2, y);

		CCMoveTo moveTo = CCMoveTo.action(14f, CGPoint.make(-zombieSprite.getContentSize().width, y));
		CCCallFuncN function = CCCallFuncN.action(this, "removeZombieFunc");
		CCSequence sequence = CCSequence.actions(moveTo, function);
		zombieSprite.runAction(sequence);

		zombieSprite.showDebugBoundingBox(debugMode);
	}

}
