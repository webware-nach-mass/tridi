package tridi.render;

import java.util.ArrayList;
import java.util.List;

import tridi.base.CableDelegate;
import tridi.base.SPoint;
import tridi.base.SVector;
import tridi.base.TriangleDelegate;

/**
 * A Renderer who writes in a ZBuffer.
 */
public class ZBufferRenderer extends TriangleRenderer {

	public final List<Light> lights = new ArrayList<Light>();

	public void setLights(final List<Light> lights) {
		this.lights.clear();
		this.lights.addAll(lights);
	}

	public void addColor(final SColor color, final SPoint point, final SVector normal, final SColor dest) {
		dest.set(0.0, 0.0, 0.0);
		for (Light l : lights) {
			l.addColor(color, point, normal, dest);
		}
	}

	private int background = 0xFF000000;

	public int getBackground() {
		return background;
	}

	public void setBackground(final int background) {
		this.background = background | 0xFF000000;
	}

	public final Camera camera;
	public final ZBuffer buf;

	@Override
	public void startObject(final String id) {
		// nothing to do
	}

	@Override
	public void endObject() {
		// nothing to do
	}

	/**
	 * A CableCoords to use as a delegate for rendering.
	 */
	public class RenderCableDelegate extends CableDelegate {

		public RenderCableDelegate() {
			super();
		}

		private final SColor[] renderColors = { new SColor(), new SColor() };
		private final ZPoint[] renderPoints = { new ZPoint(), new ZPoint() };

		@Override
		public void renderAsDelegate() {
			if (transform != null) {
				transform.transform(this, this);
			}
			if (normalsOK) {
				// calculate colors
				for (int i = 0; i < 2; ++i) {
					addColor(color, points[i], normals[i], renderColors[i]);
				}
			} else {
				for (int i = 0; i < 2; ++i) {
					renderColors[i].setFrom(color);
				}
			}
			// transform points
			RenderTransform rt = camera.getTransform();
			boolean ok = true;
			for (int i = 0; i < 2 && ok; ++i) {
				rt.transform(points[i], renderPoints[i]);
				ok = renderPoints[i].coords[2] > 0;
			}
			if (!ok) { // at least one vertex is behind the eye
				// TODO intersect view cone and show visible part
				return;
			}
			// calculate difference for interpolation
			renderPoints[1].subtract(renderPoints[0]);
			if (renderPoints[1].coords[0] == 0 && renderPoints[1].coords[1] == 0) { // segment is a point
				return;
			}
			// calculate difference for color interpolation
			renderColors[1].subtract(renderColors[0]);
			// iterate in buffer space and set
			// TODO intersect with buffer edges!
			if (Math.abs(renderPoints[1].coords[0]) > Math.abs(renderPoints[1].coords[1])) {
				int s = renderPoints[1].coords[1] > 0 ? 1 : renderPoints[1].coords[1] < 0 ? -1 : 0;
				renderPoints[1].coords[1] *= s;
				if (renderPoints[1].coords[0] >= 0) {
					for (int u = renderPoints[0].coords[0], du = 0, v = renderPoints[0].coords[1], err = 0; du <= renderPoints[1].coords[0]; ++u, ++du) {
						double f = du / (double) renderPoints[1].coords[0];
						ZBufferRenderer.this.buf.set(u, v,
								renderPoints[0].coords[2]
										+ (du * renderPoints[1].coords[2]) / renderPoints[1].coords[0], //
								renderColors[0].coords[0] + f * renderColors[1].coords[0], //
								renderColors[0].coords[1] + f * renderColors[1].coords[1], //
								renderColors[0].coords[2] + f * renderColors[1].coords[2]);
						err += renderPoints[1].coords[1];
						if (err >= renderPoints[1].coords[0]) {
							v += s;
							err -= renderPoints[1].coords[0];
						}
					}
				} else {
					for (int u = renderPoints[0].coords[0], du = 0, v = renderPoints[0].coords[1], err = 0; du >= renderPoints[1].coords[0]; --u, --du) {
						double f = du / (double) renderPoints[1].coords[0];
						buf.set(u, v,
								renderPoints[0].coords[2]
										+ (du * renderPoints[1].coords[2]) / renderPoints[1].coords[0], //
								renderColors[0].coords[0] + f * renderColors[1].coords[0], //
								renderColors[0].coords[1] + f * renderColors[1].coords[1], //
								renderColors[0].coords[2] + f * renderColors[1].coords[2]);
						err -= renderPoints[1].coords[1];
						if (err <= renderPoints[1].coords[0]) {
							v += s;
							err -= renderPoints[1].coords[0];
						}
					}
				}
			} else {
				int s = renderPoints[1].coords[0] > 0 ? 1 : renderPoints[1].coords[0] < 0 ? -1 : 0;
				renderPoints[1].coords[0] *= s;
				if (renderPoints[1].coords[1] >= 0) {
					for (int v = renderPoints[0].coords[1], dv = 0, u = renderPoints[0].coords[0], err = 0; dv <= renderPoints[1].coords[1]; ++v, ++dv) {
						double f = dv / (double) renderPoints[1].coords[1];
						buf.set(u, v,
								renderPoints[0].coords[2]
										+ (dv * renderPoints[1].coords[2]) / renderPoints[1].coords[1], //
								renderColors[0].coords[0] + f * renderColors[1].coords[0], //
								renderColors[0].coords[1] + f * renderColors[1].coords[1], //
								renderColors[0].coords[2] + f * renderColors[1].coords[2]);
						err += renderPoints[1].coords[0];
						if (err >= renderPoints[1].coords[1]) {
							u += s;
							err -= renderPoints[1].coords[1];
						}
					}
				} else {
					for (int v = renderPoints[0].coords[1], dv = 0, u = renderPoints[0].coords[0], err = 0; dv >= renderPoints[1].coords[1]; --v, --dv) {
						double f = dv / (double) renderPoints[1].coords[1];
						buf.set(u, v,
								renderPoints[0].coords[2]
										+ (dv * renderPoints[1].coords[2]) / renderPoints[1].coords[1], //
								renderColors[0].coords[0] + f * renderColors[1].coords[0], //
								renderColors[0].coords[1] + f * renderColors[1].coords[1], //
								renderColors[0].coords[2] + f * renderColors[1].coords[2]);
						err -= renderPoints[1].coords[0];
						if (err <= renderPoints[1].coords[1]) {
							u += s;
							err -= renderPoints[1].coords[1];
						}
					}
				}
			}
		}
	}

