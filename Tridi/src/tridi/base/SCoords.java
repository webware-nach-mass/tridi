package tridi.base;

/**
 * A set of 3 double coordinates.
 */
public class SCoords {

	public final double[] coords = new double[3];

	public SCoords(final double x, final double y, final double z) {
		coords[0] = x;
		coords[1] = y;
		coords[2] = z;
	}

	public SCoords() {
		this(0.0, 0.0, 0.0);
	}

	public void setFrom(final SCoords o) {
		coords[0] = o.coords[0];
		coords[1] = o.coords[1];
		coords[2] = o.coords[2];
	}

	public void set(final double x, final double y, final double z) {
		coords[0] = x;
		coords[1] = y;
		coords[2] = z;
	}

	public static void set(final double[] coords, final double x, final double y, final double z) {
		coords[0] = x;
		coords[1] = y;
		coords[2] = z;
	}

	public void subtract(final SCoords o) {
		coords[0] -= o.coords[0];
		coords[1] -= o.coords[1];
		coords[2] -= o.coords[2];
	}

	public void subtract(final double x, final double y, final double z) {
		coords[0] -= x;
		coords[1] -= y;
		coords[2] -= z;
	}

	public void add(final SCoords o) {
		coords[0] += o.coords[0];
		coords[1] += o.coords[1];
		coords[2] += o.coords[2];
	}

	public void add(final double x, final double y, final double z) {
		coords[0] += x;
		coords[1] += y;
		coords[2] += z;
	}

	public void setToInterpolate(final SCoords o0, final SCoords o1, final double p) {
		coords[0] = o0.coords[0] + p * (o1.coords[0] - o0.coords[0]);
		coords[1] = o0.coords[1] + p * (o1.coords[1] - o0.coords[1]);
		coords[2] = o0.coords[2] + p * (o1.coords[2] - o0.coords[2]);
	}

	public void addInterpolate(final SCoords o, final double p) {
		coords[0] += p * o.coords[0];
		coords[1] += p * o.coords[1];
		coords[2] += p * o.coords[2];
	}

	public void multiply(final double p) {
		coords[0] *= p;
		coords[1] *= p;
		coords[2] *= p;
	}

	public void setToInterpolate(final SCoords o, final double p) {
		coords[0] = p * o.coords[0];
		coords[1] = p * o.coords[1];
		coords[2] = p * o.coords[2];
	}

	public boolean sameAs(final double x, final double y, final double z) {
		return coords[0] == x && coords[1] == y && coords[2] == z;
	}

	public boolean sameAs(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof SCoords)) {
			return false;
		}
		SCoords s = (SCoords) obj;
		return coords[0] == s.coords[0] && coords[1] == s.coords[1] && coords[2] == s.coords[2];
	}

	/**
	 * Make equals() final to prevent subclasses to use it to compare the coords,
	 * making SCoords unusable as a map key because too mutable.
	 */
	@Override
	final public boolean equals(final Object obj) {
		return super.equals(obj);
	}

	@Override
	final public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return super.toString() + "[" + coords[0] + "," + coords[1] + "," + coords[2] + "]";
	}
}
