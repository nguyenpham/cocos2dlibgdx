package org.cclgdx.layers;

import org.cclgdx.director.CCDirector;
import org.cclgdx.nodes.CCTransformValue;
import org.cclgdx.types.CGSize;
import org.cclgdx.types.ccColor4B;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

//
// CCColorLayer
//
public class CCColorLayer extends CCLayer {
	/** creates a CCLayer with color */
	public static CCColorLayer node(ccColor4B color) {
		CGSize size = CCDirector.sharedDirector().winSizeRef();
		return new CCColorLayer(color, size.width, size.height);
	}

	/** creates a CCLayer with color, width and height */
	public static CCColorLayer node(ccColor4B color, float width, float height) {
		return new CCColorLayer(color, width, height);
	}

	/** initializes a CCLayer with color */
	protected CCColorLayer(ccColor4B color) {
		CGSize s = CCDirector.sharedDirector().winSizeRef();
		init(color, s.width, s.height);
	}

	/** initializes a CCLayer with color, width and height */
	protected CCColorLayer(ccColor4B color, float width, float height) {
		init(color, width, height);
	}

	protected void init(ccColor4B color, float width, float height) {
		setContentSize(width, height);
		setColor(color);
	}

	@Override
	public void setColor(ccColor4B color) {
		super.setColor(color);

		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
		pixmap.setColor(color.r, color.g, color.b, color.a);
		pixmap.fill();
		texture = new Texture(pixmap);
		// It's the textures responsibility now... get rid of the pixmap
		pixmap.dispose();
	}

	private Texture texture = null;

	@Override
	public void draw(float dt) {
		CCTransformValue abTransformValue = getScreenTransformValue();
		renderSpriteBatch.draw(texture, abTransformValue.getX(), abTransformValue.getY(), getBoundingBox().width, getBoundingBox().height);
	}
}
