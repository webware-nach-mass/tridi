package tridi.csg;

import tridi.base.SPoint;
import tridi.base.SVector;

public class Plane {

	public SPoint anchor;
	public SVector normal;

	public Plane(final SPoint a, final SVector n) {
		anchor = a;
		normal = n;
	}

	/**
	 * Possible positions of a facet or point relative to a plane
	 */
	public static enum Classification {
		COINCIDENT, SECANT, FRONT, BACK
	}

	/**
	 * @return p0 or p1, if they lie in <code>this</code>, null, if there's no
	 *         intersection between segment [p0,p1] and <code>this</code> The
	 *         intersection point otherwise
	 */
	public SPoint intersect(final SPoint p0, final SPoint p1, final SVector temp) {
		SVector.subtract(p0, p1, temp);
		double det = SVector.scalar(temp, normal);
		if (Math.abs(det) < 1e-6) {
			return null;
		}
		SVector.subtract(p0, anchor, temp);
		double l = SVector.scalar(temp, normal) / det;
		if (l < -1e-3 || l > 1.0 + 1e-3) {
			return null;
		}
		if (l < 1e-3) {
			return p0;
		}
		if (l > 1.0 - 1e-3) {
			return p1;
		}
		SPoint result = new SPoint();
		result.setToInterpolate(p0, p1, l);
		return result;
	}

	@Override
	public String toString() {
		return "plane anchor=" + anchor + " normal=" + normal;
	}
}
