package org.cclgdx.text;

import java.util.ArrayList;

import org.cclgdx.nodes.CCTexture2D;
import org.cclgdx.nodes.CCTextureCache;
import org.cclgdx.sprites.CCSprite;
import org.cclgdx.types.CGSize;
import org.cclgdx.types.ccColor3B;
import org.cclgdx.types.ccColor4B;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * CCLabel
 */
public class CCLabel extends CCSprite {

	public enum TextAlignment {
		LEFT, CENTER, RIGHT
	}

	public enum TextVerticalAlignment {
		TOP, CENTER, BOTTOM
	}

	private static final int DEFAULT_PADDING_W = 2;
	private static final int DEFAULT_PADDING_H = 2;

	// private CGSize _dimensions;
	private TextAlignment _halignment;
	private TextVerticalAlignment _valignment;
	private String _fontName;
	private ccColor3B color = ccColor3B.ccWHITE;
	private ccColor4B _backgroundColor;
	private CharSequence _string;
	private int paddingWidth = DEFAULT_PADDING_W;
	private int paddingHeight = DEFAULT_PADDING_H;
	private boolean autoSetSize;
	private boolean needCreateInternalSprite;
	private static GlyphLayout textBounds = new GlyphLayout();

	private static String defaultFontPath = null;

	public static void setDefaultFontPath(String defaultFontPath_) {
		defaultFontPath = defaultFontPath_;
	}

	/**
	 * creates a CCLabel from a fontName, alignments, dimension and background
	 * color
	 */
	public static CCLabel makeLabel(String string, CGSize dimensions, String fontPath) {
		return new CCLabel(string, dimensions, TextAlignment.CENTER, TextVerticalAlignment.CENTER, fontPath, ccColor4B.ccc4(0, 0, 0, 0));
	}

	public static CCLabel makeLabel(String string, CGSize dimensions, TextAlignment alignment, String fontPath) {
		return new CCLabel(string, dimensions, alignment, TextVerticalAlignment.CENTER, fontPath, ccColor4B.ccc4(0, 0, 0, 0));
	}

	public static CCLabel makeLabel(String string, CGSize dimensions, TextAlignment alignment, String fontPath, ccColor4B backgroundColor) {
		return new CCLabel(string, dimensions, alignment, TextVerticalAlignment.CENTER, fontPath, backgroundColor);
	}

	public static CCLabel makeLabel(String string, CGSize dimensions, TextAlignment alignment, TextVerticalAlignment valignment, String fontPath) {
		return new CCLabel(string, dimensions, alignment, valignment, fontPath, ccColor4B.ccc4(0, 0, 0, 0));
	}

	public static CCLabel makeLabel(String string, CGSize dimensions, TextAlignment alignment, TextVerticalAlignment valignment, String fontPath, ccColor4B backgroundColor) {
		return new CCLabel(string, dimensions, alignment, valignment, fontPath, backgroundColor);
	}

	public static CCLabel makeLabel(String string, String fontPath) {
		return new CCLabel(string, fontPath);
	}

	/*
	 * Use default font
	 */
	public static CCLabel makeLabel(String string) {
		return new CCLabel(string, null);
	}

	/**
	 * initializes the CCLabel with a font name, alignment, dimension and font
	 * size
	 */
	protected CCLabel(CharSequence string, final CGSize dimensions, TextAlignment alignment, TextVerticalAlignment valignment, String fontPath, ccColor4B backgroundColor) {
		super();
		addToMaintenanceList();

		setContentSize(dimensions);
		this._halignment = alignment;
		this._valignment = valignment;
		this._fontName = fontPath;
		this._backgroundColor = backgroundColor;

		autoSetSize = false;

		setString(string);
	}

	/**
	 * initializes the CCLabel with a font name, alignment, dimension and font
	 * size
	 */
	protected CCLabel(CharSequence string, String fontPath) {
		super();
		addToMaintenanceList();
		this._halignment = TextAlignment.CENTER;
		this._valignment = TextVerticalAlignment.CENTER;
		this._fontName = fontPath;
		this._backgroundColor = ccColor4B.ccCLEAR;

		autoSetSize = true;
		setString(string);
	}

