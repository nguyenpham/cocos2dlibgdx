package org.cclgdx.actions.interval;

import java.util.ArrayList;

import org.cclgdx.nodes.CCTexture2D;
import org.cclgdx.nodes.CCTextureCache;
import org.cclgdx.sprites.CCSpriteFrame;
import org.cclgdx.types.CGPoint;
import org.cclgdx.types.CGRect;
import org.cclgdx.types.CGSize;

/** an Animation object used within Sprites to perform animations */
public class CCAnimation {

	/** name of the animation */
	private final String name_;
	/** delay between frames in seconds. */
	private final float delay_;

	// /** array of frames */
	private final ArrayList<CCSpriteFrame> frames_;

	public String name() {
		return name_;
	}

	public float delay() {
		return delay_;
	}

	public ArrayList<CCSpriteFrame> frames() {
		return frames_;
	}

	/**
	 * Creates a CCAnimation with a name
	 */
	public static CCAnimation animation(String name) {
		return new CCAnimation(name);
	}

	/**
	 * Creates a CCAnimation with a name and frames
	 */
	public static CCAnimation animation(String name, ArrayList<CCSpriteFrame> frames) {
		return new CCAnimation(name, frames);
	}

	/** Creates a CCAnimation with a name and delay between frames. */
	public static CCAnimation animation(String name, float delay) {
		return new CCAnimation(name, delay);
	}

	/**
	 * Creates a CCAnimation with a name, delay and an array of CCSpriteFrames.
	 */
	public static CCAnimation animation(String name, float delay, ArrayList<CCSpriteFrame> frames) {
		return new CCAnimation(name, delay, frames);
	}

	/**
	 * Initializes a CCAnimation with a name
	 */
	protected CCAnimation(String name) {
		this(name, (ArrayList<CCSpriteFrame>) null);
	}

	/**
	 * Initializes a CCAnimation with a name and frames
	 */
	protected CCAnimation(String name, ArrayList<CCSpriteFrame> frames) {
		this(name, 0, frames);
	}

	/** Initializes a CCAnimation with a name and delay between frames. */
	protected CCAnimation(String name, float delay) {
		this(name, delay, (ArrayList<CCSpriteFrame>) null);
	}

	/**
	 * Initializes a CCAnimation with a name, delay and an array of
	 * CCSpriteFrames.
	 */
	protected CCAnimation(String name, float delay, ArrayList<CCSpriteFrame> frames) {
		delay_ = delay;
		name_ = name;
		frames_ = new ArrayList<CCSpriteFrame>();
		if (frames != null) {
			frames_.addAll(frames);
		}
	}

	/**
	 * Adds a frame with an image filename. Internally it will create a
	 * CCSpriteFrame and it will add it
	 */
	public void addFrame(String filename) {
		CCTexture2D tex = CCTextureCache.sharedTextureCache().addImage(filename);
		CGRect rect = CGRect.make(0, 0, tex.getWidth(), tex.getHeight());
		CCSpriteFrame frame = CCSpriteFrame.frame(tex, rect, CGPoint.zero(), CGSize.zero());
		frames_.add(frame);
	}

	public void addFrame(CCTexture2D tex) {
		CGRect rect = CGRect.make(0, 0, tex.getWidth(), tex.getHeight());
		CCSpriteFrame frame = CCSpriteFrame.frame(tex, rect, CGPoint.zero(), CGSize.zero());
		frames_.add(frame);
	}

	/** Adds a frame to a CCAnimation. */
	public void addFrame(CCSpriteFrame frame) {
		frames_.add(frame);
	}

	public CCAnimation(String n, float d, CCTexture2D... textures) {
		name_ = n;
		frames_ = new ArrayList<CCSpriteFrame>();
		delay_ = d;

		if (textures != null) {
			for (CCTexture2D tex : textures) {
				addFrame(tex);
			}
		}
	}

	/**
	 * Adds a frame with a texture and a rect. Internally it will create a
	 * CCSpriteFrame and it will add it
	 */
	public void addFrame(CCTexture2D tex, CGRect rect) {
		CCSpriteFrame frame = CCSpriteFrame.frame(tex, rect, CGPoint.zero(), CGSize.zero());
		frames_.add(frame);
	}

}
