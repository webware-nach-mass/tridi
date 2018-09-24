package tridi.render;

import tridi.base.Geometry;
import tridi.exp.ObjRenderer;

/**
 * Somebody who gives a SColor to a Geometry.
 */
public class Coloration<G extends Geometry> implements Geometry,Colored {

	public final G geometry;

	public SColor color;
	@Override
	public SColor getColor() {
		return color;
	}
	@Override
	public void setColor(final SColor color) {
		this.color=color;
	}

	public Coloration(final G geometry,final SColor color) {
		this.geometry=geometry;
		this.color=color;
	}

	@Override
	public void calculate() {
		//nothing to do
	}

	@Override
	public void render(final String id,final TriangleRenderer renderer) {
		renderer.pushColor(color);
		geometry.render(id,renderer);
		renderer.popColor();
	}

	@Override
	public void renderObj(final String id,final ObjRenderer renderer) {
		renderer.pushColor(color);
		geometry.renderObj(id,renderer);
		renderer.popColor();
	}

}