	@Override
	public RenderCableDelegate makeRenderCable() {
		return new RenderCableDelegate();
	}

	/**
	 * A triangular facet to use as a render delegate.
	 */
	public class RenderTriangleDelegate extends TriangleDelegate {

		public RenderTriangleDelegate() {
			super();
		}

		private final SColor[] renderColors = new SColor[] { new SColor(), new SColor(), new SColor() };
		private final ZPoint[] renderPoints = { new ZPoint(), new ZPoint(), new ZPoint() };
		/**
		 * Temporary for tesselating.
		 */
		private SPoint[] tempPoints = null;
		private SColor[] tempRenderColors = null;
		/**
		 * A triangle must be tesselated when its shortest side is shorter than
		 * THIN_THRESHOLD * its longest side.
		 */
		private final static double THIN_THRESHOLD = 0.1;
		private final static int MAX_TESS_COUNT = 10;

		@Override
		public void renderAsDelegate() {
			if (!getNormalsOK()) { // could not calculate normals: triangle is flat.
				return;
			}
			// apply parent tranform
			if (transform != null) {
				transform.transform(this, this);
			}
			// calculate colors
			for (int i = 0; i < 3; ++i) {
				addColor(color, points[i], normals[i], renderColors[i]);
			}
			// if triangle is too thin, tesselate
			double s0 = points[0].calcDist(points[1]), //
					s1 = points[1].calcDist(points[2]), //
					s2 = points[2].calcDist(points[0]), //
					smin = Math.min(s0, Math.min(s1, s2)), //
					smax = Math.max(s0, Math.max(s1, s2));
			if (smin < THIN_THRESHOLD * smax) {
				tesselate(s0 < s1 ? s0 < s2 ? 0 : 2 : s1 < s2 ? 1 : 2,
						Math.min(MAX_TESS_COUNT, (int) Math.ceil(THIN_THRESHOLD * smax / smin)));
			} else {
				doRenderAsDelegate();
			}
		}

		private void tesselate(final int shortestSide, final int tessCount) {
			if (tempPoints == null) {
				tempPoints = new SPoint[] { new SPoint(), new SPoint(), new SPoint() };
			}
			if (tempRenderColors == null) {
				tempRenderColors = new SColor[] { new SColor(), new SColor(), new SColor() };
			}
			for (int i = 0; i < 3; ++i) {
				tempPoints[i].setFrom(points[i]);
				tempRenderColors[i].setFrom(renderColors[i]);
			}
			int i0 = shortestSide, i1 = (shortestSide + 1) % 3, i2 = (shortestSide + 2) % 3;
			// render thin corner
			interpolate(i0, i2, i0, 1.0 / tessCount);
			interpolate(i1, i2, i1, 1.0 / tessCount);
			doRenderAsDelegate();
			// divide long sides and render stripes
			for (int tess = 1; tess < tessCount; ++tess) {
				double p0 = tess / (double) tessCount, p1 = (tess + 1) / (double) tessCount;
				interpolate(i2, i2, i0, p0);
				interpolate(i0, i2, i0, p1);
				interpolate(i1, i2, i1, p0);
				doRenderAsDelegate();
				interpolate(i2, i2, i1, p0);
				interpolate(i0, i2, i0, p1);
				interpolate(i1, i2, i1, p1);
				doRenderAsDelegate();
			}
		}

