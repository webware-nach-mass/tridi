package tridi.render;

import tridi.base.Geometry;
import tridi.base.STransform;
import tridi.exp.ObjRenderer;

/**
 * Applying a STransform to a Geometry.
 */
public class Transformation<G extends Geometry> implements Geometry {

	public G geometry;
	public STransform transform;

	public Transformation(final G geometry) {
		this(geometry,new STransform());
	}
	public Transformation(final G geometry,final STransform transform) {
		super();
		this.geometry=geometry;
		this.transform=transform;
	}

	@Override
	public void calculate() {
		//nothing to do
	}

	@Override
	public void render(final String id,final TriangleRenderer renderer) {
		if(geometry == null) {
			return;
		}
		renderer.pushTransform(transform);
		geometry.render(id,renderer);
		renderer.popTransform();
	}

	@Override
	public void renderObj(final String id,final ObjRenderer renderer) {
		if(geometry == null) {
			return;
		}
		renderer.pushTransform(transform);
		geometry.renderObj(id,renderer);
		renderer.popTransform();
	}

}
