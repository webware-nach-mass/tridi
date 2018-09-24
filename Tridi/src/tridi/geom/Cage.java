package tridi.geom;

import tridi.base.SPoint;
import tridi.base.SVector;
import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * A rectangular cage.
 */
public class Cage extends BrickCoords {

	public boolean renderLight;

	public Cage() {
		super();
		renderLight=false;
	}

	public Cage(final SPoint corner,final SVector size) {
		super(corner,size);
		renderLight=false;
	}

	private final static double NORMAL_COORD=1.0 / Math.sqrt(3.0);
	@Override
	public void render(final String id,final TriangleRenderer renderer) {
		renderer.startObject(id);
		renderEdge(renderer,false,false,false,true,false,false);
		renderEdge(renderer,false,false,false,false,true,false);
		renderEdge(renderer,false,false,false,false,false,true);
		renderEdge(renderer,true,false,false,true,true,false);
		renderEdge(renderer,true,false,false,true,false,true);
		renderEdge(renderer,false,true,false,true,true,false);
		renderEdge(renderer,false,true,false,false,true,true);
		renderEdge(renderer,false,false,true,true,false,true);
		renderEdge(renderer,false,false,true,false,true,true);
		renderEdge(renderer,true,true,false,true,true,true);
		renderEdge(renderer,true,false,true,true,true,true);
		renderEdge(renderer,false,true,true,true,true,true);
		renderer.endObject();
	}
	private void renderEdge(final TriangleRenderer renderer,final boolean x0,final boolean y0,final boolean z0,final boolean x1,final boolean y1,final boolean z1) {
		renderer.renderCable.points[0].set(x0 ? corner.coords[0] + size.coords[0] : corner.coords[0], //
				y0 ? corner.coords[1] + size.coords[1] : corner.coords[1], //
				z0 ? corner.coords[2] + size.coords[2] : corner.coords[2]);
		renderer.renderCable.points[1].set(x1 ? corner.coords[0] + size.coords[0] : corner.coords[0], //
				y1 ? corner.coords[1] + size.coords[1] : corner.coords[1], //
				z1 ? corner.coords[2] + size.coords[2] : corner.coords[2]);
		if(renderLight) {
			renderer.renderCable.normals[0].set(x0 ? NORMAL_COORD : -NORMAL_COORD,y0 ? NORMAL_COORD : -NORMAL_COORD,z0 ? NORMAL_COORD : -NORMAL_COORD);
			renderer.renderCable.normals[1].set(x1 ? NORMAL_COORD : -NORMAL_COORD,y1 ? NORMAL_COORD : -NORMAL_COORD,z1 ? NORMAL_COORD : -NORMAL_COORD);
			renderer.renderCable.normalsOK=true;
		} else {
			renderer.renderCable.normalsOK=false;
		}
		renderer.renderCable.renderAsDelegate();
	}
	@Override
	public void renderObj(final String id,final ObjRenderer renderer) {
		//nothing to do, .obj doesn't contain lines
	}
}
