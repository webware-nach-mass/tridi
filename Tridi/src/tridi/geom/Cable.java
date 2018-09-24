package tridi.geom;

import tridi.base.CableCoords;
import tridi.base.Geometry;
import tridi.base.SPoint;
import tridi.base.SVector;
import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * A line.
 */
public class Cable extends CableCoords implements Geometry {
	public Cable() {
		super();
	}
	public Cable(final SPoint p0,final SPoint p1) {
		super(p0,p1);
	}
	public Cable(final SPoint p0,final SVector n0,final SPoint p1,final SVector n1) {
		super(p0,n0,p1,n1);
	}

	@Override
	public void calculate() {
		//nothing to do
	}

	@Override
	public void render(final String id,final TriangleRenderer renderer) {
		renderer.startObject(id);
		renderer.renderCable.setFrom(this);
		renderer.renderCable.renderAsDelegate();
		renderer.endObject();
	}
	@Override
	public void renderObj(final String id,final ObjRenderer renderer) {
		//nothing to do, .obj doesn't contain lines
	}
}
