package org.cclgdx.director;

import java.util.ArrayList;

import org.cclgdx.actions.base.CCActionCallback;
import org.cclgdx.actions.base.CCActionManager;
import org.cclgdx.actions.base.CCScheduler;
import org.cclgdx.events.CCKeyDispatcher;
import org.cclgdx.events.CCKeyEvent;
import org.cclgdx.events.CCMotionEvent;
import org.cclgdx.events.CCTouchDispatcher;
import org.cclgdx.layers.CCScene;
import org.cclgdx.nodes.CCNode;
import org.cclgdx.nodes.CCTextureCache;
import org.cclgdx.nodes.CCTransformValue;
import org.cclgdx.sprites.CCSpriteFrameCache;
import org.cclgdx.text.CCFontCache;
import org.cclgdx.text.CCLabel;
import org.cclgdx.types.CGSize;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Class that creates and handle the main Window and manages how and when to
 * execute the Scenes.
 * 
 * Since the CCDirector is a singleton, the standard way to use it is by
 * calling: - [[CCDirector sharedDirector] xxxx];
 */
public class CCDirector implements InputProcessor {
	private static final String LOG_TAG = CCDirector.class.getSimpleName();

	private static CCDirector _sharedDirector = new CCDirector();

	/** returns a shared instance of the director */
	public static CCDirector sharedDirector() {
		return _sharedDirector;
	}

	protected CCDirector() {
		// CCScenes
		runningCCScene_ = null;
		nextCCScene_ = null;

		CCScenesStack_ = new ArrayList<CCScene>(10);

		// FPS
		displayFPS = true;

		transformValue.reset();
		// paused?
		isPaused = false;
		screenSize_ = CGSize.zero();
	}

	/*
	 * LibGdx may take a while to initialize, all scenes should be created after
	 * that initiation done
	 */
	protected CCActionCallback readyCallback = null;

	public void setReadyCallback(CCActionCallback readyCallback) {
		this.readyCallback = readyCallback;
	}

	/*
	 * DirectorGame and DirectorScreen are an extension from LibGdx classes,
	 * created to run all CCLGDX scenes. User can extend and/or replace the
	 * default ones
	 */
	private Game directorGame = null;
	private DirectorScreen directorScreen = null;

	public Game getGame() {
		if (directorGame == null) {
			directorGame = new DirectorGame();
		}
		return directorGame;
	}

	public void setGame(Game game) {
		this.directorGame = game;
	}

	public class DirectorGame extends Game {
		@Override
		public void create() {
			directorScreen = new DirectorScreen();
			setScreen(directorScreen);
		}
	}

	public class DirectorScreen extends CCNode implements Screen {

		public DirectorScreen() {
			renderSpriteBatch = new SpriteBatch();
		}

		@Override
		public void render(float delta) {
			onDirectorRender(delta);
		}

		@Override
		public void resize(int width, int height) {
			onDirectorResize(width, height);
		}

		@Override
		public void show() {
			onDirectorShow();
		}

		@Override
		public void hide() {
			onDirectorHide();
		}

		@Override
		public void pause() {
			onDirectorPause();
		}

		@Override
		public void resume() {
			onDirectorResume();
		}

		@Override
		public void dispose() {
			onDirectorDispose();
		}

		public void startSpriteBath() {
			renderSpriteBatch.setProjectionMatrix(camera.combined);
			renderSpriteBatch.begin();
		}

		protected void endSpriteBath() {
			renderSpriteBatch.end();
		}
	}

	/* is the running scene paused */
	private boolean isPaused;

	/** Whether or not the Director is paused */
	public boolean getIsPaused() {
		return isPaused;
	}

	/* The running CCScene */
	private CCScene runningCCScene_;

	/* will be the next 'runningCCScene' in the next frame */
	private CCScene nextCCScene_;

	/* If yes, then "old" CCScene will receive the cleanup message */
	private boolean sendCleanupToCCScene_;

	/**
	 * Whether or not the replaced CCScene will receive the cleanup message. If
	 * the new CCScene is pushed, then the old CCScene won't receive the
	 * "cleanup" message. If the new CCScene replaces the old one, the it will
	 * receive the "cleanup" message.
	 */
	public boolean getSendCleanupToScene() {
		return sendCleanupToCCScene_;
	}

	/* scheduled CCScenes */
	private final ArrayList<CCScene> CCScenesStack_;

	/* screen, different than surface size */
	private final CGSize screenSize_;

	/**
	 * The current running CCScene. Director can only run one CCScene at the
	 * time
	 */
	public CCScene getRunningScene() {
		return runningCCScene_;
	}

	/**
	 * Whether or not to display the FPS on the bottom-left corner
	 */
	private boolean displayFPS;

	public void setDisplayFPS(boolean value) {
		displayFPS = value;
	}

	private boolean initOnce = true;

