package com.softgaroo.cclgdxdemos;

import org.cclgdx.actions.base.CCRepeatForever;
import org.cclgdx.actions.interval.CCMoveTo;
import org.cclgdx.actions.interval.CCRotateBy;
import org.cclgdx.actions.interval.CCScaleTo;
import org.cclgdx.actions.interval.CCSequence;
import org.cclgdx.director.CCDirector;
import org.cclgdx.layers.CCScene;
import org.cclgdx.sprites.CCSprite;
import org.cclgdx.types.CGPoint;
import org.cclgdx.types.CGSize;

public class DemoParentChildrenLayer extends DemoLayer {

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		DemoParentChildrenLayer layer = new DemoParentChildrenLayer();
		scene.addChild(layer);
		return scene;
	}

	protected DemoParentChildrenLayer() {
		super();
		setInfoString("This demo illutrates how to animate some sprites\nwhich linked togeter in parent-children");
		createSolarSystem();
	}

	private void createSolarSystem() {
		CGSize winSize = CCDirector.sharedDirector().winSize();
		float x = 100f, y = winSize.height / 2;

		CCSprite sun = CCSprite.spriteWithFileName("images/sun.png");
		sun.setPosition(x, y);
		sun.setTag(0);
		addChild(sun);

		CCSprite earth = CCSprite.spriteWithFileName("images/earth.png");
		earth.setPosition(earth.getContentSizeRef().width * 2, earth.getContentSizeRef().height * 2);
		earth.setTag(1);
		sun.addChild(earth);

		CCSprite satellite = CCSprite.spriteWithFileName("images/satellite.png");
		satellite.setPosition(earth.getContentSizeRef().width / 2, earth.getContentSizeRef().height / 2);
		satellite.setRotation(180);
		satellite.setAnchorPoint(-1.0f, 1.0f);
		satellite.setTag(2);
		earth.addChild(satellite);

		/*
		 * Animates
		 */
		CCRotateBy satRotateBy = CCRotateBy.action(0.6f, -90);
		CCRepeatForever rpRotateSat = CCRepeatForever.action(satRotateBy);
		satellite.runAction(rpRotateSat);

		CCRotateBy earthRotateBy = CCRotateBy.action(1, 45);
		CCRepeatForever rpRotateGlobe = CCRepeatForever.action(earthRotateBy);
		earth.runAction(rpRotateGlobe);

		CCRotateBy sunRotateBy = CCRotateBy.action(5, 45);
		CCRepeatForever rpRotateSun = CCRepeatForever.action(sunRotateBy);
		sun.runAction(rpRotateSun);

		CCMoveTo moveTo0 = CCMoveTo.action(5, CGPoint.make(winSize.width - 100, y));
		CCMoveTo moveTo1 = CCMoveTo.action(5, CGPoint.make(100, y));
		CCSequence sq = CCSequence.actions(moveTo0, moveTo1);
		CCRepeatForever rpMoveSun = CCRepeatForever.action(sq);
		sun.runAction(rpMoveSun);

		CCScaleTo scaleTo0 = CCScaleTo.action(3f, 2);
		CCScaleTo scaleTo1 = CCScaleTo.action(3f, 0.5f);
		CCSequence sqScales = CCSequence.actions(scaleTo0, scaleTo1);
		CCRepeatForever rpScaleSun = CCRepeatForever.action(sqScales);
		sun.runAction(rpScaleSun);

	}
}