		private void interpolate(final int idest, final int icenter, final int isrc, final double p) {
			points[idest].setToInterpolate(tempPoints[icenter], tempPoints[isrc], p);
			renderColors[idest].setToInterpolate(tempRenderColors[icenter], tempRenderColors[isrc], p);
		}

		private void doRenderAsDelegate() {
			// transform points
			RenderTransform rt = camera.getTransform();
			int minu = buf.w - 1, maxu = 0, minv = buf.h - 1, maxv = 0;
			boolean ok = true;
			for (int i = 0; i < 3 && ok; ++i) {
				rt.transform(points[i], renderPoints[i]);
				minu = Math.min(minu, renderPoints[i].coords[0]);
				maxu = Math.max(maxu, renderPoints[i].coords[0]);
				minv = Math.min(minv, renderPoints[i].coords[1]);
				maxv = Math.max(maxv, renderPoints[i].coords[1]);
				ok = renderPoints[i].coords[2] > 0;
			}
			if (!ok) { // at least one vertex is behind the eye
				// TODO intersect view cone and show visible part
				return;
			}
			// calculate differences for interpolation
			renderPoints[1].subtract(renderPoints[0]);
			renderPoints[2].subtract(renderPoints[0]);
			int det = renderPoints[1].coords[0] * renderPoints[2].coords[1]
					- renderPoints[1].coords[1] * renderPoints[2].coords[0];
			if (det == 0) { // triangle ist flat
				return;
			}
			// calculate differences for color interpolation
			renderColors[1].subtract(renderColors[0]);
			renderColors[2].subtract(renderColors[0]);
			// iterate in buffer space and draw
			int sdet = det > 0 ? 1 : -1;
			det *= sdet;
			if (minu < 0) {
				minu = 0;
			}
			if (maxu >= buf.w) {
				maxu = buf.w - 1;
			}
			if (minv < 0) {
				minv = 0;
			}
			if (maxv >= buf.h) {
				maxv = buf.h - 1;
			}
			for (int u = minu; u <= maxu; ++u) {
				for (int v = minv; v <= maxv; ++v) {
					int l = sdet * ((u - renderPoints[0].coords[0]) * renderPoints[2].coords[1]
							- (v - renderPoints[0].coords[1]) * renderPoints[2].coords[0]);
					int m = sdet * (renderPoints[1].coords[0] * (v - renderPoints[0].coords[1])
							- renderPoints[1].coords[1] * (u - renderPoints[0].coords[0]));
					if (l >= 0 && m >= 0 && l + m <= det) {
						// if(u == buf.w / 2 && v == buf.h / 2) {
						// System.err.println("" + u + "," + v + "," + (renderPoints[0].coords[2] + (l *
						// renderPoints[1].coords[2] + m * renderPoints[2].coords[2]) / det));
						// }
						buf.set(u, v,
								renderPoints[0].coords[2]
										+ (l * renderPoints[1].coords[2] + m * renderPoints[2].coords[2]) / det, //
								renderColors[0].coords[0]
										+ (l * renderColors[1].coords[0] + m * renderColors[2].coords[0]) / det, //
								renderColors[0].coords[1]
										+ (l * renderColors[1].coords[1] + m * renderColors[2].coords[1]) / det, //
								renderColors[0].coords[2]
										+ (l * renderColors[1].coords[2] + m * renderColors[2].coords[2]) / det);
					}
				}
			}
		}
	}

	@Override
	protected RenderTriangleDelegate makeRenderTriangle() {
		return new RenderTriangleDelegate();
	}

	@Override
	public double getScaleAt(final SPoint p) {
		return camera.getTransform().getScaleAt(p);
	}

	public ZBufferRenderer(final Camera camera, final int w, final int h) {
		super();
		this.camera = camera;
		buf = new ZBuffer(w, h);
	}

	public void startFrame() {
		if (lights.size() <= 0) {
			lights.add(new PointLight(camera.eye, 1.0, 1.0, 1.0));
		}
		camera.calcTransform(buf.w, buf.h);
		reset();
		buf.startFrame();
		buf.clear(background);
	}

	public void endFrame() {
		buf.endFrame();
	}
}
