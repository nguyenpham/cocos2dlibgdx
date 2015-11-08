package org.cclgdx.sprites;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.cclgdx.config.ccMacros;
import org.cclgdx.nodes.CCTexture2D;
import org.cclgdx.nodes.CCTextureCache;
import org.cclgdx.types.CGPoint;
import org.cclgdx.types.CGRect;
import org.cclgdx.types.CGSize;
import org.cclgdx.utils.CacheMap;
import org.cclgdx.utils.GeometryUtil;
import org.cclgdx.utils.PlistParser;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

/**
 * Singleton that handles the loading of the sprite frames. It saves in a cache
 * the sprite frames.
 */

public class CCSpriteFrameCache extends CacheMap<CCSpriteFrame> {
	private static CCSpriteFrameCache sharedSpriteFrameCache_ = null;

	/** Returns this shared instance of the Sprite Frame cache */
	public static CCSpriteFrameCache sharedSpriteFrameCache() {
		if (sharedSpriteFrameCache_ == null) {
			sharedSpriteFrameCache_ = new CCSpriteFrameCache();
		}
		return sharedSpriteFrameCache_;
	}

	public CCSpriteFrame spriteFrameByName(String frameName) {
		return getSpriteFrame(frameName);
	}

	/**
	 * Purges the cache. It releases all the Sprite Frames and the retained
	 * instance.
	 */
	public static void purgeSharedSpriteFrameCache() {
		if (sharedSpriteFrameCache_ != null) {
			sharedSpriteFrameCache_.removeAllSpriteFrames();
			sharedSpriteFrameCache_ = null;
		}
	}

	/**
	 * Adds multiple Sprite Frames with a dictionary. The texture will be
	 * associated with the created sprite frames.
	 */
	protected Set<String> addSpriteFramesWithPlistData(HashMap<String, Object> dictionary, CCTexture2D texture) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> metadataDict = (HashMap<String, Object>) dictionary.get("metadata");
		@SuppressWarnings("unchecked")
		HashMap<String, Object> framesDict = (HashMap<String, Object>) dictionary.get("frames");

		int format = 0;

		// get the format
		if (metadataDict != null) {
			for (String key : metadataDict.keySet()) {
				Object obj = metadataDict.get(key);
			}
			format = (Integer) metadataDict.get("format");
		}
		// check the format
		if (!(format >= 0 && format <= 3)) {
			ccMacros.CCLOGERROR("CCSpriteFrameCache", "Unsupported Zwoptex plist file format.");
		}

