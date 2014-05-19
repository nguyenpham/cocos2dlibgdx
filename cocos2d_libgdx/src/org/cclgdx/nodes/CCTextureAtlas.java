package org.cclgdx.nodes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * A class that implements a Texture Atlas. Supported features: - The atlas file
 * can be a PVRTC, PNG or any other fomrat supported by Texture2D - Quads can be
 * udpated in runtime - Quads can be added in runtime - Quads can be removed in
 * runtime - Quads can be re-ordered in runtime - The TextureAtlas capacity can
 * be increased or decreased in runtime - OpenGL component: V3F, C4B, T2F. The
 * quads are rendered using an OpenGL ES VBO. To render the quads using an
 * interleaved vertex array list, you should modify the ccConfig.h file
 */

public class CCTextureAtlas {
	/** Texture of the texture atlas */
	private TextureAtlas textureAtlas;

	public TextureAtlas getTextureAtlas() {
		return textureAtlas;
	}

	public TextureAtlas getTexture() {
		return textureAtlas;
	}

	public void setTexture(TextureAtlas textureAtlas) {
		this.textureAtlas = textureAtlas;
	}

	public void setTexture(String name, CCTexture2D tex) {
		Texture texture = tex.getTexture();
		textureAtlas = new TextureAtlas();
		textureAtlas.addRegion(name, texture, 0, 0, texture.getWidth(), texture.getHeight());
	}

	/**
	 * creates a TextureAtlas with an filename
	 */
	public static CCTextureAtlas textureAtlas(String fileName) {
		return new CCTextureAtlas(fileName);
	}

	/**
	 * initializes a TextureAtlas with a filename
	 */
	public CCTextureAtlas(String fileName) {
		this(CCTextureCache.sharedTextureCache().addImage(fileName));
	}

	/**
	 * creates a TextureAtlas with a previously initialized Texture2D object
	 */
	public static CCTextureAtlas textureAtlas(CCTexture2D tex) {
		return new CCTextureAtlas(tex);
	}

	/**
	 * initializes a TextureAtlas with a previously initialized Texture2D object
	 */
	public CCTextureAtlas(CCTexture2D tex) {
		textureAtlas = new TextureAtlas();
		textureAtlas.addRegion("" + tex.hashCode(), tex.texture, 0, 0, tex.texture.getWidth(), tex.texture.getHeight());
	}
}
