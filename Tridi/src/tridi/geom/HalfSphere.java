package tridi.geom;

import tridi.base.Geometry;
import tridi.base.SPoint;
import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * A half sphere in direction x>=0.
 */
public class HalfSphere implements Geometry {

	public SPoint center;
	public double radius;
	/**
	 * slices=0 means adaptative rendering
	 */
	public int slices;

	public HalfSphere() {
		super();
		center = new SPoint(0.0, 0.0, 0.0);
		radius = 0.1;
		slices = 24;
	}

	/**
	 * slices=0 means adaptative rendering
	 */
	public HalfSphere(final SPoint center, final double radius, final int slices) {
		super();
		this.center = center;
		this.radius = radius;
		this.slices = slices;
	}

	@Override
	public void calculate() {
		// nothing to do
	}

	@Override
	public void render(final String id, final TriangleRenderer renderer) {
		renderer.startObject(id);
		int n = slices;
		if (n == 0) {
			renderer.renderTriangle.points[0].setFrom(center);
			renderer.renderTriangle.points[1].set(center.coords[0] + radius, center.coords[1] + radius,
					center.coords[2] + radius);
			if (renderer.transform != null) {
				renderer.transform.transform(renderer.renderTriangle.points[0], renderer.renderTriangle.points[0]);
				renderer.transform.transform(renderer.renderTriangle.points[1], renderer.renderTriangle.points[1]);
			}
			double scale = Math.min(Math.abs(renderer.getScaleAt(renderer.renderTriangle.points[0])), //
					Math.abs(renderer.getScaleAt(renderer.renderTriangle.points[1])));
			n = Math.max(6, (int) (Math.PI * radius * scale)); // each facet should be about 2px wide
		}
		for (int i = 1; i < n; ++i) {
			double la0 = i * Math.PI / n, la1 = (i + 1) * Math.PI / n, //
					cla0 = Math.cos(la0), sla0 = Math.sin(la0), //
					z0 = center.coords[2] + radius * cla0, //
					r0 = radius * sla0, //
					cla1 = Math.cos(la1), sla1 = Math.sin(la1), //
					z1 = center.coords[2] + radius * cla1, //
					r1 = radius * sla1;
			for (int j = 0; j < n; ++j) {
				double lo0 = (j - 0.5) * Math.PI / n, lo1 = j * Math.PI / n, lo2 = (j + 0.5) * Math.PI / n, //
						clo0 = Math.cos(lo0), slo0 = Math.sin(lo0), //
						clo1 = Math.cos(lo1), slo1 = Math.sin(lo1), //
						clo2 = Math.cos(lo2), slo2 = Math.sin(lo2);
				renderer.renderTriangle.points[0].set(center.coords[0] + r0 * clo0, center.coords[1] + r0 * slo0, z0);
				renderer.renderTriangle.normals[0].set(sla0 * clo0, sla0 * slo0, cla0);
				renderer.renderTriangle.points[1].set(center.coords[0] + r1 * clo1, center.coords[1] + r1 * slo1, z1);
				renderer.renderTriangle.normals[1].set(sla1 * clo1, sla1 * slo1, cla1);
				renderer.renderTriangle.points[2].set(center.coords[0] + r0 * clo2, center.coords[1] + r0 * slo2, z0);
				renderer.renderTriangle.normals[2].set(sla0 * clo2, sla0 * slo2, cla0);
				renderer.renderTriangle.setNormalsOK(true);
				renderer.renderTriangle.renderAsDelegate();
			}
		}
		for (int i = 1; i < n; ++i) {
			double la0 = (i - 1) * Math.PI / n, la1 = i * Math.PI / n, //
					cla0 = Math.cos(la0), sla0 = Math.sin(la0), //
					z0 = center.coords[2] + radius * cla0, //
					r0 = radius * sla0, //
					cla1 = Math.cos(la1), sla1 = Math.sin(la1), //
					z1 = center.coords[2] + radius * cla1, //
					r1 = radius * sla1;
			for (int j = 0; j < n; ++j) {
				double lo0 = j * Math.PI / n, lo1 = (j + 0.5) * Math.PI / n, lo2 = (j + 1) * Math.PI / n, //
						clo0 = Math.cos(lo0), slo0 = Math.sin(lo0), //
						clo1 = Math.cos(lo1), slo1 = Math.sin(lo1), //
						clo2 = Math.cos(lo2), slo2 = Math.sin(lo2);
				renderer.renderTriangle.points[0].set(center.coords[0] + r1 * clo0, center.coords[1] + r1 * slo0, z1);
				renderer.renderTriangle.normals[0].set(sla1 * clo0, sla1 * slo0, cla1);
				renderer.renderTriangle.points[1].set(center.coords[0] + r0 * clo1, center.coords[1] + r0 * slo1, z0);
				renderer.renderTriangle.normals[1].set(sla0 * clo1, sla0 * slo1, cla0);
				renderer.renderTriangle.points[2].set(center.coords[0] + r1 * clo2, center.coords[1] + r1 * slo2, z1);
				renderer.renderTriangle.normals[2].set(sla1 * clo2, sla1 * slo2, cla1);
				renderer.renderTriangle.setNormalsOK(true);
				renderer.renderTriangle.renderAsDelegate();
			}
		}
		renderer.endObject();
	}

