package tridi.exp;

import java.io.File;

import tridi.base.CableDelegate;
import tridi.base.SPoint;
import tridi.base.STransform;
import tridi.base.TriangleDelegate;
import tridi.geom.Brick;
import tridi.render.TriangleRenderer;

/**
 * A TriangleRenderer to export as (binary) .stl.
 */
public class StlRenderer extends TriangleRenderer {

	public StlRenderer(final double scale,final StlWriter out) {
		if(scale != 1.0) {
			transform=new STransform();
			transform.scale(scale);
		}
		this.out=out;
	}
	final StlWriter out;

	@Override
	public void startObject(final String id) {
		out.startObject(id);
	}
	@Override
	public void endObject() {
		out.endObject();
	}

	/**
	 * A triangular facet to use as a render delegate.
	 */
	public class StlTriangleDelegate extends TriangleDelegate {

		public StlTriangleDelegate() {
			super();
		}

		@Override
		public void renderAsDelegate() {
			orientToFirstNormal();
			if(transform != null) {
				transform.transform(this,this);
			}
			out.addTriangle(this);
		}
	}

	@Override
	protected TriangleDelegate makeRenderTriangle() {
		return new StlTriangleDelegate();
	}

	public static class StlCableDelegate extends CableDelegate {
		public StlCableDelegate() {
			super();
		}
		@Override
		public void renderAsDelegate() {
			//nothing to do, STL doesn't contain lines.
		}
	}
	@Override
	protected CableDelegate makeRenderCable() {
		return new StlCableDelegate();
	}

	@Override
	public double getScaleAt(final SPoint p) {
		return 1.0; //todo StlRenderer.getScaleAt could use transform...
	}

	/**
	 * Test main.
	 */
	public static void main(final String[] args) throws Throwable {
		StlWriter out=new StlWriter(new File("test/test.stl"),"test");
		StlRenderer r=new StlRenderer(15.0,out);
		new Brick().render("brick",r);
		out.close();
	}
}
