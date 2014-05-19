package org.cclgdx.sprites;

import java.util.HashMap;

import org.cclgdx.actions.interval.CCAnimation;
import org.cclgdx.director.CCDirector;
import org.cclgdx.nodes.CCNode;
import org.cclgdx.nodes.CCTexture2D;
import org.cclgdx.nodes.CCTransformValue;
import org.cclgdx.types.CGRect;
import org.cclgdx.types.ccColor3B;
import org.cclgdx.types.ccColor4B;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * CCSprite is a 2d image
 * 
 * CCSprite can be created with an image, or with a sub-rectangle of an image.
 * 
 */
public class CCSprite extends CCNode {

	// Animations that belong to the sprite
	private HashMap<String, CCAnimation> animations_ = null;

	protected AtlasSprite sprite;
	protected AtlasRegion region;

	/**
	 * Creates an sprite with a texture. The rect used will be the size of the
	 * texture. The offset will be (0,0).
	 */
	public static CCSprite sprite(CCTexture2D texture) {
		return new CCSprite(texture);
	}

	/**
	 * Creates an sprite with a texture and a rect. The offset will be (0,0).
	 */
	public static CCSprite sprite(CCTexture2D texture, CGRect rect) {
		return new CCSprite(texture, rect);
	}

	/**
	 * Creates an sprite with an sprite frame.
	 */
	public static CCSprite sprite(CCSpriteFrame spriteFrame) {
		return new CCSprite(spriteFrame);
	}

	/**
	 * Creates an sprite with an sprite frame name. An CCSpriteFrame will be
	 * fetched from the CCSpriteFrameCache by name. If the CCSpriteFrame doesn't
	 * exist it will raise an exception.
	 */
	public static CCSprite spriteWithFrameName(String spriteFrameName) {
		return new CCSprite(spriteFrameName, true);
	}

	/**
	 * Creates an sprite with an image filepath and a rect.
	 */
	public static CCSprite sprite(String filepath, CGRect rect) {
		return new CCSprite(filepath, rect);
	}

	/**
	 * Creates an sprite with an image filepath. The rect used will be the size
	 * of the image. The offset will be (0,0).
	 */
	public static CCSprite sprite(String filepath) {
		return new CCSprite(filepath);
	}

	public static CCSprite spriteWithFileName(String filepath) {
		return new CCSprite(filepath);
	}

	/**
	 * Initializes an sprite with libGdx TextureAtlas and region name
	 */
	public CCSprite(TextureAtlas spriteSheet, String name) {
		for (int i = 0, n = spriteSheet.getRegions().size; i < n; i++) {
			if (spriteSheet.getRegions().get(i).name.equals(name)) {
				AtlasRegion region = spriteSheet.getRegions().get(i);
				createInternalSprite(region, false);
				break;
			}
		}
	}

	/**
	 * Initializes an sprite with an image filepath. The rect used will be the
	 * size of the image. The offset will be (0,0).
	 */

	public CCSprite(String filepath) {
		assert filepath != null : "Invalid filename for sprite";
		Texture texture = new Texture(Gdx.files.internal(filepath));
		AtlasRegion region = new AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
		createInternalSprite(region, false);
	}