	/*
	 * paddingWidth, paddingHeight used when dimensions is not set
	 */
	public int getPaddingWidth() {
		return paddingWidth;
	}

	public void setPaddingWidth(int paddingWidth) {
		this.paddingWidth = paddingWidth;

		if (autoSetSize) {
			calculateSize();
			needCreateInternalSprite = true;
		}
	}

	public int getPaddingHeight() {
		return paddingHeight;
	}

	public void setPaddingHeight(int paddingHeight) {
		this.paddingHeight = paddingHeight;
		if (autoSetSize) {
			calculateSize();
			needCreateInternalSprite = true;
		}
	}

	public void setBackgoundColor(ccColor4B backgroundColor) {
		_backgroundColor = backgroundColor;
		needCreateInternalSprite = true;
	}

	@Override
	public void setContentSize(CGSize dimensions) {
		super.setContentSize(dimensions);
		autoSetSize = false;
		needCreateInternalSprite = true;
	}

	public void setAlignment(TextAlignment alignment) {
		this._halignment = alignment;
		needCreateInternalSprite = true;
	}

	public void setAlignment(TextAlignment alignment, TextVerticalAlignment verticalAlignment) {
		this._halignment = alignment;
		this._valignment = verticalAlignment;
		needCreateInternalSprite = true;
	}

	public void setVerticalAlignment(TextVerticalAlignment verticalAlignment) {
		this._valignment = verticalAlignment;
		needCreateInternalSprite = true;
	}

	public void setFontName(String fontPath) {
		this._fontName = fontPath;
		needCreateInternalSprite = true;
	}

	public void setBackgroundColor(ccColor4B backgroundColor) {
		this._backgroundColor = backgroundColor;
		needCreateInternalSprite = true;
	}

	public void setBackgroundImage(String imagePath) {
		CCTexture2D texture = CCTextureCache.sharedTextureCache().addImage(imagePath);
		region = new AtlasRegion(texture.getTexture(), 0, 0, texture.getWidth(), texture.getHeight());
		needCreateInternalSprite = true;
	}

	public void setBackgroundImage(CCSprite sprite) {
		region = sprite.getRegion();
		needCreateInternalSprite = true;
	}

	public void setBackgroundImage(AtlasRegion region) {
		this.region = region;
		needCreateInternalSprite = true;
	}

	@Override
	public ccColor3B getColor() {
		return color;
	}

	@Override
	public void setColor(ccColor3B color) {
		this.color = color;
		needCreateInternalSprite = true;
	}

	@Override
	public void setOpacity(int opacity) {
		super.setOpacity(opacity);
		needCreateInternalSprite = true;
	}

	/**
	 * changes the string to render
	 */

	public void setString(CharSequence seq) {
		if (_string != null && _string.equals(seq)) {
			return;
		}
		_string = seq.toString();
		calculateSize();
		needCreateInternalSprite = true;
	}

	private String getActualFontName() {
		return _fontName != null ? _fontName : defaultFontPath;
	}

	public CGSize getStringSize() {
		return getStringSize(_string, getActualFontName());
	}

	public static CGSize getStringSize(CharSequence text, String fontPath) {
		BitmapFont bitmapFont = CCFontCache.sharedFontCache().get(fontPath);
		return getStringSize(text, bitmapFont);
	}

	public static CGSize getStringSize(CharSequence text, BitmapFont bitmapFont) {
		textBounds.setText(bitmapFont, text);
		return CGSize.make(textBounds.width, textBounds.height - bitmapFont.getDescent());
	}

	public String getString() {
		return _string.toString();
	}

	public TextAlignment getAlignment() {
		return _halignment;
	}

	public String getFontName() {
		return _fontName;
	}

