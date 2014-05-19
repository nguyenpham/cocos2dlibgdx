package org.cclgdx.types;

/** RGBA color composed of 4 bytes */
public class ccColor4B {

	public static final ccColor4B ccCLEAR = ccc4(0, 0, 0, 0);

	// ccColor4B predefined colors
	public static final ccColor4B ccWHITE = new ccColor4B(ccColor3B.ccWHITE, 255);
	public static final ccColor4B ccYELLOW = new ccColor4B(ccColor3B.ccYELLOW, 255);
	public static final ccColor4B ccBLUE = new ccColor4B(ccColor3B.ccBLUE, 255);
	public static final ccColor4B ccGREEN = new ccColor4B(ccColor3B.ccGREEN, 255);
	public static final ccColor4B ccRED = new ccColor4B(ccColor3B.ccRED, 255);
	public static final ccColor4B ccMAGENTA = new ccColor4B(ccColor3B.ccMAGENTA, 255);
	public static final ccColor4B ccBLACK = new ccColor4B(ccColor3B.ccBLACK, 255);
	public static final ccColor4B ccORANGE = new ccColor4B(ccColor3B.ccORANGE, 255);
	public static final ccColor4B ccGRAY = new ccColor4B(ccColor3B.ccGRAY, 255);

	// public static final int size = 4;

	public int r;
	public int g;
	public int b;
	public int a;

	public ccColor4B(int rr, int gg, int bb, int aa) {
		r = rr;
		g = gg;
		b = bb;
		a = aa;
	}

	public ccColor4B(ccColor3B color, int aa) {
		this(color.r, color.g, color.b, aa);
	}

	public byte[] toByteArray() {
		return new byte[] { (byte) r, (byte) g, (byte) b, (byte) a };
	}

	public float[] toFloatArray() {
		return new float[] { r / 255f, g / 255f, b / 255f, a / 255f };
	}

	// ! helper macro that creates an ccColor4B type
	public static ccColor4B ccc4(final int r, final int g, final int b, final int a) {
		return new ccColor4B(r, g, b, a);
	}

	@Override
	public String toString() {
		return "< r=" + r + ", g=" + g + ", b=" + b + ", a=" + a + " >";
	}
}
