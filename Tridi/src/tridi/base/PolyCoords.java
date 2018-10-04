package tridi.base;

import java.util.Deque;

import tridi.csg.Plane;

/**
 * Abstract Base class for a facet.
 */
public abstract class PolyCoords {
	public final SPoint[] points;
	public final SVector[] normals;
	private boolean normalsOK;

	public void setNormalsOK(final boolean normalsOK) {
		this.normalsOK = normalsOK;
	}

	public boolean getNormalsOK() {
		return normalsOK;
	}

	public PolyCoords(final int vertexCount, final boolean initPoints, final boolean initNormals,
			final boolean normalsOK) {
		points = new SPoint[vertexCount];
		normals = new SVector[vertexCount];
		if (initPoints) {
			for (int i = 0; i < vertexCount; ++i) {
				points[i] = new SPoint();
			}
		}
		if (initNormals) {
			for (int i = 0; i < vertexCount; ++i) {
				normals[i] = new SVector();
			}
		}
		this.normalsOK = normalsOK;
	}

	@Override
	public String toString() {
		String s = "";
		for (SPoint p : points) {
			s += p;
		}
		return s;
	}

	public void setFrom(final PolyCoords orig) {
		for (int i = 0; i < points.length; ++i) {
			points[i].setFrom(orig.points[i]);
			normals[i].setFrom(orig.normals[i]);
		}
		normalsOK = orig.normalsOK;
	}

	public void setSameNormals(final double nx, final double ny, final double nz, final boolean normalsOK) {
		for (int i = 0; i < normals.length; ++i) {
			normals[i].set(nx, ny, nz);
		}
		this.normalsOK = normalsOK;
	}

	public void setSameNormals(final SVector n, final boolean normalsOK) {
		for (int i = 0; i < normals.length; ++i) {
			normals[i].setFrom(n);
		}
		this.normalsOK = normalsOK;
	}

	/**
	 * Will set all normals from (p1-p0)x(p2-p0)
	 */
	public boolean calcNormals() {
		SVector.vector(points[0], points[1], points[2], normals[0]);
		normalsOK = normals[0].normalize();
		if (normalsOK) {
			for (int i = 1; i < normals.length; i++) {
				normals[i].setFrom(normals[0]);
			}
		}
		return normalsOK;
	}

	public boolean calcNormalsAwayFrom(final SPoint center) {
		for (int i = 0; i < points.length; ++i) {
			SVector.subtract(center, points[i], normals[i]);
		}
		return normalize();
	}

	public boolean normalize() {
		normalsOK = true;
		for (int i = 0; i < normals.length; ++i) {
			if (!normals[i].normalize()) {
				normalsOK = false;
			}
		}
		return normalsOK;
	}

	/**
	 * Classifies <code>this</code> relative to the given plane
	 */
	public Plane.Classification classify(final Plane p) {
		boolean hasBack = false;
		boolean hasFront = false;
		for (int i = 0; i < points.length; ++i) {
			if (points[i].classify(p) == Plane.Classification.BACK) {
				if (hasFront) {
					return Plane.Classification.SECANT;
				}
				hasBack = true;
			} else if (points[i].classify(p) == Plane.Classification.FRONT) {
				if (hasBack) {
					return Plane.Classification.SECANT;
				}
				hasFront = true;
			}
		}
		return hasFront ? Plane.Classification.FRONT
				: hasBack ? Plane.Classification.BACK : Plane.Classification.COINCIDENT;
	}

	abstract public void split(final Plane plane, final Deque<PolyCoords> front, final Deque<PolyCoords> back,
			final SVector temp);

	protected SVector interpolateNormals(final SVector n0, final SVector n1) {
		SVector result = new SVector();
		result.setFrom(n0);
		result.addInterpolate(n1, 1.0);
		if (!result.normalize()) {
			result.setFrom(n0);
		}
		return result;
	}
}