	public ccColor4B getBackgroundColor() {
		return _backgroundColor;
	}

	private AtlasRegion region;

	private void calculateSize() {
		if (autoSetSize) {
			BitmapFont bitmapFont = CCFontCache.sharedFontCache().get(getActualFontName());
			CGSize _dimensions = getStringSize(_string, bitmapFont);
			_dimensions.width += 2 * paddingWidth;
			_dimensions.height += 2 * paddingHeight;

			super.setContentSize(_dimensions);
		}
	}

	private void createInternalSprite() {
		BitmapFont bitmapFont = CCFontCache.sharedFontCache().get(getActualFontName());

		int width = (int) contentSize_.width;
		int height = (int) contentSize_.height;

		textBounds.setText(bitmapFont, _string);

		int startX, startY;
		switch (_halignment) {
		case CENTER:
			startX = (int) ((width - textBounds.width) / 2);
			break;
		case RIGHT:
			startX = (int) (width - textBounds.width);
			break;
		case LEFT:
		default:
			startX = 0;
			break;
		}
		switch (_valignment) {
		case BOTTOM:
			startY = (int) textBounds.height - (int) bitmapFont.getDescent();
			break;
		case CENTER:
			startY = (int) ((contentSize_.height + textBounds.height) / 2);
			break;
		case TOP:
		default:
			startY = height;
			break;
		}

		disposeTexture();

		// Flip image since fonts display up side down
		Matrix4 projectionMatrix = new Matrix4();
		projectionMatrix.setToOrtho2D(0, -height, width, height);
		projectionMatrix.scale(1, -1, 1);

		renderSpriteBatch.setProjectionMatrix(projectionMatrix);

		FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
		fb.begin();

		if (_backgroundColor.a > 0) {
			renderSpriteBatch.begin();
			ShapeRenderer shape = new ShapeRenderer();
			shape.setProjectionMatrix(projectionMatrix);
			shape.begin(ShapeType.Filled);
			shape.setColor(_backgroundColor.r / 255f, _backgroundColor.g / 255f, _backgroundColor.b / 255f, _backgroundColor.a / 255f);
			shape.rect(0, 0, width, height);

			shape.end();
			renderSpriteBatch.end();
		}

		renderSpriteBatch.begin();

		if (region != null) {
			renderSpriteBatch.draw(region, 0, 0, contentSize_.width, contentSize_.height);
		}

		bitmapFont.setColor(color.r / 255f, color.g / 255f, color.b / 255f, opacity / 255f);
		bitmapFont.draw(renderSpriteBatch, textBounds, startX, startY);
		renderSpriteBatch.end();

		Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, width, height);
		fb.end();

		Texture texture = new Texture(pixmap);
		AtlasRegion region = new AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
		createInternalSprite(region, false);

		pixmap.dispose();
		fb.dispose();
	}

	@Override
	public String toString() {
		return "CCLabel <" + CCLabel.class.getSimpleName() + " = " + this.hashCode() + " | FontName = " + _fontName + ", sz = " + contentSize_ + ">";
	}

	@Override
	public void cleanup() {
		super.cleanup();
		synchronized (labelMaintenenceList) {
			labelMaintenenceList.remove(this);
		}
	}

	/*
	 * To avoid flickering, internal sprite of label will be created after
	 * rendering
	 */
	private static ArrayList<CCLabel> labelMaintenenceList = new ArrayList<CCLabel>();

	private void addToMaintenanceList() {
		synchronized (labelMaintenenceList) {
			labelMaintenenceList.add(this);
		}
	}

	private void update() {
		if (needCreateInternalSprite) {
			needCreateInternalSprite = false;
			createInternalSprite();
		}
	}

	/*
	 * This function will be called from CCDirector after rendering to avoid
	 * flickering
	 */
	public static void updateAllLabels() {
		synchronized (labelMaintenenceList) {
			for (CCLabel label : labelMaintenenceList) {
				label.update();
			}
		}
	}

}
