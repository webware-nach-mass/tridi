package tridi.geom;

import tridi.base.Geometry;
import tridi.base.SPoint;
import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * A cone.
 */
public class Cone implements Geometry {

	public SPoint baseCenter;
	public double baseRadius;
	public double topRadius;
	/**
	 * sectors=0 means adaptative rendering.
	 */
	public int sectors;
	public double length;

	public Cone() {
		super();
		baseCenter = new SPoint(0.0, 0.0, 0.0);
		baseRadius = 0.4;
		topRadius = 0.2;
		sectors = 48;
		length = 1.0;
	}

	/**
	 * sectors=0 means adaptative rendering.
	 */
	public Cone(final SPoint baseCenter, final double baseRadius, final double topRadius, final int sectors,
			final double length) {
		super();
		this.baseCenter = baseCenter;
		this.baseRadius = baseRadius;
		this.topRadius = topRadius;
		this.sectors = sectors;
		this.length = length;
	}

	@Override
	public void calculate() {
		// nothing to do
	}

	@Override
	public void render(final String id, final TriangleRenderer renderer) {
		renderer.startObject(id);
		int n = sectors;
		if (n == 0) {
			renderer.renderTriangle.points[0].setFrom(baseCenter);
			renderer.renderTriangle.points[1].set(baseCenter.coords[0], baseCenter.coords[1],
					baseCenter.coords[2] + length);
			if (renderer.transform != null) {
				renderer.transform.transform(renderer.renderTriangle.points[0], renderer.renderTriangle.points[0]);
				renderer.transform.transform(renderer.renderTriangle.points[1], renderer.renderTriangle.points[1]);
			}
			double scale = Math.min(Math.abs(renderer.getScaleAt(renderer.renderTriangle.points[0])), //
					Math.abs(renderer.getScaleAt(renderer.renderTriangle.points[1])));
			n = Math.max(6, (int) (Math.PI * Math.max(baseRadius, topRadius) * scale)); // each facet should be about
																						// 2px wide
		}
		double ta = Math.atan2(length, baseRadius - topRadius) - Math.PI / 2.0, //
				txy = Math.cos(ta), tz = Math.sin(ta);
		for (int i = 0; i < n; ++i) {
			double a0 = i * 2.0 * Math.PI / n, a1 = (i + 1) * 2.0 * Math.PI / n, //
					c0 = Math.cos(a0), s0 = Math.sin(a0), //
					c1 = Math.cos(a1), s1 = Math.sin(a1), //
					dx0b = baseRadius * c0, dy0b = baseRadius * s0, //
					dx1b = baseRadius * c1, dy1b = baseRadius * s1, //
					dx0t = topRadius * c0, dy0t = topRadius * s0, //
					dx1t = topRadius * c1, dy1t = topRadius * s1;
			renderer.renderTriangle.points[0].setFrom(baseCenter);
			renderer.renderTriangle.points[1].set(baseCenter.coords[0] + dx0b, baseCenter.coords[1] + dy0b,
					baseCenter.coords[2]);
			renderer.renderTriangle.points[2].set(baseCenter.coords[0] + dx1b, baseCenter.coords[1] + dy1b,
					baseCenter.coords[2]);
			renderer.renderTriangle.setSameNormals(0.0, 0.0, -1.0, true);
			renderer.renderTriangle.renderAsDelegate();
			renderer.renderTriangle.points[0].set(baseCenter.coords[0] + dx1b, baseCenter.coords[1] + dy1b,
					baseCenter.coords[2]);
			renderer.renderTriangle.normals[0].set(c1 * txy, s1 * txy, s1 * tz);
			renderer.renderTriangle.points[1].set(baseCenter.coords[0] + dx0b, baseCenter.coords[1] + dy0b,
					baseCenter.coords[2]);
			renderer.renderTriangle.normals[1].set(c0 * txy, s0 * txy, s0 * tz);
			renderer.renderTriangle.points[2].set(baseCenter.coords[0] + dx0t, baseCenter.coords[1] + dy0t,
					baseCenter.coords[2] + length);
			renderer.renderTriangle.normals[2].set(c0 * txy, s0 * txy, s0 * tz);
			renderer.renderTriangle.setNormalsOK(true);
			renderer.renderTriangle.renderAsDelegate();
			renderer.renderTriangle.points[0].set(baseCenter.coords[0] + dx1b, baseCenter.coords[1] + dy1b,
					baseCenter.coords[2]);
			renderer.renderTriangle.normals[0].set(c1 * txy, s1 * txy, s1 * tz);
			renderer.renderTriangle.points[1].set(baseCenter.coords[0] + dx0t, baseCenter.coords[1] + dy0t,
					baseCenter.coords[2] + length);
			renderer.renderTriangle.normals[1].set(c0 * txy, s0 * txy, s0 * tz);
			renderer.renderTriangle.points[2].set(baseCenter.coords[0] + dx1t, baseCenter.coords[1] + dy1t,
					baseCenter.coords[2] + length);
			renderer.renderTriangle.normals[2].set(c1 * txy, s1 * txy, s1 * tz);
			renderer.renderTriangle.setNormalsOK(true);
			renderer.renderTriangle.renderAsDelegate();
			renderer.renderTriangle.points[0].set(baseCenter.coords[0], baseCenter.coords[1],
					baseCenter.coords[2] + length);
			renderer.renderTriangle.points[1].set(baseCenter.coords[0] + dx1t, baseCenter.coords[1] + dy1t,
					baseCenter.coords[2] + length);
			renderer.renderTriangle.points[2].set(baseCenter.coords[0] + dx0t, baseCenter.coords[1] + dy0t,
					baseCenter.coords[2] + length);
			renderer.renderTriangle.setSameNormals(0.0, 0.0, 1.0, true);
			renderer.renderTriangle.renderAsDelegate();
		}
		renderer.endObject();
	}

