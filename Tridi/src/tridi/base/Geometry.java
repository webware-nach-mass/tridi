package tridi.base;

import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * Someone who can render itself.
 */
public interface Geometry {
	/**
	 * Calculates eventual geometric informations like contact points etc..
	 */
	public void calculate();

	/**
	 * Renders
	 */
	public void render(String id,TriangleRenderer renderer);

	/**
	 * Exports as .obj
	 */
	public void renderObj(String id,ObjRenderer renderer);
}
