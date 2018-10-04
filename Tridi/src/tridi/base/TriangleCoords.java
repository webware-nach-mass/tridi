package tridi.base;

import java.util.Deque;

import tridi.csg.Plane;
import tridi.csg.Plane.Classification;
import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * Base class for a triangular facet.
 */
public class TriangleCoords extends PolyCoords {

	public TriangleCoords() {
		super(3, true, true, false);
	}

	public TriangleCoords(final SPoint p0, final SPoint p1, final SPoint p2) {
		super(3, false, true, false);
		points[0] = p0;
		points[1] = p1;
		points[2] = p2;
	}

	public TriangleCoords(final SPoint p0, final SVector n0, final SPoint p1, final SVector n1, final SPoint p2,
			final SVector n2, final boolean normalsOK) {
		super(3, false, false, normalsOK);
		points[0] = p0;
		normals[0] = n0;
		points[1] = p1;
		normals[1] = n1;
		points[2] = p2;
		normals[2] = n2;
	}

	/**
	 * Eventually exchanges 2 points to ensure that calculated first normal will be
	 * in the present direction of the first normal.
	 */
	public void orientToFirstNormal() {
		double nx = normals[0].coords[0], ny = normals[0].coords[1], nz = normals[0].coords[2];
		SVector.vector(points[0], points[1], points[2], normals[0]);
		if (nx * normals[0].coords[0] + ny * normals[0].coords[1] + nz * normals[0].coords[2] < 0.0) {
			SPoint p = points[1];
			points[1] = points[2];
			points[2] = p;
		}
		normals[0].set(nx, ny, nz);
	}

	public void doRender(final TriangleRenderer renderer) {
		renderer.renderTriangle.setFrom(this);
		renderer.renderTriangle.renderAsDelegate();
	}

	public void doRenderObj(final ObjRenderer renderer) {
		renderer.renderTriangle.setFrom(this);
		renderer.renderTriangle.renderAsDelegate();
	}

	@Override
	public void split(final Plane plane, final Deque<PolyCoords> front, final Deque<PolyCoords> back,
			final SVector temp) {
		SPoint[] inters = new SPoint[] { plane.intersect(points[0], points[1], temp),
				plane.intersect(points[1], points[2], temp), plane.intersect(points[2], points[0], temp) };
		for (int i = 0, j = 1, k = 2; i < 3; ++i, j = (i + 1) % 3, k = (j + 1) % 3) {
			if (inters[i] == points[i]) {
				TriangleCoords t0 = new TriangleCoords(points[i], this.normals[i], points[j], this.normals[j],
						inters[j], this.normals[0], true);
				TriangleCoords t1 = new TriangleCoords(points[i], this.normals[i], inters[j], this.normals[j],
						points[k], this.normals[k], true);
				if (points[j].classify(plane) == Classification.BACK) {
					front.add(t1);
					back.add(t0);
				} else {
					front.add(t0);
					back.add(t1);
				}
				return;
			}
			if (inters[i] == null) {
				TriangleCoords t = new TriangleCoords(inters[j], interpolateNormals(this.normals[j], this.normals[k]),
						points[k], this.normals[k], inters[k], interpolateNormals(this.normals[k], this.normals[i]),
						true);
				QuadrangleCoords q = new QuadrangleCoords(points[i], this.normals[i], points[j], this.normals[j],
						inters[j], interpolateNormals(this.normals[j], this.normals[k]), inters[k],
						interpolateNormals(this.normals[k], this.normals[i]), true);
				if (points[k].classify(plane) == Classification.BACK) {
					back.add(t);
					front.add(q);
				} else {
					back.add(q);
					front.add(t);
				}
				return;
			}
		}
	}
}
