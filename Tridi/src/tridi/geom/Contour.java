package tridi.geom;

import tridi.base.Geometry;
import tridi.base.SPointList;
import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * A renderable SPointList
 */
public class Contour extends SPointList implements Geometry {

	public Contour() {
		super();
	}

	@Override
	public void calculate() {
		//nothing to do
	}

	@Override
	public void render(final String id,final TriangleRenderer renderer) {
		renderer.startObject(id);
		for(int n=size() - 1,i=0;i < n;++i) {
			renderer.renderCable.points[0].setFrom(get(i));
			renderer.renderCable.points[1].setFrom(get(i + 1));
			renderer.renderCable.renderAsDelegate();
		}
		renderer.endObject();
	}
	@Override
	public void renderObj(final String id,final ObjRenderer renderer) {
		//nothing to do, .obj doesn't contain lines
	}

}
