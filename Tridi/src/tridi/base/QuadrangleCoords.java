package tridi.base;

import java.util.Deque;

import tridi.csg.Plane;
import tridi.csg.Plane.Classification;
import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * Base class for a quadrangular facet.
 */
public class QuadrangleCoords extends PolyCoords {

	public QuadrangleCoords() {
		super(4, true, true, false);
	}

	public QuadrangleCoords(final SPoint p0, final SPoint p1, final SPoint p2, final SPoint p3) {
		super(4, false, true, false);
		points[0] = p0;
		points[1] = p1;
		points[2] = p2;
		points[3] = p3;
	}

	public QuadrangleCoords(final SPoint p0, final SVector n0, final SPoint p1, final SVector n1, final SPoint p2,
			final SVector n2, final SPoint p3, final SVector n3, final boolean normalsOK) {
		super(4, false, false, normalsOK);
		points[0] = p0;
		normals[0] = n0;
		points[1] = p1;
		normals[1] = n1;
		points[2] = p2;
		normals[2] = n2;
		points[3] = p3;
		normals[3] = n3;
	}

	/**
	 * Eventually exchanges p1 and p3 to ensure that calculated first normal
	 * (p1-p0)x(p3-p0) will be in the present direction of the first normal.
	 */
	public void orientToFirstNormal() {
		double nx = normals[0].coords[0], ny = normals[0].coords[1], nz = normals[0].coords[2];
		SVector.vector(points[0], points[1], points[3], normals[0]);
		if (nx * normals[0].coords[0] + ny * normals[0].coords[1] + nz * normals[0].coords[2] < 0.0) {
			SPoint p = points[1];
			points[1] = points[3];
			points[3] = p;
		}
		normals[0].set(nx, ny, nz);
	}

	public void doRender(final TriangleRenderer renderer) {
		for (int i = 0; i < 3; ++i) {
			renderer.renderTriangle.points[i].setFrom(points[i]);
			renderer.renderTriangle.normals[i].setFrom(normals[i]);
		}
		renderer.renderTriangle.setNormalsOK(this.getNormalsOK());
		renderer.renderTriangle.renderAsDelegate();
		for (int i = 0; i < 3; ++i) {
			renderer.renderTriangle.points[i].setFrom(points[(i + 2) % 4]);
			renderer.renderTriangle.normals[i].setFrom(normals[(i + 2) % 4]);
		}
		renderer.renderTriangle.setNormalsOK(this.getNormalsOK());
		renderer.renderTriangle.renderAsDelegate();
	}

	public void doRenderObj(final ObjRenderer renderer) {
		renderer.renderQuadrangle.setFrom(this);
		renderer.renderQuadrangle.renderObjAsDelegate();
	}

	@Override
	public void split(final Plane plane, final Deque<PolyCoords> front, final Deque<PolyCoords> back,
			final SVector temp) {
		SPoint[] inters = new SPoint[] { plane.intersect(points[0], points[1], temp),
				plane.intersect(points[1], points[2], temp), plane.intersect(points[2], points[3], temp),
				plane.intersect(points[3], points[0], temp) };
		for (int i = 0, j = 1, k = 2, l = 3; i < 4; ++i, j = (i + 1) % 4, k = (j + 1) % 3, l = (k + 1) % 4) {
			if (inters[i] == points[i] && inters[k] == points[k]) {
				TriangleCoords t0 = new TriangleCoords(points[i], this.normals[i], points[j], this.normals[j],
						points[k], this.normals[k], true);
				TriangleCoords t1 = new TriangleCoords(points[i], this.normals[i], points[k], this.normals[k],
						points[l], this.normals[l], true);
				if (points[j].classify(plane) == Classification.BACK) {
					front.add(t1);
					back.add(t0);
				} else {
					front.add(t0);
					back.add(t1);
				}
				return;
			}
			if (inters[i] == points[i]) {
				if (inters[j] == null) {
					TriangleCoords t = new TriangleCoords(points[i], this.normals[i], inters[k],
							interpolateNormals(this.normals[k], this.normals[l]), points[l], this.normals[l], true);
					// polygon this has to be changed
					QuadrangleCoords q = new QuadrangleCoords(points[i], this.normals[i], points[j], this.normals[j],
							points[k], this.normals[k], inters[k], interpolateNormals(this.normals[k], this.normals[l]),
							true);
					if (points[j].classify(plane) == Classification.BACK) {
						back.add(q);
						front.add(t);
					} else {
						back.add(t);
						front.add(q);
					}
				} else {
					TriangleCoords t = new TriangleCoords(points[i], this.normals[i], points[j], this.normals[j],
							inters[j], interpolateNormals(this.normals[j], this.normals[k]), true);
					QuadrangleCoords q = new QuadrangleCoords(points[i], this.normals[i], inters[j],
							interpolateNormals(this.normals[j], this.normals[k]), points[k], this.normals[k], points[l],
							this.normals[l], true);
					if (points[j].classify(plane) == Classification.BACK) {
						back.add(t);
						front.add(q);
					} else {
						back.add(q);
						front.add(t);
					}
				}
				return;
			}
			if (inters[i] != null && inters[j] != null) {
				TriangleCoords t0 = new TriangleCoords(inters[i], interpolateNormals(this.normals[i], this.normals[j]),
						points[j], this.normals[j], inters[j], interpolateNormals(this.normals[j], this.normals[k]),
						true);
				TriangleCoords t1 = new TriangleCoords(points[i], this.normals[i], inters[i],
						interpolateNormals(this.normals[i], this.normals[j]), points[l], this.normals[l], true);
				QuadrangleCoords q = new QuadrangleCoords(inters[i],
						interpolateNormals(this.normals[i], this.normals[j]), inters[j],
						interpolateNormals(this.normals[j], this.normals[k]), points[k], this.normals[k], points[l],
						this.normals[l], true);
				if (points[j].classify(plane) == Classification.BACK) {
					back.add(t0);
					front.add(t1);
					front.add(q);
				} else {
					back.add(t1);
					back.add(q);
					front.add(t0);
				}
				return;
			}
			if (inters[i] != null && inters[k] != null) {
				QuadrangleCoords q0 = new QuadrangleCoords(points[i], this.normals[i], inters[i],
						interpolateNormals(this.normals[i], this.normals[j]), inters[k],
						interpolateNormals(this.normals[k], this.normals[l]), points[l], this.normals[l], true);
				QuadrangleCoords q1 = new QuadrangleCoords(inters[i],
						interpolateNormals(this.normals[i], this.normals[j]), points[j], this.normals[j], points[k],
						this.normals[k], inters[k], interpolateNormals(this.normals[k], this.normals[l]), true);
				if (points[j].classify(plane) == Classification.BACK) {
					front.add(q0);
					back.add(q1);
				} else {
					front.add(q1);
					back.add(q0);
				}
				return;
			}
		}
	}
}