		// add real frames
		for (Entry<String, Object> frameDictEntry : framesDict.entrySet()) {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> frameDict = (HashMap<String, Object>) frameDictEntry.getValue();
			CCSpriteFrame spriteFrame = null;
			if (format == 0) {
				float x = ((Number) frameDict.get("x")).floatValue();
				float y = ((Number) frameDict.get("y")).floatValue();
				float w = ((Number) frameDict.get("width")).floatValue();
				float h = ((Number) frameDict.get("height")).floatValue();
				float ox = ((Number) frameDict.get("offsetX")).floatValue();
				float oy = ((Number) frameDict.get("offsetY")).floatValue();

				int ow = 0;
				int oh = 0;
				// check ow/oh
				try {
					ow = ((Number) frameDict.get("originalWidth")).intValue();
					oh = ((Number) frameDict.get("originalHeight")).intValue();
				} catch (Exception e) {
					ccMacros.CCLOG("cocos2d", "WARNING: originalWidth/Height not found on the CCSpriteFrame. AnchorPoint won't work as expected. Regenerate the .plist");
				}

				// abs ow/oh
				ow = Math.abs(ow);
				oh = Math.abs(oh);
				// create frame

				spriteFrame = CCSpriteFrame.frame(texture, CGRect.make(x, y, w, h), CGPoint.make(ox, oy), CGSize.make(ow, oh), false, true);

			} else if (format == 1 || format == 2) {
				CGRect regionRect = GeometryUtil.CGRectFromString((String) frameDict.get("frame"));
				Boolean rotated = Boolean.FALSE;

				// rotation
				if (format == 2) {
					rotated = (Boolean) frameDict.get("rotated");

					if (rotated != null && rotated.booleanValue()) {
						float w = regionRect.width;
						regionRect.width = regionRect.height;
						regionRect.height = w;
					}
				}
				CGSize sourceSize = GeometryUtil.CGSizeFromString((String) frameDict.get("sourceSize"));
				CGRect sourceColorRect = GeometryUtil.CGRectFromString((String) frameDict.get("sourceColorRect"));
				CGPoint offset;
				if (rotated) {
					offset = CGPoint.make(sourceSize.width - sourceColorRect.x - sourceColorRect.width, sourceSize.height - sourceColorRect.y - sourceColorRect.height);
				} else {
					offset = CGPoint.make(sourceColorRect.x, sourceSize.height - sourceColorRect.y - sourceColorRect.height);
				}
				spriteFrame = CCSpriteFrame.frame(texture, regionRect, offset, sourceSize, rotated, true);
			} else if (format == 3) {
				// get values
				CGSize spriteSize = GeometryUtil.CGSizeFromString((String) frameDict.get("spriteSize"));
				CGPoint spriteOffset = GeometryUtil.CGPointFromString((String) frameDict.get("spriteOffset"));
				CGSize spriteSourceSize = GeometryUtil.CGSizeFromString((String) frameDict.get("spriteSourceSize"));
				CGRect textureRect = GeometryUtil.CGRectFromString((String) frameDict.get("textureRect"));
				Boolean rotated = (Boolean) frameDict.get("textureRotated");
				CGRect regionRect;
				if (rotated) {
					regionRect = CGRect.make(textureRect.x, textureRect.y, spriteSize.height, spriteSize.width);
				} else {
					regionRect = CGRect.make(textureRect.x, textureRect.y, spriteSize.width, spriteSize.height);
				}
				// Aliases are not supported in this version while.
				// // get aliases

				// create frame
				spriteFrame = CCSpriteFrame.frame(texture, regionRect, spriteOffset, spriteSourceSize, rotated, true);
			}

			// add sprite frame
			put(frameDictEntry.getKey(), spriteFrame);
		}
		return framesDict.keySet();
	}

	public Set<String> addSpriteFrames(String fileName) {
		return fileName.endsWith(".plist") ? addSpriteFramesWithPlistFile(fileName) : addSpriteFramesWithTextFile(fileName);
	}

	public Set<String> addSpriteFramesWithTextFile(String textFileName) {
		TextureAtlas textureAtlas = new TextureAtlas(textFileName);

		CCTexture2D texture = new CCTexture2D(textureAtlas.getTextures().first());
		CCTextureCache.sharedTextureCache().addTexture(texture, textFileName);

		Set<String> set = new HashSet<String>();
		for (int i = 0, n = textureAtlas.getRegions().size; i < n; i++) {
			AtlasRegion region = textureAtlas.getRegions().get(i);
			CCSpriteFrame spriteFrame = CCSpriteFrame.frame(texture, region, false);
			String name = region.name;
			// if (region.index >= 0) {
			// name += "_" + region.index;
			// }
			name += ".png";

			put(name, spriteFrame);
			set.add(region.name);
		}

		return set;
	}

	/**
	 * Adds multiple Sprite Frames from a plist file. A texture will be loaded
	 * automatically. The texture name will composed by replacing the .plist
	 * suffix with .png If you want to use another texture, you should use the
	 * addSpriteFramesWithPlistFile:texturePath method.
	 */
	public Set<String> addSpriteFramesWithPlistFile(String plistFileName) {
		String texturePath = null;
		int i = plistFileName.lastIndexOf('.');
		if (i > 0 && i <= plistFileName.length() - 2) {
			texturePath = plistFileName.substring(0, i) + ".png";
		}
		return addSpriteFramesWithPlistFile(plistFileName, texturePath);
	}

	public Set<String> addSpriteFramesWithPlistFile(String plistFileName, String texturePath) {
		CCTexture2D texture = CCTextureCache.sharedTextureCache().addImage(texturePath);
		return addSpriteFramesWithPlistFile(plistFileName, texture);
	}

	/**
	 * Adds multiple Sprite Frames from a plist file. The texture will be
	 * associated with the created sprite frames.
	 */
	public Set<String> addSpriteFramesWithPlistFile(String plist, CCTexture2D texture) {
		HashMap<String, Object> dict = PlistParser.parse(plist);
		return addSpriteFramesWithPlistData(dict, texture);
	}

	/**
	 * Adds an sprite frame with a given name. If the name already exists, then
	 * the contents of the old name will be replaced with the new one.
	 */
	public void addSpriteFrame(CCSpriteFrame frame, String frameName) {
		put(frameName, frame);
	}

	/**
	 * Purges the dictionary of loaded sprite frames. Call this method if you
	 * receive the "Memory Warning". In the short term: it will free some
	 * resources preventing your app from being killed. In the medium term: it
	 * will allocate more resources. In the long term: it will be the same.
	 */
	public void removeSpriteFrames() {
		clear();
	}

	/**
	 * Removes unused sprite frames. Sprite Frames that have a retain count of 1
	 * will be deleted. It is convenient to call this method after when starting
	 * a new Scene.
	 */
	public void removeAllSpriteFrames() {
		clear();
	}

	/**
	 * Deletes an sprite frame from the sprite frame cache.
	 */
	public void removeSpriteFrame(String name) {
		remove(name);
	}

	/**
	 * Returns an Sprite Frame that was previously added. If the name is not
	 * found it will return nil. You should retain the returned copy if you are
	 * going to use it.
	 */
	public CCSpriteFrame getSpriteFrame(String name) {
		CCSpriteFrame frame = get(name);

		if (frame == null) {
			ccMacros.CCLOG("CCSpriteFrameCache", "Frame not found: " + name);
		}

		return frame;
	}

	@Override
	public CCSpriteFrame create(String key) {
		return null;
	}

}
