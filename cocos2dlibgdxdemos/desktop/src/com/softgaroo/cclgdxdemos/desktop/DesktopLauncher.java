package com.softgaroo.cclgdxdemos.desktop;

import org.cclgdx.actions.base.CCActionCallback;
import org.cclgdx.director.CCDirector;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.softgaroo.cclgdxdemos.DemoMainMenuLayer;

public class DesktopLauncher {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Menu Demo";
		cfg.width = 800;
		cfg.height = 600;

		final CCDirector director = CCDirector.sharedDirector();
		new LwjglApplication(director.getGame(), cfg);

		director.setReadyCallback(new CCActionCallback() {
			@Override
			public void execute() {
				director.runWithScene(DemoMainMenuLayer.scene());
			}
		});
	}

}
