package tridi.geom;

import tridi.base.Geometry;
import tridi.base.QuadrangleCoords;
import tridi.base.SPoint;
import tridi.base.SVector;
import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * A quadrangular facet.
 */
public class Quadrangle extends QuadrangleCoords implements Geometry {

	public Quadrangle() {
		super();
	}
	public Quadrangle(final SPoint p0,final SPoint p1,final SPoint p2,final SPoint p3) {
		super(p0,p1,p2,p3);
	}
	public Quadrangle(final SPoint p0,final SVector n0,final SPoint p1,final SVector n1,final SPoint p2,final SVector n2,final SPoint p3,final SVector n3,final boolean normalsOK) {
		super(p0,n0,p1,n1,p2,n2,p3,n3,normalsOK);
	}

	@Override
	public void calculate() {
		//nothing to do
	}

	@Override
	public void render(final String id,final TriangleRenderer renderer) {
		renderer.startObject(id);
		doRender(renderer);
		renderer.endObject();
	}
	@Override
	public void renderObj(final String id,final ObjRenderer renderer) {
		renderer.startObject(id);
		doRenderObj(renderer);
		renderer.endObject();
	}
}
