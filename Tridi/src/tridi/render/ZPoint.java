package tridi.render;

/**
 * A point in Z-Buffer space
 */
public class ZPoint {

	public int[] coords;
	public ZPoint() {
		coords=new int[3];
	}
	public ZPoint(final int u,final int v,final int depth) {
		coords=new int[] {u,v,depth};
	}

	public void set(final int x,final int y,final int z) {
		coords[0]=x;
		coords[1]=y;
		coords[2]=z;
	}

	public void subtract(final ZPoint p) {
		coords[0]-=p.coords[0];
		coords[1]-=p.coords[1];
		coords[2]-=p.coords[2];
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + coords[0] + "," + coords[1] + "," + coords[2] + "]";
	}
}