	private void initOnce() {
		Gdx.input.setInputProcessor(this);
		CCTouchDispatcher.sharedDispatcher().setDispatchEvents(true);

		drawFPSNode = new DrawFPSNode();
		if (readyCallback != null) {
			readyCallback.execute();
		}
	}

	public void onDirectorRender(float delta) {
		if (initOnce) {
			initOnce = false;
			initOnce();
		}

		CCTouchDispatcher.sharedDispatcher().update();
		CCKeyDispatcher.sharedDispatcher().update();

		if (!isPaused) {
			CCScheduler.sharedScheduler().tick(delta);
		}

		GL20 gl = Gdx.graphics.getGL20();
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		directorScreen.startSpriteBath();

		drawCCScene(delta);

		if (displayFPS) {
			drawFPSNode.draw(delta);
		}

		directorScreen.endSpriteBath();

		/*
		 * All internal sprites of labels will be created here to avoid
		 * flickering
		 */
		CCLabel.updateAllLabels();
	}

	/**
	 * Draw the CCScene. This method is called every frame. Don't call it
	 * manually.
	 */
	private final CCTransformValue transformValue = new CCTransformValue();

	private void drawCCScene(float delta) {
		if (nextCCScene_ != null) {
			setNextScene();
		}

		/* draw the CCScene */
		if (runningCCScene_ != null) {
			runningCCScene_.visit(delta);
		}
	}

	private DrawFPSNode drawFPSNode;

	public void loadFPSFont(String fpsFontName) {
		drawFPSNode.loadFPSFont(fpsFontName);
	}

	public class DrawFPSNode extends CCNode {
		private BitmapFont bitmapFont;
		private float x, y;

		public DrawFPSNode() {
			loadFPSFont(null);
		}

		public void loadFPSFont(String fpsFontName) {
			bitmapFont = CCFontCache.sharedFontCache().get(fpsFontName);
			TextBounds textBounds = bitmapFont.getMultiLineBounds("0");
			x = textBounds.width;
			y = textBounds.height * 1.5f;
		}

		@Override
		public void draw(float dt) {
			String fpsString = String.valueOf(Gdx.graphics.getFramesPerSecond());
			// renderSpriteBatch.begin();
			bitmapFont.setColor(Color.WHITE);
			bitmapFont.draw(renderSpriteBatch, fpsString, x, y);
			// CCNode.renderSpriteBatch.end();
		}
	}

	/**
	 * returns the size of the OpenGL view in pixels, according to the landspace
	 */
	public CGSize winSize() {
		CGSize s = CGSize.make(screenSize_.width, screenSize_.height);
		return s;
	}

	public CGSize winSizeRef() {
		return screenSize_;
	}

	/** returns the display size of the OpenGL view in pixels */
	public CGSize displaySize() {
		return CGSize.make(screenSize_.width, screenSize_.height);
	}

	// Director CCScene Management
	/**
	 * Enters the Director's main loop with the given CCScene. Call it to run
	 * only your FIRST CCScene. Don't call it if there is already a running
	 * CCScene.
	 */
	public void runWithScene(CCScene CCScene) {
		assert CCScene != null : "Argument must be non-null";
		assert runningCCScene_ == null : "You can't run a CCScene if another CCScene is running. Use replaceCCScene or pushCCScene instead";

		pushScene(CCScene);
	}

	/**
	 * Replaces the running CCScene with a new one. The running CCScene is
	 * terminated. ONLY call it if there is a running CCScene.
	 */
	public void replaceScene(CCScene CCScene) {
		assert CCScene != null : "Argument must be non-null";

		int index = CCScenesStack_.size();

		sendCleanupToCCScene_ = true;
		CCScenesStack_.set(index - 1, CCScene);
		nextCCScene_ = CCScene;
	}

	/**
	 * Suspends the execution of the running CCScene, pushing it on the stack of
	 * suspended CCScenes. The new CCScene will be executed. Try to avoid big
	 * stacks of pushed CCScenes to reduce memory allocation. ONLY call it if
	 * there is a running CCScene.
	 */
	public void pushScene(CCScene CCScene) {
		assert CCScene != null : "Argument must be non-null";

		sendCleanupToCCScene_ = false;

		CCScenesStack_.add(CCScene);
		nextCCScene_ = CCScene;
	}

	/**
	 * Pops out a CCScene from the queue. This CCScene will replace the running
	 * one. The running CCScene will be deleted. If there are no more CCScenes
	 * in the stack the execution is terminated. ONLY call it if there is a
	 * running CCScene.
	 */
	public void popScene() {
		assert runningCCScene_ != null : "A running CCScene is needed";

		CCScenesStack_.remove(CCScenesStack_.size() - 1);
		int c = CCScenesStack_.size();

		if (c == 0) {
			end();
		} else {
			nextCCScene_ = CCScenesStack_.get(c - 1);
		}
	}