	@Override
	public void renderObj(final String id, final ObjRenderer renderer) {
		renderer.startObject(id);
		int n = sectors;
		double ta = Math.atan2(length, baseRadius - topRadius) - Math.PI / 2.0, //
				txy = Math.cos(ta), tz = Math.sin(ta);
		for (int i = 0; i < n; ++i) {
			double a0 = i * 2.0 * Math.PI / n, a1 = (i + 1) * 2.0 * Math.PI / n, //
					c0 = Math.cos(a0), s0 = Math.sin(a0), //
					c1 = Math.cos(a1), s1 = Math.sin(a1), //
					dx0b = baseRadius * c0, dy0b = baseRadius * s0, //
					dx1b = baseRadius * c1, dy1b = baseRadius * s1, //
					dx0t = topRadius * c0, dy0t = topRadius * s0, //
					dx1t = topRadius * c1, dy1t = topRadius * s1;
			renderer.renderTriangle.points[0].setFrom(baseCenter);
			renderer.renderTriangle.points[1].set(baseCenter.coords[0] + dx0b, baseCenter.coords[1] + dy0b,
					baseCenter.coords[2]);
			renderer.renderTriangle.points[2].set(baseCenter.coords[0] + dx1b, baseCenter.coords[1] + dy1b,
					baseCenter.coords[2]);
			renderer.renderTriangle.setSameNormals(0.0, 0.0, -1.0, true);
			renderer.renderTriangle.renderAsDelegate();
			renderer.renderQuadrangle.points[0].set(baseCenter.coords[0] + dx1b, baseCenter.coords[1] + dy1b,
					baseCenter.coords[2]);
			renderer.renderQuadrangle.normals[0].set(c1 * txy, s1 * txy, s1 * tz);
			renderer.renderQuadrangle.points[1].set(baseCenter.coords[0] + dx0b, baseCenter.coords[1] + dy0b,
					baseCenter.coords[2]);
			renderer.renderQuadrangle.normals[1].set(c0 * txy, s0 * txy, s0 * tz);
			renderer.renderQuadrangle.points[2].set(baseCenter.coords[0] + dx0t, baseCenter.coords[1] + dy0t,
					baseCenter.coords[2] + length);
			renderer.renderQuadrangle.normals[2].set(c0 * txy, s0 * txy, s0 * tz);
			renderer.renderQuadrangle.points[3].set(baseCenter.coords[0] + dx1t, baseCenter.coords[1] + dy1t,
					baseCenter.coords[2] + length);
			renderer.renderQuadrangle.normals[3].set(c1 * txy, s1 * txy, s1 * tz);
			renderer.renderQuadrangle.setNormalsOK(true);
			renderer.renderQuadrangle.renderObjAsDelegate();
			renderer.renderTriangle.points[0].set(baseCenter.coords[0], baseCenter.coords[1],
					baseCenter.coords[2] + length);
			renderer.renderTriangle.points[1].set(baseCenter.coords[0] + dx1t, baseCenter.coords[1] + dy1t,
					baseCenter.coords[2] + length);
			renderer.renderTriangle.points[2].set(baseCenter.coords[0] + dx0t, baseCenter.coords[1] + dy0t,
					baseCenter.coords[2] + length);
			renderer.renderTriangle.setSameNormals(0.0, 0.0, 1.0, true);
			renderer.renderTriangle.renderAsDelegate();
		}
		renderer.endObject();
	}
}