	public CCSprite(String filepath, CGRect rect) {
		assert filepath != null : "Invalid filename for sprite";
		Texture texture = new Texture(Gdx.files.internal(filepath));
		AtlasRegion region = new AtlasRegion(texture, (int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
		createInternalSprite(region, false);
	}

	protected void createInternalSprite(AtlasRegion region, boolean cocos2dSpriteSheet) {
		this.region = region;

		sprite = new AtlasSprite(region);
		setContentSize(region.originalWidth, region.originalHeight);
		sprite.setOrigin(0, 0);
		if (region.rotate && cocos2dSpriteSheet) {
			/*
			 * The image of cocos2d spritesheet may be turned unclockwise, we
			 * need rotate it to 180 degree. However to avoid the dilemma of
			 * setting correct origin values before rotating, we flip it twice
			 */
			sprite.setFlip(true, true);
		}

		oFlipX = false;
		oFlipY = false;
		rotated = 0;
		setSpiteColor();
	}

	@Override
	public void setColor(ccColor3B color3) {
		super.setColor(color3);
		setSpiteColor();
	}

	private void setSpiteColor() {
		sprite.setColor(color_.r / 255f, color_.g / 255f, color_.b / 255f, opacity / 255f);
	}

	/**
	 * Initializes an sprite with a texture. The rect used will be the size of
	 * the texture. The offset will be (0,0).
	 */
	public CCSprite(CCTexture2D texture) {
		this(texture, CGRect.make(0, 0, texture.getWidth(), texture.getHeight()));
	}

	public CCSprite(CCTexture2D texture, CGRect rect) {
		AtlasRegion region = new AtlasRegion(texture.getTexture(), (int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
		createInternalSprite(region, false);
	}

	/**
	 * Initializes an sprite with an sprite frame.
	 */
	public CCSprite(CCSpriteFrame spriteFrame) {
		init(spriteFrame);
	}

	protected void init(CCSpriteFrame spriteFrame) {
		assert spriteFrame != null : "Invalid spriteFrame for sprite";
		setDisplayFrame(spriteFrame);
	}

	/**
	 * Initializes an sprite with an sprite frame name. An CCSpriteFrame will be
	 * fetched from the CCSpriteFrameCache by name. If the CCSpriteFrame doesn't
	 * exist it will raise an exception.
	 */
	protected CCSprite(String spriteFrameName, boolean isFrame) {
		assert spriteFrameName != null : "Invalid spriteFrameName for sprite";
		CCSpriteFrame frame = CCSpriteFrameCache.sharedSpriteFrameCache().getSpriteFrame(spriteFrameName);
		init(frame);
	}

	public CCSprite() {
	}

	private CCSpriteFrame frame;

	/** sets a new display frame to the CCSprite. */
	public void setDisplayFrame(CCSpriteFrame frame) {
		if (frame == null) {
			sprite = null;
			return;
		}
		this.frame = frame;
		createInternalSprite(frame.getRegion(), frame.isCocos2dSpriteSheet());
	}

	/** changes the display frame based on an animation and an index. */
	public void setDisplayFrame(String animationName, int frameIndex) {
		if (animations_ == null) {
			initAnimationDictionary();
		}

		CCAnimation anim = animations_.get(animationName);
		CCSpriteFrame frame = anim.frames().get(frameIndex);
		setDisplayFrame(frame);
	}

	/** returns whether or not a CCSpriteFrame is being displayed */
	public boolean isFrameDisplayed(CCSpriteFrame frame) {
		if (frame == null || this.frame == null) {
			return false;
		}
		return this.frame == frame;
	}

	/** returns the current displayed frame. */
	public CCSpriteFrame displayedFrame() {
		return frame;
		// return CCSpriteFrame.frame(getTexture(), rect_, CGPoint.zero());
	}

	/** adds an Animation to the Sprite. */
	public void addAnimation(CCAnimation anim) {
		if (animations_ == null) {
			initAnimationDictionary();
		}

		animations_.put(anim.name(), anim);
	}

	/** returns an Animation given it's name. */
	public CCAnimation animationByName(String animationName) {
		assert animationName != null : "animationName parameter must be non null";
		return animations_.get(animationName);
	}

	private void initAnimationDictionary() {
		animations_ = new HashMap<String, CCAnimation>();
	}

	public void disposeTexture() {
		if (region != null && region.getTexture() != null) {
			region.getTexture().dispose();
			region = null;
		}
	}

	private boolean oFlipX = false, oFlipY = false;
	private float rotated;

	/*
	 * ShapreRenderer used to display color background when debugging bounding
	 * box
	 */
	private ShapeRenderer shapeRenderer;

	@Override
	public void draw(float delta) {
		if (sprite == null) {
			return;
		}

		CCTransformValue abTransformValue = getScreenTransformValue();

		if (debugBoundingBoxMode) {
			drawDebugBoundingBox();
		}

		if (sprite.getOriginX() != originX_ || sprite.getOriginY() != originY_) {
			sprite.setOrigin(originX_, originY_);
		}
		if (abTransformValue.getScaleX() != sprite.getScaleX() || abTransformValue.getScaleY() != sprite.getScaleY()) {
			sprite.setScale(abTransformValue.getScaleX(), abTransformValue.getScaleY());
		}

		float x = abTransformValue.getX() - originX_;
		float y = abTransformValue.getY() - originY_;
		if (x != sprite.getX() || y != sprite.getY()) {
			sprite.setPosition(x, y);
		}

		if (abTransformValue.getRotation() != rotated) {
			rotated = abTransformValue.getRotation();
			sprite.setRotation(abTransformValue.getRotation());
		}

		if (oFlipX != abTransformValue.isFlipX() || oFlipY != abTransformValue.isFlipY()) {
			if (region.rotate) {
				sprite.flip(oFlipY != abTransformValue.isFlipY(), oFlipX != abTransformValue.isFlipX());
			} else {
				sprite.flip(oFlipX != abTransformValue.isFlipX(), oFlipY != abTransformValue.isFlipY());
			}
			oFlipX = abTransformValue.isFlipX();
			oFlipY = abTransformValue.isFlipY();
		}

		if (abTransformValue.getOpacity() != 255) {
			sprite.draw(renderSpriteBatch, abTransformValue.getOpacity() / 255.0f);
		} else {
			sprite.draw(renderSpriteBatch);
		}
	}

	/*
	 * Display color background in debug bounding box mode
	 */
	private boolean debugBoundingBoxMode = false;
	private ccColor4B debugBoundingBoxColor = ccColor4B.ccc4(200, 0, 0, 128);

	public void showDebugBoundingBox(boolean showMode) {
		debugBoundingBoxMode = showMode;
		if (!showMode && shapeRenderer != null) {
			shapeRenderer.dispose();
			shapeRenderer = null;
		}
	}

	public void setDebugBoundingBoxColor(ccColor4B color) {
		debugBoundingBoxColor = color;
	}

	private void drawDebugBoundingBox() {
		renderSpriteBatch.end();

		if (shapeRenderer == null) {
			shapeRenderer = new ShapeRenderer();
			shapeRenderer.setProjectionMatrix(CCDirector.sharedDirector().getCamera().combined);
		}

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(debugBoundingBoxColor.r / 255f, debugBoundingBoxColor.g / 255f, debugBoundingBoxColor.b / 255f, debugBoundingBoxColor.a / 255f);
		shapeRenderer.rect(screenBoundingBox_.x, screenBoundingBox_.y, screenBoundingBox_.width, screenBoundingBox_.height);
		shapeRenderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);

		renderSpriteBatch.begin();
	}

	public AtlasRegion getRegion() {
		return region;
	}
}
