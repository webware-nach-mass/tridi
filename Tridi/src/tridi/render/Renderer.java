package tridi.render;

import java.util.Stack;

import tridi.base.STransform;

/**
 * Base class for rendering.
 */
abstract public class Renderer {

	public Renderer() {
		//empty
	}
	protected void reset() {
		transforms.clear();
		transform=null;
		colors.clear();
		color=SColor.DEFAULT_COLOR;
	}

	abstract public void startObject(String id);

	public STransform transform=null;
	private final Stack<STransform> transforms=new Stack<STransform>();
	public void pushTransform(final STransform st) {
		transforms.push(transform);
		if(transform == null) {
			transform=st;
		} else if(st != null) {
			STransform nt=new STransform();
			nt.setFrom(st);
			nt.multiply(transform);
			transform=nt;
		}
	}
	public void popTransform() {
		transform=transforms.pop();
	}

	public SColor color=SColor.DEFAULT_COLOR;
	private final Stack<SColor> colors=new Stack<SColor>();
	public void pushColor(final SColor color) {
		colors.push(this.color);
		if(color != null) {
			this.color=color;
		}
	}
	public void popColor() {
		color=colors.pop();
	}

	abstract public void endObject();

}
