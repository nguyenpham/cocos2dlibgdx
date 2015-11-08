package org.cclgdx.nodes;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Singleton that handles the loading of textures Once the texture is loaded,
 * the next time it will return a reference of the previously loaded texture
 * reducing GPU & CPU memory
 */
public class CCTextureCache {
	private HashMap<String, WeakReference<CCTexture2D>> textures;

	private static CCTextureCache _sharedTextureCache;

	/** Retruns ths shared instance of the cache */
	public static CCTextureCache sharedTextureCache() {
		synchronized (CCTextureCache.class) {
			if (_sharedTextureCache == null) {
				_sharedTextureCache = new CCTextureCache();
			}
			return _sharedTextureCache;
		}
	}

	/**
	 * purges the cache. It releases the retained instance.
	 */
	public static void purgeSharedTextureCache() {
		if (_sharedTextureCache != null) {
			_sharedTextureCache.removeAllTextures();
		}
	}

	private CCTextureCache() {
		assert _sharedTextureCache == null : "Attempted to allocate a second instance of a singleton.";

		synchronized (CCTextureCache.class) {
			textures = new HashMap<String, WeakReference<CCTexture2D>>(10);
		}
	}

	/**
	 * Returns a Texture2D object given an file image If the file image was not
	 * previously loaded, it will create a new CCTexture2D object and it will
	 * return it. It will use the filename as a key. Otherwise it will return a
	 * reference of a previosly loaded image. Supported image extensions: .png,
	 * .bmp, .tiff, .jpeg, .pvr, .gif
	 */
	public CCTexture2D addImage(String path) {
		assert path != null : "TextureMgr: path must not be null";

		WeakReference<CCTexture2D> texSR = textures.get(path);
		CCTexture2D tex = null;
		if (texSR != null) {
			tex = texSR.get();
		}

		if (tex == null) {
			tex = new CCTexture2D(path);
			addTexture(tex, path);
		}
		return tex;
	}

	/**
	 * Returns a Texture2D object given an file image from external path.
	 */
	public CCTexture2D addImageExternal(String path) {
		return addImage(path);
	}

	/**
	 * Purges the dictionary of loaded textures. Call this method if you receive
	 * the "Memory Warning" In the short term: it will free some resources
	 * preventing your app from being killed In the medium term: it will
	 * allocate more resources In the long term: it will be the same
	 */
	public void removeAllTextures() {
		/* Do nothing, or do all. */
		for (WeakReference<CCTexture2D> texSR : textures.values()) {
			CCTexture2D tex = texSR.get();
			if (tex != null) {
				// tex.releaseTexture(CCDirector.gl);
				tex.dispose();
			}
		}
		textures.clear();
	}

	// /**
	// * Removes unused textures Textures that have a retain count of 1 will be
	// * deleted It is convenient to call this method after when starting a new
	// * Scene
	// */
	// public void removeUnusedTextures() {
	// /*
	// * NSArray *keys = [textures allKeys]; for( id key in keys ) { id value
	// * = [textures objectForKey:key]; if( [value retainCount] == 1 ) {
	// * CCLOG(@"cocos2d: CCTextureCache: removing unused texture: %@", key);
	// * [textures removeObjectForKey:key]; } }
	// */
	// }

	/**
	 * Deletes a texture from the cache given a texture
	 */
	public void removeTexture(CCTexture2D tex) {
		if (tex == null) {
			return;
		}

		textures.values().remove(tex);
	}

	/*
	 * Add a texture to the cache so it gets managed
	 */
	public void addTexture(CCTexture2D tex) {
		if (tex == null) {
			return;
		}
		textures.put(String.valueOf(tex.hashCode()), new WeakReference<CCTexture2D>(tex));
	}

	public void addTexture(CCTexture2D tex, String name) {
		if (tex == null) {
			return;
		}
		textures.put(name, new WeakReference<CCTexture2D>(tex));
	}

	/**
	 * Deletes a texture from the cache given a its key name
	 */
	public void removeTexture(String textureKeyName) {
		if (textureKeyName == null)
			return;
		textures.remove(textureKeyName);
	}
}
