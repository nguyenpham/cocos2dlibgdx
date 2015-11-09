package com.softgaroo.cclgdxdemos.client;

import org.cclgdx.actions.base.CCActionCallback;
import org.cclgdx.director.CCDirector;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.softgaroo.cclgdxdemos.DemoMainMenuLayer;

public class HtmlLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig() {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(480, 320);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener() {
		final CCDirector director = CCDirector.sharedDirector();

		director.setReadyCallback(new CCActionCallback() {
			@Override
			public void execute() {
				director.runWithScene(DemoMainMenuLayer.scene());
			}
		});

		return director.getGame();
	}
}