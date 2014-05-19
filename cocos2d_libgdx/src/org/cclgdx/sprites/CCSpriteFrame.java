package org.cclgdx.sprites;

import org.cclgdx.nodes.CCTexture2D;
import org.cclgdx.types.CGPoint;
import org.cclgdx.types.CGRect;
import org.cclgdx.types.CGSize;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;

/**
 * A CCSpriteFrame has: - texture: A CCTexture2D that will be used by the
 * CCSprite - rectangle: A rectangle of the texture
 * 
 * 
 * You can modify the frame of a CCSprite by doing:
 * 
 * CCSpriteFrame *frame = [CCSpriteFrame frameWithTexture:texture rect:rect
 * offset:offset]; [sprite setDisplayFrame:frame];
 */
public class CCSpriteFrame implements Disposable {

	private AtlasRegion region;

	/*
	 * images of Cocos2d SpriteSheet (plist files) may be rotated by closewise,
	 * when images of libgdx spritesheet may be rotated by anticlosewise
	 */
	private boolean cocos2dSpriteSheet;

	public boolean isCocos2dSpriteSheet() {
		return cocos2dSpriteSheet;
	}

	public AtlasRegion getRegion() {
		return region;
	}

	/** rect of the frame */
	public CGRect getRegionRect() {
		return CGRect.make(region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight());
	}

	/** original size of the frame */
	public CGSize getOriginalSize() {
		return CGSize.make(region.originalWidth, region.originalHeight);
	}

	/** offset of the frame */
	public CGPoint getOffset() {
		return CGPoint.make(region.offsetX, region.offsetY);
	}

	/** texture of the frame */
	private CCTexture2D texture_;

	public CCTexture2D getTexture() {
		return texture_;
	}

	public boolean getRotated() {
		return region.rotate;
	}

	/**
	 * Create a CCSpriteFrame with a texture, rect, offset and originalSize. The
	 * originalSize is the size in pixels of the frame before being trimmed.
	 */
	public static CCSpriteFrame frame(CCTexture2D texture, CGRect regionRect, CGPoint offset, CGSize originalSize) {
		return new CCSpriteFrame(texture, regionRect, offset, originalSize, false, false);
	}

	/**
	 * Create a CCSpriteFrame with a texture, rect, offset, originalSize and
	 * rotated. The originalSize is the size in pixels of the frame before being
	 * trimmed.
	 */
	public static CCSpriteFrame frame(CCTexture2D texture, CGRect regionRect, CGPoint offset, CGSize originalSize, boolean rotated, boolean cocos2dSpriteSheet) {
		return new CCSpriteFrame(texture, regionRect, offset, originalSize, rotated, cocos2dSpriteSheet);
	}

	public static CCSpriteFrame frame(CCTexture2D texture, AtlasRegion region, Boolean cocos2dSpriteSheet) {
		return new CCSpriteFrame(texture, region, cocos2dSpriteSheet);
	}

	/**
	 * Initializes a CCSpriteFrame with a texture, rect, offset and
	 * originalSize. The originalSize is the size in pixels of the frame before
	 * being trimmed.
	 */
	protected CCSpriteFrame(CCTexture2D texture, CGRect regionRect, CGPoint offset, CGSize originalSize, boolean rotated, boolean cocos2dSpriteSheet) {
		AtlasRegion region = new AtlasRegion(texture.getTexture(), (int) regionRect.x, (int) regionRect.y, (int) regionRect.width, (int) regionRect.height);
		region.offsetX = offset.x;
		region.offsetY = offset.y;
		region.packedWidth = (int) regionRect.width;
		region.packedHeight = (int) regionRect.height;
		region.originalWidth = (int) originalSize.width;
		region.originalHeight = (int) originalSize.height;
		region.rotate = rotated;

		init(texture, region, cocos2dSpriteSheet);
	}

	protected CCSpriteFrame(CCTexture2D texture, AtlasRegion region, boolean cocos2dSpriteSheet) {
		init(texture, region, cocos2dSpriteSheet);
	}

	private void init(CCTexture2D texture, AtlasRegion region, boolean cocos2dSpriteSheet) {
		texture_ = texture;
		this.cocos2dSpriteSheet = cocos2dSpriteSheet;
		this.region = region;
	}

	public CCSpriteFrame copy() {
		CCSpriteFrame copy = new CCSpriteFrame(texture_, region, cocos2dSpriteSheet);
		return copy;
	}

	@Override
	public String toString() {
		return regionToString(region);
	}

	public static String regionToString(AtlasRegion region) {
		String str = "<ofs=" + region.offsetX + "," + region.offsetY + ", region=" + region.getRegionX() + "," + region.getRegionY() + "," + region.getRegionWidth() + "," + region.getRegionHeight()
				+ ", orgSz=" + region.originalWidth + "," + region.originalHeight + ",rotated=" + region.rotate + ">";
		return str;

	}

	@Override
	public void dispose() {
	}
}
