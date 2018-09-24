package tridi.render;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tridi.base.Geometry;
import tridi.exp.ObjRenderer;

/**
 * A 3D scene.
 */
public class Stage {
	public Stage() {
		super();
	}

	private int background=0xFF000000;
	public int getBackground() {
		return background;
	}
	public void setBackground(final int background) {
		this.background=background | 0xFF000000;
	}

	private final List<Light> lights=new ArrayList<Light>();
	public int getLightCount() {
		return lights.size();
	}
	public Light getLightAt(final int index) {
		return lights.get(index);
	}
	public void addLight(final Light l) {
		lights.add(l);
	}
	public void removeLight(final Light l) {
		lights.remove(l);
	}

	private final Set<Geometry> geometries=new HashSet<Geometry>();
	public int getGeometryCount() {
		return geometries.size();
	}
	public void addGeometry(final Geometry g) {
		geometries.add(g);
	}
	public void removeGeometry(final Geometry g) {
		geometries.remove(g);
	}
	public void calculateGeometry() {
		for(Geometry g : geometries) {
			g.calculate();
		}
	}
	public void render(final ZBufferRenderer renderer) {
		renderer.setLights(lights);
		renderer.setBackground(background);
		renderer.startFrame();
		for(Geometry g : geometries) {
			g.render(null, renderer);
		}
		renderer.endFrame();
	}

	public void renderObj(final ObjRenderer renderer) {
		int i=0;
		for(Geometry g : geometries) {
			g.renderObj("O_" + i,renderer);
			++i;
		}
	}
}
