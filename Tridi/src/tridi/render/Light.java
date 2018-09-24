package tridi.render;

import java.io.PrintWriter;

import tridi.base.SPoint;
import tridi.base.SVector;

/**
 * A light.
 */
public abstract class Light {
	public Light(final double r,final double g,final double b) {
		this.color=new SColor(r,g,b);
	}

	public SColor color;

	abstract public void addColor(SColor orig,SPoint p,SVector n,SColor dest);

	abstract public void exportPOV(PrintWriter out);
}
