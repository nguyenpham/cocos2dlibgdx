package com.softgaroo.cclgdxdemos;

import org.cclgdx.actions.base.CCActionCallback;
import org.cclgdx.director.CCDirector;
import org.robovm.cocoatouch.foundation.NSAutoreleasePool;
import org.robovm.cocoatouch.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

public class RobovmLauncher extends IOSApplication.Delegate {
	@Override
	protected IOSApplication createApplication() {
		IOSApplicationConfiguration config = new IOSApplicationConfiguration();
		config.orientationLandscape = true;
		config.orientationPortrait = false;

		final CCDirector director = CCDirector.sharedDirector();

		director.setReadyCallback(new CCActionCallback() {
			@Override
			public void execute() {
				director.runWithScene(DemoMainMenuLayer.scene());
			}
		});

		return new IOSApplication(director.getGame(), config);
	}

	public static void main(String[] argv) {
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(argv, null, RobovmLauncher.class);
		pool.drain();
	}
}