	@Override
	public void renderObj(final String id, final ObjRenderer renderer) {
		renderer.startObject(id);
		int n = slices;
		for (int i = 1; i < n; ++i) {
			double la0 = i * Math.PI / n, la1 = (i + 1) * Math.PI / n, //
					cla0 = Math.cos(la0), sla0 = Math.sin(la0), //
					z0 = center.coords[2] + radius * cla0, //
					r0 = radius * sla0, //
					cla1 = Math.cos(la1), sla1 = Math.sin(la1), //
					z1 = center.coords[2] + radius * cla1, //
					r1 = radius * sla1;
			for (int j = 0; j < n; ++j) {
				double lo0 = (j - 0.5) * Math.PI / n, lo1 = j * Math.PI / n, lo2 = (j + 0.5) * Math.PI / n, //
						clo0 = Math.cos(lo0), slo0 = Math.sin(lo0), //
						clo1 = Math.cos(lo1), slo1 = Math.sin(lo1), //
						clo2 = Math.cos(lo2), slo2 = Math.sin(lo2);
				renderer.renderTriangle.points[0].set(center.coords[0] + r0 * clo0, center.coords[1] + r0 * slo0, z0);
				renderer.renderTriangle.normals[0].set(sla0 * clo0, sla0 * slo0, cla0);
				renderer.renderTriangle.points[1].set(center.coords[0] + r1 * clo1, center.coords[1] + r1 * slo1, z1);
				renderer.renderTriangle.normals[1].set(sla1 * clo1, sla1 * slo1, cla1);
				renderer.renderTriangle.points[2].set(center.coords[0] + r0 * clo2, center.coords[1] + r0 * slo2, z0);
				renderer.renderTriangle.normals[2].set(sla0 * clo2, sla0 * slo2, cla0);
				renderer.renderTriangle.setNormalsOK(true);
				renderer.renderTriangle.renderAsDelegate();
			}
		}
		for (int i = 1; i < n; ++i) {
			double la0 = (i - 1) * Math.PI / n, la1 = i * Math.PI / n, //
					cla0 = Math.cos(la0), sla0 = Math.sin(la0), //
					z0 = center.coords[2] + radius * cla0, //
					r0 = radius * sla0, //
					cla1 = Math.cos(la1), sla1 = Math.sin(la1), //
					z1 = center.coords[2] + radius * cla1, //
					r1 = radius * sla1;
			for (int j = 0; j < n; ++j) {
				double lo0 = j * Math.PI / n, lo1 = (j + 0.5) * Math.PI / n, lo2 = (j + 1) * Math.PI / n, //
						clo0 = Math.cos(lo0), slo0 = Math.sin(lo0), //
						clo1 = Math.cos(lo1), slo1 = Math.sin(lo1), //
						clo2 = Math.cos(lo2), slo2 = Math.sin(lo2);
				renderer.renderTriangle.points[0].set(center.coords[0] + r1 * clo0, center.coords[1] + r1 * slo0, z1);
				renderer.renderTriangle.normals[0].set(sla1 * clo0, sla1 * slo0, cla1);
				renderer.renderTriangle.points[1].set(center.coords[0] + r0 * clo1, center.coords[1] + r0 * slo1, z0);
				renderer.renderTriangle.normals[1].set(sla0 * clo1, sla0 * slo1, cla0);
				renderer.renderTriangle.points[2].set(center.coords[0] + r1 * clo2, center.coords[1] + r1 * slo2, z1);
				renderer.renderTriangle.normals[2].set(sla1 * clo2, sla1 * slo2, cla1);
				renderer.renderTriangle.setNormalsOK(true);
				renderer.renderTriangle.renderAsDelegate();
			}
		}
		renderer.endObject();
	}
}
