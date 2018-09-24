package tridi.render;

import tridi.base.Geometry;
import tridi.exp.ObjRenderer;

/**
 * Somebody who gives a SColor to an array of Geometries.
 */
public class ArrayColoration<G extends Geometry> implements Geometry {

	public final G[] geometries;
	public SColor color;
	public ArrayColoration(final SColor color,final G... geometries) {
		this.geometries=geometries;
		this.color=color;
	}

	@Override
	public void calculate() {
		//nothing to do
	}

	@Override
	public void render(final String id,final TriangleRenderer renderer) {
		renderer.pushColor(color);
		for(int i=0;i < geometries.length;++i) {
			geometries[i].render(id == null ? null : id + "_" + i,renderer);
		}
		renderer.popColor();
	}

	@Override
	public void renderObj(final String id,final ObjRenderer renderer) {
		renderer.pushColor(color);
		for(int i=0;i < geometries.length;++i) {
			geometries[i].renderObj(id == null ? null : id + "_" + i,renderer);
		}
		renderer.popColor();
	}

}
