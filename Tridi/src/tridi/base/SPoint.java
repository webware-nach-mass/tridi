package tridi.base;


/**
 * A point in scene space.
 */
public class SPoint extends SCoords {

	public SPoint(final double x,final double y,final double z) {
		super(x,y,z);
	}

	public SPoint() {
		super();
	}

	public double calcDistSq(final SPoint p) {
		return (p.coords[0] - coords[0]) * (p.coords[0] - coords[0]) + (p.coords[1] - coords[1]) * (p.coords[1] - coords[1]) + (p.coords[2] - coords[2]) * (p.coords[2] - coords[2]);
	}
	public double calcDist(final SPoint p) {
		return Math.sqrt((p.coords[0] - coords[0]) * (p.coords[0] - coords[0]) + (p.coords[1] - coords[1]) * (p.coords[1] - coords[1]) + (p.coords[2] - coords[2]) * (p.coords[2] - coords[2]));
	}

	public static double calcDist(final double x0,final double y0,final double z0,final double x1,final double y1,final double z1) {
		return Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0) + (z1 - z0) * (z1 - z0));
	}

}