	/**
	 * Ends the execution, releases the running CCScene.
	 */
	public void end() {
		if (runningCCScene_ != null) {
			runningCCScene_.onExit();
			runningCCScene_.cleanup();
			runningCCScene_ = null;
		}
		nextCCScene_ = null;

		// remove all objects.
		// runWithCCScene might be executed after 'end'.
		CCScenesStack_.clear();

		// don't release the event handlers
		// They are needed in case the director is run again
		CCTouchDispatcher.sharedDispatcher().removeAllDelegates();

		// stopAnimation();
		// detach();

		// Purge bitmap cache
		// CCBitmapFontAtlas.purgeCachedData();

		// Purge all managers
		CCSpriteFrameCache.purgeSharedSpriteFrameCache();
		CCScheduler.purgeSharedScheduler();
		CCActionManager.purgeSharedManager();
		CCTextureCache.purgeSharedTextureCache();
	}

	public void setNextScene() {
		// If it is not a transition, call onExit
		if (runningCCScene_ != null) {// && !newIsTransition) {
			runningCCScene_.onExit();

			if (sendCleanupToCCScene_) {
				runningCCScene_.cleanup();
			}
		}

		runningCCScene_ = nextCCScene_;
		nextCCScene_ = null;

		runningCCScene_.onEnter();
	}

	public void onDirectorShow() {

	}

	public void onDirectorHide() {

	}

	public void onDirectorDispose() {

	}

	/**
	 * this should be called from activity when activity pause
	 */
	public void onDirectorPause() {
		pause();
	}

	/**
	 * this should be called from activity when activity resume
	 */
	public void onDirectorResume() {
		resume();
	}

	/**
	 * Pauses the running CCScene. The running CCScene will be _drawed_ but all
	 * scheduled timers will be paused While paused, the draw rate will be 4 FPS
	 * to reduce CPU consumption
	 */
	public void pause() {
		if (isPaused) {
			return;
		}
		isPaused = true;
	}

	/**
	 * Resumes the paused CCScene The scheduled timers will be activated again.
	 * The "delta time" will be 0 (as if the game wasn't paused)
	 */
	public void resume() {
		if (!isPaused) {
			return;
		}

		isPaused = false;
	}

	public boolean onKeyDown(CCKeyEvent event) {
		if (!CCKeyDispatcher.sharedDispatcher().getDispatchEvents())
			return false;
		CCKeyDispatcher.sharedDispatcher().queueKeyEvent(event);
		return true;
	}

	public boolean onKeyUp(CCKeyEvent event) {
		if (!CCKeyDispatcher.sharedDispatcher().getDispatchEvents())
			return false;

		CCKeyDispatcher.sharedDispatcher().queueKeyEvent(event);
		return true;
	}

	public void setIsEnableKeyEvent(boolean b) {
		CCKeyDispatcher.sharedDispatcher().setDispatchEvents(b);
	}

	public boolean isEnableKeyEvent() {
		return CCKeyDispatcher.sharedDispatcher().getDispatchEvents();
	}

	private OrthographicCamera camera = null;

	public OrthographicCamera getCamera() {
		return camera;
	}

	private void onDirectorResize(int width, int height) {
		screenSize_.set(width, height);
		if (camera == null) {
			camera = new OrthographicCamera();
		}
		camera.setToOrtho(false, width, height);
		camera.update();

		if (runningCCScene_ != null) {
			runningCCScene_.onResize(width, height);
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		return onKeyDown(new CCKeyEvent(CCKeyEvent.ACTION_DOWN, keycode));
	}

	@Override
	public boolean keyUp(int keycode) {
		return onKeyDown(new CCKeyEvent(CCKeyEvent.ACTION_UP, keycode));
	}

	@Override
	public boolean keyTyped(char character) {
		return onKeyDown(new CCKeyEvent(CCKeyEvent.ACTION_TYPED, character));
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return queueMotionEvent(screenX, screenY, pointer, button, CCMotionEvent.ACTION_DOWN);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return queueMotionEvent(screenX, screenY, pointer, button, CCMotionEvent.ACTION_UP);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return queueMotionEvent(screenX, screenY, pointer, 0, CCMotionEvent.ACTION_MOVE);
	}

	private boolean queueMotionEvent(int screenX, int screenY, int pointer, int button, int action) {
		CCMotionEvent event = new CCMotionEvent(screenX, Gdx.graphics.getHeight() - screenY, pointer, button, action);
		if (!CCTouchDispatcher.sharedDispatcher().getDispatchEvents()) {
			return false;
		}
		CCTouchDispatcher.sharedDispatcher().queueMotionEvent(event);
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return queueMotionEvent(screenX, screenY, 0, 0, CCMotionEvent.ACTION_MOVE);
	}

	@Override
	public boolean scrolled(int amount) {
		return queueMotionEvent(amount, amount, 0, 0, CCMotionEvent.ACTION_SCROLL);
	}
}
