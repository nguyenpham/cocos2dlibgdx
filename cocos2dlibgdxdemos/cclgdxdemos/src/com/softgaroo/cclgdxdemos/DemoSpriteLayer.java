package com.softgaroo.cclgdxdemos;

import org.cclgdx.director.CCDirector;
import org.cclgdx.layers.CCScene;
import org.cclgdx.sprites.CCSprite;
import org.cclgdx.sprites.CCSpriteFrameCache;
import org.cclgdx.types.CGSize;

public class DemoSpriteLayer extends DemoLayer {

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		DemoSpriteLayer layer = new DemoSpriteLayer();
		scene.addChild(layer);
		return scene;
	}

	protected DemoSpriteLayer() {
		super();

		CGSize sz = CCDirector.sharedDirector().winSize();

		/*
		 * Sprites created from individual images
		 */
		float x = 200, y = sz.height * 0.8f, gap = 100;

		CCSprite sun = CCSprite.spriteWithFileName("images/sun.png");
		sun.setPosition(x, y);
		addChild(sun);

		CCSprite earth = CCSprite.spriteWithFileName("images/earth.png");
		earth.setPosition(x + gap, y);
		addChild(earth);

		CCSprite sattelite = CCSprite.spriteWithFileName("images/satellite.png");
		sattelite.setPosition(x + 2 * gap, y);
		addChild(sattelite);

		/*
		 * Sprites created by sprite sheet files with extensions plist+png
		 */
		String plistFileName = "spritesheets/cars-hd.plist";
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames(plistFileName);

		x = 100;
		y = sz.height * 0.6f;
		gap = 130;

		CCSprite car0 = CCSprite.spriteWithFrameName("bmw-mini.png");
		car0.setPosition(x, y);
		addChild(car0);

		CCSprite car1 = CCSprite.spriteWithFrameName("camaro.png");
		car1.setPosition(x + gap, y);
		addChild(car1);

		CCSprite car2 = CCSprite.spriteWithFrameName("chevrolet-impala.png");
		car2.setPosition(x + gap * 2, y);
		addChild(car2);

		CCSprite car3 = CCSprite.spriteWithFrameName("vintage-car.png");
		car3.setPosition(x + gap * 3, y);
		addChild(car3);

		CCSprite car4 = CCSprite.spriteWithFrameName("vw-beetle.png");
		car4.setPosition(x + gap * 4, y);
		addChild(car4);

		CCSprite car5 = CCSprite.spriteWithFrameName("yellow-pickup.png");
		car5.setPosition(x + gap * 5, y);
		addChild(car5);

		/*
		 * Sprites created by sprite sheet files with extensions txt+png
		 */
		String textFileName = "spritesheets/animals.txt";
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames(textFileName);

		x = 150;
		y = sz.height * 0.3f;
		gap = 200;

		CCSprite elephant = CCSprite.spriteWithFrameName("elephant.png");
		elephant.setPosition(x + 0 * gap, y);
		addChild(elephant);

		CCSprite panda = CCSprite.spriteWithFrameName("panda.png");
		panda.setPosition(x + gap, y);
		addChild(panda);

		CCSprite squirrel = CCSprite.spriteWithFrameName("squirrel.png");
		squirrel.setPosition(x + 2 * gap, y);
		addChild(squirrel);
	}
}
