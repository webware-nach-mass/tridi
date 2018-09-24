package tridi.geom;

import java.util.ArrayList;

import tridi.base.Geometry;
import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * A compound object.
 */
public class CompoundGeometry extends ArrayList<Geometry> implements Geometry {

	public CompoundGeometry() {
		//nothing
	}

	/**
	 * Calculates eventual geometric informations like contact points etc..<br />
	 * The default implementation isn't recursive, it is up to complex objects to overload this method as needed.
	 */
	@Override
	public void calculate() {
		//nothing to do
	}

	@Override
	public void render(final String id,final TriangleRenderer renderer) {
		for(int i=size() - 1;i >= 0;--i) {
			get(i).render(id == null ? null : id + "_" + i,renderer);
		}
	}
	@Override
	public void renderObj(final String id,final ObjRenderer renderer) {
		for(int n=size(),i=0;i < n;++i) {
			get(i).renderObj(id + "_" + i,renderer);
		}
	}

}
