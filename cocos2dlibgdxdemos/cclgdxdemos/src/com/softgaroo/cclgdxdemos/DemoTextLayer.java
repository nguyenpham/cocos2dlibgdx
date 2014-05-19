package com.softgaroo.cclgdxdemos;

import org.cclgdx.actions.interval.CCMoveTo;
import org.cclgdx.actions.interval.CCRotateTo;
import org.cclgdx.actions.interval.CCScaleTo;
import org.cclgdx.actions.interval.CCSequence;
import org.cclgdx.director.CCDirector;
import org.cclgdx.events.CCMotionEvent;
import org.cclgdx.layers.CCLayer;
import org.cclgdx.layers.CCScene;
import org.cclgdx.text.CCLabel;
import org.cclgdx.text.CCLabel.TextAlignment;
import org.cclgdx.text.CCLabel.TextVerticalAlignment;
import org.cclgdx.types.CGPoint;
import org.cclgdx.types.CGSize;
import org.cclgdx.types.ccColor3B;
import org.cclgdx.types.ccColor4B;

public class DemoTextLayer extends DemoLayer {
	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new DemoTextLayer();
		scene.addChild(layer);
		return scene;
	}

	private final CCLabel escapeLabel;
	private int escapeTapCount = 0;
	private int escapeMissedCount = 0;

	protected DemoTextLayer() {
		super();

		setInfoString("This demo illustrates CCLabel");

		String fontPath = "fonts/timesnewroman26.fnt";
		String bigFontPath = "fonts/comicsans48.fnt";

		CGSize sz = CCDirector.sharedDirector().winSize();
		float x0 = sz.width / 4;
		float x1 = sz.width * 3 / 4;
		float h = sz.height / 8;
		float y = sz.height * 7 / 8;

		CCLabel label0 = CCLabel.makeLabel("Hello World - default font");
		label0.setPosition(x0, y);
		addChild(label0);

		CCLabel label01 = CCLabel.makeLabel("Im red, Times New Roman 26", fontPath);
		label01.setColor(ccColor3B.ccRED);
		label01.setPosition(x0, y - h);
		addChild(label01);

		CCLabel label02 = CCLabel.makeLabel("a blue string on a green box", fontPath);
		label02.setBackgoundColor(ccColor4B.ccGREEN);
		label02.setColor(ccColor3B.ccBLUE);
		label02.setContentSize(CGSize.make(350, 80));
		label02.setAlignment(TextAlignment.CENTER, TextVerticalAlignment.CENTER);
		label02.setPosition(x0, y - 2 * h);
		addChild(label02);

		CCLabel label03 = CCLabel.makeLabel("This is\nmulti lines\nstring");
		label03.setColor(ccColor3B.ccBLUE);
		label03.setPosition(x0, y - 3 * h);
		addChild(label03);

		CCLabel label04 = CCLabel.makeLabel("I am bias", bigFontPath);
		label04.setColor(ccColor3B.ccMAGENTA);
		label04.setPosition(x0 - 70, y - 5 * h);
		label04.setRotation(45);
		addChild(label04);

		CCLabel label05 = CCLabel.makeLabel("Me too!!!", bigFontPath);
		label05.setColor(ccColor3B.ccORANGE);
		label05.setBackgoundColor(ccColor4B.ccc4(128, 128, 128, 128));
		label05.setPosition(x0 + 70, y - 5 * h);
		label05.setRotation(-15);
		addChild(label05);

		CGSize boxSz = CGSize.make(300, 50);
		CCLabel label12 = CCLabel.makeLabel("TopLeft", boxSz, TextAlignment.LEFT, TextVerticalAlignment.TOP, fontPath);
		label12.setPosition(x1, y - h);
		label12.setBackgoundColor(ccColor4B.ccBLUE);
		addChild(label12);

		CCLabel label13 = CCLabel.makeLabel("Center", boxSz, TextAlignment.CENTER, TextVerticalAlignment.CENTER, fontPath);
		label13.setPosition(x1, y - 2 * h);
		label13.setColor(ccColor3B.ccBLACK);
		label13.setBackgoundColor(ccColor4B.ccYELLOW);
		addChild(label13);

		CCLabel label14 = CCLabel.makeLabel("BottomRight", boxSz, TextAlignment.RIGHT, TextVerticalAlignment.BOTTOM, fontPath);
		label14.setPosition(x1, y - 3 * h);
		label14.setColor(ccColor3B.ccRED);
		label14.setBackgoundColor(ccColor4B.ccGRAY);
		addChild(label14);

		/*
		 * "Escape" label
		 */
		escapeLabel = CCLabel.makeLabel("Tap me if you can", bigFontPath);

		CGPoint pt = CGPoint.make(600, 100);
		escapeLabel.setPosition(pt);
		escapeLabel.setColor(ccColor3B.ccRED);
		addChild(escapeLabel);

		// Animate to sake the label
		CCMoveTo ani1 = CCMoveTo.action(0.2f, CGPoint.make(pt.x - 10, pt.y));
		CCMoveTo ani2 = CCMoveTo.action(0.2f, CGPoint.make(pt.x + 10, pt.y));
		CCMoveTo ani3 = CCMoveTo.action(0.1f, pt);
		CCSequence aniSeq = CCSequence.actions(ani1, ani2, ani1, ani2, ani1, ani2, ani3);
		escapeLabel.runAction(aniSeq);

	}

	@Override
	public boolean ccTouchesBegan(CCMotionEvent event) {
		if (escapeLabel.containsScreenPoint(event.x, event.y)) {
			escapeTapCount++;
			escapeLabel.setString("Well done #" + escapeTapCount);
			float newX = CCDirector.sharedDirector().winSize().width * (float) Math.random();
			float newY = CCDirector.sharedDirector().winSize().height * (float) Math.random();
			CCMoveTo moveTo = CCMoveTo.action(0.15f, CGPoint.ccp(newX, newY));
			escapeLabel.runAction(moveTo);

			/*
			 * Random scale
			 */
			float scale = 0.3f + 2 * (float) Math.random();
			CCScaleTo scaleTo = CCScaleTo.action(0.15f, scale);
			escapeLabel.runAction(scaleTo);

			/*
			 * Random rotate
			 */
			if (Math.random() > 0.5) {
				float m = Math.random() > 0.5 ? 1 : -1;
				CCRotateTo rotateTo0 = CCRotateTo.action(0.15f, 180 * m);
				CCRotateTo rotateTo1 = CCRotateTo.action(0.15f, 360 * m);
				CCSequence aniSeq = CCSequence.actions(rotateTo0, rotateTo1);
				escapeLabel.setAnchorPoint(0.5f, 0.5f);
				escapeLabel.runAction(aniSeq);
			}
		} else {
			escapeMissedCount++;
			String msg = "Missed #" + escapeMissedCount;
			if (escapeMissedCount > 10) {
				msg += ", LOL";
			}
			escapeLabel.setString(msg);
		}
		return true;
	}

}
