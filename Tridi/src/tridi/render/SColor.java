package tridi.render;

import tridi.base.SCoords;

/**
 * A color as r,g,b between 0.0 and 1.0.
 */
public class SColor extends SCoords {

	public final static SColor DEFAULT_COLOR=new SColor(1.0,1.0,1.0);

	public SColor(final double r,final double g,final double b) {
		super(r,g,b);
	}

	public SColor() {
		super();
	}
}
