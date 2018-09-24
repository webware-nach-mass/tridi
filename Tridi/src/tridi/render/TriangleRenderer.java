package tridi.render;

import tridi.base.CableDelegate;
import tridi.base.SPoint;
import tridi.base.TriangleDelegate;

/**
 * A renderer that uses only triangles.
 */
abstract public class TriangleRenderer extends Renderer {

	public TriangleRenderer() {
		renderTriangle=makeRenderTriangle();
		renderCable=makeRenderCable();
	}

	public final TriangleDelegate renderTriangle;
	abstract protected TriangleDelegate makeRenderTriangle();
	public final CableDelegate renderCable;
	abstract protected CableDelegate makeRenderCable();

	/**
	 * Get scale at givent SPoint for adaptative rendering.
	 */
	abstract public double getScaleAt(SPoint p);
}
