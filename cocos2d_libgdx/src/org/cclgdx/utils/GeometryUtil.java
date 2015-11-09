package org.cclgdx.utils;

import org.cclgdx.types.CGPoint;
import org.cclgdx.types.CGRect;
import org.cclgdx.types.CGSize;

public class GeometryUtil {

	public static CGPoint CGPointFromString(String str) {
		String coords = str.replaceAll("[{|}]", "");
		String c[] = coords.split(",");
		return CGPoint.make(Float.parseFloat(c[0]), Float.parseFloat(c[1]));
	}

	public static CGSize CGSizeFromString(String str) {
		String coords = str.replaceAll("[{|}]", "");
		String c[] = coords.split(",");
		return CGSize.make(Float.parseFloat(c[0]), Float.parseFloat(c[1]));
	}

	public static CGRect CGRectFromString(String str) {
		String c[] = str.replaceAll("[{|}]", "").split(",");
		return CGRect.make(Float.parseFloat(c[0]), Float.parseFloat(c[1]), Float.parseFloat(c[2]), Float.parseFloat(c[3]));
	}

}
