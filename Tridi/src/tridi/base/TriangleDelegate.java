package tridi.base;


/**
 * A TriangleCoords used for rendering.
 */
abstract public class TriangleDelegate extends TriangleCoords {

	public TriangleDelegate() {
		super();
	}

	abstract public void renderAsDelegate();
}
