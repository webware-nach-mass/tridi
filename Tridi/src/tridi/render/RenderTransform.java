package tridi.render;

import tridi.base.SPoint;

/**
 * A (double x,double y,double z)->(int x,int y,int z) transform.
 */
public class RenderTransform {
	public RenderTransform() {
		super();
	}

	public double[][] m=new double[3][4];
	public double scaleDepth;

	public void transform(final SPoint orig,final ZPoint dest) {
		double persp=orig.coords[0] * m[2][0] + orig.coords[1] * m[2][1] + orig.coords[2] * m[2][2] + m[2][3];
		dest.set((int)Math.round((orig.coords[0] * m[0][0] + orig.coords[1] * m[0][1] + orig.coords[2] * m[0][2] + m[0][3]) / persp), //
				(int)Math.round((orig.coords[0] * m[1][0] + orig.coords[1] * m[1][1] + orig.coords[2] * m[1][2] + m[1][3]) / persp), //
				(int)Math.round(scaleDepth * persp));
	}

	public double getScaleAt(final SPoint p) {
		double persp=p.coords[0] * m[2][0] + p.coords[1] * m[2][1] + p.coords[2] * m[2][2] + m[2][3];
		return (m[0][0] + m[1][1]) / 2.0 / persp;
	}
}
