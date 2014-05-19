package org.cclgdx.text;

import org.cclgdx.utils.CacheMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class CCFontCache extends CacheMap<BitmapFont> {
	private static CCFontCache sharedFontCache_ = null;

	private static final String DEFAULT_BITMAPFONT_KEY = "__Default_BitmapFont_Key__";

	/** Return the shared instance of the font cache */
	public static CCFontCache sharedFontCache() {
		if (sharedFontCache_ == null) {
			sharedFontCache_ = new CCFontCache();
		}
		return sharedFontCache_;
	}

	@Override
	public BitmapFont get(String pathFntName) {
		return super.get(pathFntName == null || pathFntName.isEmpty() ? DEFAULT_BITMAPFONT_KEY : pathFntName);
	}

	private static final boolean flipFont = false;

	@Override
	public BitmapFont create(String pathFntName) {
		BitmapFont bitmapFont;
		if (pathFntName.equals(DEFAULT_BITMAPFONT_KEY)) {
			bitmapFont = new BitmapFont(flipFont);
		} else {
			String pathPngName = pathFntName.replaceAll(".fnt", ".png");
			bitmapFont = new BitmapFont(Gdx.files.internal(pathFntName), Gdx.files.internal(pathPngName), flipFont);
		}
		return bitmapFont;
	}

}
