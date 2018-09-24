package tridi.base;


/**
 * A vector in scene space.
 */
public class SVector extends SCoords {

	public SVector(final double x,final double y,final double z) {
		super(x,y,z);
	}

	public SVector() {
		super();
	}

	public double calcLength() {
		return Math.sqrt(coords[0] * coords[0] + coords[1] * coords[1] + coords[2] * coords[2]);
	}
	public static double calcLength(final double[] coords) {
		return Math.sqrt(coords[0] * coords[0] + coords[1] * coords[1] + coords[2] * coords[2]);
	}

	public boolean normalize() {
		double n=calcLength();
		if(n < 1e-6) {
			return false;
		}
		coords[0]/=n;
		coords[1]/=n;
		coords[2]/=n;
		return true;
	}

	/**
	 * @return v0.v1
	 */
	public static double scalar(final SCoords v0,final SCoords v1) {
		return v0.coords[0] * v1.coords[0] + v0.coords[1] * v1.coords[1] + v0.coords[2] * v1.coords[2];
	}

	/**
	 * Calculates v0xv1 into the given double[].
	 */
	public static void vector(final double[] v0,final double[] v1,final double[] vresult) {
		set(vresult,//
				v0[1] * v1[2] - v0[2] * v1[1], //
				v0[2] * v1[0] - v0[0] * v1[2],//
				v0[0] * v1[1] - v0[1] * v1[0]);
	}

	/**
	 * Calculates (p1-p0)x(p2-p0) into the given SVector.
	 */
	public static void vector(final SCoords p0,final SCoords p1,final SCoords p2,final SVector result) {
		double dx1=p1.coords[0] - p0.coords[0],dy1=p1.coords[1] - p0.coords[1],dz1=p1.coords[2] - p0.coords[2];
		double dx2=p2.coords[0] - p0.coords[0],dy2=p2.coords[1] - p0.coords[1],dz2=p2.coords[2] - p0.coords[2];
		result.set(dy1 * dz2 - dz1 * dy2, //
				dz1 * dx2 - dx1 * dz2, //
				dx1 * dy2 - dy1 * dx2);
	}

	/**
	 * Calculates p1-p0 into the given SVector.
	 */
	public static void subtract(final SPoint p0,final SPoint p1,final SVector result) {
		result.set(p1.coords[0] - p0.coords[0],p1.coords[1] - p0.coords[1],p1.coords[2] - p0.coords[2]);
	}

}