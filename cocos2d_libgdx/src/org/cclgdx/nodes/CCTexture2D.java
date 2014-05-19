package org.cclgdx.nodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

/**
 * CCTexture2D class. This class allows easy creation of OpenGL 2D textures from
 * images, text or raw data. It is a wrap of class Texture
 */

public class CCTexture2D implements Disposable {
	private static final String LOG_TAG = CCTexture2D.class.getSimpleName();

	protected Texture texture;

	public CCTexture2D(String internalPath) {
		this(Gdx.files.internal(internalPath));
	}

	public CCTexture2D(FileHandle file) {
		this(file, null, false);
	}

	public CCTexture2D(FileHandle file, boolean useMipMaps) {
		this(file, null, useMipMaps);
	}

	public CCTexture2D(FileHandle file, Format format, boolean useMipMaps) {
		texture = new Texture(file, format, useMipMaps);
	}

	public CCTexture2D(Pixmap pixmap) {
		texture = new Texture(pixmap);
	}

	public CCTexture2D(Texture tex) {
		texture = tex;
	}

	public Texture getTexture() {
		return texture;
	}

	public int getWidth() {
		return texture.getWidth();
	}

	public int getHeight() {
		return texture.getHeight();
	}

	@Override
	public void dispose() {
		getTexture().dispose();
	}

}
