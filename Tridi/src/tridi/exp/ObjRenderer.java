package tridi.exp;

import tridi.base.QuadrangleCoords;
import tridi.base.STransform;
import tridi.base.TriangleDelegate;
import tridi.render.Renderer;

/**
 * Someone who holds the information necessary to export .obj.
 */
public class ObjRenderer extends Renderer {

	@Override
	public void startObject(final String id) {
		out.startObject(id);
	}
	@Override
	public void endObject() {
		out.endObject();
	}

	final ObjWriter out;

	/**
	 * A triangular facet to use as a render delegate.
	 */
	public class ObjTriangleDelegate extends TriangleDelegate {

		public ObjTriangleDelegate() {
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
	public final ObjTriangleDelegate renderTriangle=new ObjTriangleDelegate();

	/**
	 * A quadrangular facet to use as a render delegate.
	 */
	public class QuadrangleDelegate extends QuadrangleCoords {
		public QuadrangleDelegate() {
			super();
		}
		public void renderObjAsDelegate() {
			orientToFirstNormal();
			if(transform != null) {
				transform.transform(this,this);
			}
			out.addQuadrangle(this);
		}
	}
	public final QuadrangleDelegate renderQuadrangle=new QuadrangleDelegate();

	public ObjRenderer(final double scale,final ObjWriter out) {
		if(scale != 1.0) {
			transform=new STransform();
			transform.scale(scale);
		}
		this.out=out;
	}

}
